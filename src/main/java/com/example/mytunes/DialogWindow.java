package com.example.mytunes;

import javafx.scene.control.TextInputDialog;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

import java.util.Optional;

public class DialogWindow {

    private String inputName;
    private String inputTitle;
    private String inputAuthor;
    private String inputGenre;

    // Constructor for playlist creation
    public DialogWindow(boolean isPlaylist) {
        if (isPlaylist) {
            showPlaylistDialog("Create Playlist", null, "Enter playlist name:");
        } else {
            showSongDialog("Add Song", null, "Enter song title:", "Enter song author:", "Enter song genre:");
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
            showSongDialog("Edit Song", null, "Enter new title for the song:", "Enter new author for the song:", "Enter new genre for the song:");
        }
    }


    private void showPlaylistDialog(String title, String header, String content) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle(title);
        dialog.setHeaderText(header);
        dialog.setContentText(content);

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(name -> inputName = name);
    }

    private void showPlaylistDialog(String title, String header, String content, String defaultValue) {
        TextInputDialog dialog = new TextInputDialog(defaultValue);
        dialog.setTitle(title);
        dialog.setHeaderText(header);
        dialog.setContentText(content);

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(name -> inputName = name);
    }

    private void showSongDialog(String title, String header, String contentTitle, String contentAuthor, String contentGenre) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle(title);
        dialog.setHeaderText(header);

        TextField titleField = new TextField();
        titleField.setPromptText("Title");
        TextField authorField = new TextField();
        authorField.setPromptText("Author");
        TextField genreField = new TextField();
        genreField.setPromptText("Genre");

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