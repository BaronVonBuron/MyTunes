package com.example.mytunes;


//business logic layer (vidste ikke hvad jeg skulle kalde den.
//Denne klasse skal tage sig af input kontrol, og indeholder metoder til at oprette/redigere playlister og sange.
//Den laver et object af DataAccess - som er dens vej ind i databasen.
//Når der trykkes på en knap i programmet, så sender controlleren besked videre til denne klasse - så controller ikke har adgang til DB uden at gå igennem denne.

import java.util.ArrayList;
import java.util.List;

public class Logic {
    private DataAccessObject dao;
    private List<Playlist> playlists;
    private List<Song> songs;

    public Logic() {
        this.dao = new DataAccessObject();
        this.songs = dao.returnAllSongs();
        this.playlists = dao.returnAllPlaylists();
    }






    public void saveSong(String title, String author, String genre){
        if (!title.isEmpty() && !author.isEmpty()) {
            dao.saveSong(title, author, genre);
            this.songs = dao.returnAllSongs();
        }
    }

    public void savePlaylist(String name){
        if (!name.isEmpty()) {
            dao.savePlaylist(name);
            this.playlists = dao.returnAllPlaylists();
        }
    }

    public void addSongToPlaylist(Playlist playlist, Song song){
        playlist.addSong(song);
        dao.saveSongToPlaylist(playlist.getName(), song.getID());
    }


}
