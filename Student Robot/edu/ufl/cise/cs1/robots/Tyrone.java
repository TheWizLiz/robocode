package edu.ufl.cise.cs1.robots;

import sampleteam.*;

import robocode.*;
import robocode.util.Utils;
import sampleteam.Point;


import java.awt.*;

import static robocode.util.Utils.normalRelativeAngleDegrees;

public class Tyrone extends TeamRobot {
    int others; // Number of other robots in the game
    static int corner = 0; // Which corner we are currently using
    // static so that it keeps it between rounds.
    boolean stopWhenSeeRobot = false; // See goCorner()

    /**
     * run:  Corners' main run function.
     */
    public void run() {
        // Set colors
        setBodyColor(Color.red);
        setGunColor(Color.black);
        setRadarColor(Color.yellow);
        setBulletColor(Color.green);
        setScanColor(Color.green);

        // Save # of other bots
        others = getOthers();

        // Move to a corner
        goCorner();

        // Initialize gun turn speed to 3
        int gunIncrement = 3;

        // Spin gun back and forth
        while (true) {
            for (int i = 0; i < 30; i++) {
                turnGunLeft(gunIncrement);
            }
            gunIncrement *= -1;
        }
    }

    /**
     * goCorner:  A very inefficient way to get to a corner.  Can you do better?
     */
    public void goCorner() {
        // We don't want to stop when we're just turning...
        stopWhenSeeRobot = false;
        // turn to face the wall to the "right" of our desired corner.
        turnRight(normalRelativeAngleDegrees(corner - getHeading()));
        // Ok, now we don't want to crash into any robot in our way...
        stopWhenSeeRobot = true;
        // Move to that wall
        ahead(5000);
        // Turn to face the corner
        turnLeft(90);
        // Move to the corner
        ahead(5000);
        // Turn gun to starting point
        turnGunLeft(90);
    }

    /**
     * onScannedRobot:  Stop and fire!
     */
    public void onScannedRobot(ScannedRobotEvent e) {
        // Should we stop, or just fire?
        if (stopWhenSeeRobot) {
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

    /**
     * smartFire:  Custom fire method that determines firepower based on distance.
     *
     * @param robotDistance the distance to the robot to fire at
     */
    public void smartFire(double robotDistance) {
        if (robotDistance > 200 || getEnergy() < 15) {
            fire(1);
        } else if (robotDistance > 50) {
            fire(2);
        } else {
            fire(3);
        }
    }

    /**
     * onDeath:  We died.  Decide whether to try a different corner next game.
     */
    public void onDeath(DeathEvent e) {
        // Well, others should never be 0, but better safe than sorry.
        if (others == 0) {
            return;
        }

        // If 75% of the robots are still alive when we die, we'll switch corners.
        if ((others - getOthers()) / (double) others < .75) {
            corner += 90;
            if (corner == 270) {
                corner = -90;
            }
            out.println("I died and did poorly... switching corner to " + corner);
        } else {
            out.println("I died but did well.  I will still use corner " + corner);
        }
    }
}

/* }

        public void goToCorner() {

            turnRight(normalRelativeAngleDegrees(0-getHeading()));

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

    /* public void onMessageReceived(MessageEvent e) {
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
            }
            else {
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
        // Should we stop, or just fire?
        if (stopWhenSeeRobot) {
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

*/
