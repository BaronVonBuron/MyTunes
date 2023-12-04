package com.example.mytunes;

import java.io.File;

import javafx.scene.media.Media;
import javafx.util.Duration;


public class Song {

    private final int ID;
    private String title, artist, genre, filename;
    private File file;
    private Media media;
    private javafx.util.Duration duration;
    public Song(String title, String artist, String genre, int ID, String filename) {
        this.ID = ID;
        this.title = title;
        this.artist = artist;
        this.genre = genre;
        this.filename = filename;
        setFile(filename);
        setMedia();
        this.duration = this.media.getDuration();
    }

    private void setMedia() {
        if (this.file != null) {
            try {
                this.media = new Media(this.file.toURI().toString());
                System.out.println("it worked"+this.media.toString());
            } catch (Exception e) {
                System.out.println("Error creating Media object for song with ID: " + getID());
                e.printStackTrace();  // Handle the exception according to your needs
            }
        } else {
            System.out.println("Cannot create Media object for song with ID: " + getID() + " - File is null");
        }
    }


    private void setFile(String filepath){
        if (filepath != null){
            this.file = new File(filepath);
        }else System.out.println("Cannot assign file to song with ID :"+getID()+" - Filepath is problem: "+filepath);
    }


    public File getFile() {
        return file;
    }

    public Duration getDuration() {
        return duration;
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
        return title + " - "+artist+" - "+genre;
    }
}
