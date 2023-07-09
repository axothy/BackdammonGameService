package ru.axothy.backdammon.gameservice.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ExistingPlayer {
    private String nickname;
    private int balance;
}