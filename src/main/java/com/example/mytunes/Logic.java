package com.example.mytunes;


//business logic layer (vidste ikke hvad jeg skulle kalde den)
//Denne klasse skal tage sig af input kontrol, og indeholder metoder til at oprette/redigere playlister og sange.
//Den laver et object af DataAccess - som er dens vej ind i databasen.
//Når der trykkes på en knap i programmet, så sender controlleren besked videre til denne klasse - så controller ikke har adgang til DB uden at gå igennem denne.

import javafx.scene.media.Media;
import javafx.util.Duration;

import java.io.File;
import java.util.List;
import java.util.Objects;

public class Logic {
    private DataAccessObject dao;
    private List<Playlist> playlists;
    private List<Song> songs;

    public Logic() {
        this.dao = new DataAccessObject();
        this.songs = dao.returnAllSongs();
        this.playlists = dao.returnAllPlaylists();
    }

    public void saveSong(String title, String author, String genre, int duration, String filename){
        if (!title.isEmpty() && !author.isEmpty()) {
            dao.saveSong(title, author, genre, duration, filename);
            this.songs = dao.returnAllSongs();
        }
    }

    public List<Playlist> getPlaylists() {
        for (Playlist p : playlists) {
            p.setDuration();
        }
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

    private Duration getMediaDuration(String filePath) {
        try {
            // Use a Media object to get the duration
            Media media = new Media(new File(filePath).toURI().toString());
            return media.getDuration();
        } catch (Exception e) {
            e.printStackTrace();
            return Duration.UNKNOWN; // or handle the exception as needed
        }
    }

    public void createSong() {
        DialogWindow dialogWindow = new DialogWindow(false, "", "", "", "");

        String selectedFilePath = dialogWindow.getMedia_uri();


        if (selectedFilePath != null && !selectedFilePath.isEmpty()) {
            // Get the duration of the selected media file
            Duration mediaDuration = getMediaDuration(selectedFilePath);


            String title = dialogWindow.getInputTitle();
            String artist = dialogWindow.getInputAuthor();
            String genre = dialogWindow.getInputGenre();

            if (title == null) {
                title = "";
            }
            if (artist == null) {
                artist = "";
            }
            if (genre == null) {
                genre = "";
            }

            // Pass the actual duration to the saveSong method
            saveSong(title, artist, genre, (int) mediaDuration.toSeconds(), selectedFilePath);
        }
    }


    public void editSong(Song song) {
        DialogWindow dw = new DialogWindow(false, song.getTitle(), song.getArtist(), song.getGenre(), song.getFile().getName());
        //Create a call to DAO to update song (either update or delete and save new).
    }

    public void editPlaylist(Playlist pl) {
        DialogWindow editPlaylistDialog = new DialogWindow(true, pl.getName());
        String newPlaylistName = editPlaylistDialog.getInputName();

        if (newPlaylistName != null && !newPlaylistName.isEmpty()) {
            System.out.println("Playlist edited: " + newPlaylistName);
            savePlaylist(newPlaylistName);
        }
    }

    public List<Song> returnSongsInPlaylist(Playlist selectedItem) {
            return dao.returnSongsInPlaylist(selectedItem);
    }

    public void deleteSong(Song song){
        DialogWindow dialogWindow = new DialogWindow(false, true);
        if (dialogWindow.isDeleteOK()){
            dao.deleteSong(song.getID());
            Song tempSong = null;
            for (Song s: songs){
                if (song.getID() == s.getID()){
                    tempSong = s;
                }
            }
            songs.remove(tempSong);
            System.out.println("Song deleted from Logic");
        }
    }

    public void deletePl(Playlist playlist){
        DialogWindow dialogWindow = new DialogWindow(true, true);
        if (dialogWindow.isDeleteOK()){
            dao.deletePlaylist(playlist.getName());
            Playlist tempPlaylist = null;
            for (Playlist p: playlists){
                if (p.getName() == playlist.getName()){
                    tempPlaylist = p;
                }
            }
            playlists.remove(tempPlaylist);
            System.out.println("Song deleted from Logic");
        }

    }
}
