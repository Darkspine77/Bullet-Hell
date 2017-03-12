import javax.swing.JOptionPane; 
import ddf.minim.*; 
AudioPlayer sFX1;
AudioPlayer sFX2;
AudioPlayer sFX3;
AudioPlayer sFX4;
AudioPlayer sFX5;
AudioPlayer sFX6;
AudioPlayer sFX7;
AudioPlayer sFX8;
AudioPlayer sFX9;
AudioPlayer sFX10;
AudioPlayer sFX11;
AudioPlayer sFX12;
AudioPlayer music;
Minim minim;
PImage img;
PImage [] enemyIMG = new PImage[4];
String [] enemyS = new String[7];
Transmission currentMessage;
ArrayList<StatusEffect> StatusEffects= new ArrayList<StatusEffect>();
ArrayList<abilityIcon> abilityIcons= new ArrayList<abilityIcon>();
ArrayList<Player> Players= new ArrayList<Player>();
ArrayList<Collectable> Collectables = new ArrayList<Collectable>();
ArrayList<Unit> Enemies = new ArrayList<Unit>();
ArrayList<Unit> Allies = new ArrayList<Unit>();
ArrayList<Unit> Units = new ArrayList<Unit>();
ArrayList<Obstacle> Obstacles = new ArrayList<Obstacle>(); 
ArrayList<Pellet> Pellets = new ArrayList<Pellet>(); 
ArrayList<Structure> Structures = new ArrayList<Structure>(); 
ArrayList<Ability> Abilities = new ArrayList<Ability>(); 
ArrayList<String> avaliableShips = new ArrayList<String>();
HashMap<String, float[]> ShipProfiles = new HashMap<String, float[]>(); 
int [] profileData = new int[2];
float [] savedShipStats = new float[8];
String [] saveData = new String[4];
String [] sounds = new String[7];
AudioPlayer [] Music = new AudioPlayer[13];
String [] loadedStrings = new String[4];
boolean keyPress = false;
int gameScreen = 0;
int currentSong = 0;
Player Player1 = new Player();
int Width = 1360;
int Height = 720;
Structure base = new Structure(1,100, 300, 375,6000, 120, 100);
Button Up= new Button(Width/2 - 450, Height/2 - 100, 30, 10, "Scroll Up", 24, 0, 255, 0);
Button Down = new Button(Width/2 - 450, Height/2+ 100, 30, 10, "Scroll Down", 24, 0, 255, 0);
Button nextShip = new Button(Width - 75, Height - 100, 30, 10, "Next", 24, 0, 255, 0); 
Button saveChanges = new Button(Width/2, 175, 30, 10, "Save Changes", 24, 0, 255, 0); 
Button prevShip = new Button(75, Height - 100, 30, 10, "Previous", 24, 0, 255, 0); 
boolean Paused = false;
String Name;
int enemyLevelrange = 1;
int gameTime;
int spawnDelay = int(gameTime); 
int gameTimeDelay;
float levelTimer;
float healthTimer;
float armorTimer;
float enemyWaitTime = int(random(1, 4));
float waitTime = int(random(10, 20));
float bsMod= 1, bdMod = 1, sMod = 1;
int songCooldown;

void openInv() {  
  if (keyPressed) {
    if (key == 'i') { 
      gameScreen = 2;
      truePause();
    }
  }
}

void truePause() {
  Paused = true;
}

void trueResume() {
  Paused = false;
  gameTimeDelay = millis() - gameTime; 
  if (gameScreen != 1) {
    gameScreen = 1;
  }
}

void changeSong(){
  if (keyPressed) {
    if (key == 'r' && songCooldown < millis()) { 
       Music[currentSong].pause();
      if(currentSong < 12){
       currentSong += 1;
      } else {
        currentSong = 0;
      }
      Music[currentSong].play(0);
      songCooldown = millis() + 200;
    }
  }
}

void Pause() {
  if (keyPressed) {
    if (key == 'p') { 
      if (!Paused) {
        Music[currentSong].pause();
        truePause();
      }
    }
  }
}

void Resume() {
  if (keyPressed) {
    if (key == 'o') { 
      if (Paused) {
        trueResume();
        Music[currentSong].play();
      }
    }
  }
}

float[] chooseLocation() {  
  float cX, cY;
  float[] results = new float[2];
  do {
    cX = random(-1000, 1300);
    cY = random(-1000, 1300);
  } while (inRange (Player1.x, Player1.y, cX, cY, 60, 60));
  results[0] = cX;
  results[1] = cY;
  return results;
}

float[] chooseLocationObstacles() {
  float[] results = new float[2];
  float cX, cY;
  for (int i = 0; i<Obstacles.size (); i++) {  
    do {
      cX = random(-2000, 2000);
      cY = random(-2000, 2000);
    } while (inRange (Player1.x, Player1.y, cX, cY, 100, 100) || inRange(Obstacles.get(i).x, Obstacles.get(i).y, cX, cY, 60, 60));
    results[0] = cX;
    results[1] = cY;
  }
  return results;
}
float[] chooseLocationStructures() {
  float[] results = new float[2];
  float cX, cY;
    do {
      cX = random(-2000, 2000);
      cY = random(-2000, 2000);
    } while (inRange (Player1.x, Player1.y, cX, cY, 1000, 1000) || inRange(base.x, base.y, cX, cY, 1000, 1000));
    results[0] = cX;
    results[1] = cY;
  return results;
}

void spawnEnemiesRandom() {
  //Chooses what type of enemy to spawn and where to spawn it.
  if (gameTime - enemyWaitTime*1000 >= spawnDelay) { 
    float coords[] = chooseLocation();
    int spawnChoice = int(random(1, 5));
    spawnEnemy(coords[0], coords[1], spawnChoice);
    spawnDelay = int(gameTime);
    enemyWaitTime = int(random(1, 5));
  }
}
boolean inRange(float x, float y, float x2, float y2, float xz, float yz) {
  //Tests to see if a certain x and y coordinate is within another x and y coordiante range
  if (x <= x2+xz && y <= y2+yz && x >= x2-xz && y >= y2-yz) {
    return true;
  } else {
    return false;
  }
}

void printText(float x, float y, float size, String text, float c1, float c2, float c3) {
  //prints text with variations in size, color and position
  textSize(size);   
  textAlign(CENTER);
  fill(c1, c2, c3);
  text(text, x, y);
}

void endGame() {
 //Ends game and bring player back to the starting screen
 //  checkRecord();
gameScreen = 0;
}

void repositionGame() {
  for (int i = 0; i<Pellets.size (); i++) {
    Pellets.get(i).cx += Player1.tx;
    Pellets.get(i).cy += Player1.ty;
  }  
  for (int i = 0; i<Enemies.size (); i++) {
    Enemies.get(i).x += + Player1.tx;
    Enemies.get(i).y += + Player1.ty;
  }  
  for (int i = 0; i<Obstacles.size (); i++) {
    Obstacles.get(i).x += Player1.tx;
    Obstacles.get(i).y += Player1.ty;
  }  
  for (int i = 0; i<Collectables.size (); i++) {
    Collectables.get(i).x += Player1.tx;
    Collectables.get(i).y += Player1.ty;
  }  
  for (int i = 0; i<Structures.size (); i++) {
    Structures.get(i).x += Player1.tx;
    Structures.get(i).y += Player1.ty;
  }  
  for (int i = 0; i<Allies.size (); i++) {
    Allies.get(i).x += Player1.tx;
    Allies.get(i).y += Player1.ty;
  }  
  Player1.tx = 0;
  Player1.ty = 0;
}

void spawnHealth() {
  if (gameTime - 5000 >= healthTimer) {
    float[] coords = chooseLocation();
    Collectable Health = new Collectable(2, 10,coords[0],coords[1], 5, 4); 
    Collectables.add(Health);
    healthTimer = gameTime;
  }
}
void increaseEnemyLevel() {
  if (gameTime - 15000 >= levelTimer) {
    enemyLevelrange += 1;
    levelTimer = gameTime;
  }
}

// void playSFX(int x){
//  sFX = minim.loadFile(sounds[x]);
//  sFX.play(); 
//  
// }
// 
//  void playMusic(int x){
//  music = minim.loadFile(Music[x]);
//  music.play(); 
// }
void resetTimers(){
  spawnDelay = 0;
  healthTimer = 0;
  gameTime = 0;
 for (int i = 0; i<Players.size (); i++) {
      Players.get(i).showDamageTimer = 0;
      Players.get(i).useDelay = 0;
    }  
    for (int i = 0; i<Enemies.size (); i++) {
      Enemies.get(i).showDamageTimer = 0;
      Enemies.get(i).shootDelay = 0;
    }  
    for (int i = 0; i<Allies.size (); i++) {
      Allies.get(i).showDamageTimer = 0;
    }  
    for (int i = 0; i<Structures.size (); i++) {
     Structures.get(i).showDamageTimer = 0;
    }  
     for (int i = 0; i<delays.length; i++) {
      delays[i] = 0;
    }  
 
}
void resetEntities(){
 Units.clear();
 Allies.clear();
 Enemies.clear();
}

void setup() {
  //Creates window and loads and previous save data
  //img = loadImage("background.tif"); 
  discoveredShips = 4; 
  savedAbilities[0] = 1;
  savedAbilities[1] = 2;
  savedAbilities[2] = 3;
  savedAbilities[3] = 4;
  frameRate(70);
  loadShipProfiles();
  loadAbilityProfiles();
  Players.add(Player1);
  selectedShip = avaliableShips.get(selectedShipId);
  loadAvalibleShipData();
  savedShipStats = selectedShipStats;
  minim = new Minim(this);
  //size(600,600);
  size(600, 720); 
  Player1.img = loadImage("Player.png");
  base.img = loadImage("AllyTurret.png");
  Player1.x = width/2;
  Player1.y = height/2;
  //  Load();
  sFX1 = minim.loadFile("Armor.wav");
  sFX2 = minim.loadFile("EnemyDamage.wav");
  sFX3 = minim.loadFile("EnemyShoot.wav");
  sFX4 = minim.loadFile("Health.wav");
  sFX6 = minim.loadFile("Shoot.wav");
  sFX7 = minim.loadFile("EnemyHurt.wav");
  sFX8 = minim.loadFile("ATKSpee.wav");
  sFX11 = minim.loadFile("Wall.wav");
  sFX12 = minim.loadFile("ShieldBreak.wav");
  Music[0] = minim.loadFile("First.mp3");
  Music[1] = minim.loadFile("Second.mp3");
  Music[2] = minim.loadFile("Third.mp3");
  Music[3] = minim.loadFile("Fourth.mp3");
  Music[4] = minim.loadFile("Fifth.mp3");
  Music[5] = minim.loadFile("Sixth.mp3");
  Music[6] = minim.loadFile("Seventh.mp3");
  Music[7] = minim.loadFile("Eighth.mp3");
  Music[8] = minim.loadFile("Ninth.mp3");
  Music[9] = minim.loadFile("Tenth.wav");
  Music[10] = minim.loadFile("Eleven.wav");
  Music[11] = minim.loadFile("Twelve.wav");
  Music[12] = minim.loadFile("Thirteen.wav");
  enemyIMG[0] = loadImage("EnemyG.png");
  enemyIMG[1] = loadImage("EnemyGG.png");
  enemyIMG[2] = loadImage("EnemyO.png");
  enemyIMG[3] = loadImage("EnemyP.png");
  enemyS[0] = "Lay siege my brethren!";
  enemyS[1] = "Our technology is superior!";
  enemyS[2] = "Do not give in comrades!";
  enemyS[3] = "Surrender Terran!";
  enemyS[4] = "You are no match for our intelligence!";
  enemyS[5] = "We will destroy you!";
  enemyS[6] = "We will destroy you!";
}

void draw() {
  //Either displays game screen or main menu screen depending on what the player is supposed to see
  background(1);
  //imageMode(CENTER); 
  //image(img, width/2, height/2);
  Button toMainMenu = new Button(width/2, 50, 30, 10, "Cancel", 24, 0, 255, 0);
  if (toMainMenu.isClicked()) {
    gameScreen = 0;
  }
  if (gameScreen >= 2) {
    toMainMenu.drawMe();
  }
  if (gameScreen == 0) {
    drawMainMenu();
  }
  if (gameScreen == 1) {
    drawMainGame();
  }
  if (gameScreen == 2) { 
    drawShipSelect();
  }
  if (gameScreen == 3) { 
    drawAbilitySelect();
  }
   if (gameScreen == 4) { 
    drawAbilityChange();
  }
}