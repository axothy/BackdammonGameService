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
@Table(name = "boards")
public class Board implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BOARD_ID")
    private int boardId;

    @OneToMany
    @JsonManagedReference
    private List<Tower> towers = new ArrayList<>();

    @OneToOne(mappedBy = "board")
    private Room room;

}
