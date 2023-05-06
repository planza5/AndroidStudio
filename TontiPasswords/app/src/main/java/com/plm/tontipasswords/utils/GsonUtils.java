package com.plm.tontipasswords.utils;

import com.google.gson.*;
import com.plm.tontipasswords.model.TontiApp;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.nio.charset.StandardCharsets;


public class GsonUtils {
    private static Gson gson;

    public static TontiApp loadAll(File file) throws Exception{
        return gson.fromJson(new FileReader(file), TontiApp.class);
    }

    public static void saveAll(TontiApp app, File file) throws Exception{
        String json=gson.toJson(app);
        FileOutputStream fos=new FileOutputStream(file);
        fos.write(json.getBytes(StandardCharsets.UTF_8));
        fos.flush();
        fos.close();
    }

    static{
        gson = new Gson();
    }
}

