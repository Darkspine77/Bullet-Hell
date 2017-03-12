void drawMainGame() {
  if (Music[currentSong].position() == Music[currentSong].length()) {
    if (currentSong < 12) { 
      currentSong += 1;
    } else {
      currentSong = 0;
    }
    Music[currentSong].play(0);
  }
  changeSong();
  Pause();
  Resume();
  openInv();
  if (base.health <= 0) {
    endGame();
  }
  if (!Paused) {
    gameTime = millis() - gameTimeDelay;
    spawnEnemiesRandom();
    spawnHealth();
    increaseEnemyLevel();
  }
  for (int i = 0; i<Players.size (); i++) {
    Players.get(i).run();
  }  
  for (int i = 0; i<Pellets.size (); i++) {
    Pellets.get(i).run();
  }  
  for (int i = 0; i<Enemies.size (); i++) {
    Enemies.get(i).run();
  }  
  for (int i = 0; i<Obstacles.size (); i++) {
    Obstacles.get(i).run();
  }  
  for (int i = 0; i<Collectables.size (); i++) {
    Collectables.get(i).run();
  }  
  for (int i = 0; i<Structures.size (); i++) {
    Structures.get(i).run();
  } 
  for (int i = 0; i<Allies.size (); i++) {
    Allies.get(i).run();
  }  
  for (int i = 0; i<StatusEffects.size (); i++) {
    StatusEffects.get(i).run();
  } 
  if(currentMessage != null && currentMessage.duration > gameTime){
  image(currentMessage.img,width/2 - 450, (height - 90),64,64); 
  textSize(32);   
  textAlign(LEFT);
  fill(255);
  text(currentMessage.text,width/2 -400,height - 90);
  } else {
  currentMessage = null;
  }
  if(random(0,10000) < 100 && currentMessage == null){
    Transmission message = new Transmission(enemyIMG[int(random(0,4))],enemyS[int(random(0,7))],3000);
  }
  printText(width/2, 60,36,"Time Survived: " + str(int(gameTime/1000)), 255, 0, 0);
  if (Paused) {
    printText(width/2, height/2, 48, "Paused", 100, 255, 0);
  }
  printText(60, height - 20, 12, "Base Health:", 0, 255, 0);
  printText(140, height - 20, 12, int(base.health) + "/" + int(base.maxHealth), 0, 255, 0);
  for (int i = 0; i<Players.get(0).selectedAbilities.length; i++) {
    printText(50, height - 200 + i * 30, 12, Abilities.get(Players.get(0).selectedAbilities[i] - 1).name, 0, 255, 0);
    float cd = ((delays[Players.get(0).selectedAbilities[i] - 1]) + Abilities.get(Players.get(0).selectedAbilities[i] - 1).cooldown) - gameTime;
    if (cd > 0) {
      printText(150, height - 200 + i * 30, 12,nf(cd/1000,1,1),255,0, 0);
    } else {
      printText(150, height - 200 + i * 30, 12, "Ready", 0, 255, 0);
    }
  }
  noStroke();
  fill(255,255,0);
  ellipse(200,height - 200 + ((Players.get(0).intAbility - 1) * 30),10,10);
  strokeWeight(8);
  stroke(255, 0, 0);
  line(180, height - 25, 1280, height - 25); 
  stroke(0, 255, 0);
  line(180, height - 25, (base.health*((1280)-(180)))/base.maxHealth + (180), height - 25);  
  stroke(0, 0, 0);
  strokeWeight(1);
  //printText(width - 200, 60, 48, str(int(frameRate)), 0, 255, 0);
}