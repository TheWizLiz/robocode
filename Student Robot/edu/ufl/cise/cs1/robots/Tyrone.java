package edu.ufl.cise.cs1.robots;

import sampleteam.*;

import robocode.*;
import robocode.util.Utils;
import sampleteam.Point;


import java.awt.*;

import static robocode.util.Utils.normalRelativeAngleDegrees;

public class Tyrone extends TeamRobot implements Droid {

    public void goToCorner() {

        turnRight(normalRelativeAngleDegrees(0 - getHeading()));

        ahead(2000);
        turnLeft(90);
        ahead(2000);
        turnGunLeft(90);
    }

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

    public void onHitRobot(HitRobotEvent e) {
        if (isTeammate(e.getName())) {
            back(50);
        }
        if (e.getBearing() > -10 && e.getBearing() < 10) {
            fire(3);
            setMaxVelocity(100);
            ahead(1000);


        }
    }

    public void onHitWall(HitWallEvent e) {

        turnRight(180);
        setMaxVelocity(4);
        ahead(300);
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

            if (theta <= 180) {
                turnRight(normalRelativeAngleDegrees(theta - getGunHeading()));
                ahead(Math.sqrt((p.getY() - this.getY()) * (p.getY() - this.getY()) + (p.getX() - this.getX()) * (p.getX() - this.getX())) + 5);
                if (Math.sqrt((p.getY() - this.getY()) * (p.getY() - this.getY()) + (p.getX() - this.getX()) * (p.getX() - this.getX())) <= 8)
                    fire(1);
            } else {
                turnLeft(normalRelativeAngleDegrees(theta - getGunHeading()));
                ahead(Math.sqrt((p.getY() - this.getY()) * (p.getY() - this.getY()) + (p.getX() - this.getX()) * (p.getX() - this.getX())) + 5);
                if (Math.sqrt((p.getY() - this.getY()) * (p.getY() - this.getY()) + (p.getX() - this.getX()) * (p.getX() - this.getX())) <= 8)
                    fire(1);

            }
        }
        // Set our colors
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
        //
        /* if (stopWhenSeeRobot) {
            // Stop everything!  You can safely call stop multiple times.
            stop();
            // Call our custom firing method
            smartFire(e.getDistance());
            // Look for another robot.
            // NOTE:  If you call scan() inside onScannedRobot, and it sees a robot,
            // the game will interrupt the event handler and start it over
            scan();
            // We won't get here if we saw another robot.
            // Okay, we didn't see another robot... start moving or turning again.
            resume();
        } else {
            smartFire(e.getDistance());
        }


    }

}
}
         */
    }
}