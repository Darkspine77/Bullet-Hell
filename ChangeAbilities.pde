int changingAbility;
int showingAbilitySelections;
Ability choosenAbility;
void drawAbilityChange(){ 
choosenAbility = Abilities.get(showingAbilitySelections);
Button Select = new Button(width/2, height/2, 30, 10, "Select", 24, 0, 255, 0);
Select.drawMe();
if(showingAbilitySelections >= 2){
printText(width - width/4,height/2 - 300,48,Abilities.get(showingAbilitySelections-2).name,255,0,0);
printText(width/2, height/2 + 50,12,Abilities.get(showingAbilitySelections).info,255,255,255);
}
if(showingAbilitySelections >= 1){
printText(width - width/4,height/2 - 150,48,Abilities.get(showingAbilitySelections-1).name,255,0,0);
}
rectMode(CENTER);
stroke(0,255,0);
noFill();
rect(width - width/4,height/2 - 25,400,100);
printText(width - width/4,height/2,48,Abilities.get(showingAbilitySelections).name,255,0,0);
if(showingAbilitySelections + 1 < Abilities.size()){
printText(width - width/4,height/2 + 150,48,Abilities.get(showingAbilitySelections+1).name,255,0,0);
}
if(showingAbilitySelections + 2 < Abilities.size()){
printText(width - width/4,height/2 + 300,48,Abilities.get(showingAbilitySelections+2).name,255,0,0);
}
boolean flag1 = showingAbilitySelections - 1 < 0;
boolean flag2 = showingAbilitySelections + 2 > Abilities.size();
if(!flag1){
Up.drawMe();  
}
if(!flag2){
Down.drawMe();  
}
if(Down.isClicked() & !flag2){
showingAbilitySelections += 1; 
}
if(Up.isClicked() & !flag1){
showingAbilitySelections -= 1; 
}
if(Select.isClicked()){
  dispalyedAbilities[changingAbility-1]= choosenAbility.id;
  pullAbilitiesData();
  gameScreen = 3;
}
}