package com.example.mytunes;

public class Song {

    private int ID;
    private String title, author, genre;

    public Song(String title, String author, String genre, int ID) {
        this.ID = ID;
        this.title = title;
        this.author = author;
        this.genre = genre;
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

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
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
                ", author= '" + author + '\'' +
                ", genre= '" + genre + '\'' +
                '}';
    }
}
