package com.project.codenames.models.game;

import com.project.codenames.models.entity.Player;
import com.project.codenames.models.enums.CardColor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class GameManager {

    private static GameManager instance;
    private List<Game> games;

    private List<Player> players;

    public Player searchPlayer(String playerId){
        Player player = null;
        for(Player i_Player: players){
            if(i_Player.getId().equals(playerId)){
                player = i_Player;
            }
        }
        return player;
    }

    public Game searchGame(String gameId){
        Game game = null;
        for(Game i_game: games){
            if(i_game.getId().equals(gameId)){
                game = i_game;
            }
        }
        return game;
    }

    private GameManager(){
        games = new ArrayList<Game>();
        players = new ArrayList<Player>();
    }

    public String createGame(){
        String t_uuid = UUID.randomUUID().toString();
        Game t_game = new Game(t_uuid);
        this.games.add(t_game);
        return t_uuid;
    }

    public static GameManager getInstance() {
        if (instance == null) {
            synchronized (GameManager.class) {
                if (instance == null) {
                    instance = new GameManager();
                }
            }
        }
        return instance;
    }

    public Game joinGame(String gameId, String playerId){
        Game t_game = null;
        Player t_player = searchPlayer(playerId);
        for(Game i_game: games){
            if (i_game.getId().equals(gameId)){
                t_game=i_game;
                t_game.getPlayers().add(t_player);
            }
        }
        return t_game;
    }

    public void registerPlayer(Player player){
        players.add(player);
        System.out.println("added: "+player.getName());
    }

    public Game joinGame(String gameId, Player player){
        Game t_game = null;
        for(Game i_game: games){
            if (i_game.getId().equals(gameId)){
                t_game=i_game;
                if(!t_game.getPlayers().contains(player)){
                    t_game.getPlayers().add(player);
                }
            }
        }
        return t_game;
    }
    public boolean disconnectPlayer(String gameId, String playerId){
        return false;
    }

    public boolean disconnectPlayer(String gameId, Player player){
        Game t_game = null;
        for(Game i_game: games){
            if (Objects.equals(i_game.getId(), gameId)){
                t_game=i_game;
                t_game.getPlayers().remove(player);
            }
        }
        return true;
    }

    public Game startGame(String gameId){
        Game t_game = null;
        for(Game i_game: games){
            if (Objects.equals(i_game.getId(), gameId)){
                t_game=i_game;
                t_game.start();
            }
        }
        return t_game;
    }
}
