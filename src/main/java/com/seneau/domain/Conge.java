package com.seneau.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * A Conge.
 */
@Entity
@Data
@Table(name = "conge")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Conge implements Serializable {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @NotNull
    @Column(name = "matricule", nullable = false)
    private Long matricule;

    @NotNull
    @Column(name = "date_conge", nullable = false)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDate dateConge = LocalDate.now();

    @NotNull
    @Column(nullable = false, name = "date_depart_conge")
    private LocalDate dateDepartConge;

    @Column(name = "date_retour_provisoire")
    private LocalDate dateRetourProvisoire;

    @NotNull
    @Column(name = "mois", nullable = false)
    private Integer mois;

    @NotNull
    @Column(name = "annee", nullable = false)
    private Integer annee;

    @Column(name = "date_retour_effective")
    private LocalDate dateRetourEffective;

    @Column(name = "nombre_jour_conge")
    private Long NombreJourConge;

    @Column(name = "matricule_creator")
    private Long matriculeCreator;

    @ManyToOne
    @JoinColumn(name = "tracker_id", nullable = false)
    @JsonIgnoreProperties(value = "conges", allowSetters = true)
    private Tracker tracker;

    @OneToOne(cascade=CascadeType.ALL)
    @JoinColumn(name="rappel_id")
    private Rappel rappel;
    @NotNull
    @Column(name="nbr_jour_reel")
    private Integer nbrJourReel=0;

    @NotNull
    @Column(name = "nbr_jour_absence")
    private Integer nbrJourAbsence=0;

    @Column(name = "is_cancelled", columnDefinition = "boolean default false")
    private boolean isCancelled=false;

    @NotNull
    @Column(name = "process_instance_id")
    private String processInstanceId ;



}
