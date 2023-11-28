package com.example.mytunes;

import java.util.List;

public class Playlist {

    private String name;
    private List<Song> songs;

    public Playlist(String name) {
        this.name = name;
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
