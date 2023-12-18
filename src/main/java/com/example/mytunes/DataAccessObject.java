package com.example.mytunes;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DataAccessObject {

    private Connection con;


    public DataAccessObject()  {
        try{
            con = DriverManager.getConnection("jdbc:sqlserver://10.176.111.34:1433;database=MyTunesG3;userName=CSe2023t_t_1;password=CSe2023tT1#23;encrypt=true;trustServerCertificate=true");
        } catch (SQLException e) {
            System.err.println("Can't connect to Database: " + e.getErrorCode() + e.getMessage());
        }
        System.out.println("Forbundet til databasen... ");
    } //opret forbindelse til db og catch exceptions hvis det ikke kan lade sig gøre


    public void updateSomething(String s){
        try {
            Statement database = con.createStatement();
            database.executeUpdate(s);
            System.out.println("Statement: "+s+" Has been executed.");
        } catch (SQLException e) {
            System.out.println("Can't do requested statement: "+s+ " Code: "+ e.getErrorCode()+" Because: " + e.getMessage());
        }
    } //script til at opdatere i db hver gang det skal bruges, så det ikke skrives igen

    public List<Playlist> returnAllPlaylists(){
        List<Playlist> playlists = new ArrayList<>();
        String s = "SELECT * FROM Playlist";
        try {
            Statement database = con.createStatement();
            ResultSet rs = database.executeQuery(s);
            while (rs.next()){
                String name = rs.getString("name");
                playlists.add(new Playlist(name));
            }
            System.out.println("Statement: "+s+" Has been executed.");
        } catch (SQLException e) {
            System.out.println("Can't do requested statement: "+s+ " Because: "+ e.getErrorCode() + e.getMessage());
        }
        return playlists;
    } //load playlisterne fra db indtil der ikke er flere

    public List<Song> returnAllSongs(){//tilføj et kald til duration.
        List<Song> songs = new ArrayList<>();
        String s = "SELECT * FROM Song";
        try {
            Statement database = con.createStatement();
            ResultSet rs = database.executeQuery(s);
            while (rs.next()){
                String title = rs.getString("title");
                String artist = rs.getString("artist");
                String genre = rs.getString("genre");
                String filename = rs.getString("filename");
                int ID = rs.getInt("ID");
                int position = rs.getInt("position");
                int duration = rs.getInt("duration");
                songs.add(new Song(title, artist, genre, ID, filename, position, duration));
            }
            System.out.println("Statement: "+s+" Has been executed.");
        } catch (SQLException e) {
            System.out.println("Can't do requested statement: "+s+ " Because: "+ e.getErrorCode() + e.getMessage());
        }
        return songs;
    } //load sangene fra db indtil der ikke er flere

    public List<Song> returnSongsInPlaylist(Playlist pl){//nøjes med at returnere ID'er på sange, og så håndter det på objekt niveau, istedet for db.
        List<Song> songsinPL = new ArrayList<>();
        List<Integer> songIDs = new ArrayList<>();
        String s = "SELECT * FROM PlaylistSong WHERE playlist_name = '"+ pl.getName() +"'";
        try {
            Statement database = con.createStatement();
            ResultSet rs = database.executeQuery(s);
            while (rs.next()){
                int ID = rs.getInt("song_id");
                songIDs.add(ID);
            }
            for (Integer i : songIDs) {
                String sql = "SELECT * FROM Song WHERE ID = "+i;
                ResultSet rs1 = database.executeQuery(sql);
                while (rs1.next()){
                    String title = rs1.getString("title");
                    String artist = rs1.getString("artist");
                    String genre = rs1.getString("genre");
                    String filename = rs1.getString("filename");
                    int ID = rs1.getInt("ID");
                    int position = rs1.getInt("position");
                    int duration = rs1.getInt("duration");
                    songsinPL.add(new Song(title, artist, genre, ID, filename, position, duration));
                }
            }
            System.out.println("Statement: "+s+" Has been executed.");
        } catch (SQLException e) {
            System.out.println("Can't do requested statement: "+s+ " Because: "+ e.getErrorCode() + e.getMessage());
        }
        return songsinPL;
    } //load alle sange i en valgt playliste fra db indtil der ikke er flere

    public void saveSong(String songTitle, String songArtist, String songGenre, int duration, String filename){
        String s = "INSERT INTO Song (title, artist, genre, duration, filename) VALUES ('"+songTitle+"', '"+songArtist+"', '"+songGenre+"', '"+duration+"', '"+filename+"')";
        updateSomething(s);
    } //Specifikt script til at tilføje sang

    public void savePlaylist(String name){
        String s = "INSERT INTO Playlist (name) VALUES ('"+name+"')";
        updateSomething(s);
    } //Spec. script til at tilføje playlist


    public void saveSongToPlaylist(String name, int id, int position) {
        String s = "INSERT INTO PlaylistSong (playlist_name, song_id, position) VALUES ('"+name+"', "+id+", "+position+")";
        updateSomething(s);
    } //Spec. script til at tilføje en sang til en playlist

    public void deleteSong(int songID){
        String s = "DELETE FROM PlaylistSong WHERE song_id = ('"+songID+"')";
        updateSomething(s);
        String s1 = "DELETE FROM Song WHERE id = ('"+songID+"')";
        updateSomething(s1);
    } //Spec. script to delete a song

    public void deletePlaylist(String name) {
        String s = "DELETE FROM PlaylistSong WHERE playlist_name = ('" + name + "')";
        updateSomething(s);
        String s1 = "DELETE FROM Playlist WHERE name = ('" + name + "')";
        updateSomething(s1);
    } //Spec. script to delete playlist

    public void editPlaylist(String newPlaylistName, String oldPlaylistName) {
        String s = "UPDATE PlaylistSong SET playlist_name = '"+newPlaylistName+"' " + "WHERE playlist_name = '"+oldPlaylistName+"'";
        updateSomething(s);
        String s1 = "UPDATE Playlist SET name = '"+newPlaylistName+"' " + "WHERE name = '"+oldPlaylistName+"'";
        updateSomething(s1);
    }


    public void editSong(String newTitle, String newArtist, String newGenre, int id) {
        String s = "UPDATE Song SET title = '"+newTitle+"', artist = '"+newArtist+"', genre = '"+newGenre+"' WHERE ID = "+id+";";
        updateSomething(s);
    }

    public void deleteSongFromPlaylist(int id) {
        String s = "DELETE FROM PlaylistSong WHERE song_id = "+id;
        updateSomething(s);
    }


    public int getSongPosition(int id, Playlist selectedItem){
        String s = "SELECT * FROM PlaylistSong WHERE song_id = '"+id+"' AND playlist_name = '"+selectedItem.getName()+"'";
        Statement database;
        int pos = 0;
        try {
            database = con.createStatement();
            ResultSet rs = database.executeQuery(s);
            while (rs.next()) {
                pos = rs.getInt("position");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return pos;
    }
}
