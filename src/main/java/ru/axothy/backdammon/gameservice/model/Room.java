package ru.axothy.backdammon.gameservice.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter
@Entity
@javax.persistence.Table(name = "rooms")
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ROOM_ID", nullable = false)
    private int roomId;

    @Column(name = "ROOM_NAME", nullable = false)
    private String name;

    @Column(name = "ROOM_PASSWORD", nullable = true)
    private Integer roomPassword;

    @OneToMany(mappedBy = "rooms", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<Player> players = new ArrayList<>();

    @Column(name = "BET")
    private int bet = 0;

    @Column(name = "IS_GAME_STARTED")
    private boolean isGameStarted = false;

    @OneToOne
    private Board board;

}
