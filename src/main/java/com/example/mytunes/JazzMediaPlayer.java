package com.example.mytunes;

import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;
import javafx.util.Duration;

import java.util.List;

public class JazzMediaPlayer {
    private MediaPlayer mediaPlayer;
    private Slider playTimeSlider;
    private Media media;
    private Song song;
    private List<Song> allSongs;

    private Label timeLeftLabel, timePlayedLabel;

    private MyTunesController controller;


    public JazzMediaPlayer(Slider playTimeSlider, List<Song> allSongs, Label timeLeftLabel, Label timePlayedLabel, MyTunesController controller) {
        this.playTimeSlider = playTimeSlider;
        this.allSongs = allSongs;
        this.song = allSongs.getFirst();
        this.media = new Media(song.getFile().toURI().toString());
        newMediaPlayer();
        this.timeLeftLabel = timeLeftLabel;
        this.timePlayedLabel = timePlayedLabel;
        this.controller = controller;
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
        //lytter til ændringer i afspilningstiden af sange og opdatere labels
        mediaPlayer.currentTimeProperty().addListener((observable, oldValue, newValue) -> {
            if (!this.playTimeSlider.isValueChanging()) {
                this.playTimeSlider.setValue(newValue.toSeconds());
                updateTimePlayedLabel(newValue);
                updateTimeLeftLabel();
            }
        });

        //lytter til ændringer hvis man river i slideren
        this.playTimeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (Math.abs(newValue.doubleValue() - mediaPlayer.getCurrentTime().toSeconds()) > 0.5) {
                mediaPlayer.seek(Duration.seconds(newValue.doubleValue()));
            }
        });

        mediaPlayer.setOnEndOfMedia(() ->{
            playNextSong();
            controller.currentSongPlaying();

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
        } else mediaPlayer.setVolume(0);
    }

    public void volumeIncrementUp() {
        if (mediaPlayer.getVolume() < 0.90){
            mediaPlayer.setVolume(mediaPlayer.getVolume() + 0.10);
        } else mediaPlayer.setVolume(1.0);
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
            mediaPlayer.setVolume(controller.getVolumeSlider().getValue());
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
                mediaPlayer.setVolume(controller.getVolumeSlider().getValue());
                this.song = nextSong;
            } else {
                // skifter til næste sang i listen
                Song nextSong = allSongs.get(currentIndex + 1);
                setMedia(nextSong);
                mediaPlayer.play();
                mediaPlayer.setVolume(controller.getVolumeSlider().getValue());
                this.song = nextSong;
            }
        }
    }

    public void updateTimePlayedLabel(Duration currentTime) {
        if (timePlayedLabel != null){
            String formattedTime = formatTime(currentTime);
            timePlayedLabel.setText(formattedTime);
        }
    }

    public void updateTimeLeftLabel(){
        if (timeLeftLabel != null){
            Duration totalTime = media.getDuration();
            double currentTime = getCurrentTime();
            String formatTimeLeft = formatTimeLeft(totalTime, Duration.seconds(currentTime));
            timeLeftLabel.setText(formatTimeLeft);
        }
    }

    private String formatTime(Duration time){
        int totalSeconds = (int) time.toSeconds();
        int min = (totalSeconds % 3600) / 60;
        int sec = totalSeconds % 60;

        return String.format("%02d:%02d", min, sec);
    }

    private String formatTimeLeft (Duration totalTime, Duration currentTime){
        int totalSec = (int) totalTime.toSeconds();
        int currentSec = (int) currentTime.toSeconds();
        int remainingSec = totalSec - currentSec;

        if (remainingSec < 0 ){
            remainingSec = 0;
        }

        int min = (remainingSec % 3600) / 60;
        int sec = remainingSec % 60;

        return String.format("%02d:%02d", min, sec);
    }

    public void setOnEndOfMediaHandler(Runnable handler){
        mediaPlayer.setOnEndOfMedia(handler);

    }

}