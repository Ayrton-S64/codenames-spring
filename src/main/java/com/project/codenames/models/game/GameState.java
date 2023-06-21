package com.project.codenames.models.game;

import com.project.codenames.models.enums.CardColor;
import com.project.codenames.models.enums.PlayerRole;

public class GameState {
    private CardColor activeTeam;

    private PlayerRole currRole;

    private int currentTurn;

    private Boolean is_gameOver;

    public GameState(CardColor activeTeam) {
        this.activeTeam = activeTeam;
        this.currentTurn = 0;
        this.is_gameOver = false;
    }

    public CardColor getActiveTeam() {
        return activeTeam;
    }

    public void setActiveTeam(CardColor activeTeam) {
        this.activeTeam = activeTeam;
    }

    public PlayerRole getCurrRole() {
        return currRole;
    }

    public void setCurrRole(PlayerRole currRole) {
        this.currRole = currRole;
    }

    public int getCurrentTurn() {
        return currentTurn;
    }

    public void setCurrentTurn(int currentTurn) {
        this.currentTurn = currentTurn;
    }

    public Boolean getIs_gameOver() {
        return is_gameOver;
    }

    public void setIs_gameOver(Boolean is_gameOver) {
        this.is_gameOver = is_gameOver;
    }
}
