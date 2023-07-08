package ru.axothy.backdammon.gameservice.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
@Table(name = "players")
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "PLAYER_NICKNAME", nullable = false)
    private String nickname;

    @Column(name = "IS_READY")
    private boolean isReady = false;

    @Column(name = "BALANCE")
    private int balance;

    @Column(name = "COLOR")
    private Color color;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ROOM_ID")
    @JsonBackReference
    private Room room;
}
