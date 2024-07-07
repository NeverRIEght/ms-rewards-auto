package dev.mkomarov;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import static dev.mkomarov.Main.ROOT_PASSWORD;

public class TerminalController {
    public static Process executeCommand(String command) {
        try {
            ProcessBuilder pb = new ProcessBuilder("bash", "-c", command);
            return pb.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void executeCommand(String command, boolean rootAccess, boolean printLog) {
        if (rootAccess) {
            command = appendRootPassword(command);
        }

        Process process = executeCommand(command);

        if (printLog) {
            printCommandLog(process);
        }
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
}
