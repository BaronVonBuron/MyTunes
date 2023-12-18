package com.example.mytunes;
import javafx.scene.control.TableCell;
import javafx.util.Duration;

public class TimeTableCell<S> extends TableCell<S, Duration> {

    @Override
    protected void updateItem(Duration item, boolean empty) {
        super.updateItem(item, empty);

        if (empty || item == null) {
            setText(null);
        } else {
            long minutes = (long) item.toMinutes();
            long seconds = (long) (item.toSeconds() % 60);
            String formattedTime = String.format("%02d:%02d", minutes, seconds);
            setText(formattedTime);
        }
    }
}