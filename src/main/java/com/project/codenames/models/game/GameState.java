package com.project.codenames.models.game;

import com.project.codenames.models.enums.CardColor;
import com.project.codenames.models.enums.PlayerRole;

public class GameState {
    private CardColor activeTeam;

    private PlayerRole currRole;

    // true operadores adivinando
    // false spymaster dando pista
    private boolean b_guessing ;

    private Boolean is_gameOver;

    private CardColor winner = null;

    public GameState(CardColor activeTeam) {
        this.activeTeam = activeTeam;
        this.is_gameOver = false;
        this.b_guessing = false;
    }

    public CardColor getActiveTeam() {
        return activeTeam;
    }

    public void setActiveTeam(CardColor activeTeam) {
        this.activeTeam = activeTeam;
    }

    public void changeTeam(){
        if(this.activeTeam.equals(CardColor.rojo)) {
            this.activeTeam = CardColor.azul;
        } else if (this.activeTeam.equals(CardColor.azul)) {
            this.activeTeam = CardColor.rojo;
        }
    }

    public PlayerRole getCurrRole() {
        return currRole;
    }

    public void setCurrRole(PlayerRole currRole) {
        this.currRole = currRole;
    }


    public boolean is_guessing() {
        return b_guessing;
    }

    public void set_guessing(boolean b_guessing) {
        this.b_guessing = b_guessing;
    }

    public Boolean getIs_gameOver() {
        return is_gameOver;
    }

    public void setIs_gameOver(Boolean is_gameOver) {
        this.is_gameOver = is_gameOver;
    }

    public CardColor getWinner() {
        return winner;
    }

    public void setWinner(CardColor winner) {
        this.winner = winner;
    }
}
