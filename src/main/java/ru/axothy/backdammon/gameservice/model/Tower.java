package ru.axothy.backdammon.gameservice.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter @Setter
@Table(name = "towers")
public class Tower implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TOWER_ID", nullable = false)
    private int towerId;

    @Column(name = "IS_MAIN_TOWER", nullable = false)
    private boolean isMainTower = false;

    @Column(name = "TOWER_NUMBER_ON_BOARD")
    private int towerNumberOnBoard;

    @OneToMany
    @LazyCollection(LazyCollectionOption.FALSE)
    @JsonManagedReference
    private List<Chip> chips = new ArrayList<>();

}
