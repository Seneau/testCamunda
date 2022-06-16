package com.seneau.service.cron;


import com.seneau.service.CongeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.text.SimpleDateFormat;
import java.util.Date;

@EnableScheduling
@Configuration
public class CancelleCongeCron {

    @Autowired private CongeService congeService;
   // @Scheduled(cron="0 0/1 * * * ?")
   @Scheduled(cron="0 0 9 28 * ?")
    public void cronJobSch() {
       // SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
       // Date now = new Date();
       // String strDate = sdf.format(now);
       // System.out.println("Java cron job expression:: " + strDate);


        congeService.cancelConge();
    }
}
