package ru.axothy.backdammon.gameservice.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter
@Entity
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
    @JsonManagedReference
    private List<Chip> chips = new ArrayList<>();

}
