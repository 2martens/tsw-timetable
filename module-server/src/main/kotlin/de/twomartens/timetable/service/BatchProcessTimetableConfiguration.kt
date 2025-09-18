package de.twomartens.timetable.service

import de.twomartens.timetable.bahnApi.model.db.BahnTimetable
import de.twomartens.timetable.bahnApi.repository.BahnTimetableRepository
import de.twomartens.timetable.model.common.ServiceId
import de.twomartens.timetable.model.common.TimetableId
import de.twomartens.timetable.model.common.UserId
import de.twomartens.timetable.model.db.Service
import de.twomartens.timetable.model.db.ServiceRepository
import de.twomartens.timetable.model.db.ServiceStop
import de.twomartens.timetable.model.db.Timetable
import de.twomartens.timetable.model.dto.TimetableState
import de.twomartens.timetable.timetable.TimetableRepository
import de.twomartens.timetable.types.NonEmptyString
import org.springframework.batch.core.Job
import org.springframework.batch.core.JobParameters
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.batch.core.configuration.annotation.StepScope
import org.springframework.batch.core.job.builder.JobBuilder
import org.springframework.batch.core.launch.JobLauncher
import org.springframework.batch.core.launch.support.TaskExecutorJobLauncher
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.step.builder.StepBuilder
import org.springframework.batch.item.ItemProcessor
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder
import org.springframework.batch.item.data.builder.RepositoryItemWriterBuilder
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.task.SimpleAsyncTaskExecutor
import org.springframework.data.domain.Sort
import org.springframework.transaction.PlatformTransactionManager

@EnableBatchProcessing
@Configuration
class BatchProcessTimetableConfiguration {

    @Bean("asyncJobLauncher")
    fun jobLauncher(jobRepository: JobRepository): JobLauncher {
        val launcher = TaskExecutorJobLauncher()
        launcher.setJobRepository(jobRepository)
        launcher.setTaskExecutor(SimpleAsyncTaskExecutor())
        launcher.afterPropertiesSet()
        return launcher
    }

    @Bean
    fun processTimetableJob(jobRepository: JobRepository,
                            createStopStorageRequestsStep: Step,
                            storeStopsStep: Step): Job {
        val job = JobBuilder("processTimetable", jobRepository)
                .start(createStopStorageRequestsStep)
                .next(storeStopsStep)
                .build()
        return job
    }

    @Bean("createStopStorageRequestsStep")
    @StepScope
    fun createStopStorageRequestsStep(
            jobRepository: JobRepository,
            transactionManager: PlatformTransactionManager,
            bahnTimetableRepository: BahnTimetableRepository,
            stopStorageRequestRepository: StopStorageRequestRepository,
            @Value("#{jobParameters}") jobParameters: JobParameters): Step {
        val reader = RepositoryItemReaderBuilder<BahnTimetable>()
                .repository(bahnTimetableRepository)
                .methodName("getBahnTimetablesByUserIdAndTswTimetableId")
                .name("BahnTimetableReader")
                .arguments(
                        UserId.of(NonEmptyString(jobParameters.getString("userId")!!)),
                        TimetableId.of(NonEmptyString(jobParameters.getString("timetableId")!!))
                )
                .sorts(mapOf("eva" to Sort.Direction.ASC, "hourAtDay" to Sort.Direction.ASC))
                .pageSize(26) // one station per page
                .saveState(true)
                .build()
        val processor = ItemProcessor<BahnTimetable, List<StopStorageRequest>> { timetable ->
            timetable.stops.map {
                StopStorageRequest(
                        userId = timetable.userId.toString(),
                        timetableId = timetable.tswTimetableId.value,
                        eva = it.eva.toString(),
                        trainNumber = it.tripLabel.trainNumber,
                        plannedArrivalTime = it.arrival?.plannedTime,
                        plannedDepartureTime = it.departure?.plannedTime,
                        plannedArrivalPlatform = it.arrival?.plannedPlatform,
                        plannedDeparturePlatform = it.departure?.plannedPlatform,
                        arrivalLine = it.arrival?.line,
                        departureLine = it.departure?.line,
                        plannedArrivalPath = it.arrival?.plannedPath,
                        plannedDeparturePath = it.departure?.plannedPath
                )
            }
        }

        return StepBuilder("createStopStorageRequests", jobRepository)
                .chunk<BahnTimetable, List<StopStorageRequest>>(10, transactionManager)
                .reader(reader)
                .processor(processor)
                .writer {
                    val items = it.items.flatMap { requests -> requests }
                    stopStorageRequestRepository.saveAll(items)
                }
                .build()
    }

    @Bean("storeStopsStep")
    @StepScope
    fun storeStopsStep(jobRepository: JobRepository,
                       transactionManager: PlatformTransactionManager,
                       stopStorageRequestRepository: StopStorageRequestRepository,
                       serviceRepository: ServiceRepository,
                       @Value("#{jobParameters}") jobParameters: JobParameters): Step {
        val userId = UserId.of(NonEmptyString(jobParameters.getString("userId")!!))
        val timetableId = TimetableId.of(NonEmptyString(jobParameters.getString("timetableId")!!))
        val reader = RepositoryItemReaderBuilder<StopStorageRequest>()
                .repository(stopStorageRequestRepository)
                .methodName("getStopStorageRequestsByUserIdAndTimetableId")
                .name("StopStorageRequestReader")
                .arguments(userId, timetableId)
                .sorts(mapOf("eva" to Sort.Direction.ASC))
                .pageSize(10)
                .saveState(true)
                .build()


        val processor = ItemProcessor<StopStorageRequest, Service> {
            val serviceId = ServiceId.of(NonEmptyString(it.trainNumber))
            val serviceExists = serviceRepository.existsServiceByUserIdAndTimetableIdAndServiceId(
                    userId, timetableId, serviceId)
            val service: Service = if (serviceExists) {
                serviceRepository.getServiceByUserIdAndTimetableIdAndServiceId(
                        userId, timetableId, serviceId)!!
            } else {
                Service(userId, timetableId, serviceId)
            }
            service.addStop(ServiceStop(
                    it.eva,
                    it.plannedArrivalTime,
                    it.plannedDepartureTime,
                    it.plannedArrivalPlatform,
                    it.plannedDeparturePlatform,
                    it.arrivalLine,
                    it.departureLine,
                    it.plannedArrivalPath,
                    it.plannedDeparturePath
            ))
            service
        }
        val writer = RepositoryItemWriterBuilder<Service>()
                .repository(serviceRepository)
                .methodName("save")
                .build()
        return StepBuilder("storeStopsStep", jobRepository)
                .chunk<StopStorageRequest, Service>(26, transactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build()
    }

    @Bean("updateTimetableStateStep")
    @StepScope
    fun updateTimetableStateStep(jobRepository: JobRepository,
                                 transactionManager: PlatformTransactionManager,
                                 timetableRepository: TimetableRepository,
                                 @Value("#{jobParameters}") jobParameters: JobParameters): Step {
        val reader = RepositoryItemReaderBuilder<Timetable>()
                .repository(timetableRepository)
                .methodName("getTimetableByUserIdAndTimetableId")
                .name("TimetableReader")
                .arguments(
                        UserId.of(NonEmptyString(jobParameters.getString("userId")!!)),
                        TimetableId.of(NonEmptyString(jobParameters.getString("timetableId")!!))
                )
                .pageSize(1)
                .build()
        val processor = ItemProcessor<Timetable, Timetable> {
            it.timetableState = TimetableState.SERVICES_COLLECTED
            it
        }
        val writer = RepositoryItemWriterBuilder<Timetable>()
                .repository(timetableRepository)
                .methodName("save")
                .build()
        return StepBuilder("updateTimetableStateStep", jobRepository)
                .chunk<Timetable, Timetable>(1, transactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build()
    }
}