package com.seneau.domain;

import lombok.Data;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * A CongeData.
 */
@Entity
@Data
@Table(name = "conge_data")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class CongeData implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "matricule", nullable = false)
    private Long matricule;

    //@NotNull
    @Column(name = "nbr_jour")
    private Long nbrJour;

    // @NotNull
    @Column(name = "nbr_jour_recup")
    private Long nbrJourRecup;


    @Column(name = "date_retour")
    private LocalDate dateRetour;

    @Column(name = "date_depart")
    private LocalDate dateDepart;


    @Column(name = "nbr_jour_absence")
    private Long nbrJourAbsence;


    @Column(name = "nbr_jour_absence_non_deductible")
    private Long nbrJourAbsenceNonDeductible;


}
