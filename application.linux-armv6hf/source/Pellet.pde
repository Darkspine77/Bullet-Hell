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
  void drawMe() {
    //Draws this instance of a Pellet
    imageMode(CENTER);
    //boolean flag1 = cx > 0 && cx < width; 
    //boolean flag2 = cy > 0 && cy < height;
    //if(flag1 && flag2){
    image(img, cx, cy, size*2, size*2);
    //}
  }

  void run() {
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
  void loseTarget() {
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
  void Collide() {
    //Detects for collision between obstacles
    for (int i = 0; i<Obstacles.size (); i++) {
      if (inRange(Obstacles.get(i).x, Obstacles.get(i).y, cx, cy, Obstacles.get(i).size/2, Obstacles.get(i).size/2)) {
        Pellets.remove(this);
        sFX7.play(7);
        Obstacles.get(i).health -= damage;
      }
    }
  }
  void expire(){
   if(time + 5000 <= gameTime){
    Pellets.remove(this);
   }
  }
  String chooseTexture(int x1) {
    String y = "";  
    if (x1 == 1) {
      y = "Bullet2.png";
    }
    if (x1 == 2) {
      y = "Bullet.png";
    }
    return y;
  }

  void Movement() {
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