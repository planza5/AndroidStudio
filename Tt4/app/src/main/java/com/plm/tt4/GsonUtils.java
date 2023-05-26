package com.plm.tt4;

import android.content.Context;
import android.util.Log;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.List;

public class GsonUtils {
    private static final String TAG = "GsonUtils";
    private static final String FILE_NAME = Ctes.ITEM_FILE_NAME;

    public static void saveItemToFile(Context context, Item item, String fileName) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapper.addMixIn(Item.class, ItemMixin.class);

        try {
            String json = mapper.writeValueAsString(item);
            File file = new File(context.getFilesDir(), fileName);
            FileOutputStream outputStream = new FileOutputStream(file);
            outputStream.write(json.getBytes());
            outputStream.close();
            Log.d(TAG, "Item saved to file: " + file.getAbsolutePath());
        } catch (Exception e) {
            Log.e(TAG, "Error saving item to file: " + e.getMessage());
        }
    }

    public static Item loadItemFromFile(Context context, String fileName) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapper.addMixIn(Item.class, ItemMixin.class);

        try {
            File file = new File(context.getFilesDir(), fileName);
            FileInputStream inputStream = new FileInputStream(file);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
            reader.close();
            inputStream.close();

            String json = stringBuilder.toString();
            Item item = mapper.readValue(json, Item.class);

            Log.d(TAG, "Item loaded from file: " + file.getAbsolutePath());
            return item;
        } catch (Exception e) {
            Log.e(TAG, "Error loading item from file: " + e.getMessage());
        }

        return new Item();
    }

    // Clase mixin para manejar referencias circulares en Item
    static abstract class ItemMixin {
        @JsonManagedReference
        abstract List<Item> getChilds();

        @JsonBackReference
        abstract Item getParent();
    }
}
