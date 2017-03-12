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
  void drawMe() {
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
  boolean isClicked() {
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