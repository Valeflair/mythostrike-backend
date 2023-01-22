package edu.kit.iti.scale.laralab.model;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "users", schema = "lara")
@Getter
@NoArgsConstructor
@RequiredArgsConstructor
public class User {

    @Id
    @GeneratedValue
    private long id;
    @NotNull
    private String username;
    @NotNull
    private String password;
    @OneToMany(mappedBy = "user")
    private List<Research> researches;

}
