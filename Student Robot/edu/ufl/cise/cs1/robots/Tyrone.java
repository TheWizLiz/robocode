package edu.ufl.cise.cs1.robots;

import robocode.*;
import robocode.util.Utils;


import java.awt.*;

    public class Tyrone extends TeamRobot implements Droid {
     public void run() {
        setBodyColor(Color.cyan);
        setGunColor(Color.cyan);
        setRadarColor(Color.cyan);
        setBulletColor(Color.cyan);
        setScanColor(Color.cyan);

        setAdjustGunForRobotTurn(true);
        setAdjustRadarForGunTurn(true);

        setTurnRadarRight(Double.POSITIVE_INFINITY);

        while (true) {
            ahead(100);
            turnGunRight(360);
            back(100);
            turnGunRight(360);
        }
    }
    public void onScannedRobot(ScannedRobotEvent e) {
         if (isTeammate(e.getName())) {
             scan();

         }
        fire(3);
         setMaxVelocity(8);
        setTurnRadarRight(2.0 * Utils.normalRelativeAngleDegrees(getHeading() + e.getBearing() - getRadarHeading()));
    }
}


