package ru.yandex.dunaev.mick.theborschmorning;

public class Photo {
    private String type;
    private String url;

    public Photo(String type, String url) {
        this.type = type;
        this.url = url;
    }

    public String getType() {
        return type;
    }

    public String getUrl() {
        return url;
    }

    public int getSize(){
        return Integer.parseInt(type.substring(type.indexOf('_') + 1));
    }

}
