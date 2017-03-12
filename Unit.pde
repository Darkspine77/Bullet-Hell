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
  void drawMe() {
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
   float chooseRotation(){
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
  String chooseTexture(int x1) {
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
  void levelAdjust() {
    for (int i = 0; i<=level; i++) {
      armor += Basearmor * .2;
      maxHealth += BaseMaxHealth * .2;
      pD += BasepD * .25; 
      damage += Basedamage * .5;
      health = maxHealth;
    }
  }
  void drawLevel() {
    //Draws the players level
    stroke(255, 0, 0);
    printText(x-size * 2, y-size/2, 12, str(level), 0, 0, 255);
  }
  void run() {
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
  void displayDamage() {
    String displaydamage = "" + Math.round(damageTaken);
    printText(x, y, 24, displaydamage, 255, 0, 0);
  } 
  void chooseTarget() {
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
  void checkCollisions() {
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
  void Collide() {
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
  boolean isAgroPlayer() {
    boolean val;
    float distance = sqrt(sq(Player1.x - x) + sq(Player1.y - y));
    if (distance < agroRange) {
      val = true;
    } else {
      val = false;
    }
    return val;
  }
  boolean isAgroEnemy() {
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
  boolean isAgroBase() {
    boolean val;
    float distance = sqrt(sq(base.x - x) + sq(base.y - y));
    if (distance < agroRange*2) {
      val = true;
    } else {
      val = false;
    }
    return val;
  }
  void Shoot(int type, float targetx, float targety) {
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
  void drawHealth() {
    //Draws this enemies helath bar
    stroke(255, 0, 0);
    line(x-size, y-size, x+size, y-size); 
    stroke(0, 255, 0);
    line(x-size, y-size, (health*((x+size)-(x-size)))/maxHealth + (x-size), y-size);  
    stroke(0, 0, 0);
  }
  void Movement() {
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
  void Die() {
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