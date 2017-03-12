class abilityIcon { 
  float x, y;
  int ability;
  abilityIcon(float x1, float y1, float size1, int ability1, char keyConfig1) {
    x = x1;
    y = y1;
    ability = ability1;
  }
  void run() {
    drawMe();
  }
  void drawMe() {
    fill(255, 255, 0);
    rectMode(CENTER);
    rect(x, y, 75, 75);
  } 
}  