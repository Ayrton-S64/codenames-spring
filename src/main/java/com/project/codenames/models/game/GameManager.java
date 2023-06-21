package com.project.codenames.models.game;

import com.project.codenames.models.entity.Player;
import com.project.codenames.models.enums.CardColor;

import java.util.List;
import java.util.UUID;

public class GameManager {
    private List<Game> games;

    private List<Player> players;

    public String createGame(){
        String t_uuid = UUID.randomUUID().toString();
        Game t_game = new Game(t_uuid);
        this.games.add(t_game);
        return t_uuid;
    };

    public Game joinGame(String gameId, Long playerId){
        return null;
    }

    public Game joinGame(String gameId, Player player){
        Game t_game = null;
        for(Game i_game: games){
            if (i_game.getId()==gameId){
                t_game=i_game;
                t_game.getPlayers().add(player);
            }
        }
        return t_game;
    }
    public boolean disconnectPlayer(String gameId, Long playerId){
        return false;
    }

    public boolean disconnectPlayer(String gameId, Player player){
        Game t_game = null;
        for(Game i_game: games){
            if (i_game.getId()==gameId){
                t_game=i_game;
                t_game.getPlayers().remove(player);
            }
        }
        return true;
    }

    public Game startGame(String gameId){
        Game t_game = null;
        for(Game i_game: games){
            if (i_game.getId()==gameId){
                t_game=i_game;
                t_game.start();
            }
        }
        return t_game;
    }

    public Game endTurn(String gameId){
        Game t_game = null;
        for(Game i_game: games){
            if (i_game.getId()==gameId){
                t_game=i_game;
                t_game.endTurn();
            }
        }
        return t_game;
    }
}
