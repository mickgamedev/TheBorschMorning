package ru.yandex.dunaev.mick.theborschmorning;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PhotoHelper {
    private static List<List<Photo>> photos = new ArrayList<>();
    private static List<Photo> current_sizes;

    public static void addNewPhoto(){
        if(current_sizes != null) photos.add(current_sizes);
        current_sizes = new ArrayList<>();
    }
    public static void addNewSize(String type,String url){
        if(current_sizes == null) current_sizes = new ArrayList<>();
        current_sizes.add(new Photo(type,url));
    }

    public static void parseJSON(JSONObject jsonObject){
        JSONArray list;
        try {
            jsonObject = jsonObject.getJSONObject("response");
            list = jsonObject.getJSONArray("items");
            for(int i = 0;i < list.length(); i++){
                jsonObject = list.getJSONObject(i);
                JSONArray attachments = jsonObject.getJSONArray("attachments");
                for(int j = 0;j < attachments.length();j++){
                    jsonObject = attachments.getJSONObject(j);
                    jsonObject = jsonObject.getJSONObject("photo");
                    PhotoHelper.addNewPhoto();
                    Iterator<String> ll = jsonObject.keys();
                    while(ll.hasNext()){
                        String key = ll.next();
                        if(key.startsWith("photo_")){
                            String url = jsonObject.getString(key);
                            PhotoHelper.addNewSize(key,url);
                        }
                    }

                }
            }
            Log.v("JSON","Разбор ответа");

        } catch (JSONException e) {
            return;
        }
        PhotoHelper.addNewPhoto();
    }

    private static Photo getMaxPhotoSize(List<Photo> photos){
        int max_size = 0;
        Photo ph = null;
        for(Photo photo : photos){
            int size = photo.getSize();
            if(size > max_size) {
                max_size = size;
                ph = photo;
            }
        }
        return ph;
    }

    private static Photo getMidPhotoSize(List<Photo> photos, int upLevel){
        int mid_size = 0;
        Photo ph = null;
        for(Photo photo : photos){
            int size = photo.getSize();
            if(size > mid_size && size < upLevel) {
                mid_size = size;
                ph = photo;
            }
        }
        return ph;
    }

    public static List<PhotoImage> getPhotoImages(){
        List<PhotoImage> photoImages = new ArrayList<>();

        for(List<Photo> phl : photos){
            Photo max_size = getMaxPhotoSize(phl);
            Photo mid_size = getMidPhotoSize(phl,800);
            //Log.v("Sizes"," " + mid_size + " " + max_size);
            photoImages.add(new PhotoImage(mid_size.getUrl(),max_size.getUrl()));
        }

        return photoImages;
    }

}
