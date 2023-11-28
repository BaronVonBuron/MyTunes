package com.example.mytunes;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.sql.Connection;

public class MyTunesController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
        DataAccessObject dao = new DataAccessObject();
    }
}