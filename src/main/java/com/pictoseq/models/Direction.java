package com.pictoseq.models;

public enum Direction {
    LEFT,
    RIGHT,
    UP,
    DOWN;

    public static Direction getDirection(String direction) {
        switch (direction) {
            case "À gauche":
                return LEFT;
            case "À droite":
                return RIGHT;
            case "En haut":
                return UP;
            case "En bas":
                return DOWN;
            default:
                return null;
        }
    }
}