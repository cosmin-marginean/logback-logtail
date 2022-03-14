package com.logtail.logback;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import javax.ws.rs.ProcessingException;
import java.util.UUID;

import static org.junit.Assert.*;

/**
 * ! One LOGTAIL_INGEST_KEY must be set as an environment variable before launching the test !
 *
 *
 * @author tomas@logtail.com
 */
public class LogtailAppenderMillisecondsIssueTest {

    private Logger logger = (Logger) LoggerFactory.getLogger(LogtailAppenderMillisecondsIssueTest.class);

    private LogtailAppenderDecorator appender;

    @Before
    public void init() throws JoranException {
        LoggerContext loggerContext = ((LoggerContext) LoggerFactory.getILoggerFactory());
        loggerContext.reset();

        JoranConfigurator configurator = new JoranConfigurator();
        configurator.setContext(loggerContext);
        configurator.doConfigure("src/test/resources/logback-milliseconds-issue.xml");

        this.appender = new LogtailAppenderDecorator();
        this.appender.setContext((LoggerContext) LoggerFactory.getILoggerFactory());
        this.appender.setIngestKey(System.getenv("LOGTAIL_INGEST_KEY"));
        this.appender.start();

        this.logger.addAppender(appender);
    }

    @After
    public void tearDown() {
        this.logger.detachAppender(appender);
    }

    @Test
    public void testMillisecondsIssue() {
        this.logger.info("Milliseconds issue TEST 1");
        this.logger.info("Milliseconds issue TEST 2");
        this.logger.info("Milliseconds issue TEST 3");

        this.appender.flush();
    }
}