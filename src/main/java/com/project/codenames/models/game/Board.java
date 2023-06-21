package com.project.codenames.models.game;

import com.project.codenames.models.entity.Card;
import com.project.codenames.models.enums.CardColor;
import org.jetbrains.annotations.NotNull;

import java.util.List;

interface CheckFunction<Card>{
    boolean check(Card element);
}

public class Board {
    private Card[] cards = new Card[25];

    public Board() {
        this.genCards();
        this.setUpBoard();
    }

    public Board(int first) {
        this.genCards();
        this.setUpBoard(first);
    }

    public Card[] getCards() {
        return cards;
    }

    private void genCards(){
        for(int i = 0; i<25; i++){
                Card card = new Card();
                card.setWord("Test");
                card.setColor(CardColor.Neutral);
                cards[i] = new Card();
        }
    }

    private void setUpBoard(){
        int first = (int)Math.round(Math.random());
        for(int i=0;  i < 9; i++){
            boolean valid = false;
            do {
                int selectedIndex = (int) Math.floor(Math.random()*25);
                Card selectedCard = cards[selectedIndex];
                if(selectedCard.getColor()==CardColor.Neutral){
                    valid = true;
                    selectedCard.setColor((first==0)?CardColor.Red:CardColor.Blue);
                }
            }while (!valid);
        }
        for(int i=0;  i < 8; i++){
            boolean valid = false;
            do {
                int selectedIndex = (int) Math.floor(Math.random()*25);
                Card selectedCard = cards[selectedIndex];
                if(selectedCard.getColor()==CardColor.Neutral){
                    valid = true;
                    selectedCard.setColor((first!=0)?CardColor.Red:CardColor.Blue);
                }
            }while (!valid);
        }
        boolean is_blackSet = false;
        do {
            int selectedIndex = (int) Math.floor(Math.random()*25);
            Card selectedCard = cards[selectedIndex];
            if(selectedCard.getColor()==CardColor.Neutral){
                is_blackSet = true;
                selectedCard.setColor(CardColor.Black);
            }
        }while (!is_blackSet);
    }

    private void setUpBoard(int first){
        for(int i=0;  i < 9; i++){
            boolean valid = false;
            do {
                int selectedIndex = (int) Math.floor(Math.random()*25);
                Card selectedCard = cards[selectedIndex];
                if(selectedCard.getColor()==CardColor.Neutral){
                    valid = true;
                    selectedCard.setColor((first==0)?CardColor.Red:CardColor.Blue);
                }
            }while (!valid);
        }
        for(int i=0;  i < 8; i++){
            boolean valid = false;
            do {
                int selectedIndex = (int) Math.floor(Math.random()*25);
                Card selectedCard = cards[selectedIndex];
                if(selectedCard.getColor()==CardColor.Neutral){
                    valid = true;
                    selectedCard.setColor((first!=0)?CardColor.Red:CardColor.Blue);
                }
            }while (!valid);
        }
        boolean is_blackSet = false;
        do {
            int selectedIndex = (int) Math.floor(Math.random()*25);
            Card selectedCard = cards[selectedIndex];
            if(selectedCard.getColor()==CardColor.Neutral){
                is_blackSet = true;
                selectedCard.setColor(CardColor.Black);
            }
        }while (!is_blackSet);
    }

    private int countCards(CheckFunction<Card> checkFunction){
        int count = 0;
        for(Card card: cards){
            if(checkFunction.check(card)){
                count++;
            }
        }
        return count;
    }

    public CardColor revealCard(int index){
        cards[index].setRevealed(true);
        return cards[index].getColor();
    }

    public CardColor revealCard(@NotNull Card card){
        card.setRevealed(true);
        return card.getColor();
    }

    public int getRedCardsRemaining() {
        return this.countCards((element -> element.getColor()== CardColor.Red && !element.getRevealed()));
    }

    public int getBlueCardsRemaining() {
        return this.countCards((element -> element.getColor()== CardColor.Blue && !element.getRevealed()));
    }

    public Boolean getIs_assassinRevealed() {
        return this.countCards((element -> element.getColor()== CardColor.Black && element.getRevealed()))>0;
    }
}
