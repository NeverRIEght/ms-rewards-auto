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

    private static void printCommandLog(Process process) {
        try {
            BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));
            BufferedReader stdError = new BufferedReader(new InputStreamReader(process.getErrorStream()));

            String s;
            while ((s = stdInput.readLine()) != null) {
                System.out.println(s);
            }

            while ((s = stdError.readLine()) != null) {
                System.out.println(s);
            }

            process.waitFor();

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private static String appendRootPassword(String command) {
        return "echo " + ROOT_PASSWORD + " | sudo -S " + command;
    }
}
