package edu.ufl.cise.cs1.robots;

import robocode.*;
import java.awt.*;

public class Pablo extends TeamRobot {
    public void run() {
        setBodyColor(Color.PINK);
        setGunColor(Color.PINK);
        setRadarColor(Color.cyan);
        setBulletColor(Color.cyan);
        setScanColor(Color.PINK);
        while (true) {
            ahead(100);
            turnGunRight(360);
            back(100);
            turnGunRight(360);
        }
    }

    public void onScannedRobot(ScannedRobotEvent e) {
        fire(1);
    }

    public void onHitByBullet(HitByBulletEvent e) {
        back(10);
    }

    public void onHitWall(HitWallEvent e) {
        back(20);
    }
}
