package com.epam.jdi.uitests.core.logger;

import org.apache.log4j.*;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * Logger in XML format class.
 */
public class XMLLogger {

    private Logger log;
    private Map<Appender,Layout> layouts;
    private Level logLevel;

    public XMLLogger(Class clazz) {
        log = Logger.getLogger(clazz);
        logLevel =Logger.getRootLogger().getLevel();
        layouts = new HashMap<>();

        Enumeration<Logger> loggers = LogManager.getCurrentLoggers();
        for(Logger logger ; loggers.hasMoreElements();) {
            logger = loggers.nextElement();

            Enumeration<Appender> appenderEnumeration = logger.getAllAppenders();
            for (Appender appender; appenderEnumeration.hasMoreElements(); ) {
                appender = appenderEnumeration.nextElement();
                layouts.put(appender, appender.getLayout());
            }
        }

        Enumeration<Appender> appenderEnumeration = LogManager.getRootLogger().getAllAppenders();
        for (Appender appender; appenderEnumeration.hasMoreElements(); ) {
            appender = appenderEnumeration.nextElement();
            layouts.put(appender, appender.getLayout());
        }
    }

    /**
     * Adds logs with "info" level into logging file and console and executes another internal method.
     * @param message Message to be shown in logging file and console.
     * @param lambda Lambda expression without arguments which will be executed.
     */
    public void info(String message, ILambdaExpression lambda) {
        log(LogLevels.INFO, message, lambda);
    }

    /**
     * Adds logs with "info" level into logging file and console.
     * @param message Message to be shown in logging file and console.
     */
    public void info(String message) {
        log(LogLevels.INFO, message);
    }

    /**
     * Adds logs with "warn" level into logging file and console and executes another internal method.
     * @param message Message to be shown in logging file and console.
     * @param lambda Lambda expression without arguments which will be executed.
     */
    public void warn(String message, ILambdaExpression lambda) {
        log(LogLevels.WARNING, message, lambda);
    }

    /**
     * Adds logs with "warn" level into logging file and console.
     * @param message Message to be shown in logging file and console.
     */
    public void warn(String message) {
        log(LogLevels.WARNING, message);
    }

    /**
     * Adds logs with "off" level into logging file and executes another internal method.
     * @param message Message to be shown in logging file and console.
     * @param lambda Lambda expression without arguments which will be executed.
     */
    public void off(String message, ILambdaExpression lambda) {
        log(LogLevels.OFF, message, lambda);
    }

    /**
     * Adds logs with "off" level into logging file and console.
     * @param message Message to be shown in logging file and console.
     */
    public void off(String message) {
        log(LogLevels.OFF, message);
    }

    /**
     * Adds logs with "error" level into logging file and console and executes another internal method.
     * @param message Message to be shown in logging file and console.
     * @param lambda Lambda expression without arguments which will be executed.
     */
    public void error(String message, ILambdaExpression lambda) {
        log(LogLevels.ERROR, message, lambda);
    }

    /**
     * Adds logs with "error" level into logging file and console.
     * @param message Message to be shown in logging file and console.
     */
    public void error(String message) {
        log(LogLevels.ERROR, message);
    }

    /**
     * Adds logs with "debug" level into logging file and console and executes another internal method.
     * @param message Message to be shown in logging file and console.
     * @param lambda Lambda expression without arguments which will be executed.
     */
    public void debug(String message, ILambdaExpression lambda) {
        log(LogLevels.DEBUG, message, lambda);
    }

    /**
     * Adds logs with "debug" level into logging file and console.
     * @param message Message to be shown in logging file and console.
     */
    public void debug(String message) {
        log(LogLevels.DEBUG, message);
    }

    /**
     * Adds logs with "trace" level into logging file and executes another internal method and console.
     * @param message Message to be shown in logging file and console.
     * @param lambda Lambda expression without arguments which will be executed.
     */
    public void trace(String message, ILambdaExpression lambda) {
        log(LogLevels.TRACE, message, lambda);
    }

    /**
     * Adds logs with "trace" level into logging file and console.
     * @param message Message to be shown in logging file and console.
     */
    public void trace(String message) {
        log(LogLevels.TRACE, message);
    }

    /**
     * Adds logs with "fatal" level into logging file and console and executes another internal method.
     * @param message Message to be shown in logging file and console.
     * @param lambda Lambda expression without arguments which will be executed.
     */
    public void fatal(String message, ILambdaExpression lambda) {
        log(LogLevels.FATAL, message, lambda);
    }

    /**
     * Adds logs with "fatal" level into logging file and console.
     * @param message Message to be shown in logging file and console.
     */
    public void fatal(String message) {
        log(LogLevels.FATAL, message);
    }

    /**
     * Adds logs with "all" level into logging file and console and executes another internal method.
     * @param message Message to be shown in logging file and console.
     * @param lambda Lambda expression without arguments which will be executed.
     */
    public void all(String message, ILambdaExpression lambda) {
        log(LogLevels.ALL, message, lambda);
    }

    /**
     * Adds logs with "all" level into logging file and console.
     * @param message Message to be shown in logging file and console.
     */
    public void all(String message) {
        log(LogLevels.ALL, message);
    }

    /**
     * Adds log with one of the possible levels into logging file and console and executes lambda expression.
     * @param logLevel Provides the information about the log level.
     * @param message Message to be shown in logging file.
     * @param lambda Lambda expression without arguments which will be executed.
     */
    public void log (LogLevels logLevel, String message, ILambdaExpression lambda){
        setOurLayout("%m");
        writeLog(logLevel,"<" + logLevel.toString().toLowerCase() + ">");
        setUserLayout();
        writeLog(logLevel,message);
        lambda.doInternalAction();
        setOurLayout("%m%n");
        writeLog(logLevel,"</" + logLevel.toString().toLowerCase() + ">");
        setUserLayout();
    }

    /**
     * Adds log with one of the possible levels into logging file and console
     * @param logLevel Provides the information about the log level.
     * @param message Message to be shown in logging file.
     */
    public void log(LogLevels logLevel, String message) {
        setOurLayout("%m");
        writeLog(logLevel,"<" + logLevel.toString().toLowerCase() + ">");
        setUserLayout();
        writeLog(logLevel,message);
        setOurLayout("%m%n");
        writeLog(logLevel,"</" + logLevel.toString().toLowerCase() + ">");
        setUserLayout();
    }

    private void writeLog(LogLevels logLevel, String message){
        switch(logLevel){
            case ALL:
                log.trace(message);
                break;
            case TRACE:
                if(this.logLevel.toInt() <= Level.TRACE.toInt()){
                    log.trace(message);
                }
                break;
            case DEBUG:
                if(this.logLevel.toInt() <= Level.DEBUG.toInt()){
                    log.debug(message);
                }
                break;
            case INFO:
                if(this.logLevel.toInt() <= Level.INFO.toInt()){
                    log.info(message);
                }
                break;
            case WARNING:
                if(this.logLevel.toInt() <= Level.WARN.toInt()){
                    log.warn(message);
                }
                break;
            case ERROR:
                if(this.logLevel.toInt() <= Level.ERROR.toInt()){
                    log.error(message);
                }
                break;
            case FATAL:
                if(this.logLevel.toInt() <= Level.FATAL.toInt()){
                    log.fatal(message);
                }
                break;
            case OFF:
                log.fatal(message);
                break;
        }
    }

    private void setOurLayout(String pattern){
        for (Appender appender: layouts.keySet()){
            appender.setLayout(new PatternLayout(pattern));
        }
    }

    private void setUserLayout(){
        for(Map.Entry<Appender, Layout> entry: layouts.entrySet()){
            entry.getKey().setLayout(entry.getValue());
        }
    }
}

/**
 * Test class that shows functionality of XMLLogger.
 */
class Test {

    private static XMLLogger xmlLogger = new XMLLogger(Test.class);

    public static void main(String[] args) {
        Test test = new Test();
        test.method1();
    }

    void method1() {
        xmlLogger.fatal("message1");
        xmlLogger.error("message1");
        xmlLogger.warn("message1");
        xmlLogger.info("message1");
        xmlLogger.debug("message1");
        xmlLogger.trace("message1");
        method2();
    }

    void method2() {
        xmlLogger.info("message5", () -> method3());
    }

    void method3() {
        xmlLogger.error("message6", () -> method4());
    }

    void method4() {
        xmlLogger.debug("message7");
    }
}

