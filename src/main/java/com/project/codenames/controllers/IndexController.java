package com.project.codenames.controllers;

import com.project.codenames.models.game.Game;
import com.project.codenames.models.game.GameManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class IndexController {

    GameManager gameManager = GameManager.getInstance();

    @GetMapping({"/", "index","home"})
    public String welcome(){
        return "index";
    }

    @GetMapping("/create-game")
    public String createGame(){
        String gameid = gameManager.createGame();
        return "redirect:/lobby/"+gameid;
    }

    @GetMapping({"/lobby","/lobby/{gameid}"})
    public String lobby(@PathVariable String gameid, Model model){
        Game game = gameManager.joinGame(gameid);
        if(game==null){
            return "redirect:/";
        }
        model.addAttribute("gameId", gameid);
        model.addAttribute("game",game);
        return "room";
    }

    @GetMapping({"/lobby/{gameid}/game","/lobby/{gameid}/game"})
    public String game(@PathVariable String gameid, Model model){
        Game game = gameManager.joinGame(gameid);
        if(game==null){
            return "redirect:/";
        }
        model.addAttribute("gameId", gameid);
        model.addAttribute("game",game);
        return "board";
    }
}
