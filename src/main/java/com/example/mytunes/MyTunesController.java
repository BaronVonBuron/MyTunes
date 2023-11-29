package com.example.mytunes;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;


public class MyTunesController {

    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }

    public void playButtonPressed(MouseEvent mouseEvent) {
    }

    public void forwardButtonPressed(MouseEvent mouseEvent) {
    }

    public void backwardsButtonPressed(MouseEvent mouseEvent) {
    }

    public void newPlaylistButtonPressed(ActionEvent event) {
    }

    public void editPlaylistButtonPressed(ActionEvent event) {
    }

    public void deletePlaylistButtonPressed(ActionEvent event) {
    }

    public void listviewDownButtonPressed(ActionEvent event) {
    }

    public void listviewUpButtonPressed(ActionEvent event) {
    }

    public void deleteSongLWButtonPressed(ActionEvent event) {
    }

    public void newSongButtonPressed(ActionEvent event) {
    }

    public void editSongButtonPressed(ActionEvent event) {
    }

    public void deleteSongButtonPressed(ActionEvent event) {
    }

    public void volumeDownButtonPressed(MouseEvent mouseEvent) {
    }

    public void VolumeUpButtonPressed(MouseEvent mouseEvent) {
    }

    public void muteButtonPressed(MouseEvent mouseEvent) {
    }
}