package com.mythostrike.account.repository;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

@Entity
@Table(name = "users", schema = "mythostrike")
@Data
@NoArgsConstructor
public class User {
    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @NotNull
    private String username;
    @NotNull
    @JsonIgnore
    private String password;

    private int rankPoints;

    private int drachma;

    private int avatarNumber;


    public User(@NotNull String username, @NotNull String password) {
        this.username = username;
        this.password = password;
        this.avatarNumber = 1;
    }

    public boolean changeDrachma(int amount) {
        if (this.drachma + amount < 0) {
            return false;
        }
        this.drachma += amount;
        return true;
    }

    public boolean changeRankPoints(int amount) {
        if (this.rankPoints + amount < 0) {
            return false;
        }
        this.rankPoints += amount;
        return true;
    }
}
