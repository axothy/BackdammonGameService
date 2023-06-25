package ru.axothy.backdammon.gameservice.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Stack;

@Getter @Setter
@Entity
public class Tower {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TOWER_ID", nullable = false)
    private int towerId;

    @Column(name = "IS_MAIN_TOWER", nullable = false)
    private boolean isMainTower;

    @OneToMany
    private Stack<Chip> chips = new Stack<>();

}
