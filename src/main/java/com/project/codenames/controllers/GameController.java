package com.project.codenames.controllers;

import com.project.codenames.models.entity.Player;
import com.project.codenames.models.enums.CardColor;
import com.project.codenames.models.enums.PlayerRole;
import com.project.codenames.models.game.Game;
import com.project.codenames.models.game.GameManager;
import jakarta.servlet.http.HttpSession;
import org.aspectj.bridge.Message;
import org.springframework.boot.Banner;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.socket.WebSocketSession;

import java.util.Map;

@Controller
@RequestMapping("/")
public class GameController {

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



    @MessageMapping("/room/{roomID}/iniciarPartida")
    @SendTo("/topic/{roomID}/boardUpdate")
    public Game iniciarPartida(@DestinationVariable  String roomID, @Payload Map<String, Object> message){
        System.out.println("iniciando  roomID: " + roomID);
        Game game = gameManager.searchGame(roomID);
        System.out.println(">Sala iniciada");
        game.start();
        String respuesta = (String) message.toString();
        System.out.println(respuesta);

        return game;
    }

    @MessageMapping("/room/{roomID}/set-team-role")
    @SendTo("/topic/{roomID}")
    public Game configPlayer(@DestinationVariable  String roomID, @Payload Map<String, Object> message){
        String userID = (String) message.get("userid");
        Player player = gameManager.searchPlayer(userID);
        player.setRole(PlayerRole.valueOf((String) message.get("role")));
        player.setTeam(CardColor.valueOf((String) message.get("team")));

        Game game = gameManager.searchGame(roomID);
        return game;
    }

    @MessageMapping("/room/{roomID}/select-card")
    @SendTo("/topic/{roomID}/boardUpdate")
    public Game selectCard(@DestinationVariable  String roomID, @Payload Map<String, Object> message){
        String userID = (String) message.get("userid");
        int cardIndex = (Integer) message.get("cardindex");

        Player player = gameManager.searchPlayer(userID);

        Game game = gameManager.searchGame(roomID);

        if(player!=null && player.getRole().equals(PlayerRole.operador)){
            game.revealCard(cardIndex, player.getTeam());
        }

        return game;
    }

    @MessageMapping("/room/{roomID}/suggest-card")
    @SendTo("/topic/{roomID}/boardUpdate")
    public Game suggestCard(@DestinationVariable  String roomID, @Payload Map<String, Object> message){
        String userID = (String) message.get("userid");
        int cardIndex = (Integer) message.get("cardindex");

        Player player = gameManager.searchPlayer(userID);

        Game game = gameManager.searchGame(roomID);

        if(player!=null && player.getRole().equals(PlayerRole.operador)){
            game.suggestCard(cardIndex, player.getName());
        }

        return game;
    }

    @MessageMapping("/room/{roomID}/give-clue")
    @SendTo("/topic/{roomID}/boardUpdate")
    public Game giveClue(@DestinationVariable  String roomID, @Payload Map<String, Object> message){
        System.out.println(message.toString());

        String userID = (String) message.get("userid");
        String clue = (String) message.get("clue");
        int numberOfCards = Integer.parseInt((String) message.get("amount"));

        Player player = gameManager.searchPlayer(userID);

        Game game = gameManager.searchGame(roomID);

        if(player!=null && player.getRole().equals(PlayerRole.spymaster)){
            game.setClue(clue, numberOfCards,  player.getTeam());
        }

        return game;
    }

    @MessageMapping("/room/{roomID}/joined")
    @SendTo("/topic/{roomID}/playersUpdate")
    public Game playerJoined(@DestinationVariable  String roomID, @Payload Map<String, Object> message){
        System.out.println("Jugador se conectó a roomID: " + roomID);
        Game game = gameManager.searchGame(roomID);
        return game;
    }

    @GetMapping({"/lobby","/lobby/{gameid}"})
    public String lobby(HttpSession session, @PathVariable String gameid, Model model){
        String userID = (String)  session.getAttribute("userID");
        Player player = gameManager.searchPlayer(userID);

        if(player==null){
            return "redirect:"+gameid+"/configure-user";
        }

        Game game = gameManager.joinGame(gameid,  player);
        if(game==null){
            return "redirect:/";
        }

        System.out.println("Conectando: "+player.getName()+"["+session.getId()+"] a  - "+gameid);
        model.addAttribute("gameId", gameid);
        model.addAttribute("userID", userID);
        model.addAttribute("team",  player.getTeam());
        model.addAttribute("game",game);
        return "room";
    }

//    @GetMapping({"/lobby/{gameid}/game","/lobby/{gameid}/game"})
//    public String game(@PathVariable String gameid, Model model){
//        Game game = gameManager.joinGame(gameid);
//        if(game==null){
//            return "redirect:/";
//        }
//        model.addAttribute("gameId", gameid);
//        model.addAttribute("game",game);
//        return "board";
//    }
    @GetMapping({"/configure-user","/lobby/{gameid}/configure-user"})
    public String setUsername(@PathVariable(required = false) String  gameid,  Model model){
        String buttonMessage = "Crear sala";
        if(gameid!=null){
            buttonMessage = "Unirse a sala";
        }
        model.addAttribute("mensaje", buttonMessage);
        model.addAttribute("gameid",  gameid);
        return "userform";
    }
    @PostMapping({"/configure-user",})
    public String setUsername(HttpSession session, @RequestParam(name = "gameid") String gameid, @RequestParam String nickname){
        Player player = new Player(nickname);
        session.setAttribute("userID", player.getId());
        player.setSessionId(session.getId());
        System.out.println("Session creada para: "+nickname+"["+session.getId()+"]");
        gameManager.registerPlayer(player);
        if(gameid!=null  && !gameid.equals("")){
            System.out.println("Ingresando a :"+gameid);
            return "redirect:/lobby/"+gameid;
        }
        System.out.println("Creando lobby");
        return "redirect:/create-game";
    }

    private String extractJSessionId(String cookieHeader) {
        if (cookieHeader != null) {
            String[] cookies = cookieHeader.split(";");

            for (String cookie : cookies) {
                if (cookie.trim().startsWith("JSESSIONID=")) {
                    return cookie.substring("JSESSIONID=".length());
                }
            }
        }
        return null;
    }
}
