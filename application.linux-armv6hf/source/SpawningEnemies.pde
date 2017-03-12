int enemyRangeMin = 1;
void spawnEnemy(float x, float y, int x1) {
  if(enemyLevelrange - 5 <= 1){
   enemyRangeMin = 1; 
  } else {
    enemyRangeMin = enemyLevelrange - 5;
  }
  //int level1,int type1, int worth1, float x1, float y1, float speed1, float health1, int armor1, float size1, float damage1
  //int level1,int type1, int worth1, float x1, float y1, float speed1, float health1, int armor1, float size1, float damage1, boolean canShoot1,float agroRange1, float pD1, float pSi1, float pSP1, float fireRate1,boolean Aoe1,float aoeD1, float aoeR1
  //Spawns an enemyy 
  if (x1 == 1) {
    //
    Unit Enemy1 = new Unit(1, int(random(enemyRangeMin, enemyLevelrange)), x1, 16, x, y, 1, 100, 30, 10, 40);  
    Enemies.add(Enemy1);
    Units.add(Enemy1);
  }
  if (x1 == 2) {
    //Shooter
    Unit Enemy1 = new Unit(1, int(random(enemyRangeMin, enemyLevelrange)), x1, 43, x, y, 2, 100, 10, 15, 0, true, false, 200, 20, 5, 2, 1000, false, 0, 0);  
    Enemies.add(Enemy1);
    Units.add(Enemy1);
  }
  if (x1 == 3) {
    //Fast Shooter
    Unit Enemy1 = new Unit(1, int(random(enemyRangeMin, enemyLevelrange)), x1, 43, x, y, 2, 100, 10, 20, 0, true, false, 200, 3, 5, 3, 150, false, 0, 0);  
    Enemies.add(Enemy1);
    Units.add(Enemy1);
  }
  if (x1 == 4) {
    //Homing Shooter
    Unit Enemy1 = new Unit(1, int(random(enemyRangeMin, enemyLevelrange)), x1, 43, x, y, 2, 50, 10, 20, 0, true, true, 200, 3, 5, 3, 300, false, 0, 0);  
    Enemies.add(Enemy1);
    Units.add(Enemy1);
  }
}