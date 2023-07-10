package com.project.codenames.models.entity;

import com.project.codenames.models.enums.CardColor;

import java.util.ArrayList;
import java.util.List;

public class Card {
    private String word;

    private CardColor color;

    private Boolean revealed = false;

    private List<String> suggestedBy = new ArrayList<String>();

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public CardColor getColor() {
        return this.color;
    }

    public void setColor(CardColor color) {
        this.color = color;
    }

    public Boolean getRevealed() {
        return revealed;
    }

    public void setRevealed(Boolean revealed) {
        this.revealed = revealed;
    }

    public List<String> getSuggestedBy() {
        return suggestedBy;
    }

    public void addSuggestion(String playerName){
        if(!this.revealed){
            if(!this.suggestedBy.contains(playerName))
                this.suggestedBy.add(playerName);
            else{
                this.suggestedBy.remove(playerName);
            }
        }
    }

    public void setSuggestedBy(List<String> suggestedBy) {
        this.suggestedBy = suggestedBy;
    }
}
