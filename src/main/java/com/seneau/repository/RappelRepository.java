package com.seneau.repository;

import com.seneau.domain.Rappel;
import io.swagger.models.auth.In;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@SuppressWarnings("unused")
@Repository
public interface RappelRepository extends JpaRepository<Rappel, Long> {

     List<Rappel> findAllByDateRetourBetween(LocalDate debut, LocalDate end);

    @Query("SELECT count(r) as nombre from Rappel r WHERE r.matricule in :matricules" +
            " and function('month', CURRENT_DATE) = function('month', r.dateRetour)")
    Object findCountRappelsMonthByResponsable(@Param("matricules") List<Integer> matricules);


    @Query("SELECT r from Rappel r WHERE r.matricule in :matricules " +
            " and function('year', CURRENT_DATE) = function('year', r.dateRetour)")
    List<Rappel> findRappelsYearByResponsable(@Param("matricules") List<Integer> matricules);


    @Query("SELECT count(r) as nombre from Rappel r WHERE r.matricule in :matricules " +
            " and function('month', CURRENT_DATE) = function('month', r.dateRetour) and r.tracker.step=5")
    Object findCountRappelsMonthByDirection(@Param("matricules") List<Integer> matricules);


    @Query("SELECT r from Rappel r WHERE r.matricule in :matricules and function('year', CURRENT_DATE) = function('year', r.dateRetour)")
    List<Rappel> findRappelsYearByDirection(@Param("matricules") List<Integer> matricules);


    @Query("SELECT count(r) from Rappel r WHERE  function('year', CURRENT_DATE) = function('year', r.dateRetour)" +
            " and r.tracker.step=5")
    Long getNombreRappelYear();

    @Query("SELECT count(r) from Rappel r WHERE  function('year', CURRENT_DATE) = function('year', r.dateRetour)" +
            " and function('month', CURRENT_DATE) = function('month', r.dateRetour)" +
            " and r.tracker.step=5")
    Long getNombreRappelCurrentMonth();
}
