package ru.axothy.backdammon.gameservice.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Getter @Setter
@Entity
@Table(name = "chips")
public class Chip implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CHIP_ID")
    private int chipId;

    @Column(name = "CHIP_COLOR")
    private Color color;

    @Column(name = "CHIP_NUMBER_ON_BOARD")
    private int chipNumberOnBoard;


}
