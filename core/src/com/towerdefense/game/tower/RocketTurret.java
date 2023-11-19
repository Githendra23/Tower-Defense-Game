package com.towerdefense.game.tower;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.towerdefense.game.enemy.AEnemy;
import com.towerdefense.game.tower.projectiles.HomingRocket;

import java.util.ArrayList;
import java.util.List;

public class RocketTurret extends ATower {
    private final List<HomingRocket> rocketList;
    private final float ATTACK_INTERVAL = (float) 1;
    private AEnemy enemy = null;
    private float spawnTimer = 0;
    private int targetX;
    private int targetY;

    public RocketTurret(int x, int y) {
        super(200, 300, x, y, "defense/rocket_turret/turret.png");
        this.coolDown = 20;
        rocketList = new ArrayList<>();

        this.price = 50;
    }

    @Override
    public void spawnProjectile(int x, int y) {
        if (rocketList.isEmpty()) {
            spawnTimer += Gdx.graphics.getDeltaTime();

            if (spawnTimer >= ATTACK_INTERVAL) {
                HomingRocket homingRocket = new HomingRocket(this.getAxisX(), this.getAxisY());
                homingRocket.aim(x, y);
                homingRocket.setTower(this);
                homingRocket.setLifetime(210);
                homingRocket.setDmg(20);
                homingRocket.setTargetCoords(x, y);
                rocketList.add(homingRocket);

                spawnTimer = 0;
            }
        }
    }

    public void updateProjectile(AEnemy enemy) {
        if (this.enemy == null || this.enemy.isDead()) {
            if (this.isInRange(enemy)) {
                this.enemy = enemy;
            } else {
                this.enemy = null;

                if (!rocketList.isEmpty())
                    rocketList.remove(0);
            }
        }

        if (!rocketList.isEmpty()) {
            for (HomingRocket rocket : rocketList) {
                rocket.setTargetCoords(this.enemy.getAxisX() + ((float) this.enemy.getImg().getRegionWidth() / 2), this.enemy.getAxisY() + ((float) this.enemy.getImg().getRegionHeight() / 2));
            }
        }
    }

    public void ProjectileHit(AEnemy enemy) {
        /*if (this.enemy == null || this.enemy.isDead()) {
            if (this.isInRange(enemy)) {
                this.enemy = enemy;
            } else {
                this.enemy = null;

                if (!rocketList.isEmpty())
                    rocketList.remove(0);
            }
        }*/

        if (!rocketList.isEmpty()) {
            for (int i = 0; i < rocketList.size(); i++) {
                HomingRocket rocket = rocketList.get(i);

                if (rocket.hitbox.overlaps(this.enemy.hitbox())) {
                    rocket.dispose();
                    rocketList.remove(rocket);
                    this.enemy.loseHp(this.damage);
                }
            }
        }
    }
    public void projectileMove()
    {
        for (HomingRocket rocket : rocketList) {
            rocket.homing(targetX, targetY);
            rocket.aim(targetX, targetY);
        }
    }
    public void projectileAim(AEnemy enemy) {
        /*if (this.enemy == null || this.enemy.isDead()) {
            if (this.isInRange(enemy)) {
                this.enemy = enemy;

                targetX=this.enemy.getAxisX();
                targetY= this.enemy.getAxisY();
            } else {
                this.enemy = null;

                if (!rocketList.isEmpty())
                    rocketList.remove(0);
            }
        } else {
            targetX=this.enemy.getAxisX();
            targetY= this.enemy.getAxisY();
        }*/

        if (this.enemy != null) {
            targetX = this.enemy.getAxisX();
            targetY = this.enemy.getAxisY();
        }

//        if (!rocketList.isEmpty()) {
//            for (HomingRocket rocket : rocketList) {
//                rocket.homing(this.enemy.getAxisX(), this.enemy.getAxisY());
//                rocket.aim(this.enemy.getAxisX(), this.enemy.getAxisY());
//            }
    }


    public void drawProjectile(SpriteBatch batch) {
        /*if (this.enemy == null || this.enemy.isDead()) {
            if (!rocketList.isEmpty())
                rocketList.remove(0);
        }*/

        if (!rocketList.isEmpty()) {
            for (HomingRocket rocket : rocketList) {
                if (this.enemy != null) {
                    batch.draw(rocket.drawShadow(), rocket.getPositionX() + 20, rocket.getPositionY() - 20, 9, 9, 21, 7, 2, 2, rocket.getRotation());
                    batch.draw(rocket.drawRocket(), rocket.getPositionX(), rocket.getPositionY(), 9, 9, 21, 7, 2, 2, rocket.getRotation());
                }
            }
        }
    }

    public List<HomingRocket> getProjectileList() {
        return rocketList;
    }

    @Override
    public void dispose() {
        super.dispose();

        for (HomingRocket rocket : rocketList) {
            rocket.dispose();
        }
    }
}