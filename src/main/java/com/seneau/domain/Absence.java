package com.seneau.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Data
@Table(name = "absence")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Absence implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "matricule", nullable = false)
    private Long matricule;

    @NotNull
    @Column(name = "date_absence", nullable = false)
    private LocalDate dateAbsence;

    @NotNull
    @Column(name = "date_retour_absence", nullable = false)
    private LocalDate dateRetourAbsence;


    @NotNull
    @Column(name = "nbr_jour", nullable = false)
    private Integer nbrJour;

    @NotNull
    @Column(name = "motif", nullable = false)
    private String motif;

    @NotNull
    @Column(name = "deductible", nullable = false)
    private Boolean deductible;

    @ManyToOne
    @JsonIgnoreProperties(value = "absences", allowSetters = true)
    private Tracker tracker;

}
