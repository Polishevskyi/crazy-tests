package helpers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.ThreadMXBean;
import java.nio.file.Paths;

public class ScriptRunner {
    private static final String SCRIPT_PATH = Paths.get(System.getProperty("user.dir"), "check_active_users.sh")
            .toString();
    private static final String DATA_DIR = Paths.get(System.getProperty("user.dir"), "data").toString();

    private String errorMessage = "";
    private String outputMessage = "";
    private int exitCode;
    private long executionTimeMs;
    private long memoryUsedBytes;
    private long cpuTimeMs;

    public void executeScript() {
        String usersFile = Paths.get(DATA_DIR, "users.txt").toString();
        String loginsFile = Paths.get(DATA_DIR, "logins.csv").toString();
        String bannedFile = Paths.get(DATA_DIR, "banned.json").toString();

        runScript(usersFile, loginsFile, bannedFile);
    }

    public void executeScript(String usersFile, String loginsFile, String bannedFile) {
        runScript(usersFile, loginsFile, bannedFile);
    }

    private void runScript(String usersFile, String loginsFile, String bannedFile) {


        long startTime = System.currentTimeMillis();
        long startMemory = getUsedMemory();
        long startCpuTime = getCpuTime();

        ProcessBuilder processBuilder = new ProcessBuilder("bash", SCRIPT_PATH, usersFile, loginsFile, bannedFile);

        try {
            Process process = processBuilder.start();

            StringBuilder outputBuilder = new StringBuilder();
            StringBuilder errorBuilder = new StringBuilder();

            try (BufferedReader outputReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                    BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {

                String line;
                while ((line = outputReader.readLine()) != null) {
                    outputBuilder.append(line).append("\n");
                }

                while ((line = errorReader.readLine()) != null) {
                    errorBuilder.append(line).append("\n");
                }
            }

            exitCode = process.waitFor();
            outputMessage = outputBuilder.toString();
            errorMessage = errorBuilder.toString();

            // If no stderr but stdout contains error, use stdout
            if (errorMessage.trim().isEmpty() && outputMessage.contains("❌")) {
                errorMessage = outputMessage;
            }

            long endTime = System.currentTimeMillis();
            long endMemory = getUsedMemory();
            long endCpuTime = getCpuTime();

            executionTimeMs = endTime - startTime;
            memoryUsedBytes = endMemory - startMemory;
            cpuTimeMs = (endCpuTime - startCpuTime) / 1_000_000;



        } catch (IOException | InterruptedException e) {
            errorMessage = e.getMessage();
            executionTimeMs = System.currentTimeMillis() - startTime;
        }
    }

    private long getUsedMemory() {
        MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
        return memoryBean.getHeapMemoryUsage().getUsed();
    }

    private long getCpuTime() {
        ThreadMXBean threadBean = ManagementFactory.getThreadMXBean();
        return threadBean.getCurrentThreadCpuTime();
    }

    public int getExitCode() {
        return exitCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public String getOutputMessage() {
        return outputMessage;
    }

    public long getExecutionTimeMs() {
        return executionTimeMs;
    }

    public long getMemoryUsedBytes() {
        return memoryUsedBytes;
    }

    public long getCpuTimeMs() {
        return cpuTimeMs;
    }

    public boolean isPerformanceAcceptable(long maxTimeMs, long maxMemoryBytes) {
        return executionTimeMs <= maxTimeMs && Math.abs(memoryUsedBytes) <= maxMemoryBytes;
    }
}