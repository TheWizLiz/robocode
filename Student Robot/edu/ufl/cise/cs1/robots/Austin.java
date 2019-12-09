package edu.ufl.cise.cs1.robots;

import robocode.*;
import robocode.util.Utils;
import sampleteam.Point;
import sampleteam.RobotColors;

import java.awt.*;
import java.io.IOException;

public class Austin extends TeamRobot
{

    public int direction = 1;

    public void run() {

        RobotColors c = new RobotColors();

        c.bodyColor = Color.cyan;
        c.gunColor = Color.cyan;
        c.radarColor = Color.cyan;
        c.scanColor = Color.cyan;
        c.bulletColor = Color.cyan;

        setColors(c.bodyColor, c.gunColor, c.radarColor, c.bulletColor, c.scanColor);

        //setAdjustGunForRobotTurn(true);
        //setAdjustRadarForGunTurn(true);

        try {
            // Send RobotColors object to our entire team
            broadcastMessage(c);
        } catch (IOException ignored) {}

        while (true) {
            setTurnRadarRight(10000);
            ahead(100);
            back(100);
        }
    }
    public void onScannedRobot(ScannedRobotEvent e) {
        // Don't fire on teammates
        if (isTeammate(e.getName())) {
            return;
        }
        // Calculate enemy bearing
        double enemyBearing = this.getHeading() + e.getBearing();
        // Calculate enemy's position
        double enemyX = getX() + e.getDistance() * Math.sin(Math.toRadians(enemyBearing));
        double enemyY = getY() + e.getDistance() * Math.cos(Math.toRadians(enemyBearing));

        try {
            // Send enemy position to teammates
            broadcastMessage(new Point(enemyX, enemyY));
        } catch (IOException ex) {
            out.println("Unable to send order: ");
            ex.printStackTrace(out);
        }
    }

    /*
    public void onHitRobot(HitRobotEvent e) {
        if (e.getBearing() >= 0) {
            direction = 1;
        } else {
            direction = -1;
        }
        turnRight(e.getBearing());
        turnGunRight(e.getBearing());
        // Determine a shot that won't kill the robot...
        // We want to ram him instead for bonus points
        if (e.getEnergy() > 16) {
            fire(3);
        } else if (e.getEnergy() > 10) {
            fire(2);
        } else if (e.getEnergy() > 4) {
            fire(1);
        } else if (e.getEnergy() > 2) {
            fire(.5);
        } else if (e.getEnergy() > .4) {
            fire(.1);
        }
        ahead(40); // Ram him again!
    }

    */
    public void onHitByBullet(HitByBulletEvent e) {
		turnLeft(90 - e.getBearing());
	}

	/*


    public void turn(double degrees, int direction) {
        setTurnRadarRight(degrees * direction);
        turnGunRight(degrees * direction);
        turnRight(degrees * direction);
    }

     */

}
