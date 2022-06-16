package com.seneau.repository;

import com.seneau.domain.Tracker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data  repository for the Tracker entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TrackerRepository extends JpaRepository<Tracker, Long> {
    Optional<Tracker> findByStep(int step);
}
