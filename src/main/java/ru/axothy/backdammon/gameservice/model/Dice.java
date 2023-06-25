package ru.axothy.backdammon.gameservice.model;

import java.util.Random;

public class Dice {
    public static int roll() {
        Random rand = new Random();
        int value = rand.nextInt(6) + 1;

        return value;
    }
}
