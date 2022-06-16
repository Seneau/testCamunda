package com.seneau.repository;

import com.seneau.agileo.dtos.CongeDTO;
import com.seneau.domain.Conge;
import com.seneau.dto.CongeAgentDto;
import com.seneau.dto.CongeDto;
import com.seneau.dto.CongesStatsDTO;
import com.seneau.dto.DirectionNombreDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the Conge entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CongeRepository extends JpaRepository<Conge, Long> {

    Optional<Conge> findByMatricule(final int matricule);

    Optional<Conge> findByProcessInstanceId(final String processInstanceId );

    @Query("SELECT c from Conge c where c.tracker.step = :step and c.matricule= :matricule")
    List<Conge> findByMatriculeStep(final Long matricule, int step);

    List<Conge> findAllByMatricule(Long matricule);

    @Query("SELECT c from Conge c where c.mois = :month and c.annee = :year")
    List<Conge> findAllByYearAndMonth(@Param("year") Integer year,@Param("month") Integer month) ;

    @Query("SELECT c.tracker.libelle ,count(c) as numberConge from Conge c group by c.tracker.libelle")
    List<Object[]> findCountCongeByState();

    @Query("SELECT function('month', c.dateConge) as mois ,count(c) as numberConge from Conge c where c.tracker.step = 6 GROUP BY function('month', c.dateConge)")
    List<Object> findCountCongeByMois();

    @Query("SELECT count(c) from Conge c where c.tracker.step = 6 AND function('month', CURRENT_DATE )= c.mois AND function('year', CURRENT_DATE )= c.annee")
    Object findCountCongeByCurrentMois();
    /*
     * Find if user has current conge request
     */

    @Query("SELECT c from Conge  c where  c.matricule= :matricule and c.tracker.step in (1, 2,4,5,6)")
    Optional<List<Conge>> findIfUserHasCCR(Long matricule);


    /*
     * GET ALL conges by the of matricule in param
     */
    @Query("SELECT c from Conge c where c.matricule in :listId")
    List<Conge>  findCongeByMatriculesList(@Param("listId") List<Long> listId);

    @Query("SELECT c from Conge c WHERE c.tracker.step = 6 AND  function('year', CURRENT_DATE) = function('year', c.dateConge)")
    List<Conge> findCongeDoneByYear();

    @Query("SELECT c from Conge c WHERE (:annee is null or c.annee= :annee) and (:mois is null or c.mois= :mois) and (:step is null or c.tracker.step = :step)")
    List<Conge> findCongeDoneByYear(@Param("annee") Integer annee, @Param("mois") Integer mois, @Param("step") Integer step );

    @Query("SELECT count(c) as nombre from Conge c WHERE c.tracker.step = 3 and c.matricule in :matricules and function('month', CURRENT_DATE) = function('month', c.dateConge)")
    Object findCongesMonthByResponsable(@Param("matricules") List<Long> matricules);
    // List<Conge> findAllByTracker();

    @Query("SELECT count(c) as nombre from Conge c WHERE c.tracker.step = 3 and c.matricule in :matricules and function('month', CURRENT_DATE) = function('month', c.dateConge)")
    Object findCountCongesMonthByDirection(@Param("matricules") List<Long> matricules);

    @Query("UPDATE  Conge c set c.isCancelled = true " +
            "where function('month', CURRENT_DATE) = function('month', c.dateConge) " +
            "and function('year', CURRENT_DATE) = function('year', c.dateConge) and c.tracker.step <> 6")
    void cancelConge();

    @Query("SELECT new com.seneau.dto.CongesStatsDTO(count(c.matricule), sum(c.NombreJourConge)) from  Conge c WHERE c.tracker.step = 6 AND  function('year', CURRENT_DATE) = function('year', c.dateConge)")
    CongesStatsDTO findNbrJoursCongesPris();


    @Query("SELECT c FROM Conge c WHERE c.matricule=:matriculeAgent and c.annee in :ans and c.mois in :mois and c.tracker.step<>0")
    List<Conge> finCongeAgentInDate(Long matriculeAgent, List<Integer> ans, List<Integer> mois);

    @Query("SELECT c FROM Conge c WHERE c.matricule=:matriculeAgent and ((c.annee=:annee2 and c.mois>=:moisFin) or (c.annee=:annee and c.mois<=:mois))")
    List<Conge>  finCongeAgentInDate(Long matriculeAgent, Integer annee, int annee2, int moisFin, Integer mois);


    @Query("SELECT new com.seneau.agileo.dtos.CongeDTO(a) from  Conge a WHERE a.matricule in :matricules AND a.tracker.step=6")
    List<CongeDTO> findCongeDtoByMatriculesIn(List<Long> matricules);
}
