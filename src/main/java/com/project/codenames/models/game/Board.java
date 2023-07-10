package com.project.codenames.models.game;

import com.project.codenames.models.entity.Card;
import com.project.codenames.models.enums.CardColor;
import com.project.codenames.utils.WordFromFile;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Collections;
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
        System.out.println("Generando cartas...");
        this.genCards();
        System.out.println(">Cartas generadas.");
        System.out.println("Configurando cartas...");
        this.setUpBoard(first);
        System.out.println(">Cartas Configuradas");
    }

    public Card[] getCards() {
        return cards;
    }

    private void genCards() {
        List<String> words =  WordFromFile.getWordFromFile("default");
        Collections.shuffle(words);
        for(int i = 0; i<25; i++){
                Card card = new Card();
                card.setWord(words.get(i));
                card.setColor(CardColor.blanco);
                cards[i] = card;
        }
    }

    private void setUpBoard(){
        int first = (int)Math.round(Math.random());
        for(int i=0;  i < 9; i++){
            boolean valid = false;
            do {
                int selectedIndex = (int) Math.floor(Math.random()*25);
                Card selectedCard = cards[selectedIndex];
                if(selectedCard.getColor()==CardColor.blanco){
                    valid = true;
                    selectedCard.setColor((first==0)?CardColor.rojo:CardColor.azul);
                }
            }while (!valid);
        }
        for(int i=0;  i < 8; i++){
            boolean valid = false;
            do {
                int selectedIndex = (int) Math.floor(Math.random()*25);
                Card selectedCard = cards[selectedIndex];
                if(selectedCard.getColor()==CardColor.blanco){
                    valid = true;
                    selectedCard.setColor((first!=0)?CardColor.rojo:CardColor.azul);
                }
            }while (!valid);
        }
        boolean is_blackSet = false;
        do {
            int selectedIndex = (int) Math.floor(Math.random()*25);
            Card selectedCard = cards[selectedIndex];
            if(selectedCard.getColor()==CardColor.blanco){
                is_blackSet = true;
                selectedCard.setColor(CardColor.negro);
            }
        }while (!is_blackSet);
    }

    private void setUpBoard(int first){
        System.out.println("Configurando cartas (1)...");
        for(int i=0;  i < 9; i++){
            boolean valid = false;
            do {
                int selectedIndex = (int) Math.floor(Math.random()*25);
                Card selectedCard = cards[selectedIndex];
//                System.out.println("("+i+")  "+selectedCard.getColor().toString());
                if(selectedCard.getColor().equals(CardColor.blanco)){
                    valid = true;
                    selectedCard.setColor((first==0)?CardColor.rojo:CardColor.azul);
                }
            }while (!valid);
        }
        System.out.println("Configurando cartas (2)...");
        for(int i=0;  i < 8; i++){
            boolean valid = false;
            do {
                int selectedIndex = (int) Math.floor(Math.random()*25);
                Card selectedCard = cards[selectedIndex];
                if(selectedCard.getColor().equals(CardColor.blanco)){
                    valid = true;
                    selectedCard.setColor((first!=0)?CardColor.rojo:CardColor.azul);
                }
            }while (!valid);
        }
        System.out.println("Configurando cartas (3)...");
        boolean is_blackSet = false;
        do {
            int selectedIndex = (int) Math.floor(Math.random()*25);
            Card selectedCard = cards[selectedIndex];
            if(selectedCard.getColor().equals(CardColor.blanco)){
                is_blackSet = true;
                selectedCard.setColor(CardColor.negro);
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

    public void suggestCard(int index, String player){
        cards[index].addSuggestion(player);
    }

    public Card revealCard(int index){
        Card selected = cards[index];
        selected.setRevealed(true);
        return selected;
    }

    public int getRedCardsRemaining() {
        return this.countCards((element -> element.getColor().equals(CardColor.rojo) && !element.getRevealed()));
    }

    public int getBlueCardsRemaining() {
        return this.countCards((element -> element.getColor().equals(CardColor.azul) && !element.getRevealed()));
    }

    public Boolean getIs_assassinRevealed() {
        return this.countCards((element -> element.getColor().equals(CardColor.negro) && element.getRevealed()))>0;
    }
}
