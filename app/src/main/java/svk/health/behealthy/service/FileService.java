package svk.health.behealthy.service;

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

    private static final String TAG = "FileService.class";

    private Context context;

    public FileService(Context context) {
        this.context = context;
    }

    public void createFile(String fileName) {
        Log.i(TAG, "createFile: fileName=" + fileName);

        File file = new File(context.getFilesDir(), fileName);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void saveFile(String text, String fileName) {
        Log.i(TAG, "saveFile: text=" + text + ", fileName=" + fileName);

        FileOutputStream fos = null;

        try {
            fos = context.openFileOutput(fileName, context.MODE_PRIVATE);
            fos.write(text.getBytes());
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
        Log.i(TAG, "loadFile: fileName=" + fileName);

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
            return sb.toString();
        } catch (FileNotFoundException e) {
            Log.e(TAG, "loadFile: fileName=" + fileName + " FileNotFoundException");
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
        Log.i(TAG, "deleteFile: fileName=" + fileName);

        File file = new File(context.getFilesDir(), fileName);
        if (file.exists()) {
            context.deleteFile(fileName);
        }
    }

}
