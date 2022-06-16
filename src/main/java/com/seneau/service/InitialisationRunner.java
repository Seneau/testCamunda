package com.seneau.service;

import com.seneau.domain.Tracker;
import com.seneau.repository.TrackerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class InitialisationRunner implements CommandLineRunner {

    @Autowired
    private TrackerRepository trackerRepository;
    private String[] listStatut = { "rejet", "en cours", "valisation manager", "validation directeur", "validation DR regional", "validation DRTH", "cloture", "en cours manager" ,"en cours directeur","validation DGA","validation DG"};

    @Override
    public void run(String... args) throws Exception {
        if(!trackerRepository.findByStep(0).isPresent()){
            for (int i= 0; i< listStatut.length; i++) {
                Tracker tracker = new Tracker();
                tracker.setLibelle(listStatut[i]);
                tracker.setStep(i);
                trackerRepository.save(tracker);
            }
        }
    }
}
