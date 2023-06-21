package com.project.codenames.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class IndexController {

    @GetMapping({"/", "index","home"})
    public String welcome(){
        return "index";
    }

    @GetMapping({"/lobby","/lobby/{gameid}"})
    public String lobby(){
        return "room";
    }

    @GetMapping({"/game","/game/{gameid}"})
    public String game(){
        return "board";
    }
}
