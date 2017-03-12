class StatusEffect { 
  float statusType;
  float statusLength, statusAmount;
  int targetType;
  int timeInterval = gameTime;
  Unit unitTarget;
  Player playerTarget;
  StatusEffect(int targetType1, Unit unitTarget1, Player playerTarget1, float statusLength1, float statusAmount1, float statusType1) {
    statusLength = gameTime + statusLength1;
    statusAmount = statusAmount1;
    targetType = targetType1;
    unitTarget = unitTarget1;
    playerTarget = playerTarget1;
    statusType = statusType1;
  }
  //1 = Health,2 = Armor,3 = Speed,4 = attackSpeed,5 = damage
  void run() {
    println(timeInterval,gameTime);
    if (gameTime <= statusLength) { 
      if (targetType == 1) { 
        if (statusType == 1) {  
          if(timeInterval + 1000<= gameTime){ 
          unitTarget.health += statusAmount;
          timeInterval = gameTime;
        }
        }
        if (statusType == 2) {
          unitTarget.armor = statusAmount;
        }
        if (statusType == 3) {
          unitTarget.speed = statusAmount;
        }
        if (statusType == 4) {
          unitTarget.fireRate = statusAmount;
        }
        if (statusType == 5) {
          unitTarget.pD = statusAmount;
        }
        if (statusType == 6) {
          unitTarget.damage = statusAmount;
        }
      }
      if (targetType == 2) {
        if (statusType == 1) {
          if(timeInterval + 1000 <= gameTime){ 
          if(playerTarget.health >= playerTarget.maxHealth){
          playerTarget.health = playerTarget.maxHealth;
          }else{
          playerTarget.health += statusAmount;
          }
          timeInterval = gameTime;
          }
        }
        if (statusType == 2) {
          playerTarget.armor = statusAmount;
        }
        if (statusType == 3) {
          playerTarget.speed = statusAmount;
        }
        if (statusType == 4) {
          playerTarget.bDelay = statusAmount;
        }
        if (statusType == 5) {
          playerTarget.bDmg = statusAmount;
        }
      }
    } else {
      if (targetType == 1) {
        if (statusType == 2) {
          unitTarget.armor = unitTarget.Basearmor;
        }
        if (statusType == 3) {
          unitTarget.speed = unitTarget.BaseSpeed;
        }
        if (statusType == 4) {
          unitTarget.fireRate = unitTarget.BasefireRate;
        }
        if (statusType == 5) {
          unitTarget.pD = unitTarget.BasepD;
        }
        if (statusType == 6) {
          unitTarget.damage = unitTarget.Basedamage;
        }
      }
      if (targetType == 2) {
        if (statusType == 2) {
          playerTarget.armor = playerTarget.BaseArmor;
        }
        if (statusType == 3) {
          playerTarget.speed = playerTarget.BaseSpeed;
        }
        if (statusType == 4) {
          playerTarget.bDelay = playerTarget.BaseBDelay;
        }
        if (statusType == 5) {
          playerTarget.bDmg = playerTarget.BaseBDmg;
        }
      }
          StatusEffects.remove(this);
    }
  }
}