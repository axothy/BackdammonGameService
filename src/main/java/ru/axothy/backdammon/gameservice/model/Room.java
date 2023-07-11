package ru.axothy.backdammon.gameservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

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

    @Column(name = "PASSWORD_ENABLED", nullable = false)
    private boolean passwordEnabled;

    @JsonIgnore
    @Column(name = "ROOM_PASSWORD", nullable = true)
    private int roomPassword;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
    @LazyCollection(LazyCollectionOption.FALSE)
    @JsonManagedReference
    private List<Player> players = new ArrayList<>();

    @Column(name = "BET")
    private int bet = 0;

    @Column(name = "IS_GAME_STARTED")
    private boolean isGameStarted = false;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "BOARD_ID", referencedColumnName = "BOARD_ID")
    @JsonManagedReference
    private Board board;

}
