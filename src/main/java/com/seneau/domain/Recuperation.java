package com.seneau.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * A Recuperation.
 */
@Entity
@Data
@Table(name = "recuperation")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Recuperation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "matricule", nullable = false)
    private Long matricule;

    @NotNull
    @Column(name = "date_recup", nullable = false)
    private LocalDate dateRecup;

    @NotNull
    @Column(name = "date_retour_recup", nullable = false)
    private LocalDate dateRetourRecup;


    @NotNull
    @Column(name = "nbr_jour", nullable = false)
    private Integer nbrJour;

    @NotNull
    @Column(name = "motif", nullable = false)
    private String motif;



    @ManyToOne
    @JsonIgnoreProperties(value = "recuperations", allowSetters = true)
    private Tracker tracker;

}
