package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        runExecutable();
    }

    void runExecutable() {
        String execFilename = "scion-android-linux_amd32_stripped";
        String dispFilename = "disp.toml";
        String appFileDirectory = "/data/data/com.example.myapplication";

        String execPath = appFileDirectory + "/" + execFilename;
        String dispPath = appFileDirectory + "/" + dispFilename;

        copyAssets(execFilename, execPath);
        copyAssets(dispFilename, dispPath);

        File execFile = new File(execPath);
        boolean success = execFile.setExecutable(true);
        Log.d("RUN EXE", "Set executable flag says: " + success);

        try {
            ProcessBuilder builder = new ProcessBuilder(
                    execPath,
                    "godispatcher",
                    "-lib_env_config",
                    dispPath);
            builder.redirectErrorStream(true);
            Process process = builder.start();

            Log.d("RUN EXE", "Successfully started exe.");

            BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = input.readLine()) != null) {
                Log.e("EXEC SAYS", line);
            }
            input.close();
        } catch (IOException e) {
            Log.e("RUN EXE", "Exception: " + e.toString());
        }
    }

    private void copyAssets(String filename, String destinationPath) {

        AssetManager assetManager = getAssets();

        Log.d("ASSETS COPY", "Attempting to copy this file: " + filename + " to: " + destinationPath);

        try {
            InputStream in = assetManager.open(filename);
            Path outputPath = Paths.get(destinationPath);
            Files.copy(in, outputPath);
            in.close();
            Log.d("ASSETS COPY", "Copy success: " + filename);
        } catch(IOException e) {
            Log.w("ASSETS COPY", "Failed to copy asset file: " + e.toString());
        }
    }
}
