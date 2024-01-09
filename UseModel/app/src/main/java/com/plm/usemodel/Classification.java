package com.plm.usemodel;

import android.content.Context;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Classification {
    private static List<String> labels;

    private static void loadLabels(Context context) {
        try (InputStream is = context.getAssets().open("imagenet_labels.txt");
             BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
            String line;
            while ((line = reader.readLine()) != null) {
                labels.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getLabel(Context context, int index) {
        if(labels==null){
            labels = new ArrayList<>();
            loadLabels(context);
        }

        if (index >= 0 && index < labels.size()) {
            return labels.get(index);
        } else {
            return "Unknown class";
        }
    }
}
