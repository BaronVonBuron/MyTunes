package com.example.mytunes;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.File;
import java.util.Optional;

public class DialogWindow {

    private String inputName;
    private String inputTitle;
    private String inputAuthor;
    private String inputGenre;
    private String media_uri;
    private boolean deleteOK;

    // Constructor for deleting song/playlist
    public DialogWindow(boolean isPlaylist, boolean isDelete){
        if (isPlaylist){
            delConfPlDialog();
        }else delConfSongDialog();
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

    // Constructor for editing/adding a song
    public DialogWindow(boolean isPlaylist, String songTitle, String songAuthor, String songGenre, String filepath) {
        if (!isPlaylist && songTitle.isEmpty()) {
            editAddSongDialog("Edit/Add Song", songTitle, songAuthor, songGenre, filepath);
        } else if (!isPlaylist) {
            editAddSongDialog("Edit/Add Song", songTitle, songAuthor, songGenre, filepath);
        }
    }

    // Method for combined Edit/Add Song window
    public void editAddSongDialog(String title, String defaultTitle, String defaultAuthor, String defaultGenre, String filepath) {
        Stage stage = new Stage();
        stage.setTitle(title);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        TextField titleField = new TextField();
        titleField.setPromptText("Enter song title");
        titleField.setText(defaultTitle);

        TextField authorField = new TextField();
        authorField.setPromptText("Enter song author");
        authorField.setText(defaultAuthor);

        TextField genreField = new TextField();
        genreField.setPromptText("Enter song genre");
        genreField.setText(defaultGenre);

        TextField fileField = new TextField();
        if (!filepath.isEmpty()){
            fileField.setText(filepath);
        }else fileField.setPromptText("Choose file");
        fileField.setEditable(false);

        Button chooseButton = new Button("Choose...");
        chooseButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle(title);
            String resourceUrl = System.getProperty("user.dir");
            if (resourceUrl != null) {
                try {
                    File resourceDir = new File(resourceUrl);
                    fileChooser.setInitialDirectory(resourceDir);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            } else System.out.println("Couldn't find the URL of the Applications folder.");

            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Audio Files", "*.mp3", "*.wav"),
                    new FileChooser.ExtensionFilter("All Files", "*.*")
            );

            File selectedFile = fileChooser.showOpenDialog(stage);
            if (selectedFile != null) {
                fileField.setText(selectedFile.getName());
                // If the user is adding a new song, auto-fill the title
                if (defaultTitle.isEmpty()) {
                    titleField.setText(selectedFile.getName());
                }
            }
        });

        Button saveButton = new Button("Save");
        saveButton.setOnAction(e -> {
            // Logic to save the song details
            inputTitle = titleField.getText();
            inputAuthor = authorField.getText();
            inputGenre = genreField.getText();
            media_uri = fileField.getText();
            // Close the dialog after saving
            stage.close();
        });

        Button cancelButton = new Button("Cancel");
        cancelButton.setOnAction(e -> {
            inputTitle = titleField.getText();
            inputAuthor = authorField.getText();
            inputGenre = genreField.getText();
            media_uri = fileField.getText();
            stage.close();
        });

        grid.add(new Label("Title:"), 0, 0);
        grid.add(titleField, 1, 0);
        grid.add(new Label("Artist:"), 0, 1);
        grid.add(authorField, 1, 1);
        grid.add(new Label("Genre:"), 0, 2);
        grid.add(genreField, 1, 2);
        grid.add(new Label("File:"), 0, 3);
        grid.add(fileField, 1, 3);
        grid.add(chooseButton, 2, 3);
        grid.add(saveButton, 0, 4);
        grid.add(cancelButton, 1, 4);

        Scene scene = new Scene(grid);
        stage.setScene(scene);
        stage.showAndWait();
    }



    /*private void addSongWithFileChooser(String title, Window mainWindow) {
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
    }*/


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
            deleteOK = true;
            System.out.println("Song deleted");
        }
    } //confirm window for deleting songs

    public void delConfPlDialog(){
        Alert window = new Alert(Alert.AlertType.CONFIRMATION);
        window.setTitle("Delete a playlist");
        window.setContentText("Are you sure you want to delete this playlist?");

        Optional<ButtonType> result = window.showAndWait();
        if (result.get() == ButtonType.OK){
            deleteOK = true;
            System.out.println("Playlist Deleted");
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

    public String getMedia_uri() {
        return media_uri;
    }

    public boolean isDeleteOK() {
        return deleteOK;
    }
}