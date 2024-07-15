package dev.mkomarov.terminal;

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
                log.append(s).append("\n");
            }

            while ((s = stdError.readLine()) != null) {
                log.append(s).append("\n");
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
        Thread thread = new YdotoolDaemonThread();
        thread.setName("ydotoold");
        thread.start();
        System.out.println("Ydotoold thread started");
        return thread;
    }

    private static class YdotoolDaemonThread extends Thread implements Closeable {
        private Process process;

        @Override
        public void run() {
            process = executeCommand(YDOTOOLD_PATH  + " &> /dev/null &", true, true);
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    Thread.sleep(1000);
                }
            } catch (InterruptedException e) {
                System.out.println("Ydotoold is interrupted");
            } finally {
                this.close();
            }
        }

        @Override
        public void close() {
            System.out.println("Ydotoold is closing...");
            try {
                if (process != null) {
                    process.destroy();
                    process.waitFor();
                    System.out.println("Ydotoold is closed.");
                }
            } catch (Exception e) {
                System.err.println("Could not close ydotoold process");
                e.printStackTrace();
            }
        }
    }
}
