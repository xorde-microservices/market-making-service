package co.xorde.market_making_service.service

import io.micronaut.context.annotation.Replaces
import io.micronaut.scheduling.DefaultTaskExceptionHandler
import jakarta.inject.Singleton
import kotlin.system.exitProcess

@Singleton
@Replaces(DefaultTaskExceptionHandler::class)
class TaskExceptionHandler: DefaultTaskExceptionHandler() {

    override fun handle(bean: Any?, throwable: Throwable) {
        super.handle(bean, throwable)
        println("Terminating application with code 100")
        exitProcess(100)
    }
}