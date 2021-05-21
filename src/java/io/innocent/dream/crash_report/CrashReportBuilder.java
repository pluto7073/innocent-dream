package io.innocent.dream.crash_report;

import java.io.PrintStream;

public class CrashReportBuilder {

    private final PrintStream err;
    private final Throwable throwable;

    protected CrashReportBuilder(PrintStream err, Throwable throwable) {
        this.err = err;
        this.throwable = throwable;
    }

    protected String createStackTrace() {
        StringBuilder stackTraceBuilder = new StringBuilder();
        err.println(throwable.toString());
        stackTraceBuilder.append(throwable).append("\n");
        StackTraceElement[] elements = throwable.getStackTrace();
        for (StackTraceElement element : elements) {
            err.println("\t" + element);
            stackTraceBuilder.append("\t").append(element).append("\n");
        }
        return stackTraceBuilder.toString();
    }
    
    protected String createStackTrace(String message) {
        StringBuilder stackTraceBuilder = new StringBuilder();
        err.println(throwable.toString());
        stackTraceBuilder.append(message).append(": ").append(throwable).append("\n");
        StackTraceElement[] elements = throwable.getStackTrace();
        for (StackTraceElement element : elements) {
            err.println("\t" + element);
            stackTraceBuilder.append("\t").append(element).append("\n");
        }
        return stackTraceBuilder.toString();
    }

    protected String getSystemInformation() {
        StringBuilder informationBuilder = new StringBuilder();
        String os = System.getProperty("os.name");
        String os_version = System.getProperty("os.version");
        String work_dir = System.getProperty("user.dir");
        String java_version = System.getProperty("java.version");
        String user_name = System.getProperty("user.name");
        String java_home = System.getProperty("java.home");
        informationBuilder.append("Operating System: ").append(os);
        informationBuilder.append("\nOperating System Version: ").append(os_version);
        informationBuilder.append("\nProgram Working Directory: ").append(work_dir);
        informationBuilder.append("\nJava Version: ").append(java_version);
        informationBuilder.append("\nUser: ").append(user_name);
        informationBuilder.append("\nJava Home: ").append(java_home);
        return informationBuilder.toString();
    }

}
