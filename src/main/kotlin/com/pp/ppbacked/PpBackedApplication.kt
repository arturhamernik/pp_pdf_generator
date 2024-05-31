package com.pp.ppbacked

import com.openhtmltopdf.util.XRLog
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import java.util.function.Consumer
import java.util.logging.Level

@SpringBootApplication
class PpBackedApplication

fun main(args: Array<String>) {
    runApplication<PpBackedApplication>(*args)

    XRLog.listRegisteredLoggers().forEach(Consumer { logger: String? ->
        XRLog.setLevel(logger, Level.SEVERE)
    })

}