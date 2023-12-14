package com.example.mytunes;

import javafx.scene.control.Slider;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;
import javafx.util.Duration;

import java.io.File;
import java.util.List;

public class JazzMediaPlayer {
    private MediaPlayer mediaPlayer;
    private Slider playTimeSlider;
    private Media media;
    private Song song;
    private List<Song> allSongs;


    public JazzMediaPlayer(Song song, Slider playTimeSlider, List<Song> allSongs) {
        String uriString = song.getFile().toURI().toString();
        this.song = song;
        media = new Media(uriString);
        this.playTimeSlider = playTimeSlider;
        newMediaPlayer();
        this.allSongs = allSongs;
    }

    public void newMediaPlayer(){
        if (mediaPlayer != null){
            mediaPlayer.dispose();
        }

        mediaPlayer = new MediaPlayer(this.media);
        mediaPlayer.setOnStalled(() -> {
            System.out.println("Media player stalled");
        });

        //sætter max value til antal sekunder af sangenen
        mediaPlayer.setOnReady(() -> {
            Duration duration = mediaPlayer.getMedia().getDuration();
            this.playTimeSlider.setMax(duration.toSeconds());
        });
        //lytter til ændringer i afspilningstiden af sange og
        mediaPlayer.currentTimeProperty().addListener((observable, oldValue, newValue) -> {
            if (!this.playTimeSlider.isValueChanging()) {
                this.playTimeSlider.setValue(newValue.toSeconds());
            }
        });

        //lytter til ændringer hvis man river i slideren
        this.playTimeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (Math.abs(newValue.doubleValue() - mediaPlayer.getCurrentTime().toSeconds()) > 0.5) {
                mediaPlayer.seek(Duration.seconds(newValue.doubleValue()));
            }
        });
    }

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    public void setMedia(Song song) {
        this.song = song;
        String filepath = song.getFile().toURI().toString();
        this.media = new Media(filepath);
        newMediaPlayer();
        this.mediaPlayer.play();
    }

    public void play() {
        if (mediaPlayer.getStatus() == Status.PLAYING) {
            mediaPlayer.pause();
        } else if (mediaPlayer.getStatus() != Status.PLAYING) {
            mediaPlayer.play();
        }
    }

    public void stop() {
        mediaPlayer.stop();
    }

    public double getCurrentTime() {
        return mediaPlayer.getCurrentTime().toSeconds();
    }


    public void volumeIncrementDown() {
        if (mediaPlayer.getVolume() > 0.10){
            mediaPlayer.setVolume(mediaPlayer.getVolume() - 0.10);
        }
    }

    public void volumeIncrementUp() {
        if (mediaPlayer.getVolume() < 0.90){
            mediaPlayer.setVolume(mediaPlayer.getVolume() + 0.10);
        }
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


    public Song getSong() {
        return this.song;
    }
    public void playPreviousSong(){
        int currentIndex = allSongs.indexOf(this.song);

        if (currentIndex > 0 && !allSongs.isEmpty()) {
            Song previousSong = allSongs.get(currentIndex - 1);

            setMedia(previousSong);
            mediaPlayer.play();
            this.song = previousSong;
        }
    }
    public void playNextSong() {
        int currentIndex = allSongs.indexOf(this.song);

        if (!allSongs.isEmpty()) {
            if (currentIndex == allSongs.size() - 1) {
                // Starter forfra i listen
                Song nextSong = allSongs.get(0);
                setMedia(nextSong);
                mediaPlayer.play();
                this.song = nextSong;
            } else {
                // skifter til næste sang i listen
                Song nextSong = allSongs.get(currentIndex + 1);
                setMedia(nextSong);
                mediaPlayer.play();
                this.song = nextSong;
            }
        }
    }
}