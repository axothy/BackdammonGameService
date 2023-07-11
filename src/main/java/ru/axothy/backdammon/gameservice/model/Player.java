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

    @Column(name = "IS_ROOM_OWNER")
    private boolean isRoomOwner = false;

    @Column(name = "COLOR")
    private Color color;

    @Column(name = "START_VALUE_FIRST")
    private int startValueFirst;

    @Column(name = "START_VALUE_SECOND")
    private int startValueSecond;

    @Column(name = "MOVES_FIRST")
    private boolean movesFirst;

    @ManyToOne
    @JoinColumn(name = "ROOM_ID")
    @JsonBackReference
    private Room room;
}
