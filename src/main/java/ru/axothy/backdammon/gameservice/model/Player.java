package ru.axothy.backdammon.gameservice.model;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "PLAYER_NICKNAME", nullable = false)
    private String nickname;

    @Column(name = "IS_READY")
    private boolean isReady = false;

    @Column(name = "BALANCE", nullable = false)
    private int balance;

    @Column(name = "COLOR")
    private Color color;
}
