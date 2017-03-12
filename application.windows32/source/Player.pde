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
  void drawMe() {
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
  void run() {
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
  void loadSelectedStats(){
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
  void loadAbilities(){
  selectedAbilities = savedAbilities;
  selectedAbility = selectedAbilities[0];
  }
  void gainLevel() {
    if (exp >= expMax) {
      exp = expMax - exp;
      expMax += expMax * .5;
      level += 1;
      armor +=  stats[3] * .1;
      maxHealth += stats[0] * .1;
      bDmg += stats[1] * .37;
      skillpoints += 1;
      BaseArmor +=  stats[3] * .1;
      BaseMaxHealth += stats[0] * .1;
      BaseBDmg += stats[1] * .37;
    }
  }
  void Spawn(){
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
  void Die() {
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
  void displayDamage() {
    String displaydamage = "" + Math.round(damageTaken);
    printText(x, y, 24, displaydamage, 255, 0, 0);
  } 
  void checkCollisions() {
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
  void Collide() {
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
          rotation = 1.5;
        }
        if (xdir > 0) {
          rotation = .5;
        }
      }
    }
  }
  void drawHealth() {
    //Draws the players health bar
    strokeWeight(4);
    stroke(255, 0, 0);
    line(x-size, y-size, x+size, y-size); 
    stroke(0, 255, 0);
    line(x-size, y-size, (health*((x+size)-(x-size)))/maxHealth + (x-size), y-size);  
    stroke(0, 0, 0);
    strokeWeight(1);
  }
  void drawLevel() {
    //Draws the players level
    stroke(255, 0, 0);
    printText(x-size * 2, y-size/2, 12, str(level), 0, 0, 255);
  }
  void hitWalls() {
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
  void Movement() {
    //Allows user to controll the player using W,A,S,or D
    if (gameScreen == 1) {
      if (keyPressed) {
        if (key == 'a') { 
          xdir = -1;  
          ydir = 0;
          rotation = 1.5;
        }
        if (key =='d') {
          xdir = 1; 
          ydir = 0;
          rotation = .5;
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
  void Shoot() {
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
  void selectAbilities() {
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
  void activateAbility() {
    if (mousePressed  && (mouseButton == RIGHT)) { 
      useAbility(selectedAbility);
    }
  }
}