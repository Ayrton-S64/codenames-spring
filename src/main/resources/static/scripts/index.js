var stompClient = null;

const gameDOM = document.querySelector('#game');
const menuDOM = document.querySelector('.menu');

const clueElement = document.querySelector('#clue')
const clueText = document.querySelector('#clue-text')
const clueAmount = document.querySelector('#clue-amount')

const gameId = document.body.dataset.gameid;
const userId = document.body.dataset.userid;

var are_guessing = false;

function setConnected(connected) {
  console.log('setConnected  =:>',  connected)
}

function connect() {
  var socket = new SockJS('/game-websocket');
  stompClient = Stomp.over(socket);
  stompClient.connect({}, function (frame) {
    setConnected(true);
    console.log('Connected: ' + frame);
    enviarSolicitudPlayerJoined();
    stompClient.subscribe(`/topic/${gameId}`, function (gameResponse) {
      gameJSON = JSON.parse(gameResponse.body);
      console.log(gameJSON)
      are_guessing = gameJSON.gameState._guessing;
      refreshPlayerList(gameJSON.players);
      refreshTeams(gameJSON.players);
      refreshBoard(gameJSON.board.cards);
    });


    stompClient.subscribe(`/topic/${gameId}/boardUpdate`, function (gameResponse) {
      gameJSON = JSON.parse(gameResponse.body);
      console.log(gameJSON);
      document.body.dataset.activeteam =  gameJSON.gameState.activeTeam;
      are_guessing = gameJSON.gameState._guessing
      document.querySelector('.team-info-red .score-counter').textContent=gameJSON.board.redCardsRemaining
      document.querySelector('.team-info-blue .score-counter').textContent=gameJSON.board.blueCardsRemaining
      refreshBoard(gameJSON.board.cards);
      setClues(gameJSON.clue, gameJSON.amountOfCards, gameJSON.gameState._guessing, gameJSON.gameState.activeTeam);

      if(gameJSON.gameState.is_gameOver){
        alert(`${gameJSON.gameState.winner} gana la partida!`);
        showView('menu')
      }
    });


    stompClient.subscribe(`/topic/${gameId}/playersUpdate`, function (gameResponse) {
      gameJSON = JSON.parse(gameResponse.body);
      console.log(gameJSON)
      refreshPlayerList(gameJSON.players);
      refreshTeams(gameJSON.players);
    });
  });
}

function disconnect() {
  if (stompClient !== null) {
    stompClient.disconnect();
  }
  setConnected(false);
  console.log("Disconnected");
}

function iniciarPartida(){
  enviarSolicitudInicioDePartida()
}

function enviarSolicitudPlayerJoined(){
  stompClient.send(`/app/room/${gameId}/joined`, {}, JSON.stringify({}));
  // showView('game');
}

function seleccionarRole(team,role){
  enviarSolicitudSetTeamRole(team, role);
}

function enviarPista(){
  let pista = document.querySelector('#txtClue').value;
  let cantidad = document.querySelector('#cboCantidad').value;
  console.log('enviando pista, ', pista, cantidad);
  enviarSolicitudEnvioPista(pista, cantidad)
}
function enviarSolicitudEnvioPista(pista, cantidad){
  stompClient.send(`/app/room/${gameId}/give-clue`, {}, JSON.stringify({'userid': userId,'clue': pista, 'amount':  cantidad}));
}

function enviarSolicitudSeleccionCarta(index){
  stompClient.send(`/app/room/${gameId}/select-card`, {}, JSON.stringify({'userid': userId,'cardindex':index}));
}

function enviarSolicitudSugerenciaCarta(index){
  stompClient.send(`/app/room/${gameId}/suggest-card`, {}, JSON.stringify({'userid': userId,'cardindex':index}));
}

function enviarSolicitudSetTeamRole(team, role){
  stompClient.send(`/app/room/${gameId}/set-team-role`, {}, JSON.stringify({'userid': userId,'team': team, 'role':role}));
}

function enviarSolicitudInicioDePartida() {
  stompClient.send(`/app/room/${gameId}/iniciarPartida`, {}, JSON.stringify({'name': "ayrton"}));
}

const botones = document.querySelectorAll('.mostrar-info');
const modals = document.querySelectorAll('div[id$="-modal"]');

const teamContainers = {
  rojo:{
    operador: document.querySelector('.team-info-red .operatives-container .players'),
    spymaster: document.querySelector('.team-info-red .spymasters-container .players'),
  },
  azul:{
    operador: document.querySelector('.team-info-blue .operatives-container .players'),
    spymaster: document.querySelector('.team-info-blue .spymasters-container .players'),
  }
}

const mensajes = {
  true:{
    operador: {
      true: 'Intenta adivinar una palabra',
      false: 'Espera a que tus spymasters te den una pista',
    },
    spymaster: {
      true: 'Dale una pista a tus operadores',
      false: 'Tus operadores estan adivinando',
    },
  },
  false:{
    operador: {
      true: 'Es turno del otro equipo, espera tu turno',
      false: 'Es turno del otro equipo, espera tu turno',
    },
    spymaster: {
      true: 'Es turno del otro equipo, espera tu turno',
      false: 'Es turno del otro equipo, espera tu turno',
    },
  }
}

function genCardDomObject(cardObject, index) {
  let t_card = document.createElement('div');
  let t_textC = document.createElement('div');
  let t_suggC = document.createElement('div');
  let t_textS = document.createElement('span');
  t_textS.textContent = cardObject.word;

  t_textC.className = 'card-value';
  t_textC.append(t_textS);

  cardObject.suggestedBy.forEach(suggestion => {
    let t_suggS = document.createElement('span');
    t_suggS.className = "suggestion";
    t_suggS.textContent = suggestion;
    t_suggC.append(t_suggS);
  });

  t_suggC.className = "suggestion-container";


  if(are_guessing && document.body.dataset.activeteam===document.body.dataset.team){
    let t_cardButton = document.createElement('button');
    t_card.append(t_cardButton);

    t_cardButton.className = 'card-selector';
    t_cardButton.addEventListener('click', (ev) => {
      console.log('seleccionando', 'llamar al socket de seleccion');
      enviarSolicitudSeleccionCarta(index);
      console.log(cardObject, index);
      ev.stopPropagation();
    })

    t_card.addEventListener('click', (ev) => {
      console.log('sugiriendo', 'llamar a socket de sugerencia');
      enviarSolicitudSugerenciaCarta(index);
      console.log(cardObject);
    })

  }

  let t_color = 'blanco'
  if(cardObject.revealed || document.body.dataset.role==='spymaster'){
    t_color = cardObject.color
  }

  t_card.dataset.color = t_color;
  t_card.className = `card ${(cardObject.revealed) ? ' active' : ''}`;
  t_card.append(t_suggC);
  t_card.append(t_textC);



  return t_card;
}

function showView(option){
  switch (option){
    case 'game':
      gameDOM.hidden=false;
      menuDOM.style.display='none'
      break;

    case 'menu':
      gameDOM.hidden=true;
      menuDOM.style.display='flex'
      break
  }
}

function setClues(clue, number, guessing, activeTeam){
  clueText.textContent = clue;
  clueAmount.textContent = number;
  clueElement.hidden = !guessing;
  console.log(guessing, activeTeam, document.body.dataset.team, activeTeam!=document.body.dataset.team)
  document.querySelector('#clue-input-container').hidden = (guessing)?guessing:activeTeam!=(document.body.dataset.team);
}

function refreshCluesVisibility(){
  document.querySelector('#clue-input-container').hidden = document.body.dataset.activeteam!=(document.body.dataset.team);
}

function refreshBoard(cards) {
  const board = document.getElementById('board');
  board.innerHTML = "";
  if(!cards || cards.length==0){
    showView('menu')
    return 0
  }
  console.log(cards)
  showView('game')
  console.log();
  cards.forEach((carta, i) => {
    board.append(genCardDomObject(carta,  i));
  })
}

function refreshPlayerList(players){
  const contenedor = document.querySelector('#playerList');
  contenedor.innerHTML='';
  players.forEach((player)=>{
    const template = document.createElement('div');
    template.className='usuario-conectado'
    const template_text = document.createElement('span');
    template_text.textContent=player.name;
    template.appendChild(template_text);
    contenedor.appendChild(template)
  })

}

function refreshTeams(players){
  for(let team in  teamContainers){
    for(let role  in  teamContainers[team]){
      teamContainers[team][role].innerHTML = '';
    }
  }
  players.forEach((player)=>{
    if(player && player.team && player.role){
      if(player.id  ==  userId){
        document.body.dataset.team =  player.team;
        document.body.dataset.role =  player.role;
        refreshCluesVisibility();
      }
      const template_text = document.createElement('span');
      template_text.textContent=player.name;
      teamContainers[player.team][player.role].appendChild(template_text)
    }
  })
}

function init(){
  connect();
}

window.onload = ()=>{
  init();
}

function mostrarModal(target) {
  console.log('mostrando')
  modals.forEach(function(modal) {
    if (modal.id === target) {
      modal.classList.add('mostrar');
      modal.classList.remove('oculto');
    } else {
      modal.classList.remove('mostrar');
      modal.classList.add('oculto');
    }
  });
}

function ocultarModals() {
  console.log('ocultando')
  modals.forEach(function(modal) {
    modal.classList.remove('mostrar');
    modal.classList.add('oculto');
  });
}

botones.forEach(function(boton) {

  boton.addEventListener('click', function() {
    const target = this.getAttribute('data-target');

    if (document.getElementById(target).classList.contains('oculto')) {
      ocultarModals();
      setTimeout(function() {
        mostrarModal(target);
      }, 10);
    } else {
      ocultarModals();
    }
  });
});

document.addEventListener('click', function(event) {
  const target = event.target;

  const isOutsideButtons = !target.closest('.mostrar-info');
  const isOutsideModals = !target.closest('div[id$="-modal"]');

  if (isOutsideButtons && isOutsideModals) {
    ocultarModals();
  }
});

function getCookieValue(cookieName) {
  const cookies = document.cookie.split('; ');
  for (let i = 0; i < cookies.length; i++) {
    const cookie = cookies[i].split('=');
    if (cookie[0] === cookieName) {
      return cookie[1];
    }
  }
  return null;
}

//sockets




