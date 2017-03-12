String selectedShip;
int selectedShipId;
int discoveredShips;
float [] selectedShipStats = new float[8];
void drawShipSelect(){  
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
void loadAvalibleShipData(){
selectedShipStats = ShipProfiles.get(selectedShip); 
}
void displayShipStats(){
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