package ru.axothy.backdammon.gameservice.model;

import javax.persistence.*;

enum ChipColor {
    BLACK , WHITE
}

@Entity
public class Chip {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CHIP_ID")
    private int chipId;

    @Column(name = "CHIP_COLOR")
    private ChipColor color;


}
