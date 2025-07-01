package helpers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Paths;

public class ScriptRunner {
    private static final String INPUT_FILE = Paths.get(System.getProperty("user.dir"), "script", "data", "transactions.csv").toString();
    private static final String SCRIPT_PATH = Paths.get(System.getProperty("user.dir"), "script", "calculate_averages.sh").toString();

    private String errorMessage;
    private int exitCode;

    public void executeScript() {
        runScript(INPUT_FILE);
    }

    public void executeScript(String filePath) {
        runScript(filePath);
    }

    private void runScript(String filePath) {
        System.out.println("Running script..");
        ProcessBuilder processBuilder = new ProcessBuilder("bash", SCRIPT_PATH, filePath);
        try {
            Process process = processBuilder.start();

            StringBuilder outputBuilder = new StringBuilder();
            try(BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    outputBuilder.append(line).append("\n");
                }
            }

            errorMessage = outputBuilder.toString();
            exitCode = process.waitFor();

        } catch (IOException | InterruptedException e) {
            errorMessage = e.getMessage();
        }
    }

    public int getExitCode() {
        return exitCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
