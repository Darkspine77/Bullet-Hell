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
  void drawMe() {
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
  void run() {
    //Runs all methods of this instance of the obstacle
    drawMe();
  }
  void Movement() {
    x += (Player1.xvel * -1);
    y += (Player1.yvel * -1);
  }
  void drawHealth() {
    //Draws the health bar for this obstacle
    stroke(255, 0, 0);
    line(x-size, y-size, x+size, y-size); 
    stroke(0, 255, 0);
    line(x-size, y-size, (health*((x+size)-(x-size)))/maxHealth + (x-size), y-size);  
    stroke(0, 0, 0);
  }
  void Die() {
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