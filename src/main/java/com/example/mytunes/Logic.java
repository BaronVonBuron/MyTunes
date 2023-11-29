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
    private DialogWindow dw;

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

    public List<Playlist> getPlaylists() {
        return playlists;
    }

    public List<Song> getSongs() {
        return songs;
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


    public void play() {
        //afspil sang eller pause - alt efter om der kører en sang nu eller ej.
    }

    public void nextSong() {
        //stop nuværende sang, og så videre til næste sang på playlisten.
    }

    public void replayOrGoBack() {
        //tilbageknap - replay ved ét tryk, gå til forrige sang ved dobbelttryk. Hvis sangen har kørt i under 5 sekunder kræves kun ét tryk for at gå tilbage.
    }

    public void createPlaylist() {

        DialogWindow createPlaylistDialog = new DialogWindow(true);
        String playlistName = createPlaylistDialog.getInputName();

        if (playlistName != null && !playlistName.isEmpty()) {
            System.out.println("Playlist created: " + playlistName);
            savePlaylist(playlistName);
        }
    }


    public void createSong() {
        DialogWindow addSongDialog = new DialogWindow(false);
        String songTitle = addSongDialog.getInputTitle();
        String songAuthor = addSongDialog.getInputAuthor();
        String songGenre = addSongDialog.getInputGenre();

        if (songTitle != null && !songTitle.isEmpty() && songAuthor != null && !songAuthor.isEmpty() && songGenre != null && !songGenre.isEmpty()) {
            System.out.println("Song added: " + songTitle + " by " + songAuthor + " (Genre: " + songGenre + ")");
            saveSong(songTitle, songAuthor, songGenre);
        }

    }


    public void editSong(Song song) {

    }

    public void editPlaylist(Playlist pl) {
        DialogWindow editPlaylistDialog = new DialogWindow(true, pl.getName());
        String newPlaylistName = editPlaylistDialog.getInputName();

        if (newPlaylistName != null && !newPlaylistName.isEmpty()) {
            System.out.println("Playlist edited: " + newPlaylistName);
            savePlaylist(newPlaylistName);
        }
    }
}
