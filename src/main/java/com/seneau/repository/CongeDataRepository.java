package com.seneau.repository;

import com.seneau.domain.CongeData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the CongeData entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CongeDataRepository extends JpaRepository<CongeData, Long> {
    Optional<CongeData> findByMatricule(Long matricule);

    @Query("SELECT cd  FROM CongeData cd WHERE cd.matricule in :listmatricules")
    List<Object> findByListMatricule(Long[] listmatricules);

    @Query("SELECT c from CongeData c WHERE c.matricule in :matricules")
    List<CongeData> findAllByMatricule(@Param("matricules") List<Long> matriculesAgents);
}
