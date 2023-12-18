package com.example.mytunes;


import javafx.util.Duration;
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

    // Constructor that accepts both name and songs
    public Playlist(String name, List<Song> songs) {
        this.name = name;
        this.songs = new ArrayList<>(songs);
        setDuration();
    }

    public Duration getDuration() {
        return duration;
    }

    // Calculate and set the duration based on the songs
    public void setDuration(Duration newDuration) {
        this.duration = newDuration;
    }

    public void setDuration() {
        double totalDuration = 0;
        if (!this.songs.isEmpty()) {
            for (Song s : this.songs) {
                if (s.getDuration() != null) {
                    totalDuration += s.getDuration().toSeconds();
                }
            }
            this.duration = Duration.seconds((long) totalDuration);
        } else {
            this.duration = Duration.seconds(0);
        }
    }

    public int getNumberOfSongs() {
        return numberOfSongs;
    }

    public void setNumberOfSongs() {
        this.numberOfSongs = songs.size();
    }

    public void addSong(Song s) {
        if (!songs.isEmpty()) {
            for (int i = 0; i < songs.size(); i++) {
                s.setPosition(i + 2);
            }
        } else {
            s.setPosition(1);
        }
        this.songs.add(s);
        setDuration(); // Update playlist duration when adding a song
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

    // Update the songs list and recalculate the duration
    public void setSongs(List<Song> songs) {
        this.songs.clear();
        this.songs.addAll(songs);
        setDuration(); // Update playlist duration
        setNumberOfSongs();
    }

    public void removeSong(int id) {
        for (Song s : songs) {
            if (s.getID() == id) {
                songs.remove(s);
                setDuration(); // Update playlist duration when removing a song
                setNumberOfSongs();
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