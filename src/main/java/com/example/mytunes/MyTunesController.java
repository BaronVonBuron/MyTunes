package com.example.mytunes;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import java.util.List;


public class MyTunesController {

    @FXML
    private Slider volumeSlider;
    private ObservableList allSongs, allPlaylists;
    private List<Song> songsInPlaylist;
    @FXML
    private ListView SongsInPlaylist;

    private final Logic logic = new Logic();
    private JazzMediaPlayer jmp;
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
    private TableColumn<Playlist, Integer> numberOfSongsInPlaylist = new TableColumn<>();
    @FXML
    private TableColumn<Playlist, Integer> playlistDuration = new TableColumn<>();

    public void initialize(){
        System.out.println("Controller initialized");//Den er her for at vise at controlleren bliver startet ordentligt.
        updateTables();//loader tableviews så sange og playlister er der fra starten af appen
        jmp = new JazzMediaPlayer("C:\\Users\\andbu\\OneDrive\\Dokumenter\\GitHub\\MyTunes\\Billie_Holiday_-_Blue_Moon.mp3");

        volumeSlider.setValue(0.25);

        volumeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            volumeSliderMoved(newValue.doubleValue());
        });
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
        SongsInPlaylist.refresh();
    }

    @FXML
    public void playButtonPressed(MouseEvent mouseEvent) {
        jmp.setVolume(volumeSlider.getValue());
        jmp.play();
        System.out.println("playbutton pressed");
    }
    @FXML
    public void forwardButtonPressed(MouseEvent mouseEvent) {
    }
    @FXML
    public void backwardsButtonPressed(MouseEvent mouseEvent) {
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
        updateTables();
    }
    @FXML
    public void deleteSongButtonPressed(ActionEvent event) {
    }
    @FXML
    public void volumeDownButtonPressed(MouseEvent mouseEvent) {
        jmp.volumeIncrementDown();
    }
    @FXML
    public void VolumeUpButtonPressed(MouseEvent mouseEvent) {
        jmp.volumeIncrementUp();
    }
    @FXML
    public void muteButtonPressed(MouseEvent mouseEvent) {
        jmp.mute();
    }
    @FXML
    public void playListClicked(MouseEvent mouseEvent){
        if(mouseEvent.getButton().equals(MouseButton.PRIMARY) && mouseEvent.getClickCount() == 2 && AllPlaylists.getSelectionModel().getSelectedItem() != null) {
            Playlist pl = (Playlist) AllPlaylists.getSelectionModel().getSelectedItem();
            pl.setDuration();
            this.songsInPlaylist = logic.returnSongsInPlaylist(pl);
            ObservableList observableSongsInPlaylist = FXCollections.observableList(songsInPlaylist);
            SongsInPlaylist.setItems(observableSongsInPlaylist);
        }
    } //når man dobble clikker på en playliste kommer der listView op med sangene i den

    public void addSongToPlaylist(ActionEvent actionEvent) {
        if (AllPlaylists.getSelectionModel().getSelectedItem() != null) {
            Playlist pl = (Playlist) AllPlaylists.getSelectionModel().getSelectedItem();
            pl.addSong(AllSongs.getSelectionModel().getSelectedItem());
            logic.addSongToPlaylist(pl, AllSongs.getSelectionModel().getSelectedItem());
            pl.setDuration();
            updateTables();
        } else System.out.println("Please select a playlist");
    }//Når man har valgt en sang og en playliste, og trykker på tilføj, så smides den valgte sang ind i valgte playliste.

    public void searchButtonPressed(MouseEvent mouseEvent) {
    }

    public void searchFieldAction(ActionEvent event) {
    }

    @FXML
    public void volumeSliderMoved(double newValue) {
        jmp.setVolume(newValue);
    }
}