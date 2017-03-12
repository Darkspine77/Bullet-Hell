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