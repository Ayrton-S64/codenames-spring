class Player{
  constructor(username){
    this.username = username;
    this.team = 0;
    this.role = 0;
  }
}

class Card{
  constructor(text,color,index){
    this.text=text;
    this.color = color;
    this.index = index;
    this.revealed = false;
    this.suggestedBy = ['user1','user2','user3'];
  }
  // <div class="card"> 
  //   <button class="card-selector">o</button>
  //   <div class="card-value">
  //     <span>text</span>
  //   </div>
  // </div>
  _genDomObject(){
    let t_card = document.createElement('div');
    let t_textC = document.createElement('div');
    let t_suggC = document.createElement('div');
    let t_textS = document.createElement('span');
    let t_cardButton = document.createElement('button');
    
    t_textS.textContent = this.text;

    t_textC.className='card-value';
    t_textC.append(t_textS);

    this.suggestedBy.forEach(suggestion=>{
      let t_suggS = document.createElement('span');
      t_suggS.className = "suggestion";
      t_suggS.textContent = suggestion;
      t_suggC.append(t_suggS);
    });

    t_suggC.className = "suggestion-container";

    t_cardButton.className='card-selector';
    t_cardButton.addEventListener('click',(ev)=>{
      console.log('seleccionando');
      console.log(this);
      ev.stopPropagation();
    })

    t_card.dataset.color = this.color;
    t_card.className = `card${(this.reveal)?' active':''}`;
    t_card.append(t_cardButton);
    t_card.append(t_suggC);
    t_card.append(t_textC);

    t_card.addEventListener('click',(ev)=>{
      console.log('sugiriendo');
      console.log(this);x
    })
    
    return t_card;
  }

  returnSpyMasterInfo(){
    return {
      text: this.text,
      color: this.color,
      index: this.index,
      is_revealed: this.revealed,
      suggestion: this.suggestedBy,
      DOM_object: this._genDomObject()
    }
  }

  returnOperatorInfo(){
    return {
      text: this.text,
      index: this.index,
      is_revealed: this.revealed,
      suggestion: this.suggestedBy,
      DOM_object: this._genDomObject()
    }
  }

  addSuggestion(player){
    this.suggestedBy.push(player);
    this.suggestedBy.sort();
    return this.card;
  }

  reveal(guesser){
    return this.color;
  }
}

const wordbank = "Perro,Gato,Iglesia,Guitarra,Red,Computadora,Mesa,Doctor,Rey,Reyna,Caballo,Agua,Tierra,Palacio,Guillotina,Formula,TV,Lentes,Vaso,Gorra,Mochila,Celular,Servidor,Cabello,Pantalon,Zapatilla,Polo,Escuela,Profesor,Matematicas,Ciencia,Computación,Universidad,Diente,Odontologo,Nube,Cielo,Jugo,Laptop,Carpeta,Hoja,Lapiz,Lapicero,Tapiz,Alfombra,Parlante,Sombrilla,Cable,Ruta,Perico,Raton,Rata,Alcohol,Carne,España,Ingles,Croacia,Russia,Tanque,Rifle,Bala,Zapato,Interruptor,Foco,Cuaderno,Tornillo,Clavo,Madera,Ventilador";

const cards = [];

const player = new Player('Ayrton');

function createCards(){
  const words = wordbank.split(',').map(e=>e.toUpperCase());
  for(let i = 0; i<25; i++){
    const i_word = Math.floor(Math.random()*(words.length))
    const t_text = words.splice(i_word,1);
    t_card= new Card(t_text,'blanco',i);
    cards.push(t_card);
  }
}

function setCardColors(){
  const inicia = Math.round(Math.random())
  for(let i=0;i<9;i++){
    valid = false;
    do{
      const selectedCardID = Math.floor(Math.random()*25)
      const card = cards[selectedCardID];
      console.log('inicia',inicia,selectedCardID, card)
      if(card.color === 'blanco'){
        valid = true;
        card.color = (inicia)?'rojo':'azul';
      }
    }while(!valid)
  }
  for(let i=0;i<8;i++){
    valid = false;
    do{
      const selectedCardID = Math.floor(Math.random()*25)
      const card = cards[selectedCardID];
      if(card.color === 'blanco'){
        valid = true;
        card.color = (!inicia)?'rojo':'azul';
      }
    }while(!valid)
  }
  valid = false
  do{
    const selectedCardID = Math.floor(Math.random()*25)
    const blackcard = cards[selectedCardID];
    if(blackcard.color === 'blanco'){
      valid = true;
      blackcard.color = 'negro';
    }
  }while(!valid)
}

function genBoard(){
  const board = document.getElementById('board');
  board.innerHTML = "";
  console.log(cards);
  cards.forEach((card)=>{
    board.append(card.returnSpyMasterInfo().DOM_object);
  })

}

function init(){
  createCards();
  setCardColors();
  genBoard();
}

window.onload = ()=>{
  init();
}