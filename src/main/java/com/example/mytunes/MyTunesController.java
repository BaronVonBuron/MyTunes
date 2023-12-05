package com.example.mytunes;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.MediaPlayer;

import java.util.List;


public class MyTunesController {

    private ObservableList allSongs, allPlaylists;
    private List<Song> songsInPlaylist;
    @FXML
    private ListView SongsInPlaylist;

    private final Logic logic = new Logic();
    @FXML
    private TableView<Song> AllSongs  = new TableView<>();
    @FXML
    private TableColumn<Song, String> Title = new TableColumn<>();
    @FXML
    private TableColumn<Song, String> Artist = new TableColumn<>();
    @FXML
    private TableColumn<Song, String> Category = new TableColumn<>();
    @FXML
    private TableColumn<Song, String> Time = new TableColumn<>();

    @FXML
    private TableView AllPlaylists = new TableView<>();
    @FXML
    private TableColumn<Playlist, String> Name = new TableColumn<>();
    @FXML
    private TableColumn<Integer, String> numberOfSongsInPlaylist = new TableColumn<>();
    @FXML
    private TableColumn<Integer, String> playlistDuration = new TableColumn<>();

    public void initialize(){
        System.out.println("Controller initialized");
        updateTables();
    }

    public void updateTables(){
        this.allSongs = FXCollections.observableList(logic.getSongs());
        this.allPlaylists = FXCollections.observableList(logic.getPlaylists());
        AllSongs.setItems(allSongs);
        AllPlaylists.setItems(allPlaylists);

        Title.setCellValueFactory(new PropertyValueFactory<>("title"));
        Artist.setCellValueFactory(new PropertyValueFactory<>("artist"));
        Category.setCellValueFactory(new PropertyValueFactory<>("genre"));
        Time.setCellValueFactory(new PropertyValueFactory<>("duration"));


        Name.setCellValueFactory(new PropertyValueFactory<>("name"));
        numberOfSongsInPlaylist.setCellValueFactory(new PropertyValueFactory<>("songs"));
        playlistDuration.setCellValueFactory(new PropertyValueFactory<>("duration"));

        AllSongs.refresh();
        AllPlaylists.refresh();
    }//loader tableviews så sange og playlister er der fra starten af appen

    @FXML
    public void playButtonPressed(MouseEvent mouseEvent) {
        logic.play();
    }
    @FXML
    public void forwardButtonPressed(MouseEvent mouseEvent) {
        logic.nextSong();
    }
    @FXML
    public void backwardsButtonPressed(MouseEvent mouseEvent) {
        logic.replayOrGoBack();
    }
    @FXML
    public void newPlaylistButtonPressed(ActionEvent event) {
        logic.createPlaylist();
        updateTables();
    }
    @FXML
    public void editPlaylistButtonPressed(ActionEvent event) {
        logic.editPlaylist((Playlist) AllPlaylists.getSelectionModel().getSelectedItem());
    }
    @FXML
    public void deletePlaylistButtonPressed(ActionEvent event) {
    }
    @FXML
    public void listviewDownButtonPressed(ActionEvent event) {
    }
    @FXML
    public void listviewUpButtonPressed(ActionEvent event) {
    }
    @FXML
    public void deleteSongLWButtonPressed(ActionEvent event) {
    }
    @FXML
    public void newSongButtonPressed(ActionEvent event) {
        logic.createSong(AllSongs.getScene().getRoot().getScene().getWindow());
        updateTables();
    }
    @FXML
    public void editSongButtonPressed(ActionEvent event) {
        logic.editSong((Song) AllSongs.getSelectionModel().getSelectedItem());
    }
    @FXML
    public void deleteSongButtonPressed(ActionEvent event) {
    }
    @FXML
    public void volumeDownButtonPressed(MouseEvent mouseEvent) {
    }
    @FXML
    public void VolumeUpButtonPressed(MouseEvent mouseEvent) {
    }
    @FXML
    public void muteButtonPressed(MouseEvent mouseEvent) {
    }
    @FXML
    public void playListClicked(MouseEvent mouseEvent){
        if(mouseEvent.getButton().equals(MouseButton.PRIMARY) && mouseEvent.getClickCount() == 2) {
            this.songsInPlaylist = logic.returnSongsInPlaylist((Playlist) AllPlaylists.getSelectionModel().getSelectedItem());
            ObservableList observableSongsInPlaylist = FXCollections.observableList(songsInPlaylist);
            SongsInPlaylist.setItems(observableSongsInPlaylist);
        }
    } //når man dobble clikker på en playliste kommer der listView op med sangene i den

    public void addSongToPlaylist(ActionEvent actionEvent) {
        Playlist pl = (Playlist) AllPlaylists.getSelectionModel().getSelectedItem();
        pl.addSong(AllSongs.getSelectionModel().getSelectedItem());
        logic.addSongToPlaylist(pl, AllSongs.getSelectionModel().getSelectedItem());
    }
}