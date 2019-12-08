package edu.ufl.cise.cs1.robots;

import robocode.*;
import robocode.util.Utils;

<<<<<<< HEAD
import java.awt.*;

=======
>>>>>>> fa0e7f89e7d384712e52be76558a735a32d32b4f
public class Tyrone extends TeamRobot
{
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
        fire(3);
        setTurnRadarRight(2.0 * Utils.normalRelativeAngleDegrees(getHeading() + e.getBearing() - getRadarHeading()));
    }
}


