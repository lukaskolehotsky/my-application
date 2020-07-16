package com.example.behealthy.service;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class FileService {

    private static final String TAG = "FileService";

    private Context context;

    public FileService(Context context) {
        this.context = context;
    }

    public void createFile(String fileName) {
        File file = new File(context.getFilesDir(), fileName);
        if (!file.exists()) {
            try {
                file.createNewFile();
                Log.i(TAG, "FileService.createTextFile() — create file " + fileName);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void saveFile(String text, String fileName) {
        FileOutputStream fos = null;

        try {
            fos = context.openFileOutput(fileName, context.MODE_PRIVATE);
            fos.write(text.getBytes());
            Log.i(TAG, "FileService.save() — save file " + fileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public String loadFile(String fileName) {
        FileInputStream fis = null;

        try {
            fis = context.openFileInput(fileName);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String text;

            while ((text = br.readLine()) != null) {
                sb.append(text).append("\n");
            }
            Log.i(TAG, "FileService.load() — load file " + fileName);
            return sb.toString();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return null;
    }

    public void deleteFile(String fileName) {
        File file = new File(context.getFilesDir(), fileName);
        if (file.exists()) {
            context.deleteFile(fileName);
            Log.i(TAG, "FileService.delete() — delete file " + fileName);
        }
    }

}
