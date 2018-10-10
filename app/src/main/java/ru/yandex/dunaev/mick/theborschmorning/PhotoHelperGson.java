package ru.yandex.dunaev.mick.theborschmorning;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PhotoHelperGson {

    public static List<PhotoImage> parseJSON(JSONObject json){
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();

        try {
            json = json.getJSONObject("response");
        } catch (JSONException e) {
            Log.v("parseJSON","непонятный JSON для разбора");
            return new ArrayList<>();
        }
        return gson.fromJson(json.toString(),BorschPhoto.class).getPhotoImages();
    }
}
