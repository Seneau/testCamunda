package com.seneau.repository;

import com.seneau.agileo.dtos.CongeDTO;
import com.seneau.domain.Recuperation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

/**
 * Spring Data  repository for the Recuperation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RecuperationRepository extends JpaRepository<Recuperation, Long> {

    List<Recuperation> findAllByMatricule(Long matricule);

    @Query("SELECT r from Recuperation r " +
            "WHERE r.matricule in :matricules " +
            "and function('year', CURRENT_DATE) = function('year', r.dateRecup)")
   List<Recuperation> findRecuparationYear(List<Long> matricules);

    /*
     | list des recuperations validées/cloturées suivant l'année encours;
     */
    @Query("SELECT r from Recuperation r WHERE r.tracker.step in (2,3) " +
            "and function('year', CURRENT_DATE) = function('year', r.dateRecup)")
    List<Recuperation> findAllRecuperationValidateByYear();


    /*
     | list des recuperations validées/cloturées suivant le mois encours;
     */

    @Query("SELECT r from Recuperation r WHERE r.tracker.step in (2,3)" +
            "and function('year', CURRENT_DATE) = function('year', r.dateRecup)" +
            "and function('month', CURRENT_DATE) = function('month', r.dateRecup)")
    List<Recuperation> findAllRecuperationValidateByMonth();

    @Query("SELECT new com.seneau.agileo.dtos.CongeDTO(r) from  Recuperation r WHERE r.matricule in :matricules AND r.tracker.step in (2,3) " +
            " and function('year', CURRENT_DATE) = function('year', r.dateRecup)" )
    List<CongeDTO> findCongeDtoByMatriculesIn(List<Long> matricules);
}
