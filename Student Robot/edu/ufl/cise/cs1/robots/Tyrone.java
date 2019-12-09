package edu.ufl.cise.cs1.robots;

import sampleteam.*;

import robocode.*;
import robocode.util.Utils;
import sampleteam.Point;


import java.awt.*;

import static robocode.util.Utils.normalRelativeAngleDegrees;

public class Tyrone extends TeamRobot implements Droid {
        public void run() {
            /* setBodyColor(Color.cyan);
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
            } */
        }

    public void onMessageReceived(MessageEvent e) {
        // Fire at a point
        if (e.getMessage() instanceof Point) {
            Point p = (Point) e.getMessage();
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

        public void onScannedRobot(ScannedRobotEvent e) {
            /* if (isTeammate(e.getName())) {
                scan();

            }

            if (getDistanceRemaining() < 200) {
                fire(3);
            }
            fire(1);

            if (getEnergy() > 30) {
                fire(3);
            } else {
                fire(1);
            }

            setTurnRadarRight(2.0 * Utils.normalRelativeAngleDegrees(getHeading() + e.getBearing() - getRadarHeading()));
        }

        public void whenHitByBullet(HitByBulletEvent e) {
            turnLeft(45);
            back(50); */

        }
    }


