package com.project.codenames.models.entity;

import com.project.codenames.models.enums.CardColor;
import com.project.codenames.models.enums.PlayerRole;

import java.util.UUID;

public class Player {

    private String id;
    private String name;

    private CardColor team;

    private PlayerRole role;

    private int Score;

    public Player(String name) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CardColor getTeam() {
        return team;
    }

    public void setTeam(CardColor team) {
        this.team = team;
    }

    public PlayerRole getRole() {
        return role;
    }

    public void setRole(PlayerRole role) {
        this.role = role;
    }

    public int getScore() {
        return Score;
    }

    public void setScore(int score) {
        Score = score;
    }
}
