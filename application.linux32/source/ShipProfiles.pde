//Health
//Damage
//Speed
//Armor
//Size
//Bullet Speed
//Rate of fire
//Bullet Size
void loadShipProfiles(){
float [] aegisStats = new float[8];
aegisStats[0] = 300; 
aegisStats[1] = 25; 
aegisStats[2] = 3; 
aegisStats[3] = 20; 
aegisStats[4] = 12; 
aegisStats[5] = 6; 
aegisStats[6] = 300; 
aegisStats[7] = 4; 
ShipProfiles.put("Aegis",aegisStats);
float [] aresStats = new float[8];
aresStats[0] = 150; 
aresStats[1] = 31; 
aresStats[2] = 3.5; 
aresStats[3] = 15; 
aresStats[4] = 10; 
aresStats[5] = 4; 
aresStats[6] = 175; 
aresStats[7] = 3.75; 
ShipProfiles.put("Ares",aresStats);
float [] isisStats = new float[8];
isisStats[0] = 400; 
isisStats[1] = 40; 
isisStats[2] = 1; 
isisStats[3] = 40; 
isisStats[4] = 15; 
isisStats[5] = 7; 
isisStats[6] = 500; 
isisStats[7] = 7; 
ShipProfiles.put("Isis",isisStats);
float [] hermesStats = new float[8];
hermesStats[0] = 175; 
hermesStats[1] = 25; 
hermesStats[2] = 7; 
hermesStats[3] = 50; 
hermesStats[4] = 7; 
hermesStats[5] = 9; 
hermesStats[6] = 225; 
hermesStats[7] = 10; 
ShipProfiles.put("Hermes",hermesStats);
avaliableShips.add("Aegis");
avaliableShips.add("Ares");
avaliableShips.add("Isis");
avaliableShips.add("Hermes");
}