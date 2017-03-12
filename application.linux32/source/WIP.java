import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import javax.swing.JOptionPane; 
import ddf.minim.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class WIP extends PApplet {

 
 
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
int spawnDelay = PApplet.parseInt(gameTime); 
int gameTimeDelay;
float levelTimer;
float healthTimer;
float armorTimer;
float enemyWaitTime = PApplet.parseInt(random(1, 4));
float waitTime = PApplet.parseInt(random(10, 20));
float bsMod= 1, bdMod = 1, sMod = 1;
int songCooldown;

public void openInv() {  
  if (keyPressed) {
    if (key == 'i') { 
      gameScreen = 2;
      truePause();
    }
  }
}

public void truePause() {
  Paused = true;
}

public void trueResume() {
  Paused = false;
  gameTimeDelay = millis() - gameTime; 
  if (gameScreen != 1) {
    gameScreen = 1;
  }
}

public void changeSong(){
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

public void Pause() {
  if (keyPressed) {
    if (key == 'p') { 
      if (!Paused) {
        Music[currentSong].pause();
        truePause();
      }
    }
  }
}

public void Resume() {
  if (keyPressed) {
    if (key == 'o') { 
      if (Paused) {
        trueResume();
        Music[currentSong].play();
      }
    }
  }
}

public float[] chooseLocation() {  
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

public float[] chooseLocationObstacles() {
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
public float[] chooseLocationStructures() {
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

public void spawnEnemiesRandom() {
  //Chooses what type of enemy to spawn and where to spawn it.
  if (gameTime - enemyWaitTime*1000 >= spawnDelay) { 
    float coords[] = chooseLocation();
    int spawnChoice = PApplet.parseInt(random(1, 5));
    spawnEnemy(coords[0], coords[1], spawnChoice);
    spawnDelay = PApplet.parseInt(gameTime);
    enemyWaitTime = PApplet.parseInt(random(1, 5));
  }
}
public boolean inRange(float x, float y, float x2, float y2, float xz, float yz) {
  //Tests to see if a certain x and y coordinate is within another x and y coordiante range
  if (x <= x2+xz && y <= y2+yz && x >= x2-xz && y >= y2-yz) {
    return true;
  } else {
    return false;
  }
}

public void printText(float x, float y, float size, String text, float c1, float c2, float c3) {
  //prints text with variations in size, color and position
  textSize(size);   
  textAlign(CENTER);
  fill(c1, c2, c3);
  text(text, x, y);
}

public void endGame() {
 //Ends game and bring player back to the starting screen
 //  checkRecord();
gameScreen = 0;
}

public void repositionGame() {
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

public void spawnHealth() {
  if (gameTime - 5000 >= healthTimer) {
    float[] coords = chooseLocation();
    Collectable Health = new Collectable(2, 10,coords[0],coords[1], 5, 4); 
    Collectables.add(Health);
    healthTimer = gameTime;
  }
}
public void increaseEnemyLevel() {
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
public void resetTimers(){
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
public void resetEntities(){
 Units.clear();
 Allies.clear();
 Enemies.clear();
}

public void setup() {
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

public void draw() {
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
int [] delays = new int[8];
public void startAbilties(){
delays[0] = gameTime + 50;
delays[1] = gameTime + 5000;
delays[2] = gameTime + 10000;
delays[3] = gameTime + 10000;
delays[4] = gameTime + 10000;
delays[5] = gameTime + 1000;
delays[6] = gameTime + 20000; 
delays[7] = gameTime + 15000; 
}
public void useAbility(int id) { 
  if (id == 1) {   
    //println("Made it");
    if (gameTime - 50 >= delays[0]) {  
       for(int i = 0;i<Enemies.size();i++){
         if(inRange(mouseX,mouseY,Enemies.get(i).x,Enemies.get(i).y,Enemies.get(i).size*4,Enemies.get(i).size*4)) {
                sFX6.play(7);
      Pellet A1bullet = new Pellet(Player1.bDmg * .05f, Player1.bSize* .8f, Player1.bSpd * .25f, 1,Player1.x, Player1.y, false, 0, 0,Enemies.get(i),null);
      Pellets.add(A1bullet);//Pellet(float damage1, float size1, float speed1, int target1, float sx1, float sy1, boolean Aoe1, float aoeD1, float aoeR1)
      delays[0] = gameTime;
         }
       }
    }
  }
   if (id == 2) {   
    if (gameTime - 5000 >= delays[1]) {  
       sFX6.play(7);
      Pellet A2bullet = new Pellet(Player1.bDmg*2.5f, Player1.bSize*3, Player1.bSpd * .75f, 1, Player1.x, Player1.y, mouseX, mouseY, false, 0, 0);
      Pellets.add(A2bullet);
      delays[1] = gameTime;
    }
  }
  if (id == 3) {    
    if (gameTime - 10000 >= delays[2]) {  
      sFX12.play(7);
      int x;
      if(Player1.level < 5){
      x = 1;   
      } else {
      x = Player1.level - 4; 
      }
      
      Unit AllyTurret = new Unit(2,x,-1, 43, Player1.x, Player1.y, 10000000, 25, 10, 10, 0, true,false, 200, Player1.bDmg/6, 5, 4, 300, false, 0, 0); 
      Allies.add(AllyTurret);
      Units.add(AllyTurret);
      delays[2] = gameTime;
    }
  }
  if (id == 4) { 
    if (gameTime - 10000 >= delays[3]) { 
        sFX8.play(7);
      StatusEffect Test1 = new StatusEffect(2,null,Player1,5000,Player1.armor*.10f,2);
      StatusEffects.add(Test1);
      StatusEffect Test2 = new StatusEffect(2,null,Player1,5000,Player1.bDelay*.5f,4);
      StatusEffects.add(Test2);
      delays[3] = gameTime;
    }
  }
      if (id == 5) {
    if (gameTime - 10000 >= delays[4]) { 
        sFX8.play(7);
      StatusEffect Test1 = new StatusEffect(2,null,Player1,5000,Player1.BaseSpeed * 3,3);
      StatusEffects.add(Test1);
      StatusEffect Test2 = new StatusEffect(2,null,Player1,5000,5000,4);
      StatusEffects.add(Test2);
      delays[4] = gameTime;
    }
  }
  if (id == 6) {    
    //println("Made it");
    if (gameTime - 1000 >= delays[5]) {  
       for(int i = 0;i<Enemies.size();i++){
         if(inRange(mouseX,mouseY,Enemies.get(i).x,Enemies.get(i).y,Enemies.get(i).size*4,Enemies.get(i).size*4)) {
                sFX6.play(7);
      Pellet A1bullet = new Pellet(Player1.bDmg * .66f, Player1.bSize, Player1.bSpd * .25f, 1,Player1.x + 50, Player1.y, false, 0, 0,Enemies.get(i),null);
      Pellets.add(A1bullet);
      Pellet A2bullet = new Pellet(Player1.bDmg * .66f, Player1.bSize, Player1.bSpd * .25f, 1,Player1.x - 50, Player1.y, false, 0, 0,Enemies.get(i),null);
      Pellets.add(A2bullet);
      Pellet A3bullet = new Pellet(Player1.bDmg * .66f, Player1.bSize, Player1.bSpd * .25f, 1,Player1.x, Player1.y - 50, false, 0, 0,Enemies.get(i),null);
      Pellets.add(A3bullet);//Pellet(float damage1, float size1, float speed1, int target1, float sx1, float sy1, boolean Aoe1, float aoeD1, float aoeR1)
      delays[5] = gameTime;
         }
       }
    }
  }
   if (id == 7) { 
    if (gameTime - 20000 >= delays[6]) { 
        sFX8.play(7);
      StatusEffect Test1 = new StatusEffect(2,null,Player1,10000,5,1);
      StatusEffects.add(Test1);
      delays[6] = gameTime;
    }
  }
  if (id == 8) {
    if (gameTime - 15000 >= delays[7]) { 
        sFX8.play(7);
      StatusEffect Test1 = new StatusEffect(2,null,Player1,5000,Player1.BaseSpeed * .5f,3);
      StatusEffects.add(Test1);
      StatusEffect Test2 = new StatusEffect(2,null,Player1,5000,Player1.BaseBDelay * .25f,4);
      StatusEffects.add(Test2);
      StatusEffect Test3 = new StatusEffect(2,null,Player1,5000,Player1.BaseArmor * 4,2);
      StatusEffects.add(Test3);
      StatusEffect Test4 = new StatusEffect(2,null,Player1,5000,Player1.BaseBDmg * .5f,5);
      StatusEffects.add(Test4);
      delays[7] = gameTime;
    }
  }
}
class Ability{
String name;
String info;
int id;
int cooldown;

Ability(int id1,String name1,String info1,int cooldown1){
id = id1;
name = name1;
info = info1;
cooldown = cooldown1;
}
}
public void loadAbilityProfiles(){
Ability Ability1 = new Ability(1,"Bullet Stream","Rapidly shoots enemies near cursor with homing bullets",50);  
Abilities.add(Ability1);
Ability Ability2 = new Ability(2,"Meteor Shot","Shoots a high damage projectile",5000);  
Abilities.add(Ability2);
Ability Ability3 = new Ability(3,"Turret(D-M1)","Places a turret that shoots at enemies",10000);  
Abilities.add(Ability3);
Ability Ability4 = new Ability(4,"Exposed Barage","Increases Firerate/Decreases Armor",10000);  
Abilities.add(Ability4);
Ability Ability5 = new Ability(5,"Nitrous Thrusters","Increases Speed but you can not attack",10000);  
Abilities.add(Ability5);
Ability Ability6 = new Ability(6,"Tripe Shot","Shoots 3 homing projectiles",1000);  
Abilities.add(Ability6);
Ability Ability7 = new Ability(7,"Nano Repair","Recovers 50 health over 10 seconds",20000);  
Abilities.add(Ability7);
Ability Ability8 = new Ability(8,"Turret Mode","Increase Firerate:Armor/Decreases Damage:Speed",15000);  
Abilities.add(Ability8);
}
int[] dispalyedAbilities = new int[4]; 
int[] savedAbilities = new int[4];
Ability[] abilityData = new Ability[4];
public void pullAbilitiesData(){
abilityData[0] = Abilities.get(dispalyedAbilities[0]-1);
abilityData[1] = Abilities.get(dispalyedAbilities[1]-1);
abilityData[2] = Abilities.get(dispalyedAbilities[2]-1);
abilityData[3] = Abilities.get(dispalyedAbilities[3]-1);
}
public void drawAbilitySelect(){
  println(dispalyedAbilities);
   Button saveChanges = new Button(width/2, 175, 30, 10, "Save Changes", 24, 0, 255, 0);
    saveChanges.drawMe();
    Button changeAbility1 = new Button(width/2 - 450, height - 300, 30, 10, "Change Ability 1", 24, 0, 255, 0);
    changeAbility1.drawMe();
    Button changeAbility2 = new Button(width/2 - 150, height - 300, 30, 10, "Change Ability 2", 24, 0, 255, 0);
    changeAbility2.drawMe();
    Button changeAbility3 = new Button(width/2 + 150, height - 300, 30, 10, "Change Ability 3", 24, 0, 255, 0);
    changeAbility3.drawMe();
    Button changeAbility4 = new Button(width/2 + 450, height - 300, 30, 10, "Change Ability 4", 24, 0, 255, 0);
    changeAbility4.drawMe();
    if(changeAbility1.isClicked()){
    showingAbilitySelections = 2;
    changingAbility = 1;
    gameScreen = 4;
    }
    if(changeAbility2.isClicked()){
    showingAbilitySelections = 2;
    changingAbility = 2;
    gameScreen = 4;
    }
    if(changeAbility3.isClicked()){
    showingAbilitySelections = 2;
    changingAbility = 3;
    gameScreen = 4;
    }
    if(changeAbility4.isClicked()){
    showingAbilitySelections = 2;
    changingAbility = 4;  
    gameScreen = 4;
    }
    displayAbilities();
    if (saveChanges.isClicked()) {
    savedAbilities = dispalyedAbilities;
    gameScreen = 0;
    }
 displayAbilities(); 
}
public void displayAbilities(){
printText(width/2 - 450,height/2 - 150,24,"1",255,0,0); 
printText(width/2 - 450,height/2 - 100,24,abilityData[0].name,255,0,0); 
printText(width/2 - 450,height/2,12,abilityData[0].info,255,0,0); 
printText(width/2 - 150,height/2 - 150,24,"2",255,0,0); 
printText(width/2 - 150,height/2 - 100,24,abilityData[1].name,255,0,0); 
printText(width/2 - 150,height/2,12,abilityData[1].info,255,0,0); 
printText(width/2 + 150,height/2 - 150,24,"3",255,0,0); 
printText(width/2 + 150,height/2 - 100,24,abilityData[2].name,255,0,0); 
printText(width/2 + 150,height/2,12,abilityData[2].info,255,0,0); 
printText(width/2 + 450,height/2 - 150,24,"4",255,0,0); 
printText(width/2 + 450,height/2 - 100,24,abilityData[3].name,255,0,0); 
printText(width/2 + 450,height/2,12,abilityData[3].info,255,0,0); 
}
class Button {
  PImage img;
  float x;
  float y;
  float xsize, ysize, tSize;
  boolean flag;
  String text = "null";
  float C1, C2, C3;
  String image1 = "null";
  int timer = millis();
  Button(float X, float Y, float xSize, float ySize, String Text, float textSize, float c1, float c2, float c3) {
    x = X;
    y = Y;
    xsize = xSize;
    ysize = ySize;
    tSize = textSize;
    text = Text;
    C1 = c1;
    C2 = c2;
    C3 = c3;
  }
  Button(float X, float Y, String image) {
    x = X;
    y = Y;
    image1 = image; 
    imageMode(CENTER); 
    img = loadImage(image1);
  }
  public void drawMe() {
    //Draws this instance of a button 
    textSize(tSize);  
    textAlign(CENTER);
    fill(C1, C2, C3); 
    if (text != "null") {
      text(text, x, y);
    }
    if (image1 != "null") {
      image(img, x, y, xsize*2, ysize*2);
    }
    
  }
  public boolean isClicked() {
    //Detects to see if this button is pressed
    flag = mousePressed && inRange(mouseX, mouseY, x, y, xsize*3, ysize);
    if (flag & timer <= millis()) {
      timer = millis() + 250;
      return true;
    } else {
      return false;
    }
  }
}
int changingAbility;
int showingAbilitySelections;
Ability choosenAbility;
public void drawAbilityChange(){ 
choosenAbility = Abilities.get(showingAbilitySelections);
Button Select = new Button(width/2, height/2, 30, 10, "Select", 24, 0, 255, 0);
Select.drawMe();
if(showingAbilitySelections >= 2){
printText(width - width/4,height/2 - 300,48,Abilities.get(showingAbilitySelections-2).name,255,0,0);
printText(width/2, height/2 + 50,12,Abilities.get(showingAbilitySelections).info,255,255,255);
}
if(showingAbilitySelections >= 1){
printText(width - width/4,height/2 - 150,48,Abilities.get(showingAbilitySelections-1).name,255,0,0);
}
rectMode(CENTER);
stroke(0,255,0);
noFill();
rect(width - width/4,height/2 - 25,400,100);
printText(width - width/4,height/2,48,Abilities.get(showingAbilitySelections).name,255,0,0);
if(showingAbilitySelections + 1 < Abilities.size()){
printText(width - width/4,height/2 + 150,48,Abilities.get(showingAbilitySelections+1).name,255,0,0);
}
if(showingAbilitySelections + 2 < Abilities.size()){
printText(width - width/4,height/2 + 300,48,Abilities.get(showingAbilitySelections+2).name,255,0,0);
}
boolean flag1 = showingAbilitySelections - 1 < 0;
boolean flag2 = showingAbilitySelections + 2 > Abilities.size();
if(!flag1){
Up.drawMe();  
}
if(!flag2){
Down.drawMe();  
}
if(Down.isClicked() & !flag2){
showingAbilitySelections += 1; 
}
if(Up.isClicked() & !flag1){
showingAbilitySelections -= 1; 
}
if(Select.isClicked()){
  dispalyedAbilities[changingAbility-1]= choosenAbility.id;
  pullAbilitiesData();
  gameScreen = 3;
}
}
class Collectable {
  PImage img;
  float amount, x, y, size, c1, c2, c3, speed;
  int type;
  Collectable(int type1, float amount1, float x1, float y1, float size1, float speed1) {
    x = x1;
    y = y1;
    size = size1;
    type = type1;
    amount = amount1;
    speed = speed1;
    String texture = chooseTexture(type);
    img = loadImage(texture);
  } 

  public String chooseTexture(int x1) {
    String y = "";  
    if (x1 == 2) {
      y = "Health.png";
    }
    if (x1 == 3) {
      y = "Armor.png";
    }
    return y;
  }

  public void drawMe() {
    //Draws this instance of an obstacle
    imageMode(CENTER);
    image(img, x, y, size*2, size*2);
  }
  public void run() {
    //Runs all methods of this instance of the obstacle
    drawMe();
    if (!Paused) {
      CollectPlayer();
      Movement();
    }
  }
  public void Movement() {
    //Moves this enemy towards the player
    float distance = sqrt(sq(Player1.x - x) + sq(Player1.y - y));
    x += (Player1.xvel * -1) +(Player1.x - x)/(distance*speed);
    y += (Player1.yvel * -1) +(Player1.y - y)/(distance*speed);
  }
  public void CollectPlayer() {
    if (inRange(x, y, Player1.x, Player1.y, Player1.size, Player1.size)) {
      giveBuff(type, amount);
      Collectables.remove(this);
    }
  }
  public void giveBuff(int x, float y) {
    if (x == 1) {
      Player1.cash += y;
    }
    if (x == 2) {
      sFX4.play(7);
      if (base.health - y <= base.maxHealth) {
        base.health += y;
      } else {
        base.health = base.maxHealth;
      }
      }
    if (x == 3) {
      sFX1.play(7);
      Player1.armor += y;
    }
  }
}
class abilityIcon { 
  float x, y;
  int ability;
  abilityIcon(float x1, float y1, float size1, int ability1, char keyConfig1) {
    x = x1;
    y = y1;
    ability = ability1;
  }
  public void run() {
    drawMe();
  }
  public void drawMe() {
    fill(255, 255, 0);
    rectMode(CENTER);
    rect(x, y, 75, 75);
  } 
}  
public void drawMainGame() {
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
    Transmission message = new Transmission(enemyIMG[PApplet.parseInt(random(0,4))],enemyS[PApplet.parseInt(random(0,7))],3000);
  }
  printText(width/2, 60,36,"Time Survived: " + str(PApplet.parseInt(gameTime/1000)), 255, 0, 0);
  if (Paused) {
    printText(width/2, height/2, 48, "Paused", 100, 255, 0);
  }
  printText(60, height - 20, 12, "Base Health:", 0, 255, 0);
  printText(140, height - 20, 12, PApplet.parseInt(base.health) + "/" + PApplet.parseInt(base.maxHealth), 0, 255, 0);
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
public void loadWorld(){
  for (int i =0; i< 100; i++) { 
        float coords[] = chooseLocationObstacles();
        Obstacle Obstacle1 = new Obstacle(coords[0], coords[1], random(250, 400), 30, 0, 20, 30, 40, true);
        Obstacles.add(Obstacle1);
      }
}

public void drawMainMenu(){
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
      currentSong = PApplet.parseInt(random(0,13)); 
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
class Obstacle {
  PImage img;
  float rotation;
  int hurtDelay = gameTime; 
  float tx, ty, x, y, health, maxHealth, size, damage, c1, c2, c3;
  boolean canBreak, canHurt;
  Obstacle(float x1, float y1, float health1, float size1, float damage1, float C1, float C2, float C3, boolean canBreak1) {
    x = x1;
    y = y1;
    health = health1;
    maxHealth = health1;
    size = size1;
    damage = damage1;
    c1 = C1;
    c2 = C2;
    c3 = C3;
    canBreak = canBreak1;
    img = loadImage("Asteroid.png");
    rotation = random(0, 2);
  } 
  public void drawMe() {
    //Draws this instance of an obstacle
    pushMatrix();
    translate(x, y);
    rotate(PI * rotation);
    imageMode(CENTER);
    //boolean flag1 = x > 0 && x < width; 
    //boolean flag2 = y > 0 && y < height;
    //if(flag1 && flag2){
    image(img, 0, 0, size*2, size*2);
    //}
    popMatrix();
    if (canBreak) {
      drawHealth();  
      Die();
    }
      if (!Paused) {
      Movement();
      }
  }
  public void run() {
    //Runs all methods of this instance of the obstacle
    drawMe();
  }
  public void Movement() {
    x += (Player1.xvel * -1);
    y += (Player1.yvel * -1);
  }
  public void drawHealth() {
    //Draws the health bar for this obstacle
    stroke(255, 0, 0);
    line(x-size, y-size, x+size, y-size); 
    stroke(0, 255, 0);
    line(x-size, y-size, (health*((x+size)-(x-size)))/maxHealth + (x-size), y-size);  
    stroke(0, 0, 0);
  }
  public void Die() {
    //Kills this obstacle if it is out of health.
    if (health <= 0) {
      sFX4.play(2);
      Obstacles.remove(this);
      float coords[] = chooseLocationObstacles();
      Obstacle Obstacle1 = new Obstacle(coords[0], coords[1], random(10, 20), 30, 0, 20, 30, 40, true);
      Obstacles.add(Obstacle1);
    }
  }
}
class Pellet {
  PImage img;
  float sx, sy, cx, cy, tx, ty;
  float damage, size, speed;
  int target;
  float aoeD, aoeR;
  boolean Aoe;
  boolean homing = false;
  PVector movement;
  int time;
  Unit Target;
  Player playerTarget;
  Pellet(float damage1, float size1, float speed1, int target1, float sx1, float sy1, float tx1, float ty1, boolean Aoe1, float aoeD1, float aoeR1) {
    aoeD = aoeD1;
    aoeR = aoeR1;
    Aoe = Aoe1;
    damage = damage1;
    size = size1;
    speed = speed1;
    target = target1;
    sx = sx1;
    sy = sy1;
    cx = sx1;
    cy = sy1;
    tx = tx1;
    ty = ty1;
    time = gameTime;
    movement = new PVector((tx - sx), (ty - sy));
    String texture = chooseTexture(target);
    img = loadImage(texture);
  }
  Pellet(float damage1, float size1, float speed1, int target1, float sx1, float sy1, boolean Aoe1, float aoeD1, float aoeR1, Unit Target1,Player playerTarget1) {
    aoeD = aoeD1;
    aoeR = aoeR1;
    Aoe = Aoe1;
    damage = damage1;
    size = size1;
    speed = speed1;
    target = target1;
    sx = sx1;
    sy = sy1;
    cx = sx1;
    cy = sy1;
    homing = true;
    time = gameTime;
    Target = Target1;
    playerTarget = playerTarget1;
    String texture = chooseTexture(target);
    img = loadImage(texture);
  }
  public void drawMe() {
    //Draws this instance of a Pellet
    imageMode(CENTER);
    //boolean flag1 = cx > 0 && cx < width; 
    //boolean flag2 = cy > 0 && cy < height;
    //if(flag1 && flag2){
    image(img, cx, cy, size*2, size*2);
    //}
  }

  public void run() {
    //Runs all methods inside of the Pellet class
    drawMe();
    expire();
    if (!Paused) {
      Movement();
      Collide();
      if (homing) {
        loseTarget();
      }
    }
  } 
  public void loseTarget() {
    if(target == 1){
    boolean flag = Target.health > 0;
    if (!flag) {
      Pellets.remove(this);
    }
    }
    if(target == 2){
    boolean flag = playerTarget.health > 0;
    if (!flag) {
      Pellets.remove(this);
    }
    }
  }
  public void Collide() {
    //Detects for collision between obstacles
    for (int i = 0; i<Obstacles.size (); i++) {
      if (inRange(Obstacles.get(i).x, Obstacles.get(i).y, cx, cy, Obstacles.get(i).size/2, Obstacles.get(i).size/2)) {
        Pellets.remove(this);
        sFX7.play(7);
        Obstacles.get(i).health -= damage;
      }
    }
  }
  public void expire(){
   if(time + 5000 <= gameTime){
    Pellets.remove(this);
   }
  }
  public String chooseTexture(int x1) {
    String y = "";  
    if (x1 == 1) {
      y = "Bullet2.png";
    }
    if (x1 == 2) {
      y = "Bullet.png";
    }
    return y;
  }

  public void Movement() {
    //Moves this pellet towards its target
    if (!homing) {
      movement.normalize();
      cx += (Player1.xvel * -1) + speed*movement.x;
      cy += (Player1.yvel * -1) + speed*movement.y;
    } else {
      if(target == 1){
      float distance = sqrt(sq(Target.x - cx) + sq(Target.y - cy));
      cx += (Player1.xvel * -1) + (Target.x - cx)/(distance*speed/10);
      cy += (Player1.yvel * -1) + (Target.y - cy)/(distance*speed/10);
      }
      if(target == 2){
        float distance = sqrt(sq(playerTarget.x - cx) + sq(playerTarget.y - cy));
      cx += (Player1.xvel * -1) + (playerTarget.x - cx)/(distance*speed/10);
      cy += (Player1.yvel * -1) + (playerTarget.y - cy)/(distance*speed/10);
      }
    }
  }
}
class Player {
  PImage img;
  int skillpoints= 1;
  float tx = width/2, ty = height/2;
  int level = 1;
  float exp = 0, expMax = 10;
  int useDelay = gameTime; 
  float[] stats = new float[8];
  int[] selectedAbilities = new int[4];
  float armor;
  float speed;
  float xvel;
  float yvel;
  float xdir;
  float ydir;
  float BaseBDmg,BaseBSpd,BaseBSize,BaseBDelay,BaseMaxHealth,BaseSpeed,BaseArmor,BaseSize;
  float bDmg, bSpd , bSize, bDelay;
  float x, y;
  int score;
  int cash;
  float size = 10;
  float health, maxHealth;
  float rotation;
  float a;
  float damageTaken;
  float showDamageTimer;
  int selectedAbility;
  boolean damaged;
  int intAbility = 1;
  public void drawMe() {
    //Draws the player
    pushMatrix();
    translate(x, y);
    rotate(PI * rotation);
    imageMode(CENTER);
    image(img, 0, 0, size*2, size*2);
    popMatrix();
    drawHealth();
    drawLevel();
  }
  public void run() {
    //Runs all the methods in the player class
    drawMe();
    if (!Paused) {
      //applyBuffs();
      gainLevel();
      Movement();
      Shoot();
      selectAbilities();
      activateAbility();
      Die();
      Collide();
      checkCollisions();
      hitWalls();
      if (damaged & gameTime - showDamageTimer <= 200) {
        displayDamage();
      }
    }
  } 
  public void loadSelectedStats(){
    stats = savedShipStats;
    BaseMaxHealth = stats[0];
    BaseBDmg = stats[1];
    BaseSpeed = stats[2];
    BaseArmor = stats[3];
    BaseSize = stats[4];
    BaseBSpd =  stats[5];
    BaseBDelay =  stats[6];
    BaseBSize =  stats[7];
  }
  public void loadAbilities(){
  selectedAbilities = savedAbilities;
  selectedAbility = selectedAbilities[0];
  }
  public void gainLevel() {
    if (exp >= expMax) {
      exp = expMax - exp;
      expMax += expMax * .5f;
      level += 1;
      armor +=  stats[3] * .1f;
      maxHealth += stats[0] * .1f;
      bDmg += stats[1] * .37f;
      skillpoints += 1;
      BaseArmor +=  stats[3] * .1f;
      BaseMaxHealth += stats[0] * .1f;
      BaseBDmg += stats[1] * .37f;
    }
  }
  public void Spawn(){
    maxHealth = BaseMaxHealth;
     health = Player1.maxHealth;
    skillpoints = 1;
     level = 1;
      exp = 0;
     expMax = 10;
      cash = 0;
      speed = stats[2];
     size = stats[4];
      armor = stats[3];
      bDmg = stats[1];
     bSpd = stats[5];
      bSize = stats[7];
     bDelay = stats[6];
  }
  public void Die() {
    //Kills player if health is 0
    if (health <= 0) {
      repositionGame();
      Spawn();
      Player1.speed = BaseSpeed;
      Player1.yvel = -1* speed;  
      Player1.xvel = 0;
      Player1.rotation = 0;
      Transmission message = new Transmission(loadImage("Commander.png"),"Get back out ther pilot! We need your help!",3000);
    }
  }
  public void displayDamage() {
    String displaydamage = "" + Math.round(damageTaken);
    printText(x, y, 24, displaydamage, 255, 0, 0);
  } 
  public void checkCollisions() {
    for (int i = 0; i < Enemies.size (); i++) {
     Unit attacker = Enemies.get(i);
      boolean flag = inRange(attacker.x, attacker.y, x, y, size/2, size/2);      
      if (flag & gameTime - 500 >= attacker.hurtDelay) {
        sFX7.play(7);
        float damageDealt = attacker.damage - ((armor/(armor + 100)) * attacker.damage);
        health -= damageDealt;
        damageTaken = damageDealt;
        damaged = true;
        showDamageTimer = gameTime;
        attacker.hurtDelay = gameTime;
        Pellets.remove(attacker);
      }
    }
    for (int i = 0; i < Pellets.size (); i++) {
      Pellet attacker = Pellets.get(i);
      boolean flag = inRange(attacker.cx, attacker.cy, x, y, size/2, size/2);      
      if (flag & attacker.target == 2)
      {
        sFX7.play(7);
        float damageDealt = attacker.damage - ((armor/(armor + 100)) * attacker.damage);
        health -= damageDealt;
        damageTaken = damageDealt;
        damaged = true;
        showDamageTimer = gameTime;
        Pellets.remove(attacker);
      }
    }
  }
  public void Collide() {
    //Detects collsions between obstacles and players
    for (int i = 0; i<Obstacles.size (); i++) {
      if (inRange(Obstacles.get(i).x, Obstacles.get(i).y, x, y, Obstacles.get(i).size/2, Obstacles.get(i).size/2)) {
        float damageDealt = Obstacles.get(i).health/8 - ((armor/(armor + 100)) * Obstacles.get(i).health/8);
        health -= damageDealt;
        Obstacles.remove(i);
      }
    }
  for (int i = 0; i<Structures.size (); i++) {
      if (inRange(Structures.get(i).x, Structures.get(i).y, x, y, Structures.get(i).size/2, Structures.get(i).size/2)) {
        sFX11.play(7);
        xvel = -speed;
        yvel = -speed;
        if (ydir < 0) {
          rotation = 0;
        }
        if (ydir > 0) {
          rotation = 1;
        }
        if (xdir < 0) {
          rotation = 1.5f;
        }
        if (xdir > 0) {
          rotation = .5f;
        }
      }
    }
  }
  public void drawHealth() {
    //Draws the players health bar
    strokeWeight(4);
    stroke(255, 0, 0);
    line(x-size, y-size, x+size, y-size); 
    stroke(0, 255, 0);
    line(x-size, y-size, (health*((x+size)-(x-size)))/maxHealth + (x-size), y-size);  
    stroke(0, 0, 0);
    strokeWeight(1);
  }
  public void drawLevel() {
    //Draws the players level
    stroke(255, 0, 0);
    printText(x-size * 2, y-size/2, 12, str(level), 0, 0, 255);
  }
  public void hitWalls() {
    //Detects collision between walls and player
    //if (tx-size <= -1300 || tx+size >= 1000 || ty-size <= -1300 || ty+size >= 1000) {
    //  sFX11.play(7);
    //   xdir *= -1;
    //    ydir *= -1;
    //  if (ydir < 0) {
    //    rotation = 0;
    //  }
    //  if (ydir > 0) {
    //    rotation = 1;
    //  }
    //  if (xdir < 0) {
    //    rotation = 1.5;
    //  }
    //  if (xdir > 0) {
    //    rotation = .5;
    //  }
    //}
  }
  public void Movement() {
    //Allows user to controll the player using W,A,S,or D
    if (gameScreen == 1) {
      if (keyPressed) {
        if (key == 'a') { 
          xdir = -1;  
          ydir = 0;
          rotation = 1.5f;
        }
        if (key =='d') {
          xdir = 1; 
          ydir = 0;
          rotation = .5f;
        }
        if (key == 'w') {
          ydir = -1;  
          xdir = 0;
          rotation = 0;
        }
        if (key == 's') {
          ydir = 1;  
          xdir = 0;
          rotation = 1;
        }
      }
      xvel = xdir * speed; 
      yvel = ydir * speed;
      tx += xvel;
      ty += yvel;
    }
  }
  public void Shoot() {
    //Allos the player to shoot a pellet upon clicking te mouse
    if (mousePressed  && (mouseButton == LEFT)) {
      if (gameTime - bDelay >= useDelay) {  
        sFX6.play(7);
        Pellet Pbullet = new Pellet(bDmg*bdMod, bSize, bSpd, 1, x, y, mouseX, mouseY, false, 0, 0); 
        Pellets.add(Pbullet);
        useDelay = gameTime;
      }
    }
  }
  public void selectAbilities() {
    if (keyPressed) {
      if (key == '1') { 
        intAbility = 1;
        selectedAbility = selectedAbilities[0];
      }
      if (key =='2') {
        intAbility = 2;
        selectedAbility = selectedAbilities[1];
      }
      if (key == '3') {
        intAbility = 3;
        selectedAbility = selectedAbilities[2];
      }
      if (key == '4') {
        intAbility = 4;
        selectedAbility = selectedAbilities[3];
      }
    }
  }
  public void activateAbility() {
    if (mousePressed  && (mouseButton == RIGHT)) { 
      useAbility(selectedAbility);
    }
  }
}
//Health
//Damage
//Speed
//Armor
//Size
//Bullet Speed
//Rate of fire
//Bullet Size
public void loadShipProfiles(){
float [] aegisStats = new float[8];
aegisStats[0] = 300; 
aegisStats[1] = 25; 
aegisStats[2] = 3; 
aegisStats[3] = 20; 
aegisStats[4] = 12; 
aegisStats[5] = 6; 
aegisStats[6] = 300; 
aegisStats[7] = 4; 
ShipProfiles.put("Aegis",aegisStats);
float [] aresStats = new float[8];
aresStats[0] = 150; 
aresStats[1] = 31; 
aresStats[2] = 3.5f; 
aresStats[3] = 15; 
aresStats[4] = 10; 
aresStats[5] = 4; 
aresStats[6] = 175; 
aresStats[7] = 3.75f; 
ShipProfiles.put("Ares",aresStats);
float [] isisStats = new float[8];
isisStats[0] = 400; 
isisStats[1] = 40; 
isisStats[2] = 1; 
isisStats[3] = 40; 
isisStats[4] = 15; 
isisStats[5] = 7; 
isisStats[6] = 500; 
isisStats[7] = 7; 
ShipProfiles.put("Isis",isisStats);
float [] hermesStats = new float[8];
hermesStats[0] = 175; 
hermesStats[1] = 25; 
hermesStats[2] = 7; 
hermesStats[3] = 50; 
hermesStats[4] = 7; 
hermesStats[5] = 9; 
hermesStats[6] = 225; 
hermesStats[7] = 10; 
ShipProfiles.put("Hermes",hermesStats);
avaliableShips.add("Aegis");
avaliableShips.add("Ares");
avaliableShips.add("Isis");
avaliableShips.add("Hermes");
}
String selectedShip;
int selectedShipId;
int discoveredShips;
float [] selectedShipStats = new float[8];
public void drawShipSelect(){  
    Button saveChanges = new Button(width/2, 175, 30, 10, "Save Changes", 24, 0, 255, 0); 
    nextShip.drawMe();
    prevShip.drawMe();
    println(nextShip.timer,millis());
    saveChanges.drawMe();
    selectedShip = avaliableShips.get(selectedShipId);
    loadAvalibleShipData();
    if (nextShip.isClicked()) {
      if (discoveredShips - selectedShipId == 1) {
        selectedShipId = 0;
      } else {
        selectedShipId += 1;
      }
    }
    if (saveChanges.isClicked()) {
      savedShipStats = selectedShipStats;
      gameScreen = 0;
    }
    if (prevShip.isClicked()) {
      if (selectedShipId == 0) {
        selectedShipId = discoveredShips - 1;
      } else {
        selectedShipId -= 1;
      }
    } 
displayShipStats();

}
public void loadAvalibleShipData(){
selectedShipStats = ShipProfiles.get(selectedShip); 
}
public void displayShipStats(){
  printText(width/2,100,48,"Ship Select",255,0,0);
  printText(width/2 - 150,220,24,"Hp",255,0,0);
  printText(width/2 - 150,245,18,str(selectedShipStats[0]),255,0,0);
  printText(width/2 - 150,270,24,"Damage",255,0,0);
  printText(width/2 - 150,295,18,str(selectedShipStats[1]),255,0,0);
  printText(width/2 - 150,320,24,"Speed",255,0,0);
  printText(width/2 - 150,345,18,str(selectedShipStats[2]),255,0,0);
  printText(width/2 - 150,370,24,"Armor",255,0,0);
  printText(width/2 - 150,395,18,str(selectedShipStats[3]),255,0,0);
  printText(width/2 + 150,220,24,"Size",255,0,0);
  printText(width/2 + 150,245,18,str(selectedShipStats[4]),255,0,0);
  printText(width/2 + 150,270,24,"Bullet Speed",255,0,0);
  printText(width/2 + 150,295,18,str(selectedShipStats[5]),255,0,0);
  printText(width/2 + 150,320,24,"Rate of Fire",255,0,0);
  printText(width/2 + 150,345,18,str(selectedShipStats[6]),255,0,0);
  printText(width/2 + 150,370,24,"Bullet Size",255,0,0);
  printText(width/2 + 150,395,18,str(selectedShipStats[7]),255,0,0);
  printText(width/2,height - 200,24,"Passive",255,0,0);
  printText(width/2,height - 175,18,"Test",255,0,0);
  printText(width/2,height - 100,48,"Ship Selected",255,0,0);
  printText(width/2,height - 50,48,selectedShip,255,0,0);
}
int enemyRangeMin = 1;
public void spawnEnemy(float x, float y, int x1) {
  if(enemyLevelrange - 5 <= 1){
   enemyRangeMin = 1; 
  } else {
    enemyRangeMin = enemyLevelrange - 5;
  }
  //int level1,int type1, int worth1, float x1, float y1, float speed1, float health1, int armor1, float size1, float damage1
  //int level1,int type1, int worth1, float x1, float y1, float speed1, float health1, int armor1, float size1, float damage1, boolean canShoot1,float agroRange1, float pD1, float pSi1, float pSP1, float fireRate1,boolean Aoe1,float aoeD1, float aoeR1
  //Spawns an enemyy 
  if (x1 == 1) {
    //
    Unit Enemy1 = new Unit(1, PApplet.parseInt(random(enemyRangeMin, enemyLevelrange)), x1, 16, x, y, 1, 100, 30, 10, 40);  
    Enemies.add(Enemy1);
    Units.add(Enemy1);
  }
  if (x1 == 2) {
    //Shooter
    Unit Enemy1 = new Unit(1, PApplet.parseInt(random(enemyRangeMin, enemyLevelrange)), x1, 43, x, y, 2, 100, 10, 15, 0, true, false, 200, 20, 5, 2, 1000, false, 0, 0);  
    Enemies.add(Enemy1);
    Units.add(Enemy1);
  }
  if (x1 == 3) {
    //Fast Shooter
    Unit Enemy1 = new Unit(1, PApplet.parseInt(random(enemyRangeMin, enemyLevelrange)), x1, 43, x, y, 2, 100, 10, 20, 0, true, false, 200, 3, 5, 3, 150, false, 0, 0);  
    Enemies.add(Enemy1);
    Units.add(Enemy1);
  }
  if (x1 == 4) {
    //Homing Shooter
    Unit Enemy1 = new Unit(1, PApplet.parseInt(random(enemyRangeMin, enemyLevelrange)), x1, 43, x, y, 2, 50, 10, 20, 0, true, true, 200, 3, 5, 3, 300, false, 0, 0);  
    Enemies.add(Enemy1);
    Units.add(Enemy1);
  }
}
class StatusEffect { 
  float statusType;
  float statusLength, statusAmount;
  int targetType;
  int timeInterval = gameTime;
  Unit unitTarget;
  Player playerTarget;
  StatusEffect(int targetType1, Unit unitTarget1, Player playerTarget1, float statusLength1, float statusAmount1, float statusType1) {
    statusLength = gameTime + statusLength1;
    statusAmount = statusAmount1;
    targetType = targetType1;
    unitTarget = unitTarget1;
    playerTarget = playerTarget1;
    statusType = statusType1;
  }
  //1 = Health,2 = Armor,3 = Speed,4 = attackSpeed,5 = damage
  public void run() {
    println(timeInterval,gameTime);
    if (gameTime <= statusLength) { 
      if (targetType == 1) { 
        if (statusType == 1) {  
          if(timeInterval + 1000<= gameTime){ 
          unitTarget.health += statusAmount;
          timeInterval = gameTime;
        }
        }
        if (statusType == 2) {
          unitTarget.armor = statusAmount;
        }
        if (statusType == 3) {
          unitTarget.speed = statusAmount;
        }
        if (statusType == 4) {
          unitTarget.fireRate = statusAmount;
        }
        if (statusType == 5) {
          unitTarget.pD = statusAmount;
        }
        if (statusType == 6) {
          unitTarget.damage = statusAmount;
        }
      }
      if (targetType == 2) {
        if (statusType == 1) {
          if(timeInterval + 1000 <= gameTime){ 
          if(playerTarget.health >= playerTarget.maxHealth){
          playerTarget.health = playerTarget.maxHealth;
          }else{
          playerTarget.health += statusAmount;
          }
          timeInterval = gameTime;
          }
        }
        if (statusType == 2) {
          playerTarget.armor = statusAmount;
        }
        if (statusType == 3) {
          playerTarget.speed = statusAmount;
        }
        if (statusType == 4) {
          playerTarget.bDelay = statusAmount;
        }
        if (statusType == 5) {
          playerTarget.bDmg = statusAmount;
        }
      }
    } else {
      if (targetType == 1) {
        if (statusType == 2) {
          unitTarget.armor = unitTarget.Basearmor;
        }
        if (statusType == 3) {
          unitTarget.speed = unitTarget.BaseSpeed;
        }
        if (statusType == 4) {
          unitTarget.fireRate = unitTarget.BasefireRate;
        }
        if (statusType == 5) {
          unitTarget.pD = unitTarget.BasepD;
        }
        if (statusType == 6) {
          unitTarget.damage = unitTarget.Basedamage;
        }
      }
      if (targetType == 2) {
        if (statusType == 2) {
          playerTarget.armor = playerTarget.BaseArmor;
        }
        if (statusType == 3) {
          playerTarget.speed = playerTarget.BaseSpeed;
        }
        if (statusType == 4) {
          playerTarget.bDelay = playerTarget.BaseBDelay;
        }
        if (statusType == 5) {
          playerTarget.bDmg = playerTarget.BaseBDmg;
        }
      }
          StatusEffects.remove(this);
    }
  }
}
class Structure {
  PImage img;
  float x, y, health, maxHealth, armor, size;
  int worth;
  int type;
  float damageTaken;
  float showDamageTimer;
  boolean damaged;
  String imgString;
  Structure(int type1,int worth1, float x1, float y1, float health1, int armor1, float size1) {
    x = x1; 
    y = y1;
    health = health1; 
    armor = armor1;  
    maxHealth = health1;
    size = size1;
    worth = worth1;
    type = type1;
    if(type == 2){
    imgString = chooseTexture();
    img = loadImage(imgString);
    }
  }
  public void run() {
    drawMe();
    checkCollisions();
    Die();
    if (!Paused) {
    Movement();
    }
    if (damaged & gameTime - showDamageTimer <= 200) {
      displayDamage();
    }
  }
  public String chooseTexture(){
    String returnImg = null;
    if(type == 2){
      returnImg = "EnemeyBase.png";
    }
    return returnImg;
  }
  public void Die(){
  if (health <= 0) {
      sFX2.play(7);
      Player1.level += 1;
      Player1.exp += 1000;
      Structures.remove(this);
      //      for (int i = 0; i<=random (1, 5); i++) {
      //        Collectable Coin = new Collectable(1, int(random(1, 3)), x+int(random(size*-1, size)), y+int(random(size*-1, size)), 5, 237, 237, 40, 4);
      //        Collectables.add(Coin);
      //      }
    }  
  }
  public void Movement() {
    //Moves this enemy towards the player
    x += (Player1.xvel * -1);
    y += (Player1.yvel * -1);
  }
  public void drawMe() {
       imageMode(CENTER);
       image(img, x, y, size*2, size*2);
    drawHealth();
  }
  public void drawHealth() {
    //Draws this units helath bar
    stroke(255, 0, 0);
    line(x-size, y-size, x+size, y-size); 
    stroke(0, 255, 0);
    line(x-size, y-size, (health*((x+size)-(x-size)))/maxHealth + (x-size), y-size);  
    stroke(0, 0, 0);
  }
  public void checkCollisions() {
    for (int i = 0; i < Enemies.size (); i++) {
     Unit attacker = Enemies.get(i);
      boolean flag = inRange(attacker.x, attacker.y, x, y, size/2, size/2);      
      if (flag & gameTime - 500 >= attacker.hurtDelay) {
        //sFX7.play(7);
        float damageDealt = attacker.damage - ((armor/(armor + 100)) * attacker.damage);
        health -= damageDealt;
        damageTaken = damageDealt;
        damaged = true;
        showDamageTimer = gameTime;
        attacker.hurtDelay = gameTime;
      }
    }
    for (int i = 0; i < Pellets.size (); i++) {
      Pellet attacker = Pellets.get(i);
      boolean flag = inRange(attacker.cx, attacker.cy, x, y, size/2, size/2); 
      if(type == 1){ 
      if (flag & attacker.target == 2)
      {
        sFX7.play(7);
        float damageDealt = attacker.damage - ((armor/(armor + 100)) * attacker.damage);
        health -= damageDealt;
        damageTaken = damageDealt;
        damaged = true;
        showDamageTimer = gameTime;
        Pellets.remove(attacker);
      }
      }
      if(type == 2){ 
      if (flag & attacker.target == 1)
      {
        sFX7.play(7);
        float damageDealt = attacker.damage - ((armor/(armor + 100)) * attacker.damage);
        health -= damageDealt;
        damageTaken = damageDealt;
        damaged = true;
        showDamageTimer = gameTime;
        Pellets.remove(attacker);
      }
      }
    }
  }
  public void displayDamage() {
    String displaydamage = "" + Math.round(damageTaken);
    printText(x, y, 24, displaydamage, 255, 0, 0);
  }
}
class Transmission {
  PImage img;
  String text;
  int duration;
  Transmission(PImage img1, String text1, int duration1) {
    img = img1;
    text = text1;
    duration = gameTime + duration1;
    currentMessage = this;
  }

}
class Unit {     
  int mode; 
  int type;
  int level;
  PImage img;
  int hurtDelay = gameTime; 
  int shootDelay = gameTime; 
  float BaseSpeed, BaseMaxHealth, Basearmor, Basedamage,BasepD,BasefireRate;
  float x, y, speed, health, maxHealth, armor, size, damage,pD, pSi, pSP, fireRate;
  int worth;
  boolean canShoot = false; 
  float a;
  float damageTaken;
  float showDamageTimer;
  boolean damaged;
  boolean agro;
  float agroRange;
  boolean homing;
  boolean Aoe;
  float aoeD, aoeR;
  Unit(int mode1, int level1, int type1, int worth1, float x1, float y1, float speed1, float health1, int armor1, float size1, float damage1) {
    mode = mode1;
    level = level1;
    x = x1; 
    y = y1;
    BaseSpeed = speed1;
    health = health1;
    Basearmor = armor1;
    BaseMaxHealth = health1;
    size = size1;
    Basedamage = damage1;
    worth = worth1;
    type = type1;
    levelAdjust();
    String texture = chooseTexture(type);
    img = loadImage(texture);
  }
  Unit(int mode1, int level1, int type1, int worth1, float x1, float y1, float speed1, float health1, int armor1, float size1, float damage1, boolean canShoot1,boolean homing1,  float agroRange1, float pD1, float pSi1, float pSP1, float fireRate1, boolean Aoe1, float aoeD1, float aoeR1) {
    homing = homing1;
    mode = mode1;
    level = level1;
    worth = worth1;
    x = x1;
    y = y1;
    BaseSpeed = speed1; 
    speed = speed1; 
    health =  health1;
    BaseMaxHealth = health1;
    maxHealth = health1;
    Basearmor = armor1;
    armor = armor1;
    size = size1;
    Basedamage = damage1;
    damage = damage1;
    canShoot = canShoot1;
    BasepD = pD1;
    pD = pD1;
    pSi = pSi1;
    pSP = pSP1;
    BasefireRate = fireRate1;
    fireRate = fireRate1;
    type = type1;
    agroRange = agroRange1;
    Aoe = Aoe1;
    aoeD = aoeD1;
    aoeR = aoeR1;
    levelAdjust();
    String texture = chooseTexture(type);
    img = loadImage(texture);
  }
  public void drawMe() {
    //    Draws this instance of an enemy
    pushMatrix();
    translate(x, y);
    //a = chooseRotation();
    //rotate(a);
    imageMode(CENTER);
    //boolean flag1 = x > 0 && x < width; 
    //boolean flag2 = y > 0 && y < height;
    //if(flag1 && flag2){
    image(img, 0, 0, size*2, size*2);
    //}
    popMatrix();
    drawHealth();
    drawLevel();
  }
   public float chooseRotation(){
  float a = 0; 
  if(mode == 1){
  if(isAgroBase()){
     a = atan2(base.y-y,base.x-x);
   }
   if(isAgroPlayer()){
     a = atan2(Player1.y-y, Player1.x-x);
   }
  }
   if(mode == 2){ 
   if(isAgroEnemy()){
     for (int i = 0; i<Enemies.size(); i++) {
     a = atan2(Enemies.get(i).y-y,Enemies.get(i).x-x);
     }
   }
   }
   return a;
 }
  public String chooseTexture(int x1) {
    String y = "";  
    if (x1 == -1) {
      y = "AllyTurret.png";
    }
    if (x1 == -2) {
      y = "EnemeyDefender.png";
    }
    if (x1 == 1) {
      y = "EnemeyShip.png";
    }
    if (x1 == 2) {
      y = "EnemeyShip2.png";
    }
    if (x1 == 3) {
      y = "EnemeyShip3.png";
    }
    if (x1 == 4) {
      y = "EnemeyShip4.png";
    }
    return y;
  }
  public void levelAdjust() {
    for (int i = 0; i<=level; i++) {
      armor += Basearmor * .2f;
      maxHealth += BaseMaxHealth * .2f;
      pD += BasepD * .25f; 
      damage += Basedamage * .5f;
      health = maxHealth;
    }
  }
  public void drawLevel() {
    //Draws the players level
    stroke(255, 0, 0);
    printText(x-size * 2, y-size/2, 12, str(level), 0, 0, 255);
  }
  public void run() {
    //Allows other methods inside the enemy class to run aslong as the game isn't paused
    drawMe();
    if (!Paused) {
      Movement();
      Die();
      Collide();
      checkCollisions();
      if (damaged & gameTime - showDamageTimer <= 200) {
        displayDamage();
      }
      chooseTarget();
    }
  }
  public void displayDamage() {
    String displaydamage = "" + Math.round(damageTaken);
    printText(x, y, 24, displaydamage, 255, 0, 0);
  } 
  public void chooseTarget() {
    if (mode == 1) {
      boolean canShootPlayer = canShoot & isAgroPlayer();
      boolean canShootBase = canShoot & isAgroBase();
      if (canShootBase & !canShootPlayer) {
        Shoot(2, base.x, base.y);
      } else { 
        if (canShootPlayer) {
          Shoot(2, Player1.x, Player1.y);
        }
      }
    }
    if (mode == 2) {
      boolean canShootEnemy = canShoot;
      if (canShootEnemy) {
        for (int i = 0; i<Enemies.size(); i++) {
          float distance = sqrt(sq(Enemies.get(i).x - x) + sq(Enemies.get(i).y - y));
          if (distance <= agroRange) {
            Shoot(1, Enemies.get(i).x, Enemies.get(i).y);
          }
        }
      }
    }
  }
  public void checkCollisions() {
    for (int i = 0; i < Pellets.size (); i++) {
      Pellet attacker = Pellets.get(i);
      boolean flag = inRange(attacker.cx, attacker.cy, x, y, size/2, size/2);
      if (mode == 1) {
        if (flag & attacker.target == 1)
        {
          sFX7.play(7);
          float damageDealt = attacker.damage - ((armor/(armor + 100)) * attacker.damage);
          health -= damageDealt;
          damageTaken = damageDealt;
          damaged = true;
          showDamageTimer = gameTime;
          Pellets.remove(attacker);
        }
      }
      if (mode == 2) {
        if (flag & attacker.target == 2)
        {
          sFX7.play(7);
          float damageDealt = attacker.damage - ((armor/(armor + 100)) * attacker.damage);
          health -= damageDealt;
          damageTaken = damageDealt;
          damaged = true;
          showDamageTimer = gameTime;
          Pellets.remove(attacker);
        }
      }
    }
  }
  public void Collide() {
    //Detects for colisions with obstacles
    for (int i = 0; i<Obstacles.size (); i++) {
      if (inRange(Obstacles.get(i).x, Obstacles.get(i).y, x, y, Obstacles.get(i).size/2, Obstacles.get(i).size/2)) {
        float damageDealt = Obstacles.get(i).health/8 - ((armor/(armor + 100)) * Obstacles.get(i).health/8);
        health -= damageDealt;
        Obstacles.remove(i);
      }
    }
    for (int i = 0; i<Structures.size (); i++) {
      if (inRange(Structures.get(i).x, Structures.get(i).y, x, y, Structures.get(i).size/2, Structures.get(i).size/2)) {
        Enemies.remove(this);
        Structures.get(i).health -= level + 5;
      }
    }
  }
  public boolean isAgroPlayer() {
    boolean val;
    float distance = sqrt(sq(Player1.x - x) + sq(Player1.y - y));
    if (distance < agroRange) {
      val = true;
    } else {
      val = false;
    }
    return val;
  }
  public boolean isAgroEnemy() {
    boolean val = false;
    for (int i = 0; i<Enemies.size(); i++) {
      float distance = sqrt(sq(Enemies.get(i).x - x) + sq(Enemies.get(i).y - y));
      if (distance < agroRange) {
        val = true;
      } else {
        val = false;
      }
    }
    return val;
  }
  public boolean isAgroBase() {
    boolean val;
    float distance = sqrt(sq(base.x - x) + sq(base.y - y));
    if (distance < agroRange*2) {
      val = true;
    } else {
      val = false;
    }
    return val;
  }
  public void Shoot(int type, float targetx, float targety) {
    //Causes this instance of an enemy to shoot pellets towards the player
    if (gameTime - fireRate >= shootDelay) {  
      sFX3.play(7);
      if(homing & isAgroPlayer()){
      Pellet Hbullet = new Pellet(pD, pSi, pSP, type, x, y, Aoe, aoeD, aoeR,null,Player1);
      Pellets.add(Hbullet);
      }
      if(!homing || !isAgroPlayer()){
      Pellet Ebullet = new Pellet(pD, pSi, pSP, type, x, y, targetx, targety, Aoe, aoeD, aoeR); 
      Pellets.add(Ebullet);
      }
      shootDelay = gameTime;
    }
  }
  public void drawHealth() {
    //Draws this enemies helath bar
    stroke(255, 0, 0);
    line(x-size, y-size, x+size, y-size); 
    stroke(0, 255, 0);
    line(x-size, y-size, (health*((x+size)-(x-size)))/maxHealth + (x-size), y-size);  
    stroke(0, 0, 0);
  }
  public void Movement() {
    //Moves this enemy towards the player
    if (mode == 1) {
      for (int i = 0; i<Players.size (); i++) {
        boolean flag = inRange(x, y, Players.get(i).x, Players.get(i).y, 200, 200);      
        if (flag) {
          float distance = sqrt(sq(Players.get(i).x - x) + sq(Players.get(i).y - y));
          x += (Player1.xvel * -1) + (Players.get(i).x - x)/(distance*speed);
          y += (Player1.yvel * -1) + (Players.get(i).y - y)/(distance*speed);
        } else {
            float distance = sqrt(sq(base.x - x) + sq(base.y - y));
            x += (Player1.xvel * -1) + (base.x - x)/(distance*speed);
            y += (Player1.yvel * -1) + (base.y - y)/(distance*speed);
            //(player2.xvel + player1.xvel) / 2
        }
      }
    }
    if (mode == 2) {
      x += (Player1.xvel * -1);
      y += (Player1.yvel * -1);
    }
  }
  public void Die() {
    //Kills this instance of an enemy if they no longer have any health
    if (health <= 0) {
      sFX2.play(7);
      Player1.exp += 3 * level;
      Enemies.remove(this);
      Allies.remove(this);
      Units.remove(this);
      //      for (int i = 0; i<=random (1, 5); i++) {
      //        Collectable Coin = new Collectable(1, int(random(1, 3)), x+int(random(size*-1, size)), y+int(random(size*-1, size)), 5, 237, 237, 40, 4);
      //        Collectables.add(Coin);
      //      }
    }
  }
}
  public void settings() {  size(1360, 720); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "--present", "--window-color=#666666", "--hide-stop", "WIP" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
