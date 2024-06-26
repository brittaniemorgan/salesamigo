/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package PromotionAndMarketing;

/**
 *
 * @author britt
 */
import java.util.Timer;
import java.util.TimerTask;
import java.util.Timer;
import java.util.TimerTask;

public class EmailScheduler {
    private static Timer timer;

    public static void scheduleEmailTask(Email email, long delay, long period) {
        if (timer == null) {
            timer = new Timer();
        }

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                email.send();
            }
        };

        // Schedule the task
        timer.schedule(task, delay, period);
    }

    public static void stopEmailTask() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }
}
