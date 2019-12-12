package edu.ufl.cise.cs1.robots;

import sampleteam.*;

import robocode.*;
import robocode.util.Utils;
import sampleteam.Point;


import java.awt.*;

import static robocode.util.Utils.normalRelativeAngleDegrees;



// my robot is a droid
public class Tyrone extends TeamRobot implements Droid {

    /* public void goToCorner() {

        turnRight(normalRelativeAngleDegrees(0 - getHeading()));

        ahead(2000);
        turnLeft(90);
        ahead(2000);
        turnGunLeft(90);
    } */

    public void run() {
        // while running our team color is cyan
        setBodyColor(Color.cyan);
        setGunColor(Color.cyan);
        setRadarColor(Color.cyan);
        setBulletColor(Color.cyan);
        setScanColor(Color.cyan);

        setAdjustGunForRobotTurn(true);

        setTurnRadarRight(Double.POSITIVE_INFINITY);

        while (true) {

            execute();

            setAhead(100);
        }
    }

    public void onHitRobot(HitRobotEvent e) {
        if (isTeammate(e.getName())) {
            back(50); // if i hit a teammate i back up
        }

        if (this.getEnergy() > 25) { // check if there is enough health to charge
            fire(3);
            setMaxVelocity(100); // charge the robots if it is an enemy
            setAhead(1000);
        } else {
            fire(3);
            back(50);
        }


        }
    // when my robot hits a wall, i want it to turn away from the wall
    public void onHitWall(HitWallEvent e) {

        setTurnRight(180);
        setMaxVelocity(8);
        setAhead(200);
    }


    public void onMessageReceived(MessageEvent e) {
        // Fire at a point
        if (e.getMessage() instanceof Point) { // I implemented some of the code from the MYFirstDroid getting started robot
            Point p = (Point) e.getMessage();
            // Calculate x and y to target
            double dx = p.getX() - this.getX();
            double dy = p.getY() - this.getY();
            // Calculate angle to target
            double theta = Math.toDegrees(Math.atan2(dx, dy));

            // use theta to calcualte the angle needed to turn my gun towards the target
            turnGunRight(normalRelativeAngleDegrees(theta - getGunHeading()));


            if (theta <= 180) {
                setTurnRight(normalRelativeAngleDegrees(theta - getHeading()));
                setAhead(Math.sqrt((p.getY() - this.getY()) * (p.getY() - this.getY()) + (p.getX() - this.getX()) * (p.getX() - this.getX())) + 5);
                if (Math.sqrt((p.getY() - this.getY()) * (p.getY() - this.getY()) + (p.getX() - this.getX()) * (p.getX() - this.getX())) <= 100)
                    fire(3);
            } else {
                setTurnLeft(normalRelativeAngleDegrees(theta - getHeading()));
                setAhead(Math.sqrt((p.getY() - this.getY()) * (p.getY() - this.getY()) + (p.getX() - this.getX()) * (p.getX() - this.getX())) + 5);
                if (Math.sqrt((p.getY() - this.getY()) * (p.getY() - this.getY()) + (p.getX() - this.getX()) * (p.getX() - this.getX())) <= 100)
                    fire(3);
            }

            fire(2);

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

    public void onRobotDeath(RobotDeathEvent l) {

        try {

            broadcastMessage(this.getName());
        } catch (Exception e) {}
    }
}