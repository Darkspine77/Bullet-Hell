int[] dispalyedAbilities = new int[4]; 
int[] savedAbilities = new int[4];
Ability[] abilityData = new Ability[4];
void pullAbilitiesData(){
abilityData[0] = Abilities.get(dispalyedAbilities[0]-1);
abilityData[1] = Abilities.get(dispalyedAbilities[1]-1);
abilityData[2] = Abilities.get(dispalyedAbilities[2]-1);
abilityData[3] = Abilities.get(dispalyedAbilities[3]-1);
}
void drawAbilitySelect(){
  println(dispalyedAbilities);
   Button saveChanges = new Button(width/2, 175, 30, 10, "Save Changes", 24, 0, 255, 0);
    saveChanges.drawMe();
    Button changeAbility1 = new Button(width/2 - 450, height - 300, 30, 10, "Change Ability 1", 24, 0, 255, 0);
    changeAbility1.drawMe();
    Button changeAbility2 = new Button(width/2 - 150, height - 300, 30, 10, "Change Ability 2", 24, 0, 255, 0);
    changeAbility2.drawMe();
    Button changeAbility3 = new Button(width/2 + 150, height - 300, 30, 10, "Change Ability 3", 24, 0, 255, 0);
    changeAbility3.drawMe();
    Button changeAbility4 = new Button(width/2 + 450, height - 300, 30, 10, "Change Ability 4", 24, 0, 255, 0);
    changeAbility4.drawMe();
    if(changeAbility1.isClicked()){
    showingAbilitySelections = 2;
    changingAbility = 1;
    gameScreen = 4;
    }
    if(changeAbility2.isClicked()){
    showingAbilitySelections = 2;
    changingAbility = 2;
    gameScreen = 4;
    }
    if(changeAbility3.isClicked()){
    showingAbilitySelections = 2;
    changingAbility = 3;
    gameScreen = 4;
    }
    if(changeAbility4.isClicked()){
    showingAbilitySelections = 2;
    changingAbility = 4;  
    gameScreen = 4;
    }
    displayAbilities();
    if (saveChanges.isClicked()) {
    savedAbilities = dispalyedAbilities;
    gameScreen = 0;
    }
 displayAbilities(); 
}
void displayAbilities(){
printText(width/2 - 450,height/2 - 150,24,"1",255,0,0); 
printText(width/2 - 450,height/2 - 100,24,abilityData[0].name,255,0,0); 
printText(width/2 - 450,height/2,12,abilityData[0].info,255,0,0); 
printText(width/2 - 150,height/2 - 150,24,"2",255,0,0); 
printText(width/2 - 150,height/2 - 100,24,abilityData[1].name,255,0,0); 
printText(width/2 - 150,height/2,12,abilityData[1].info,255,0,0); 
printText(width/2 + 150,height/2 - 150,24,"3",255,0,0); 
printText(width/2 + 150,height/2 - 100,24,abilityData[2].name,255,0,0); 
printText(width/2 + 150,height/2,12,abilityData[2].info,255,0,0); 
printText(width/2 + 450,height/2 - 150,24,"4",255,0,0); 
printText(width/2 + 450,height/2 - 100,24,abilityData[3].name,255,0,0); 
printText(width/2 + 450,height/2,12,abilityData[3].info,255,0,0); 
}