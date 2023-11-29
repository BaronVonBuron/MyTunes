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
    }


    public void doSomething(String s){
        try {
            Statement database = con.createStatement();
            database.executeUpdate(s);
            System.out.println("Statement: "+s+" Has been executed.");
        } catch (SQLException e) {
            System.out.println("Can't do requested statement: "+s+ " Code: "+ e.getErrorCode()+" Because: " + e.getMessage());
        }
    }

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
    }

    public List<Song> returnAllSongs(){
        List<Song> songs = new ArrayList<>();
        String s = "SELECT * FROM Song";
        try {
            Statement database = con.createStatement();
            ResultSet rs = database.executeQuery(s);
            while (rs.next()){
                String title = rs.getString("title");
                String artist = rs.getString("artist");
                String genre = rs.getString("genre");
                int duration = rs.getInt("duration");
                int ID = rs.getInt("ID");
                songs.add(new Song(title, artist, genre, ID, duration));
            }
            System.out.println("Statement: "+s+" Has been executed.");
        } catch (SQLException e) {
            System.out.println("Can't do requested statement: "+s+ " Because: "+ e.getErrorCode() + e.getMessage());
        }
        return songs;
    }

    public List<Song> returnSongsInPlaylist(Playlist pl){
        List<Song> songsinPL = new ArrayList<>();
        List<Integer> songIDs = new ArrayList<>();
        String s = "SELECT * FROM PlaylistSong WHERE name = '"+ pl.getName() +"'";
        try {
            Statement database = con.createStatement();
            ResultSet rs = database.executeQuery(s);
            while (rs.next()){
                int ID = rs.getInt("ID");
                songIDs.add(ID);
            }
            for (Integer i : songIDs) {
                String sql = "SELECT * FROM Song WHERE ID = "+i;
                ResultSet rs1 = database.executeQuery(sql);
                while (rs1.next()){
                    String title = rs1.getString("title");
                    String artist = rs1.getString("artist");
                    String genre = rs1.getString("genre");
                    int duration = rs1.getInt("duration");
                    int ID = rs1.getInt("ID");
                    songsinPL.add(new Song(title, artist, genre, ID, duration));
                }
            }
            System.out.println("Statement: "+s+" Has been executed.");
        } catch (SQLException e) {
            System.out.println("Can't do requested statement: "+s+ " Because: "+ e.getErrorCode() + e.getMessage());
        }
        return songsinPL;
    }

    public void saveSong(String songTitle, String songArtist, String songGenre){
        String s = "INSERT INTO Song (title, artist, genre) VALUES ('"+songTitle+"', '"+songArtist+"', '"+songGenre+"')";
        doSomething(s);
    }

    public void savePlaylist(String name){
        String s = "INSERT INTO Playlist (name) VALUES ('"+name+"')";
        doSomething(s);
    }


    public void saveSongToPlaylist(String name, int id) {
        String s = "INSERT INTO PlaylistSong (playlist_name, song_id) VALUES ('"+name+"', "+id+")";
        doSomething(s);
    }
}
