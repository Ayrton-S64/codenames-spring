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

    private String clue;

    private int amountOfCards;

    private GameState gameState = null;

    public Game(String id) {
        this.id = id;
        this.currentTurn = 0;
        this.players =  new ArrayList<Player>();
        this.gameState = new GameState(null);
    }

    public void start(){
        int first = (int) Math.round(Math.random());
        this.board = new Board(first);
        this.gameState = new GameState((first==0)?CardColor.rojo:CardColor.azul);
        this.currentTurn = 1;
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

    public String getClue() {
        return clue;
    }

    public void setClue(String clue) {
        this.clue = clue;
    }

    public int getAmountOfCards() {
        return amountOfCards;
    }

    public void setAmountOfCards(int amountOfCards) {
        this.amountOfCards = amountOfCards;
    }

    public void setClue(String clue, int numberOfCards, CardColor playerColor) {
        if (this.gameState.getActiveTeam().equals(playerColor) && !this.gameState.is_guessing()) {
            this.clue = clue;
            this.amountOfCards = numberOfCards;
            this.gameState.set_guessing(true);
        }
    }

    public void endGuessing(CardColor playerColor){
        if(this.gameState.getActiveTeam().equals(playerColor) && this.gameState.is_guessing()){
            if (this.gameState.is_guessing()) {
                this.clue =  "";
                this.amountOfCards = 0;
                this.gameState.set_guessing(false);
                this.gameState.changeTeam();
            }
        }
    }

    public void suggestCard(int index, String player){
        this.board.suggestCard(index, player);
    }

    public CardColor revealCard(int cardId, CardColor playerColor){
        if(this.gameState.getActiveTeam().equals(playerColor) && this.gameState.is_guessing()){
            CardColor revealed =  this.board.revealCard(cardId).getColor();
            if(revealed.equals(CardColor.negro)){
                this.gameState.setIs_gameOver(true);
                return  (playerColor.equals(CardColor.rojo))?CardColor.azul:CardColor.rojo;
            }else{
                if(revealed.equals(playerColor)){
                    if(amountOfCards--<=0){
                        this.gameState.changeTeam();
                        this.gameState.set_guessing(false);
                        this.clue =  "";
                        this.amountOfCards = 0;
                    }
                }else{
                    this.gameState.changeTeam();
                    this.gameState.set_guessing(false);
                    this.clue =  "";
                    this.amountOfCards = 0;
                }
            }
            return this.checkWinner();
        }
        return null;
    }

    public CardColor checkWinner(){
        boolean redWin = this.board.getRedCardsRemaining()==0;
        boolean blueWin = this.board.getBlueCardsRemaining()==0;
        if(redWin){
            this.gameState.setIs_gameOver(true);
            return CardColor.rojo;
        }
        if(blueWin){
            this.gameState.setIs_gameOver(true);
            return CardColor.azul;
        }
        return null;
    }
}
