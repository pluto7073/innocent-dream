package io.innocent.dream.crash_report;

import io.innocent.dream.InnocentDream;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class CrashReport {

    private final boolean hasParent;
    private Frame parent;
    private final PrintStream out;
    private final PrintStream err;
    private String filepath;
    private final boolean shouldExit;

    /**
     * Creates the crash report creator with no parent and the <code>out</code>
     * and <code>err</code> parameters set to <code>System.out</code> and <code>System.err</code>
     *
     * Also sets <code>shouldExit</code> to <code>true</code>
     */
    public CrashReport() {
        hasParent = false;
        shouldExit = true;
        out = System.out;
        err = System.err;
        out.println("CrashReport initialized");
        filepath = InnocentDream.path + "\\crash_reports";
    }

    /**
     * Creates a crash report creator with no parent, <code>out</code> and <code>err</code> are set to
     * <code>System.out</code> and <code>System.err</code>, and shouldExit is set to what has been specified
     * @param shouldExit whether or not the program should exit upon receiving an exception
     */
    public CrashReport(boolean shouldExit) {
        hasParent = false;
        this.shouldExit = shouldExit;
        out = System.out;
        err = System.err;
        out.println("CrashReport initialized");
    }

    /**
     * Creates a crash report creator with no parent and the <code>out</code>
     * and <code>err</code> parameters set to what you specify
     * @param out the <code>PrintStream</code> to print information to
     * @param err the <code>PrintStream</code> to print the Throwable to
     * @param shouldExit whether or not the program should exit upon receiving an exception
     */
    public CrashReport(PrintStream out, PrintStream err, boolean shouldExit) {
        hasParent = false;
        this.out = out;
        this.err = err;
        this.shouldExit = shouldExit;
        out.println("CrashReport initialized");
    }

    /**
     * Creates a crash report creator with the parent specified and the <code>out</code>
     * and <code>err</code> specified
     * @param parent the Frame to add a pop-up window specifying the <code>Exception</code> that has occurred to
     * @param out the <code>PrintStream</code> to print information to
     * @param err the <code>PrintStream</code> to print the exception to
     * @param shouldExit whether or not the program should exit upon receiving an exception
     */
    public CrashReport(Frame parent, PrintStream out, PrintStream err, boolean shouldExit) {
        hasParent = true;
        this.parent = parent;
        this.out  = out;
        this.err = err;
        this.shouldExit = shouldExit;
        out.println("CrashReport initialized");
    }

    /**
     * Changes the path used by <code>createCrashReport(Throwable t)</code> so that
     * the <code>.txt</code> file generated will be stored in the specified path\crash_reports
     * @param filepath A path to a folder where the crash report file will be stored
     */
    public void setFilepath(String filepath) {
        File file = new File(filepath + "\\crash_reports");
        if (!file.exists()) {
            file.mkdirs();
        }
        this.filepath = filepath + "\\crash_reports";
    }

    /**
     * Changes the <code>JFrame parent</code> of the crash report creator
     * @param parent the <code>JFrame to set the parent to</code>
     */
    public void setParent(JFrame parent) {
        if (hasParent) {
            this.parent = parent;
        } else {
            throw new UnsupportedOperationException("This crash report builder never had a parent");
        }
    }

    private static final String[] MESSAGES = {
            "I am so sorry that I let you down",
            "Don't worry, the game hasn't even been fully released yet!",
            "I really hate the bugs",
            "Do you want a hug?"
    };


    /**
     * Sends the stack trace of the Throwable <code>t</code>
     * and creates a crash report file containing that and
     * information like <code>os.name</code>, <code>os.version</code>,
     * <code>user.dir</code>, <code>java.home</code>, and others.
     * The generated file will be saved in the default working
     * directory specified by <code>user.dir</code> unless
     * <code>setFilePath(String filepath)</code> is called first.
     *
     * If a <code>parent</code> is specified in the constructor then the specified
     * <code>Frame</code> will close and a <code>JOptionPane</code> will pop-up
     * showing the exception that occurred.
     *
     * If <code>shouldExit</code> is specified in the constructor then the
     * program will exit at the end of the method.
     * @param t The Throwable/Exception thrown
     *
     * @return the path to the crash report file
     */
    public String createCrashReport(Throwable t) {
        CrashReportBuilder builder = new CrashReportBuilder(err, t);
        String stackTrace = builder.createStackTrace();
        String systemInfo = builder.getSystemInformation();
        int r = new Random().nextInt(MESSAGES.length);
        String message = MESSAGES[r];
        StringBuilder reportBuilder = new StringBuilder("Crash Report:\n\n//");
        reportBuilder.append(message);
        reportBuilder.append("\n\n== Stack Trace ==\n")
                .append(stackTrace);
        reportBuilder.append("\n== System Information ==\n").append(systemInfo)
                .append("\n");
        reportBuilder.append("\n== Created By Innocent Dream ==");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd_MM_yyyy_HH_mm_ss");
        LocalDateTime time = LocalDateTime.now();
        String formattedTime = time.format(formatter);
        if (hasParent) {
            parent.setVisible(false);
            JOptionPane.showMessageDialog(parent, t.toString(), "An Error has Occurred", JOptionPane.ERROR_MESSAGE);
        }
        filepath = InnocentDream.path + "\\crash_reports";
        File file = new File(filepath);
        file.mkdirs();
        file = new File(filepath + "\\" + formattedTime + ".txt");
        try {
            file.createNewFile();
            FileWriter writer = new FileWriter(file);
            writer.write(reportBuilder.toString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (shouldExit) System.exit(0);
        return file.getAbsolutePath();
    }
    
    public String createCrashReport(Throwable t, String optionalMessage) {
        CrashReportBuilder builder = new CrashReportBuilder(err, t);
        String stackTrace = builder.createStackTrace(optionalMessage);
        String systemInfo = builder.getSystemInformation();
        int r = new Random().nextInt(MESSAGES.length);
        String message = MESSAGES[r];
        StringBuilder reportBuilder = new StringBuilder("Crash Report:\n\n//");
        reportBuilder.append(message);
        reportBuilder.append("\n\n== Stack Trace ==\n")
                .append(stackTrace);
        reportBuilder.append("\n== System Information ==\n").append(systemInfo)
                .append("\n");
        reportBuilder.append("\n== Created By Innocent Dream ==");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd_MM_yyyy_HH_mm_ss");
        LocalDateTime time = LocalDateTime.now();
        String formattedTime = time.format(formatter);
        if (hasParent) {
            parent.setVisible(false);
            JOptionPane.showMessageDialog(parent, t.toString(), "An Error has Occurred", JOptionPane.ERROR_MESSAGE);
        }
        filepath = InnocentDream.path + "\\crash_reports";
        File file = new File(filepath);
        file.mkdirs();
        file = new File(filepath + "\\" + formattedTime + ".txt");
        try {
            file.createNewFile();
            FileWriter writer = new FileWriter(file);
            writer.write(reportBuilder.toString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (shouldExit) System.exit(0);
        return file.getAbsolutePath();
    }

}
