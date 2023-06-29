package ru.axothy.backdammon.gameservice.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter @Setter
@Entity
public class Chip {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CHIP_ID")
    private int chipId;

    @Column(name = "CHIP_COLOR")
    private Color color;

    @Column(name = "CHIP_NUMBER_ON_BOARD")
    private int chipNumberOnBoard;


}
