package edu.ufl.cise.cs1.robots;

import robocode.*;
import java.awt.*;

public class Pablo extends TeamRobot implements Droid {
    public void run() {
        setBodyColor(Color.PINK);
        setGunColor(Color.PINK);
        setRadarColor(Color.cyan);
        setBulletColor(Color.cyan);
        setScanColor(Color.PINK);
        while (true) {

        }
    }

    public void onScannedRobot(ScannedRobotEvent e) {
       if (isTeammate(e.getName())) {
           scan();
       } else {
                
       }
    }

    public void onHitByBullet(HitByBulletEvent e) {
        back(10);
    }

    public void onHitWall(HitWallEvent e) {
        back(20);
    }
}
