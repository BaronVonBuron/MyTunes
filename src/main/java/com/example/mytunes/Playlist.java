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
        setDuration();
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration() {
        int totalDuration = 0;
        if (!this.songs.isEmpty()){
            for (Song s : this.songs) {
                totalDuration += (int) s.getDuration().toSeconds();
            }
            this.duration = totalDuration;
        } else this.duration = 0;
    }


    public void addSong(Song s){
        this.songs.add(s);
        setDuration();
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
                ", songs=" + songs.size() +
                ", duration=" + duration +
                '}';
    }
}
