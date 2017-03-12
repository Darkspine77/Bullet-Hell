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
  void run() {
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
  String chooseTexture(){
    String returnImg = null;
    if(type == 2){
      returnImg = "EnemeyBase.png";
    }
    return returnImg;
  }
  void Die(){
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
  void Movement() {
    //Moves this enemy towards the player
    x += (Player1.xvel * -1);
    y += (Player1.yvel * -1);
  }
  void drawMe() {
       imageMode(CENTER);
       image(img, x, y, size*2, size*2);
    drawHealth();
  }
  void drawHealth() {
    //Draws this units helath bar
    stroke(255, 0, 0);
    line(x-size, y-size, x+size, y-size); 
    stroke(0, 255, 0);
    line(x-size, y-size, (health*((x+size)-(x-size)))/maxHealth + (x-size), y-size);  
    stroke(0, 0, 0);
  }
  void checkCollisions() {
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
  void displayDamage() {
    String displaydamage = "" + Math.round(damageTaken);
    printText(x, y, 24, displaydamage, 255, 0, 0);
  }
}