package com.example.mytunes;

import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Duration;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Optional;

public class DialogWindow {

    private String inputName;
    private String inputTitle;
    private String inputAuthor;
    private String inputGenre;
    private String media_uri;

    // Empty constructor
    public DialogWindow(){

    }

    // Constructor for playlist creation
    public DialogWindow(boolean isPlaylist) {
        if (isPlaylist) {
            showPlaylistDialog("Create Playlist", null, "Enter playlist name:");
        } else {
            editSongDialog("Add Song", null, "Enter song title:", "Enter song author:", "Enter song genre:");
        }
    }

    // Constructor for editing a playlist
    public DialogWindow(boolean isPlaylist, String playlistName) {
        if (isPlaylist) {
            showPlaylistDialog("Edit Playlist", null, "Enter new name for the playlist:", playlistName);
        } else {
            System.out.println("Please select a playlist");
        }
    }

    // Constructor for editing a song
    public DialogWindow(boolean isPlaylist, String songTitle, String songAuthor, String songGenre) {
        if (!isPlaylist) {
            editSongDialog("Edit Song", null, songTitle, songAuthor, songGenre);
        }
    }

    public DialogWindow(boolean isPlaylist, Window ownerWindow) {
        if (!isPlaylist) {
            addSongWithFileChooser("Add Song", ownerWindow);
        }
    }



    private void addSongWithFileChooser(String title, Window mainWindow) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(title);
        //Nedenstående 9 linier er til for at få filechooseren til at gå ind i projektets hovedmappe.
        String resourceUrl = System.getProperty("user.dir");
        if (resourceUrl != null) {
            try {
                File resourceDir = new File(resourceUrl);
                fileChooser.setInitialDirectory(resourceDir);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else System.out.println("Couldn't find the URL of the Applications folder.");

        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Audio Files", "*.mp3", "*.wav"),
                new FileChooser.ExtensionFilter("All Files", "*.*")
        );

        Stage stage = (Stage) mainWindow;

        File selectedFile = fileChooser.showOpenDialog(stage);

        if (selectedFile != null) {
            inputTitle = selectedFile.getName();
        } else {
            System.out.println("No file selected");
        }
    }


    private void showPlaylistDialog(String title, String header, String content) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle(title);
        dialog.setHeaderText(header);
        dialog.setContentText(content);

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(name -> inputName = name);
    } //playlist creation

    private void showPlaylistDialog(String title, String header, String content, String defaultValue) {
        TextInputDialog dialog = new TextInputDialog(defaultValue);
        dialog.setTitle(title);
        dialog.setHeaderText(header);
        dialog.setContentText(content);

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(name -> inputName = name);
    } //playlist editing

    private void editSongDialog(String title, String header, String contentTitle, String contentAuthor, String contentGenre) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle(title);
        dialog.setHeaderText(header);

        TextField titleField = new TextField();
        titleField.setText(contentTitle);
        TextField authorField = new TextField();
        authorField.setText(contentAuthor);
        TextField genreField = new TextField();
        genreField.setText(contentGenre);

        GridPane grid = new GridPane();
        grid.add(titleField, 0, 0);
        grid.add(authorField, 0, 1);
        grid.add(genreField, 0, 2);

        dialog.getDialogPane().setContent(grid);

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(name -> {
            inputTitle = titleField.getText();
            inputAuthor = authorField.getText();
            inputGenre = genreField.getText();
        });
    }

    public void delConfSongDialog(){
        Alert window = new Alert(Alert.AlertType.CONFIRMATION);
        window.setTitle("Delete a song");
        window.setContentText("Are you sure you want to delete this song?");

        Optional<ButtonType> result = window.showAndWait();
        if (result.get() == ButtonType.OK){
            System.out.println("Playlist deleted");
        }
    } //confirm window for deleting songs

    public void delConfPlDialog(){
        Alert window = new Alert(Alert.AlertType.CONFIRMATION);
        window.setTitle("Delete a playlist");
        window.setContentText("Are you sure you want to delete this playlist?");

        Optional<ButtonType> result = window.showAndWait();
        if (result.get() == ButtonType.OK){
            System.out.println("Playlist deleted");
        }
    } //confirm window for deleting playlist

    // Getter methods for the user input
    public String getInputName() {
        return inputName;
    }

    public String getInputTitle() {
        return inputTitle;
    }

    public String getInputAuthor() {
        return inputAuthor;
    }

    public String getInputGenre() {
        return inputGenre;
    }

}