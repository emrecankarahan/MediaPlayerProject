package com.example.homework1;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.util.Locale;

public class Music implements Serializable{


    String source;
    Bitmap bitmapImage;
    String title;
    String songName;
    String artistName;
    String duration;

    public Music(String title,String source,String duration) {
        this.source = source;
        this.title = title.substring(0,title.length()-4); // deleting ".MP3"
        this.duration = duration;
        String[] split = this.title.split("-");
        this.songName=split[0].toUpperCase().replace('_',' ');
        if(split.length >= 1){
            this.artistName=split[1].toUpperCase().replace('_',' ');;
        }
        else{
            this.artistName = " ";
        }
    }

    public Bitmap getBitmapImage() {
        return bitmapImage;
    }

    public void setBitmapImage(Bitmap bitmapImage) {
        this.bitmapImage = bitmapImage;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }
}
