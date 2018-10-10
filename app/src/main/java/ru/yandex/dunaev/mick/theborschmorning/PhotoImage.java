package ru.yandex.dunaev.mick.theborschmorning;

public class PhotoImage {
    private String iconImage;
    private String fullImage;

    public PhotoImage(String iconImage, String fullImage) {
        this.iconImage = iconImage;
        this.fullImage = fullImage;
    }

    public PhotoImage(Photo iconPhoto, Photo fullPhoto){
        iconImage = iconPhoto.getUrl();
        fullImage = fullPhoto.getUrl();
    }

    public String getIconImage() {
        return iconImage;
    }

    public String getFullImage() {
        return fullImage;
    }
}
