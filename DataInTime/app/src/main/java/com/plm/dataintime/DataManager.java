package com.plm.dataintime;

import android.content.Context;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class DataManager {

    private static final String FILE_NAME = "data.json";

    public static List<Data> loadData(Context context) {
        List<Data> dataList = new ArrayList<>();
        try {
            FileInputStream fis = context.openFileInput(FILE_NAME);
            byte[] data = new byte[fis.available()];
            fis.read(data);
            String json = new String(data, "UTF-8");

            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                Data dataItem = new Data(
                        obj.getString("date"),
                        (float) obj.getDouble("fev1Cvf"),
                        obj.getInt("fev1"),
                        obj.getInt("fev1Tpc"),
                        obj.getInt("cvf"),
                        obj.getInt("cvfTpc"),
                        obj.getInt("fev2575"),
                        obj.getInt("fev2575Tpc")
                );
                dataList.add(dataItem);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataList;
    }

    public static void saveData(Context context, List<Data> dataList) {
        try {
            JSONArray jsonArray = new JSONArray();
            for (Data data : dataList) {
                JSONObject obj = new JSONObject();
                obj.put("date", data.getStringDate());
                obj.put("fev1Cvf", data.getFev1Cvf());
                obj.put("fev1", data.getFev1());
                obj.put("fev1Tpc", data.getFev1Tpc());
                obj.put("cvf", data.getCvf());
                obj.put("cvfTpc", data.getCvfTpc());
                obj.put("fev2575", data.getFev2575());
                obj.put("fev2575Tpc", data.getFev2575Tpc());
                jsonArray.put(obj);
            }

            String json = jsonArray.toString();
            FileOutputStream fos = context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
            fos.write(json.getBytes());
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
