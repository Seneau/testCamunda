package com.seneau.rest;

import com.seneau.domain.Tracker;
import com.seneau.repository.TrackerRepository;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/api")
@Data
public class TrackerResource {

    private final Logger log = LoggerFactory.getLogger(TrackerResource.class);
    private final TrackerRepository trackerRepository;

    public TrackerResource(TrackerRepository trackerRepository) {
        this.trackerRepository = trackerRepository;
    }

    /**
     *
     * @param step
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the rappel, or with status {@code 404 (Not Found)}
     */
    @GetMapping("/trackers/{step}")
    public ResponseEntity<Tracker> getTrackerByStep(@PathVariable Integer step) {
        log.debug("Rest request to get Tracker by step : {}", step);

        Optional<Tracker> tracker = trackerRepository.findByStep(step);
        if(tracker.isPresent())
            return ResponseEntity.ok(tracker.get());
        return ResponseEntity.notFound().build();
    }
}
