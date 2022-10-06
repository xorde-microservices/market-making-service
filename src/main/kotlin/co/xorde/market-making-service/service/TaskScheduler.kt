package co.xorde.market_making_service.service

import io.micronaut.scheduling.annotation.Scheduled
import jakarta.inject.Singleton
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Singleton
class TaskScheduler(private val orderService: OrderService) {

    /**
     * Scheduled task for trading job
     */
    @Scheduled(fixedDelay = "60s", initialDelay = "5s")
    fun orderExchangeJob() {
        LOG.info("START job: order exchange")
        orderService.orderExchange()
        LOG.info("END job: order exchange")
    }

    companion object {
        private val LOG: Logger = LoggerFactory.getLogger(TaskScheduler::class.java)
    }
}
