package graphics.imageProcessing;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.SimpleTimeZone;

public class TimerLabel extends JLabel {
    private long date;
    private final Timer timer;

    public TimerLabel(){
        timer = new Timer(1000, (ActionEvent event) ->{
            long millis =  System.currentTimeMillis() - date;
            SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
            format.setTimeZone(new SimpleTimeZone(SimpleTimeZone.UTC_TIME, "UTC"));
            setText("Выполняется: " +  format.format(new Date(millis)));
        });
    }

    public void start(){
        date = System.currentTimeMillis();
        timer.start();
    }

    public void stop(){
        timer.stop();
        long millis = System.currentTimeMillis() - date;
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        format.setTimeZone(new SimpleTimeZone(SimpleTimeZone.UTC_TIME, "UTC"));
        setText("Выполняется: " +  format.format(new Date(millis)));
    }
}
