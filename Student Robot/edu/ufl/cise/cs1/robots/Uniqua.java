package edu.ufl.cise.cs1.robots;

import robocode.*;
import sampleteam.Point;
import sampleteam.RobotColors;

import java.awt.*;

import static robocode.util.Utils.normalRelativeAngleDegrees;

public class Uniqua extends TeamRobot {
    public void setColors() {
        setGunColor(Color.BLACK);
        setBodyColor(Color.GREEN);
        setRadarColor(Color.CYAN);          //Team Color
        setBulletColor(Color.CYAN);         //Team Color
        setScanColor(Color.BLUE);
    }

    public void run() {
        while (true) {
//            turnRight(90);
//            ahead(100);
//            turnGunRight(360);
//            turnRight(90);
//            ahead(100);
//            turnGunRight(360);
        }
    }

    public void onMessageReceived(MessageEvent e) {
        // Fire at a point
        if (e.getMessage() instanceof sampleteam.Point) {
            sampleteam.Point p = (Point) e.getMessage();
            // Calculate x and y to target
            double dx = p.getX() - this.getX();
            double dy = p.getY() - this.getY();
            // Calculate angle to target
            double theta = Math.toDegrees(Math.atan2(dx, dy));

            // Turn gun to target
            turnGunRight(normalRelativeAngleDegrees(theta - getGunHeading()));
            // Fire hard!
            fire(3);
        } // Set our colors
        else if (e.getMessage() instanceof RobotColors) {
            RobotColors c = (RobotColors) e.getMessage();

            setBodyColor(c.bodyColor);
            setGunColor(c.gunColor);
            setRadarColor(c.radarColor);
            setScanColor(c.scanColor);
            setBulletColor(c.bulletColor);
        }
    }

//    public void onScannedRobot(ScannedRobotEvent e) {
//        if(isTeammate(e.getName())) {
//            scan();
//        }
//        else {
//            setAhead(100);
//            fire(3);
//        }
//    }

    public void onHitByBullet(HitByBulletEvent e) {
        back(10);
    }

    public void onHitWall(HitWallEvent e)  {
        back(20);
    }
}
