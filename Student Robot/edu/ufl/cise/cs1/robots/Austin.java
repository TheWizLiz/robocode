package edu.ufl.cise.cs1.robots;

import robocode.*;
import robocode.util.Utils;
import sampleteam.Point;
import sampleteam.RobotColors;

import java.awt.*;
import java.io.IOException;


/*

Austin, the BorderSentry team leader robot coded by Gabriel Turmail.

Code created with heavy inspiration and adaption of the BorderGuard robot, Walls Robot, and MyFirstLeader

 */

//The team leader robot, uses Border Sentry to have extra health to survive and control the drones
public class Austin extends TeamRobot implements BorderSentry
{

    double enemyDirection; //Store the enemy's current location

    public void run() { //Called at the start of every turn

        //Create a map of the colors
        RobotColors c = new RobotColors();

        c.bodyColor = Color.cyan;
        c.gunColor = Color.cyan;
        c.radarColor = Color.cyan;
        c.scanColor = Color.cyan;
        c.bulletColor = Color.cyan;

        //Set the robot's colors
        setColors(c.bodyColor, c.gunColor, c.radarColor, c.bulletColor, c.scanColor);

        //Allow the radar to turn separate from the gun
        setAdjustRadarForGunTurn(true);

        //Broadcast the colors to the drones
        try {
            // Send RobotColors object to our entire team
            broadcastMessage(c);
        } catch (IOException ignored) {}

        do {
            // Turn the radar if we have no more turn, starts it if it stops and at the start of round
            if ( getRadarTurnRemaining() == 0.0 )
                setTurnRadarRightRadians( Double.POSITIVE_INFINITY );

            //Have robot spin in slow circle while running
            setTurnRight(5);
            setAhead(5);

            execute();

        } while ( true );
    }
    public void onScannedRobot(ScannedRobotEvent e) { //When the robot's scanner sees an enem
        //Stop if teammate
        if (isTeammate(e.getName())) {
            return;
        }

        //Absolute angle towards target
        double angleToEnemy = getHeadingRadians() + e.getBearingRadians();

        //Subtract current radar heading to get the turn required to face the enemy, be sure it is normalized
        double radarTurn = Utils.normalRelativeAngle( angleToEnemy - getRadarHeadingRadians() );

        //Distance we want to scan from middle of enemy to either side
        //The 36.0 is how many units from the center of the enemy robot it scans.
        double extraTurn = Math.min( Math.atan( 18.0 / e.getDistance() ), Rules.RADAR_TURN_RATE_RADIANS );

        //Adjust the radar turn so it goes that much further in the direction it is going to turn
        //Basically if we were going to turn it left, turn it even more left, if right, turn more right.
        //This allows us to overshoot our enemy so that we get a good sweep that will not slip.
        if (radarTurn < 0)
            radarTurn -= extraTurn;
        else
            radarTurn += extraTurn;

        //Turn the radar
        setTurnRadarRightRadians(radarTurn);

        //Calculate enemy bearing
        enemyDirection = this.getHeading() + e.getBearing();
        //Calculate enemy's position
        double enemyX = getX() + e.getDistance() * Math.sin(Math.toRadians(enemyDirection));
        double enemyY = getY() + e.getDistance() * Math.cos(Math.toRadians(enemyDirection));

        //Fire the gun
        calculatedFire(e.getDistance());


        //Send the enemy's position to the drones on the team
        try {
            broadcastMessage(new Point(enemyX, enemyY));
        } catch (IOException ex) {
            out.println("Unable to send order: ");
            ex.printStackTrace(out);
        }
    }

    public void onHitByBullet(HitByBulletEvent e) { //If the robot is hit with a bullet

        //Turn left and move forwards
        turnLeft(90);
        ahead(50);

    }


    public void onHitRobot(HitRobotEvent e) { //If the robot hits another robot, or is hit by a robot

        //Turn left and back up
        turnLeft(90);
        back(50);

    }

    public void onHitWall(HitWallEvent e) { //If the robot hits a wall

        //Turn left and move forwards
        turnLeft(90);
        ahead(50);

    }

    public void calculatedFire(double robotDistance) {
        if (robotDistance > 50 || getEnergy() < 15) {
            fire(1);
        } else if (robotDistance > 25) {
            fire(2);
        } else {
            fire(3);
        }
    }
}
