package com.example.mytunes;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Playlist {

    private String name;
    private List<Song> songs;
    private int duration, numberOfSongs;



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


    public int getNumberOfSongs() {
        return numberOfSongs;
    }

    public void setNumberOfSongs() {
        this.numberOfSongs = songs.size();
    }
    public void addSong(Song s){
        this.songs.add(s);
        setDuration();
        setNumberOfSongs();
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
        setNumberOfSongs();
    }

    public void removeSong(int id) {
        for (Song s : songs) {
            if (s.getID() == id){
                songs.remove(s);
                break;
            }
        }
    }

    @Override
    public String toString() {
        return "Playlist{" +
                "name='" + name + '\'' +
                ", songs=" + songs +
                ", duration=" + duration +
                ", numberOfSongs=" + numberOfSongs +
                '}';
    }
}
