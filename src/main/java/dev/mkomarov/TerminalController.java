package dev.mkomarov;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;

import static dev.mkomarov.Main.ROOT_PASSWORD;

public class TerminalController {
    private static final String YDOTOOL_PATH = "/home/mkomarov/bin/ydotool/ydotool";
    private static final String YDOTOOLD_PATH = "/home/mkomarov/bin/ydotool/ydotoold";

    private static void preconfigureYdotool() {
        executeCommand("export YDOTOOL_SOCKET=/tmp/.ydotool_socket");
    }

    private static String parseCommand(String command) {
        if (command.startsWith("ydotoold")
                || command.startsWith("echo " + ROOT_PASSWORD + " | sudo -S ydotoold")) {
            command = command.replace("ydotoold", YDOTOOLD_PATH);
        } else if (command.startsWith("ydotool")
                || command.startsWith("echo " + ROOT_PASSWORD + " | sudo -S ydotool")) {
            command = command.replace("ydotool", YDOTOOL_PATH);
        }

        return command;
    }

    public static Process executeCommand(String command) {
        command = parseCommand(command);
        try {
            ProcessBuilder pb = new ProcessBuilder("bash", "-c", command);
            return pb.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Process executeCommand(String command, boolean rootAccess, boolean printLog) {
        if (rootAccess) {
            command = appendRootPassword(command);
        }

        Process process = executeCommand(command);

        if (printLog) {
            printCommandLog(process);
        }

        return process;
    }

    public static String getCommandLog(Process process) {
        try {
            StringBuilder log = new StringBuilder();
            BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));
            BufferedReader stdError = new BufferedReader(new InputStreamReader(process.getErrorStream()));

            String s;
            while ((s = stdInput.readLine()) != null) {
                log.append(s);
            }

            while ((s = stdError.readLine()) != null) {
                log.append(s);
            }

            process.waitFor();
            return log.toString();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static void printCommandLog(Process process) {
        System.out.println(getCommandLog(process));
    }

    private static String appendRootPassword(String command) {
        return "echo " + ROOT_PASSWORD + " | sudo -S " + command;
    }

    public static Thread startYdotoolDaemon() {
        preconfigureYdotool();
        Thread thread = new YdotoolDemonThread();
        thread.setName("ydotoold");
        thread.setDaemon(true);
        thread.start();
        return thread;
    }

    private static class YdotoolDemonThread extends Thread implements Closeable {
        @Override
        public void run() {
            executeCommand(YDOTOOLD_PATH, true, true);
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    System.out.println("Ydotoold is running");
                    Thread.sleep(1000);
                }
            } catch (InterruptedException e) {
                System.out.println("Ydotoold is interrupted");
                this.close();
            } finally {
                this.close();
            }
        }

        @Override
        public void close() {
            System.out.println("Ydotoold is closing");
            try {
                String command = "echo " + ROOT_PASSWORD + " | sudo -S " + "pkill ydotoold";
                Process pr = new ProcessBuilder("bash", "-c", command).start();
                printCommandLog(pr);
            } catch (Exception e) {
                System.err.println("Could not close ydotoold process");
            }
        }
    }
}
