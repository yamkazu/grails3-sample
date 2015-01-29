import grails.util.Environment

// See http://logback.qos.ch/manual/groovy.html for details on configuration
def FILE_LOG_PATTERN = '%d{yyyy-MM-dd HH:mm:ss.SSS}\t%t\t%p\t%logger{39}\t%m%n'
def CONSOLE_LOG_PATTERN = '%d{HH:mm:ss.SSS} [%t] %highlight(%p) %cyan(\\(%logger{39}\\)) %m%n'

def logDir = 'logs'

appender('STDOUT', ConsoleAppender) {
    withJansi = true
    encoder(PatternLayoutEncoder) {
        pattern = CONSOLE_LOG_PATTERN
    }
}

appender("FILE", RollingFileAppender) {
    encoder(PatternLayoutEncoder) {
        pattern = FILE_LOG_PATTERN
    }
    rollingPolicy(TimeBasedRollingPolicy) {
        fileNamePattern = "${logDir}/application.%d{yyyy-MM-dd}.log"
        // ファイル名の最後に.zip、.gzをつけるとローリングのタイミングで圧縮される
        // fileNamePattern = 'logs/application.%d{yyyy-MM-dd_HHmm}.log.gz'
        // fileNamePattern = 'logs/application.%d{yyyy-MM-dd_HHmm}.log.zip'

        // 保存する世代数を設定できる
        // maxHistory = 3
    }
}

appender("FULL_STACKTRACE", RollingFileAppender) {
    encoder(PatternLayoutEncoder) {
        pattern = FILE_LOG_PATTERN
    }
    rollingPolicy(TimeBasedRollingPolicy) {
        fileNamePattern = "${logDir}/stacktrace.%d{yyyy-MM-dd}.log"
    }
}

Environment.executeForCurrentEnvironment {
    development {
        root(INFO, ['STDOUT', 'FILE'])
        logger("StackTrace", ERROR, ['FULL_STACKTRACE'], false)
    }
    test {
        root(INFO, ['STDOUT', 'FILE'])
        logger("StackTrace", ERROR, ['FULL_STACKTRACE'], false)
    }
    production {
        root(INFO, ['FILE'])
    }
}
