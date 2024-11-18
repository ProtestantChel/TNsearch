package com.exampletest.testtt.Schedulers;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Calendar;

@Component
@EnableScheduling
public class SchedulerNew {
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    @Scheduled(fixedRate = 3000, initialDelay = 3000)
    public void searchNew() {

        System.out.println(Thread.currentThread().getName() + " searchOrdersNew: " + format.format(Calendar.getInstance().getTime()));
    }
}
