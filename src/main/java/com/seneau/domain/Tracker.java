package com.seneau.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Tracker.
 */
@Entity
@Data
@Table(name = "tracker")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Tracker implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "step", nullable = false)
    private Integer step;

    @NotNull
    @Column(name = "libelle", nullable = false)
    private String libelle;


    @OneToMany(mappedBy = "tracker")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnore
    private Set<Conge> conges = new HashSet<>();

    @OneToMany(mappedBy = "tracker")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnore
    private Set<Rappel> rappels = new HashSet<>();

    @OneToMany(mappedBy = "tracker")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnore
    private Set<Recuperation> recuperations = new HashSet<>();


}
