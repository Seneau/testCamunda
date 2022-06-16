package com.seneau.domain;

import lombok.Data;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;

/**
 * A Agent.
 */
//@Entity
@Data
//@Table(name = "agent")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Agent implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "equipe", nullable = false)
    private Integer equipe;

    @NotNull
    @Column(name = "matricule", nullable = false)
    private Long matricule;

    @NotNull
    @Column(name = "nom", nullable = false)
    private String nom;

    @NotNull
    @Column(name = "fonction", nullable = false)
    private String fonction;

    @NotNull
    @Column(name = "statut", nullable = false)
    private String statut;

/*    @NotNull
    @Column(name = "taux", nullable = false)
    private Float taux;

    @NotNull
    @Column(name = "affectation", nullable = false)
    private String affectation;
*/
    @NotNull
    @Column(name = "etablissement", nullable = false)
    private String etablissement;

    @NotNull
    @Column(name = "direction", nullable = false)
    private String direction;

    @Column(name = "matricule_n_1")
    private Integer matriculeN1;

    @Column(name = "nom_n_1")
    private String nomN1;

    @Column(name = "service")
    private String service;
    @Column(name = "secteur")
    private String secteur;


}
