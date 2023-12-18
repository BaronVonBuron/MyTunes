package com.example.mytunes;


//business logic layer (vidste ikke hvad jeg skulle kalde den)
//Denne klasse skal tage sig af input kontrol, og indeholder metoder til at oprette/redigere playlister og sange.
//Den laver et object af DataAccess - som er dens vej ind i databasen.
//Når der trykkes på en knap i programmet, så sender controlleren besked videre til denne klasse - så controller ikke har adgang til DB uden at gå igennem denne.

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import java.io.File;
import java.util.Collections;
import java.util.List;

public class Logic {
    private DataAccessObject dao;
    private List<Playlist> playlists;
    private List<Song> songs;

    public Logic() {
        this.dao = new DataAccessObject();
        this.songs = dao.returnAllSongs();
        this.playlists = dao.returnAllPlaylists();
        for (Playlist p : playlists) {
            p.setSongs(dao.returnSongsInPlaylist(p));
        }
    }

    public void saveSong(String title, String author, String genre, int duration, String filename){
        if (!title.isEmpty() && !author.isEmpty()) {

            dao.saveSong(title, author, genre, duration,  filename);
            this.songs = dao.returnAllSongs();
        }
    }

    public List<Playlist> getPlaylists() {
        for (Playlist p : playlists) {
            p.setDuration();
            p.setNumberOfSongs();
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
        dao.saveSongToPlaylist(playlist.getName(), song.getID(), playlist.getSongs().size());
        playlist.setNumberOfSongs();
    }


    public void play() {
        //afspil sang eller pause - alt efter om der kører en sang nu eller ej.
    }

    public void nextSong() {
        //stop nuværende sang, og så videre til næste sang på playlisten.
        //tror bare denne her skal slettes - mvh. Jacob
    }

    public void replayOrGoBack() {
        //tilbageknap - replay ved ét tryk, gå til forrige sang ved dobbelttryk. Hvis sangen har kørt i under 5 sekunder kræves kun ét tryk for at gå tilbage.
        //tror bare denne her skal slettes - mvh. Jacob
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
        DialogWindow dialogWindow = new DialogWindow(false, "", "", "", "");

        String selectedFilePath = dialogWindow.getMedia_uri();


        if (selectedFilePath != null && !selectedFilePath.isEmpty()) {
            //tilføj en mediaplayer, så man kan få duration fre denne.
            //når mediaplayer er ready, så få duration, og send den videre til savesong.
            //Dette er det ENESTE tidspunkt, der må ordnes duration.
            String title = dialogWindow.getInputTitle();
            String artist = dialogWindow.getInputAuthor();
            String genre = dialogWindow.getInputGenre();
            int duration = dialogWindow.getSongDuration();

            if (title == null) {
                title = "";
            }
            if (artist == null) {
                artist = "";
            }
            if (genre == null) {
                genre = "";
            }


            saveSong(title, artist, genre, duration, selectedFilePath);//tilføj duration
        }
    }


    public void editSong(Song song) {
        DialogWindow dw = new DialogWindow(false, song.getTitle(), song.getArtist(), song.getGenre(), song.getFile().getName());
        String oldTitle = song.getTitle();
        String oldArtist = song.getArtist();
        String oldGenre = song.getGenre();
        dao.editSong(dw.getInputTitle(), dw.getInputAuthor(), dw.getInputGenre(), song.getID());
    }

    public void editPlaylist(Playlist pl) {
        DialogWindow editPlaylistDialog = new DialogWindow(true, pl.getName());
        String newPlaylistName = editPlaylistDialog.getInputName();

        if (newPlaylistName != null && !newPlaylistName.isEmpty()) {
            dao.editPlaylist(newPlaylistName, pl.getName());
            System.out.println("Playlist edited: " + newPlaylistName);
            pl.setName(newPlaylistName);
            System.out.println("Playlist name edited");
        }
    }

    public List<Song> returnSongsInPlaylist(Playlist selectedItem){
        List<Song> templist = dao.returnSongsInPlaylist(selectedItem);
        if (!templist.isEmpty()){
            for (Song s : templist) {
                s.setPosition(dao.getSongPosition(s.getID(), selectedItem));
            }
        }
            return templist;
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
            System.out.println("Playlist deleted from Logic");
        }

    }

    public void deleteSongFromPlaylist(Song song, Playlist tempPL) {
        dao.deleteSongFromPlaylist(song.getID());
        tempPL.removeSong(song.getID());
        tempPL.setNumberOfSongs();
    }

    public void moveSongDownInPlaylist(Song song, Playlist playlist) {
        if (playlist.getSongs().size() > 1 && playlist.getSongs().getLast() != song){
            int currentIndex = playlist.getSongs().indexOf(song);
            int newIndex = playlist.getSongs().indexOf(song) + 1;
            System.out.println("current index: "+currentIndex+" new index: "+newIndex);
            //Collections.swap(playlist.getSongs(), currentIndex, newIndex);
        }
    }

}
