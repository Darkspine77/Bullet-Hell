int [] delays = new int[8];
void startAbilties(){
delays[0] = gameTime + 50;
delays[1] = gameTime + 5000;
delays[2] = gameTime + 10000;
delays[3] = gameTime + 10000;
delays[4] = gameTime + 10000;
delays[5] = gameTime + 1000;
delays[6] = gameTime + 20000; 
delays[7] = gameTime + 15000; 
}
void useAbility(int id) { 
  if (id == 1) {   
    //println("Made it");
    if (gameTime - 50 >= delays[0]) {  
       for(int i = 0;i<Enemies.size();i++){
         if(inRange(mouseX,mouseY,Enemies.get(i).x,Enemies.get(i).y,Enemies.get(i).size*4,Enemies.get(i).size*4)) {
                sFX6.play(7);
      Pellet A1bullet = new Pellet(Player1.bDmg * .05, Player1.bSize* .8, Player1.bSpd * .25, 1,Player1.x, Player1.y, false, 0, 0,Enemies.get(i),null);
      Pellets.add(A1bullet);//Pellet(float damage1, float size1, float speed1, int target1, float sx1, float sy1, boolean Aoe1, float aoeD1, float aoeR1)
      delays[0] = gameTime;
         }
       }
    }
  }
   if (id == 2) {   
    if (gameTime - 5000 >= delays[1]) {  
       sFX6.play(7);
      Pellet A2bullet = new Pellet(Player1.bDmg*2.5, Player1.bSize*3, Player1.bSpd * .75, 1, Player1.x, Player1.y, mouseX, mouseY, false, 0, 0);
      Pellets.add(A2bullet);
      delays[1] = gameTime;
    }
  }
  if (id == 3) {    
    if (gameTime - 10000 >= delays[2]) {  
      sFX12.play(7);
      int x;
      if(Player1.level < 5){
      x = 1;   
      } else {
      x = Player1.level - 4; 
      }
      
      Unit AllyTurret = new Unit(2,x,-1, 43, Player1.x, Player1.y, 10000000, 25, 10, 10, 0, true,false, 200, Player1.bDmg/6, 5, 4, 300, false, 0, 0); 
      Allies.add(AllyTurret);
      Units.add(AllyTurret);
      delays[2] = gameTime;
    }
  }
  if (id == 4) { 
    if (gameTime - 10000 >= delays[3]) { 
        sFX8.play(7);
      StatusEffect Test1 = new StatusEffect(2,null,Player1,5000,Player1.armor*.10,2);
      StatusEffects.add(Test1);
      StatusEffect Test2 = new StatusEffect(2,null,Player1,5000,Player1.bDelay*.5,4);
      StatusEffects.add(Test2);
      delays[3] = gameTime;
    }
  }
      if (id == 5) {
    if (gameTime - 10000 >= delays[4]) { 
        sFX8.play(7);
      StatusEffect Test1 = new StatusEffect(2,null,Player1,5000,Player1.BaseSpeed * 3,3);
      StatusEffects.add(Test1);
      StatusEffect Test2 = new StatusEffect(2,null,Player1,5000,5000,4);
      StatusEffects.add(Test2);
      delays[4] = gameTime;
    }
  }
  if (id == 6) {    
    //println("Made it");
    if (gameTime - 1000 >= delays[5]) {  
       for(int i = 0;i<Enemies.size();i++){
         if(inRange(mouseX,mouseY,Enemies.get(i).x,Enemies.get(i).y,Enemies.get(i).size*4,Enemies.get(i).size*4)) {
                sFX6.play(7);
      Pellet A1bullet = new Pellet(Player1.bDmg * .66, Player1.bSize, Player1.bSpd * .25, 1,Player1.x + 50, Player1.y, false, 0, 0,Enemies.get(i),null);
      Pellets.add(A1bullet);
      Pellet A2bullet = new Pellet(Player1.bDmg * .66, Player1.bSize, Player1.bSpd * .25, 1,Player1.x - 50, Player1.y, false, 0, 0,Enemies.get(i),null);
      Pellets.add(A2bullet);
      Pellet A3bullet = new Pellet(Player1.bDmg * .66, Player1.bSize, Player1.bSpd * .25, 1,Player1.x, Player1.y - 50, false, 0, 0,Enemies.get(i),null);
      Pellets.add(A3bullet);//Pellet(float damage1, float size1, float speed1, int target1, float sx1, float sy1, boolean Aoe1, float aoeD1, float aoeR1)
      delays[5] = gameTime;
         }
       }
    }
  }
   if (id == 7) { 
    if (gameTime - 20000 >= delays[6]) { 
        sFX8.play(7);
      StatusEffect Test1 = new StatusEffect(2,null,Player1,10000,5,1);
      StatusEffects.add(Test1);
      delays[6] = gameTime;
    }
  }
  if (id == 8) {
    if (gameTime - 15000 >= delays[7]) { 
        sFX8.play(7);
      StatusEffect Test1 = new StatusEffect(2,null,Player1,5000,Player1.BaseSpeed * .5,3);
      StatusEffects.add(Test1);
      StatusEffect Test2 = new StatusEffect(2,null,Player1,5000,Player1.BaseBDelay * .25,4);
      StatusEffects.add(Test2);
      StatusEffect Test3 = new StatusEffect(2,null,Player1,5000,Player1.BaseArmor * 4,2);
      StatusEffects.add(Test3);
      StatusEffect Test4 = new StatusEffect(2,null,Player1,5000,Player1.BaseBDmg * .5,5);
      StatusEffects.add(Test4);
      delays[7] = gameTime;
    }
  }
}