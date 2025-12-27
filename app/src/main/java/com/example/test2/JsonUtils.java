package com.example.test2;

import android.content.Context;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;

public class JsonUtils {

    public static List<String> readNamesFromAssets(Context context) {
        List<String> names = new ArrayList<>();
        try {
            InputStream is = context.getAssets().open("names.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            String json = new String(buffer, StandardCharsets.UTF_8);
            JSONArray array = new JSONArray(json);

            for (int i = 0; i < array.length(); i++) {
                names.add(array.getString(i));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return names;
    }
}
