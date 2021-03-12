package io.innocent.dream.util;

import net.lingala.zip4j.ZipFile;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LibraryManager {

    private static final String libZipPath = "https://github.com/pluto7073/pretty-much-random-files/raw/main/innocent-natives.zip";
    private static final String[] libs = {
            "assimp.dll",
            "bgfx.dll",
            "glfw.dll",
            "lwjgl.dll",
            "lwjgl_nanovg.dll",
            "lwjgl_nuklear.dll",
            "lwjgl_opengl.dll",
            "lwjgl_par.dll",
            "lwjgl_stb.dll",
            "OpenAL.dll"
    };

    public static void testLibs() {
        List<String> libs = new ArrayList<>(Arrays.asList(LibraryManager.libs));
        System.out.println("Testing for Libraries...");
        File libsDir = new File("libs\\natives");
        if (libsDir.exists() && libsDir.isDirectory()) {
            File[] existingFiles = libsDir.listFiles();
            if (existingFiles != null) {
                for (File f : existingFiles) {
                    String fName = f.getName();
                    if (libs.contains(fName)) {
                        libs.remove(fName);
                    } else {
                        f.delete();
                    }
                }
            } else {
                downloadAllLibs();
                return;
            }
        } else {
            downloadAllLibs();
            return;
        }
        if (libs.size() > 0) {
            downloadLibsFromList(libs);
        } else System.out.println("All Libraries Exist");
    }

    private static File downloadLibsZip() {
        System.out.println("Downloading innocent-libs.zip");
        File zipFile;
        try {
            InputStream in = new URL(libZipPath).openStream();
            zipFile = File.createTempFile("innocent-libs", ".zip");
            Files.copy(in, zipFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to download the required Libraries.\nTry Checking your internet connection then try again", "An Error Has Occurred", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
            zipFile = null;
        }
        return zipFile;
    }

    private static void downloadAllLibs() {
        try {
            File libsZip = downloadLibsZip();
            File libsDir = new File("libs\\natives");
            libsDir.mkdirs();
            String path = libsDir.getAbsolutePath();
            ZipFile zipFile = new ZipFile(libsZip);
            for (String s : libs) {
                System.out.println("Extracting " + s + "...");
                String libPath = path;
                zipFile.extractFile(s, libPath);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to download the required Libraries.\nTry Checking your internet connection then try again", "An Error Has Occurred", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }

    private static void downloadLibsFromList(List<String> libsToDownload) {
        try {
            File libsZip = downloadLibsZip();
            File libsDir = new File("libs\\natives");
            libsDir.mkdirs();
            String path = libsDir.getAbsolutePath();
            ZipFile zipFile = new ZipFile(libsZip);
            for (String s : libsToDownload) {
                System.out.println("Extracting " + s + "...");
                zipFile.extractFile(s, path);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to download the required Libraries.\nTry Checking your internet connection then try again", "An Error Has Occurred", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }

}
