package com.example.mytunes;

import javafx.scene.media.Media;
import javafx.util.Duration;
import java.io.File;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;

public class Song {

    private final int ID;
    private String title, artist, genre, filename;
    private File file;
    private Media media;
    private Duration duration;
    private int position;

    public Song(String title, String artist, String genre, int ID, String filename, int position, int durationInSeconds) {
        this.ID = ID;
        this.title = title;
        this.artist = artist;
        this.genre = genre;
        this.filename = filename;
        this.position = position;
        setFile(filename);
        setMedia();
        this.duration = Duration.seconds(durationInSeconds);
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

    private void setMedia() {
        if (this.file != null) {
            try {
                this.media = new Media(this.file.toURI().toString());
                System.out.println("Media for song: " + this.ID + " - " + this.media.toString());
            } catch (Exception e) {
                System.out.println("Error creating Media object for song with ID: " + getID());
                e.printStackTrace();  // Handle the exception according to your needs
            }
        } else {
            System.out.println("Cannot create Media object for song with ID: " + getID() + " - File is null");
        }
    }

    private void setFile(String filepath) {
        if (filepath != null) {
            this.file = new File(filepath);
            System.out.println("File path for song: " + this.ID + " - " + this.file.getAbsolutePath());
        } else {
            System.out.println("Cannot assign file to song with ID :" + getID() + " - Filepath is null");
        }
    }


    public Duration getDuration() {
        return this.duration;
    }

    public File getFile() {
        return file;
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
        Duration songDuration = getDuration();
        if (songDuration != null) {
            return position + " - " + title + " - " + artist + " - " + (int) songDuration.toSeconds();
        } else {
            return position + " - " + title + " - " + artist + " - Unknown Duration";
        }
    }
}
