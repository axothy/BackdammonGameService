package ru.axothy.backdammon.gameservice.model;

public enum Color {
    WHITE, BLACK;

    public static Color opposite(Color color) {
        if (color == WHITE)
            return BLACK;
        else
            return WHITE;
    }
}
