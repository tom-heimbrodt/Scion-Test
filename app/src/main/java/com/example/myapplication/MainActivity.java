package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
        //String appFileDirectory = getFilesDir().getPath();
        String filename = "scion-android-linux_arm_pure_stripped";
        String appFileDirectory = "/data/data/com.example.myapplication";
        String executableFilePath = appFileDirectory + "/" + filename;

        copyAssets(filename);

        File execFile = new File(executableFilePath);
        execFile.setExecutable(true);

        try {
            Process process = Runtime.getRuntime().exec(executableFilePath);
        } catch (IOException e) {
            Log.e("run exe", "Exception: " + e.toString());
        }
    }

    private void copyAssets(String filename) {

        AssetManager assetManager = getAssets();

        Log.d("copy assets", "Attempting to copy this file: " + filename); // + " to: " +       assetCopyDestination);

        String appFileDirectory = "/data/data/com.example.myapplication";
        try {
            InputStream in = assetManager.open(filename);
            Path outputPath = Paths.get(appFileDirectory, filename);
            Files.copy(in, outputPath);
            in.close();
        } catch(IOException e) {
            Log.e("copy assets", "Failed to copy asset file: " + filename, e);
        }

        Log.d("copy assets", "Copy success: " + filename);
    }
}
