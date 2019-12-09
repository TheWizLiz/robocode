package edu.ufl.cise.cs1.robots;

import robocode.*;

import java.awt.*;

public class Uniqua extends TeamRobot{
    public void setColors() {
        setGunColor(Color.pink);
        setBodyColor(Color.pink);
        setRadarColor(Color.cyan);          //Team Color
        setBulletColor(Color.cyan);         //Team Color
        setScanColor(Color.pink);
    }

    public void run() {
        while (true) {
            turnRight(90);
            turnGunRight(360);
            ahead(100);
            turnRight(90);
            ahead(100);
            turnGunRight(360);

        }
    }

    public void onBulletMissed(ScannedRobotEvent e) {
        if(e.getDistance() < 40) {
            turnRight(e.getBearing());
            ahead(20);
            fire(2);
        }
    }

    public void onScannedRobot(ScannedRobotEvent e) {
        if(isTeammate(e.getName())) {
            scan();
        }
        else {
            setAhead(100);
            fire(1);
        }
    }

    public void onHitRobot(HitRobotEvent event) {
        if(isTeammate(event.getName())) {
            back(20);
        }
        else {
            fireBullet(3);
        }
    }

    public void onHitByBullet(HitByBulletEvent e) {
        back(25);
    }

    public void onHitWall(HitWallEvent e)  {
        back(20);
    }
}