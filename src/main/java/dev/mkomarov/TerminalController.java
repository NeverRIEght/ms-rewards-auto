package dev.mkomarov;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import static dev.mkomarov.Main.ROOT_PASSWORD;

public class TerminalController {
    public static void executeCommand(String command) {
        try {
            command = "echo " + ROOT_PASSWORD + " | sudo -S " + command;
            // Use ProcessBuilder to execute the command
            ProcessBuilder pb = new ProcessBuilder("bash", "-c", command);
            Process process = pb.start();

            // Read the output from the command
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
}
