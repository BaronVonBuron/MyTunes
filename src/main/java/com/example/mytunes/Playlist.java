package com.example.mytunes;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Playlist {

    private String name;
    private List<Song> songs;
    private int duration;

    public Playlist(String name) {
        this.name = name;
        songs = new ArrayList<>();
        this.duration = setDuration();
    }

    public int getDuration() {
        return duration;
    }

    private int setDuration() {
        int totalDuration = 0;
        if (!songs.isEmpty()){
            for (Song s : songs) {
                totalDuration += (int) s.getDuration().toSeconds();
            }
            return totalDuration;
        }else return totalDuration;
    }


    public void addSong(Song s){
        this.songs.add(s);
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Song> getSongs() {
        return songs;
    }

    public void setSongs(List<Song> songs) {
        this.songs = songs;
    }

    @Override
    public String toString() {
        return "Playlist{" +
                "name='" + name + '\'' +
                ", songs=" + songs +
                '}';
    }
}
