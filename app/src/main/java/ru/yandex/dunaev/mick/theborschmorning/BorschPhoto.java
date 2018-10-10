package ru.yandex.dunaev.mick.theborschmorning;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BorschPhoto {

    private final int upLevel = 800;
    private List<Item> items;

    public List<PhotoImage> getPhotoImages(){
        List<PhotoImage> photoImages = new ArrayList<>();
        for(Item item : items){
            PhotoImage image = item.getPhotoImage();
            if(image != null) photoImages.add(image);
        }
        Log.v("getPhotoImages","return " + photoImages.size() + " photos");
        return photoImages;
    }

    public class Item {
        private List<Attachment> attachments;

        public PhotoImage getPhotoImage(){
            if(attachments == null || attachments.isEmpty()) return null;
            List<Photo> photos = attachments.get(0).getPhotos();
            if(photos.isEmpty()) return null;
            Photo maxPhoto = null, midPhoto = null;
            int max_size = 0, mid_size = 0;
            for(Photo photo : photos){
                int size = photo.getSize();
                if(size > max_size){
                    max_size = size;
                    maxPhoto = photo;
                }
                if(size > mid_size && size < upLevel){
                    mid_size = size;
                    midPhoto = photo;
                }
            }
            if(maxPhoto == null || midPhoto == null) return null;
            return new PhotoImage(midPhoto, maxPhoto);
        }

        public class Attachment{
            private Map<String, String> photo;
            public List<Photo> getPhotos(){
                List<Photo> photos = new ArrayList<>();
                if(photo == null) return photos;
                for(Map.Entry<String,String> entry : photo.entrySet()){
                    if(entry.getKey().startsWith("photo_")) photos.add(new Photo(entry.getKey(),entry.getValue()));
                }
                return photos;
            }
        }
    }

}
