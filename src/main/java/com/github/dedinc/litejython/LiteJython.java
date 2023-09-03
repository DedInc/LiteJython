package com.github.dedinc.litejython;

import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

/**
 * LiteJython is a minimalist Python environment setup utility.
 * It downloads, extracts, and prepares a Python environment for execution.
 */
public class LiteJython {
    private static final String LJP_ZIP_URL64 = "https://www.dropbox.com/scl/fi/si7gasclmmqo2strukw5j/LJP64.zip?rlkey=nx8x0wat9ncrm84jsebwavko4&dl=1";
    private static final String LJP_ZIP_URL32 = "https://www.dropbox.com/scl/fi/j3pwa70fg1xabw27fls47/LJP.zip?rlkey=sdk1jx8swh0xd9o3qtz6ytkwl&dl=1";
    private static final Path PACKAGES_PATH = Paths.get(System.getenv("LOCALAPPDATA"), "Packages");
    private static final Path LJP_DIR = PACKAGES_PATH.resolve("LJP");
    private static final Path PYTHON_FILE = LJP_DIR.resolve("python.exe");
    private static final Path LJP_ZIP_PATH = PACKAGES_PATH.resolve("LJP.zip");

    public LiteJython() {
        preparePythonEnvironment();
    }

    public static void preparePythonEnvironment() {
        try {
            Files.createDirectories(PACKAGES_PATH);

            if (!Files.exists(LJP_DIR)) {
                downloadAndExtractZip();
            }

            Files.deleteIfExists(LJP_ZIP_PATH);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to prepare Python environment: " + e.getMessage(), e);
        }
    }

    private static boolean isWindows() {
        return System.getProperty("os.name").toLowerCase().startsWith("win");
    }

    private static boolean is64Bit() {
        String arch = System.getProperty("os.arch").toLowerCase();
        return arch.contains("64") || arch.contains("amd64") || arch.contains("x86_64");
    }

    private static void downloadAndExtractZip() throws IOException {
        if (!isWindows()) {
            throw new UnsupportedOperationException("The current OS is not supported.");
        }

        String downloadURL = is64Bit() ? LJP_ZIP_URL64 : LJP_ZIP_URL32;

        try (InputStream in = new URL(downloadURL).openStream()) {
            Files.copy(in, LJP_ZIP_PATH, StandardCopyOption.REPLACE_EXISTING);
            extractZipFile();
        } catch (IOException e) {
            throw new IOException("Failed to download zip: " + e.getMessage(), e);
        }
    }

    private static void extractZipFile() throws IOException {
        try {
            ZipFile zipFile = new ZipFile(LJP_ZIP_PATH.toString());
            zipFile.extractAll(PACKAGES_PATH.toString());
        } catch (ZipException e) {
            throw new IOException("Failed to extract zip: " + e.getMessage(), e);
        }
    }

    /**
     * Executes a Python script.
     *
     * @param scriptPath The path to the Python script.
     * @return A Thread executing the Python script.
     * @throws IOException If the script file does not exist.
     */
    public Thread executePythonScript(String scriptPath) throws IOException {
        if (!Files.exists(Paths.get(scriptPath))) {
            throw new IOException("Script file " + scriptPath + " does not exist");
        }
        return new Thread(() -> {
            try {
                Process p = Runtime.getRuntime().exec(PYTHON_FILE.toFile().getAbsolutePath() + " " + scriptPath);
                p.waitFor();
            } catch (Exception e) {
                throw new IllegalArgumentException("Failed to execute Python script: " + e.getMessage(), e);
            }
        });
    }

    /**
     * Executes a pip command.
     *
     * @param command The pip command to be executed.
     */
    public void executePip(String command) {
        try {
            Process p = Runtime.getRuntime().exec(PYTHON_FILE.toFile().getAbsolutePath() + " -m pip " + command);
            p.waitFor();
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to execute pip command: " + e.getMessage(), e);
        }
    }
}