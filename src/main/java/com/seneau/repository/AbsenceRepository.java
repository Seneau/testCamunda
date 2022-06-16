package com.seneau.repository;

import com.seneau.agileo.dtos.CongeDTO;
import com.seneau.domain.Absence;
import com.seneau.dto.AbsencesStatsDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the Recuperation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AbsenceRepository extends JpaRepository<Absence, Long> {
    List<Absence> findAllByMatricule(Long matricule);

    @Query("SELECT a from Absence a WHERE (:step is null or a.tracker.step = :step  or a.tracker.step = 1) and a.matricule in :matricules and function('year', CURRENT_DATE) = function('year', a.dateAbsence)")
    List<Absence> findAbsencesByYear(List<Long> matricules, @Param("step") Optional<Integer> step);

    @Query("SELECT a from Absence a WHERE a.tracker.step in (3) and function('year', CURRENT_DATE) = function('year', a.dateAbsence)")
    List<Absence> findAbsencesYear();

    @Query("SELECT new com.seneau.dto.AbsencesStatsDTO(count(a.id), sum(a.nbrJour)) from Absence  a WHERE a.tracker.step=3 and function('year', CURRENT_DATE) = function('year', a.dateAbsence)")
    AbsencesStatsDTO findNbrAbsences();


    @Query("SELECT count(a)  from Absence a WHERE a.tracker.step = 3 AND function('month', CURRENT_DATE) = function('month', a.dateAbsence) AND  function('year', CURRENT_DATE) = function('year', a.dateAbsence)")
    Object findCountAbsenceCurrentMois();

    @Query("SELECT new com.seneau.agileo.dtos.CongeDTO(a) from  Absence a WHERE a.matricule in :matricules AND a.tracker.step in (2,3)")
    List<CongeDTO> findCongeDtoByMatriculesIn(List<Long> matricules);
}
