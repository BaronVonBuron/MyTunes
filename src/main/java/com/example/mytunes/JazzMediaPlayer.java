package com.example.mytunes;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.control.Slider;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.io.File;

public class JazzMediaPlayer {
    private MediaPlayer mediaPlayer;
    private Slider playTimeSlider;


    public JazzMediaPlayer(String filePath, Slider playTimeSlider) {
        String uriString = new File(filePath).toURI().toString();
        Media media = new Media(uriString);
        mediaPlayer = new MediaPlayer(media);
        this.playTimeSlider = playTimeSlider;

        mediaPlayer.setOnStalled(() -> {
            System.out.println("Media player stalled");
        });

        mediaPlayer.currentTimeProperty().addListener((observable, oldValue, newValue) -> {
            if (!playTimeSlider.isValueChanging()) {
                playTimeSlider.setValue(newValue.toSeconds());
            }
        });

        playTimeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (Math.abs(newValue.doubleValue() - mediaPlayer.getCurrentTime().toSeconds()) > 0.5) {
                mediaPlayer.seek(Duration.seconds(newValue.doubleValue()));
            }
        });

    }

    public void play() {//få lavet så den kan pause. mediaplayer.hvis spiller, then pause - ellers play.
        mediaPlayer.play();
        mediaPlayer.setVolume(0.25);
        System.out.println("JMP play yes");
    }

    public void pause() {
        mediaPlayer.pause();
    }

    public void stop() {
        mediaPlayer.stop();
    }

    public double getCurrentTime() {
        return mediaPlayer.getCurrentTime().toSeconds();
    }


    public void volumeIncrementDown() {

    }

    public void volumeIncrementUp() {

    }

    public void mute() {
        if (mediaPlayer.isMute()) {
            mediaPlayer.setMute(false);
        }else mediaPlayer.setMute(true);
    }

    public void setVolume(double newValue) {
        mediaPlayer.setVolume(newValue);
    }

    public double getVolume() {
        return mediaPlayer.getVolume();
    }



}