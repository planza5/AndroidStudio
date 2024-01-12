package com.plm.mobilenet;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {
    public static File getFile(Context context, String assetName) throws IOException {
        File file = new File(context.getFilesDir(), assetName);

        if (file.exists() && file.length() > 0) {
            return file;
        }

        try (InputStream is = context.getAssets().open(assetName);
             OutputStream os = new FileOutputStream(file)) {
            byte[] buffer = new byte[4 * 1024];
            int read;
            while ((read = is.read(buffer)) != -1) {
                os.write(buffer, 0, read);
            }
            os.flush();
        }
        return new File(context.getFilesDir(), assetName);
    }

    public static List<String> loadLabelList(Context context, String labelPath) throws IOException {
        List<String> labelList = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(context.getAssets().open(labelPath)))) {
            String line;
            while ((line = reader.readLine()) != null) {
                labelList.add(line);
            }
        }
        return labelList;
    }

    public static Bitmap getBitmap(Context context, int id){
        return BitmapFactory.decodeResource(context.getResources(),id);
    }
}
