package com.example.mytunes;

import javafx.util.Duration;

public class Song {

    private int ID;
    private int duration;
    private String title, artist, genre;

    public Song(String title, String artist, String genre, int ID, int duration) {
        this.ID = ID;
        this.title = title;
        this.artist = artist;
        this.genre = genre;
        this.duration = duration;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getID() {
        return ID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }


    @Override
    public String toString() {
        return "Song{" +
                " ID= " + ID +
                ", title= '" + title + '\'' +
                ", author= '" + artist + '\'' +
                ", genre= '" + genre + '\'' +
                '}';
    }
}
