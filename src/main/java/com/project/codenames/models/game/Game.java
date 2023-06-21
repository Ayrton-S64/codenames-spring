package com.project.codenames.models.game;

import com.project.codenames.models.entity.Card;
import com.project.codenames.models.entity.Player;
import com.project.codenames.models.enums.CardColor;
import com.project.codenames.models.enums.PlayerRole;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class Game {
    private String id;

    private List<Player> players;

    private Board board;

    private int currentTurn;

    private GameState gameState = null;

    public Game(String id) {
        this.id = id;
        this.currentTurn = 0;
        this.players =  new ArrayList<Player>();
    }

    public void start(){
        this.currentTurn = 1;
        int first = (int) Math.round(Math.random());
        this.board = new Board(first);
        this.gameState = new GameState((first==0)?CardColor.Red:CardColor.Blue);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public int getCurrentTurn() {
        return currentTurn;
    }

    public void setCurrentTurn(int currentTurn) {
        this.currentTurn = currentTurn;
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public boolean nextTurn(){
        if(this.gameState.getIs_gameOver()){
            return false;
        }
        if (this.gameState.getCurrRole() == PlayerRole.Operative) {
            this.gameState.setActiveTeam((this.gameState.getActiveTeam()==CardColor.Red)?CardColor.Blue:CardColor.Red);
        }else{
            this.gameState.setCurrRole(PlayerRole.Operative);
        }
        this.currentTurn++;
        this.updateGameState();
        return true;
    }

    public boolean endTurn(){
        this.updateGameState();
        return  this.nextTurn();
    }

    public CardColor revealCard(int cardId){
        CardColor revealed = this.revealCard(cardId);
        this.updateGameState();
        return revealed;
    }

    public CardColor revealCard(@NotNull Card card){
        CardColor revealed = board.revealCard(card);
        this.updateGameState();
        return revealed;
    }

    public void updateGameState(){
        boolean death = this.board.getIs_assassinRevealed();
        boolean redWin = this.board.getRedCardsRemaining()==0;
        boolean blueWin = this.board.getBlueCardsRemaining()==0;
        if(redWin || blueWin || death){
            this.gameState.setIs_gameOver(true);
        }
        this.gameState.setCurrentTurn(this.currentTurn);
        this.gameState.setActiveTeam(null);
    }
}
