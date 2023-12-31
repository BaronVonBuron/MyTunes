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
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.util.Duration;
import javafx.scene.control.TextField;
import java.util.ArrayList;
import java.util.List;


public class MyTunesController {
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private Circle circlePath;//bane til label af sang, der afspilles
    @FXML
    private TextField searchField;
    @FXML
    private Slider volumeSlider, playTimeSlider;
    private ObservableList allSongs, allPlaylists;
    private List<Song> songsInPlaylist;
    private List<PathTransition> pathTransitions = new ArrayList<>();

    @FXML
    private Label timeLeftLabel, timePlayedLabel;
    @FXML
    private TableView SongsInPlaylist;
  
    private Playlist tempPL;
    @FXML
    private ImageView searchButton;
    private final Logic logic = new Logic();
    private JazzMediaPlayer jmp;
    @FXML
    TableColumn<Song, Integer> playlistPosition = new TableColumn<>();
    @FXML
    TableColumn<Song, String> playlistTitle = new TableColumn<>();
    @FXML
    TableColumn<Song, Duration> playlistSongDuration = new TableColumn<>();

    @FXML
    private TableView<Song> AllSongs  = new TableView<>();
    @FXML
    private TableColumn<Song, String> Title = new TableColumn<>();
    @FXML
    private TableColumn<Song, String> Artist = new TableColumn<>();
    @FXML
    private TableColumn<Song, String> Category = new TableColumn<>();
    @FXML
    private TableColumn<Song, Duration> Time = new TableColumn<>();

    @FXML
    private TableView AllPlaylists = new TableView<>();
    @FXML
    private TableColumn<Playlist, String> Name = new TableColumn<>();
    @FXML
    private TableColumn<Playlist, Integer> numberOfSongsInPlaylist = new TableColumn<>();
    @FXML
    private TableColumn<Playlist, Duration> playlistDuration = new TableColumn<>();

    public void initialize() {
        System.out.println("Controller initialized");//Den er her for at vise at controlleren bliver startet ordentligt.
        updateTables();//loader tableviews så sange og playlister er der fra starten af appen
        if (!allPlaylists.isEmpty() && tempPL == null){
            tempPL = (Playlist) allPlaylists.getFirst();
        }

        jmp = new JazzMediaPlayer(playTimeSlider, timeLeftLabel, timePlayedLabel, this, logic.getSongs());

        volumeSlider.setValue(0.25);
        volumeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            volumeSliderMoved(newValue.doubleValue());
        });

    }

    public void updateTables() {

        this.allSongs = FXCollections.observableList(logic.getSongs());
        this.allPlaylists = FXCollections.observableList(logic.getPlaylists());

        AllSongs.setItems(allSongs);
        AllPlaylists.setItems(allPlaylists);

        Title.setCellValueFactory(new PropertyValueFactory<>("title"));
        Artist.setCellValueFactory(new PropertyValueFactory<>("artist"));
        Category.setCellValueFactory(new PropertyValueFactory<>("genre"));
        Time.setCellValueFactory(new PropertyValueFactory<>("duration"));
        Time.setCellFactory(column -> new TimeTableCell<>());

        Name.setCellValueFactory(new PropertyValueFactory<>("name"));
        numberOfSongsInPlaylist.setCellValueFactory(new PropertyValueFactory<>("numberOfSongs"));
        playlistDuration.setCellValueFactory(new PropertyValueFactory<>("duration"));
        playlistDuration.setCellFactory(column -> new TimeTableCell<>());

        AllSongs.refresh();
        AllSongs.getSortOrder().add(Title);
        AllPlaylists.refresh();
        AllPlaylists.getSortOrder().add(Name);
        SongsInPlaylist.refresh();

        // Calculate and set playlist duration
        for (Playlist playlist : logic.getPlaylists()) {
            Duration totalDuration = calculatePlaylistDuration(playlist);
            playlist.setDuration(totalDuration);
        }
    }

    private Duration calculatePlaylistDuration(Playlist playlist) {
        List<Song> songs = logic.returnSongsInPlaylist(playlist);
        Duration totalDuration = Duration.ZERO;
        for (Song song : songs) {
            totalDuration = totalDuration.add(song.getDuration());
        }
        return totalDuration;
    }

    public Slider getVolumeSlider() {
        return volumeSlider;
    }

    public void updateSongsInPlaylist(Playlist playlist){
        this.songsInPlaylist = logic.returnSongsInPlaylist(playlist);
        ObservableList observableSongsInPlaylist = FXCollections.observableList(songsInPlaylist);
        SongsInPlaylist.setItems(observableSongsInPlaylist);

        playlistPosition.setCellValueFactory(new PropertyValueFactory<>("position"));
        playlistTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        playlistSongDuration.setCellValueFactory(new PropertyValueFactory<>("duration"));
        playlistSongDuration.setCellFactory(column -> new TimeTableCell<>());

        SongsInPlaylist.getSortOrder().add(playlistPosition);
    }

    @FXML
    public void playButtonPressed(MouseEvent mouseEvent) {
        jmp.play();
        jmp.setVolume(volumeSlider.getValue());
        System.out.println("playbutton pressed");
    }
    @FXML
    public void forwardButtonPressed(MouseEvent mouseEvent) {
        jmp.playNextSong();
        playTimeSlider.setValue(0);
        currentSongPlaying();
        System.out.println("NY SANG");
        System.out.println(jmp.getAllSongs());
    }

    @FXML
    public void allSongsSongClicked(MouseEvent mouseEvent){
        if(mouseEvent.getButton().equals(MouseButton.PRIMARY) && mouseEvent.getClickCount() == 2 && (Song) AllSongs.getSelectionModel().getSelectedItem() != null){
            jmp.setAllSongs(logic.getSongs());
            jmp.setMedia((Song) AllSongs.getSelectionModel().getSelectedItem());
            jmp.setVolume(this.volumeSlider.getValue());
            currentSongPlaying();
        }
    }
    @FXML
    public void songFromPlaylistClicked(MouseEvent mouseEvent) {
        if (mouseEvent.getButton().equals(MouseButton.PRIMARY) && mouseEvent.getClickCount() == 2 && (Song) SongsInPlaylist.getSelectionModel().getSelectedItem() != null) {
            jmp.setAllSongs(songsInPlaylist);
            jmp.setMedia((Song) SongsInPlaylist.getSelectionModel().getSelectedItem());
            jmp.setVolume(this.volumeSlider.getValue());
            currentSongPlaying();
        }
    }
    @FXML
    public void backwardsButtonPressed(MouseEvent mouseEvent) {
        if (playTimeSlider.getValue() < 5){
            jmp.playPreviousSong();
            currentSongPlaying();
            System.out.println("Spiller tidligere sang");
        } else {
            playTimeSlider.setValue(0);
            currentSongPlaying();
            System.out.println("starter sang forfra");
        }
    }
    @FXML
    public void newPlaylistButtonPressed(ActionEvent event) {
        logic.createPlaylist();
        updateTables();
        if (!allPlaylists.isEmpty()) {
            AllPlaylists.scrollTo(allPlaylists.size() - 1);
        }
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
    public void songInPlaylistDownButtonPressed(ActionEvent event) {
        if (SongsInPlaylist.getSelectionModel().getSelectedItem() != null) {
            Song s = (Song) SongsInPlaylist.getSelectionModel().getSelectedItem();
            if (songsInPlaylist.getLast() != s){
                logic.moveSongDownInPlaylist(s, tempPL);
                updateSongsInPlaylist(tempPL);
            }
        }
    }
    @FXML
    public void songInPlaylistUpButtonPressed(ActionEvent event) {
        if (SongsInPlaylist.getSelectionModel().getSelectedItem() != null) {
            Song s = (Song) SongsInPlaylist.getSelectionModel().getSelectedItem();
            if (songsInPlaylist.getFirst() != s){
                logic.moveSongUpInPlaylist(s, tempPL);
                updateSongsInPlaylist(tempPL);
            }
        }
    }


    @FXML
    public void deleteSongLWButtonPressed(ActionEvent event) {
        if(jmp.getSong() != SongsInPlaylist.getSelectionModel().getSelectedItem()) {
            if (SongsInPlaylist.getSelectionModel().getSelectedItem() != null) {
                logic.deleteSongFromPlaylist((Song) SongsInPlaylist.getSelectionModel().getSelectedItem(), tempPL);
                updateTables();
                updateSongsInPlaylist(tempPL);
            }
        }else System.out.println("Sangen der afspilles må ikke slettes");
    }
    @FXML
    public void newSongButtonPressed(ActionEvent event) {
        logic.createSong();
        AllSongs.scrollTo(allSongs.size()-1);
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
            SongsInPlaylist.scrollTo(songsInPlaylist.size()-1);
        } else System.out.println("Please select a playlist and/or a song");
    }//Når man har valgt en sang og en playliste, og trykker på tilføj, så smides den valgte sang ind i valgte playliste.

    public void searchButtonPressed(MouseEvent mouseEvent) {
        System.out.println("knappen virker");
        if (searchField != null){
            String searchText = searchField.getText().toLowerCase();

            List<Song> searchResults = new ArrayList<>();

            for (Song song : logic.getSongs()) {
                if (song.getArtist().toLowerCase().contains(searchText)||
                    song.getTitle().toLowerCase().contains(searchText) ||
                    song.getGenre().toLowerCase().contains(searchText)) {

                    if (!searchResults.contains(song)) {
                        searchResults.add(song);
                    }
                }
            }

            ObservableList<Song> observableSearchResults = FXCollections.observableList(searchResults);
            AllSongs.setItems(observableSearchResults);
        } else {
            System.out.println("whata motherfucka");
        }

    }

    public void searchFieldAction(ActionEvent event) {
        String searchText = searchField.getText().toLowerCase();

        List<Song> searchResults = new ArrayList<>();

        for (Song song : logic.getSongs()) {
            if (song.getArtist().toLowerCase().contains(searchText)||
                    song.getTitle().toLowerCase().contains(searchText) ||
                    song.getGenre().toLowerCase().contains(searchText)) {

                if (!searchResults.contains(song)) {
                    searchResults.add(song);
                }
            }
        }

        ObservableList<Song> observableSearchResults = FXCollections.observableList(searchResults);
        AllSongs.setItems(observableSearchResults);
    }

    @FXML
    public void volumeSliderMoved(double newValue) {
        jmp.setVolume(newValue);
    }

    public void currentSongPlaying(){
        stopPT();

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
                pathTransitions.add(pT);
        }
    }

    private void stopPT(){
        for (PathTransition pt : pathTransitions){
            pt.stop();
            anchorPane.getChildren().remove(pt.getNode());
        }
        pathTransitions.clear();
    }


    public void logoPressed(MouseEvent mouseEvent) {

        //stopper sangen
        if (jmp != null){
            jmp.stop();
        }
        //sætter de 2 sliders til standard værdi
        volumeSlider.setValue(0.25);
        playTimeSlider.setValue(0);
        //Stopper Pathtransition og opdaterer tabeller
        stopPT();
        updateTables();
        System.out.println("Jazzify blev resettet");
    }

}