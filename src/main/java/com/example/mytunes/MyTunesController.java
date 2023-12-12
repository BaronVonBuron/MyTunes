package com.example.mytunes;

import javafx.animation.Interpolator;
import javafx.animation.PathTransition;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.util.Duration;

import java.util.List;


public class MyTunesController {
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private Circle circlePath;//bane til label af sang, der afspilles

    @FXML
    private Slider volumeSlider, playTimeSlider;
    private ObservableList allSongs, allPlaylists;
    private List<Song> songsInPlaylist;
    @FXML
    private ListView SongsInPlaylist;
    private Playlist tempPL;

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

    public void initialize() {
        System.out.println("Controller initialized");//Den er her for at vise at controlleren bliver startet ordentligt.
        updateTables();//loader tableviews så sange og playlister er der fra starten af appen
        if (!allPlaylists.isEmpty() && tempPL == null){
            tempPL = (Playlist) allPlaylists.getFirst();
        }

        jmp = new JazzMediaPlayer(logic.getSongs().getFirst(), playTimeSlider);

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
        numberOfSongsInPlaylist.setCellValueFactory(new PropertyValueFactory<>("numberOfSongs"));
        playlistDuration.setCellValueFactory(new PropertyValueFactory<>("duration"));

        AllSongs.refresh();
        AllPlaylists.refresh();
        SongsInPlaylist.refresh();
    }

    public void updateSongsInPlaylist(Playlist playlist){
        this.songsInPlaylist = logic.returnSongsInPlaylist(playlist);
        ObservableList observableSongsInPlaylist = FXCollections.observableList(songsInPlaylist);
        SongsInPlaylist.setItems(observableSongsInPlaylist);
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
    public void allSongsSongClicked(MouseEvent mouseEvent){
        if(mouseEvent.getButton().equals(MouseButton.PRIMARY) && mouseEvent.getClickCount() == 2 && (Song) AllSongs.getSelectionModel().getSelectedItem() != null){
            jmp.setMedia((Song) AllSongs.getSelectionModel().getSelectedItem());
            jmp.setVolume(this.volumeSlider.getValue());
            currentSongPlaying();
        }
    }
    @FXML
    public void songFromPlaylistClicked(MouseEvent mouseEvent) {
        if (mouseEvent.getButton().equals(MouseButton.PRIMARY) && mouseEvent.getClickCount() == 2 && (Song) SongsInPlaylist.getSelectionModel().getSelectedItem() != null) {
            jmp.setMedia((Song) SongsInPlaylist.getSelectionModel().getSelectedItem());
            jmp.setVolume(this.volumeSlider.getValue());
            currentSongPlaying();
        }
    }
    @FXML
    public void backwardsButtonPressed(MouseEvent mouseEvent) {
        playTimeSlider.setValue(0);
        currentSongPlaying();
    }
    @FXML
    public void newPlaylistButtonPressed(ActionEvent event) {
        logic.createPlaylist();
        updateTables();
    }
    @FXML
    public void editPlaylistButtonPressed(ActionEvent event) {
        if ((Playlist) AllPlaylists.getSelectionModel().getSelectedItem() != null) {
            logic.editPlaylist((Playlist) AllPlaylists.getSelectionModel().getSelectedItem());
            updateTables();
        }
    }
    @FXML
    public void deletePlaylistButtonPressed(ActionEvent event) {
        if ((Playlist) AllPlaylists.getSelectionModel().getSelectedItem() != null){
            logic.deletePl((Playlist) AllPlaylists.getSelectionModel().getSelectedItem());
            updateTables();
        }else System.out.println("No playlist selected");
    }
    @FXML
    public void listviewDownButtonPressed(ActionEvent event) {
        //det er jo helt fucked det her
        /*if (SongsInPlaylist.getSelectionModel().getSelectedItem() != null){
            logic.moveSongDownInPlaylist((Song) SongsInPlaylist.getSelectionModel().getSelectedItem(), tempPL);
            updateTables();
        }*/
    }
    @FXML
    public void listviewUpButtonPressed(ActionEvent event) {
    }
    @FXML
    public void deleteSongLWButtonPressed(ActionEvent event) {
        if (SongsInPlaylist.getSelectionModel().getSelectedItem() != null){
            logic.deleteSongFromPlaylist((Song) SongsInPlaylist.getSelectionModel().getSelectedItem(), tempPL);
            updateTables();
            updateSongsInPlaylist(tempPL);
        }
    }
    @FXML
    public void newSongButtonPressed(ActionEvent event) {
        logic.createSong();
        updateTables();
    }
    @FXML
    public void editSongButtonPressed(ActionEvent event) {
        if ((Song) AllSongs.getSelectionModel().getSelectedItem() != null) {
            logic.editSong((Song) AllSongs.getSelectionModel().getSelectedItem());
            updateTables();
        }
    }
    @FXML
    public void deleteSongButtonPressed(ActionEvent event) {
        //Send over selected song to logic, so it can delete it properly
        if ((Song) AllSongs.getSelectionModel().getSelectedItem() != null) {
            logic.deleteSong((Song) AllSongs.getSelectionModel().getSelectedItem());
            updateTables();
        } else System.out.println("No song selected");
    }
    @FXML
    public void volumeDownButtonPressed(MouseEvent mouseEvent) {
        jmp.volumeIncrementDown();
        this.volumeSlider.setValue(jmp.getMediaPlayer().getVolume());
    }
    @FXML
    public void VolumeUpButtonPressed(MouseEvent mouseEvent) {
        jmp.volumeIncrementUp();
        this.volumeSlider.setValue(jmp.getMediaPlayer().getVolume());
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
            tempPL = pl;
            updateSongsInPlaylist(pl);
        }
    } //når man dobble clikker på en playliste kommer der listView op med sangene i den

    public void addSongToPlaylist(ActionEvent actionEvent) {
        if (AllPlaylists.getSelectionModel().getSelectedItem() != null && AllSongs.getSelectionModel().getSelectedItem() != null) {
            Playlist pl = (Playlist) AllPlaylists.getSelectionModel().getSelectedItem();
            logic.addSongToPlaylist(pl, AllSongs.getSelectionModel().getSelectedItem());
            updateTables();
            updateSongsInPlaylist(pl);
        } else System.out.println("Please select a playlist and/or a song");
    }//Når man har valgt en sang og en playliste, og trykker på tilføj, så smides den valgte sang ind i valgte playliste.

    public void searchButtonPressed(MouseEvent mouseEvent) {
    }

    public void searchFieldAction(ActionEvent event) {
    }

    @FXML
    public void volumeSliderMoved(double newValue) {
        jmp.setVolume(newValue);
    }

    public void currentSongPlaying(){//TODO: Vi skal have gjort så den stopper den gamle sangs titel i at snurre når en ny starter.
        String title = "";
        title = jmp.getSong().getTitle();
            char[] chars = title.toCharArray();

            for (int i = 0; i < chars.length; i++) {
                String temp = Character.toString(chars[i]);

                PathTransition pT = new PathTransition();
                pT.setDuration(Duration.seconds(10));
                boolean isM = false;
                if (chars[i] == ' '){
                    circlePath.setRotate(9*i);
                }else if (isM){
                    circlePath.setRotate(4.3*i);
                    isM = false;
                }else if (chars[i] == 'm' || chars[i] == 'M') {
                    isM = true;
                    circlePath.setRotate(4*i);
                }else circlePath.setRotate(4.2*i);

                pT.setPath(circlePath);
                Label label = new Label(temp);
                label.setTextFill(Color.WHITE);
                label.setFont(Font.font(15));
                anchorPane.getChildren().add(label);
                pT.setNode(label);
                pT.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
                pT.setCycleCount(Timeline.INDEFINITE);
                pT.setInterpolator(Interpolator.LINEAR);
                pT.play();
        }
    }


    public void logoPressed(MouseEvent mouseEvent) {

        //stopper sangen
        if (jmp != null){
            jmp.stop();
        }
        //sætter de 2 sliders til standard værdig
        volumeSlider.setValue(0.25);
        playTimeSlider.setValue(0);

        //opdatere tabeler
        updateTables();

        System.out.println("Jazzify blev resetet");
    }

}