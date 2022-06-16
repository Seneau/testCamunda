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
 * A Rappel.
 */
@Entity
@Data
@Table(name = "rappel")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Rappel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "matricule", nullable = false)
    private Integer matricule;

    @NotNull
    @Column(name = "date_rappel", nullable = false)
    private LocalDate dateRappel;

    @NotNull
    @Column(name = "date_retour", nullable = false)
    private LocalDate dateRetour;

    @NotNull
    @Column(name = "rappele_pour", nullable = false)
    private Integer rappelePour;

    @NotNull
    @Column(name = "motif", nullable = false)
    private String motif;

    @ManyToOne
    @JsonIgnoreProperties(value = "rappels", allowSetters = true)
    private Tracker tracker;

}
