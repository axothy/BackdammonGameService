package ru.axothy.backdammon.gameservice.model;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Move {
    private int diceValue;
    private int indexTowerFrom;
    private int indexTowerTo;

}
