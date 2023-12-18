package com.example.mytunes;

import java.io.File;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class Playlist {

    private String name;
    private List<Song> songs;
    private int numberOfSongs;
    private Duration duration;


    public Playlist(String name) {
        this.name = name;
        songs = new ArrayList<>();
        setDuration();
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration() {
        double totalDuration = 0;
        if (!this.songs.isEmpty()){
            for (Song s : this.songs) {
                if (s.getDuration() != null) {
                    totalDuration += s.getDuration().toSeconds();
                }
            }
            this.duration = Duration.ofSeconds((long) totalDuration);
        } else this.duration = Duration.ofSeconds(0);
    }


    public int getNumberOfSongs() {
        return numberOfSongs;
    }

    public void setNumberOfSongs() {
        this.numberOfSongs = songs.size();
    }
    public void addSong(Song s){
        if (!songs.isEmpty()) {
            for (int i = 0; i < songs.size(); i++) {
                s.setPosition(i + 2);
            }
        }else s.setPosition(1);
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
        for (Song s : songs) {
            addSong(s);
        }
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
