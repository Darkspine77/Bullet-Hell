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

  String chooseTexture(int x1) {
    String y = "";  
    if (x1 == 2) {
      y = "Health.png";
    }
    if (x1 == 3) {
      y = "Armor.png";
    }
    return y;
  }

  void drawMe() {
    //Draws this instance of an obstacle
    imageMode(CENTER);
    image(img, x, y, size*2, size*2);
  }
  void run() {
    //Runs all methods of this instance of the obstacle
    drawMe();
    if (!Paused) {
      CollectPlayer();
      Movement();
    }
  }
  void Movement() {
    //Moves this enemy towards the player
    float distance = sqrt(sq(Player1.x - x) + sq(Player1.y - y));
    x += (Player1.xvel * -1) +(Player1.x - x)/(distance*speed);
    y += (Player1.yvel * -1) +(Player1.y - y)/(distance*speed);
  }
  void CollectPlayer() {
    if (inRange(x, y, Player1.x, Player1.y, Player1.size, Player1.size)) {
      giveBuff(type, amount);
      Collectables.remove(this);
    }
  }
  void giveBuff(int x, float y) {
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