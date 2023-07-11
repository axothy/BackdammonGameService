package ru.axothy.backdammon.gameservice.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter @Setter
@Entity
@Table(name = "boards")
public class Board implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BOARD_ID")
    private int boardId;

    @Column(name = "WHO_MOVES")
    private Color whoMoves;

    @OneToMany
    @LazyCollection(LazyCollectionOption.FALSE)
    @JsonManagedReference
    private List<Tower> towers = new ArrayList<>();

    @OneToOne(mappedBy = "board")
    @JsonBackReference
    private Room room;



}
