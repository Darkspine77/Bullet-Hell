void loadWorld(){
  for (int i =0; i< 100; i++) { 
        float coords[] = chooseLocationObstacles();
        Obstacle Obstacle1 = new Obstacle(coords[0], coords[1], random(250, 400), 30, 0, 20, 30, 40, true);
        Obstacles.add(Obstacle1);
      }
}

void drawMainMenu(){
    imageMode(CENTER); 
    img = loadImage("Title.png");
    image(img,width/2, (height/8));
    printText(width/2, (height/4) + 75, 12, "Use number keys 1,2,3, and 4 to select different abilities, and use right click to activate them", 252, 50, 18);
    printText(width/2, (height/4) + 50, 12, "Use your mouse to aim, click to shoot, W,A,S,D to move Up,Left,Down, and Right", 252, 50, 18);
    printText(width/2, (height/4) + 25 , 12, "Defend your base from the onslaught of enemies! How long can you survive?", 252, 50, 18);
    printText(width/2, (height - 100) + 32 , 12, "By Darkspine77", 78, 16, 151);
    img = loadImage("prof.png");
    image(img,width/2, (height - 190));
    Button Start = new Button(width/2, height/2 - 20, 30, 10, "Start", 24, 0, 255, 0); 
    Start.drawMe();
    Button changeShip = new Button(width/2, height/2 + 30, 30, 10, "Change Ship", 24, 0, 255, 0); 
    changeShip.drawMe();
    Button changeAbilities = new Button(width/2, height/2+ 80, 30, 10, "Change Abilities", 24, 0, 255, 0); 
    changeAbilities.drawMe();
    //Button Shop = new Button(width/2, height/2 + 100, 30, 10, "Shop", 24, 0, 255, 0); 
    //Shop.drawMe();
    if (changeShip.isClicked()) {
      gameScreen = 2;
      selectedShipId = 0;
    }
    if (changeAbilities.isClicked()) {
      gameScreen = 3;
      dispalyedAbilities = savedAbilities; 
      pullAbilitiesData();
    }
    if (Start.isClicked()) {
      currentSong = int(random(0,13)); 
      Music[currentSong].pause();
      Music[currentSong].play(0);
      Structures.add(base);
      Transmission message = new Transmission(loadImage("Commander.png"),"Defend the base from the invading aliens!",3000);
 Player1.x = width/2;
 Player1.y = height/2;
 Units.clear();
 Allies.clear();
 Enemies.clear();
 Pellets.clear();
 Obstacles.clear();
 Collectables.clear();
 Player1.score = 0;
 gameScreen = 0;  
 Player1.Spawn();
 enemyLevelrange = 1;
 base.health = 6000;
 gameTime = 0;
 gameTimeDelay = 0;
      resetEntities();
       loadWorld();
      startAbilties();
      resetTimers();
      Player1.loadSelectedStats();
      Player1.loadAbilities();
      Player1.Spawn();
      Player1.ydir = -1;
      Player1.tx = 0;
      Player1.ty = 0;
      Player1.rotation = 0;
      levelTimer = gameTime;
      gameScreen = 1;
      healthTimer = gameTime;
      armorTimer = gameTime;
      gameTime = 0;
      gameTimeDelay += millis() - gameTime;
      //      abilityIcon ability1 = new abilityIcon(225,550,30,1,'1');
      //      abilityIcons.add(ability1);
      //      abilityIcon ability2 = new abilityIcon(275,550,30,1,'2');
      //      abilityIcons.add(ability2);
      //      abilityIcon ability3 = new abilityIcon(325,550,30,1,'3');
      //      abilityIcons.add(ability3);
      //      abilityIcon ability4 = new abilityIcon(375,550,30,1,'4');
      //      abilityIcons.add(ability4);
    }
  }