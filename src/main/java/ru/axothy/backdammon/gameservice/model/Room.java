package ru.axothy.backdammon.gameservice.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter
@Entity
@Table(name = "rooms")
public class Room implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ROOM_ID", nullable = false)
    private int roomId;

    @Column(name = "ROOM_PASSWORD", nullable = true)
    private Integer roomPassword;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<Player> players = new ArrayList<>();

    @Column(name = "BET")
    private int bet = 0;

    @Column(name = "IS_GAME_STARTED")
    private boolean isGameStarted = false;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "BOARD_ID", referencedColumnName = "BOARD_ID")
    private Board board;

}
