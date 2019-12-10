package edu.ufl.cise.cs1.robots;

import robocode.*;
import robocode.util.Utils;
import sampleteam.Point;
import sampleteam.RobotColors;

import java.awt.*;
import java.io.IOException;

import static robocode.util.Utils.normalRelativeAngleDegrees;


/*

Austin, the BorderSentry team leader robot coded by Gabriel Turmail.

Code created with heavy inspiration and adaption of the BorderGuard robot, Walls Robot, and MyFirstLeader

 */

//The team leader robot, uses Border Sentry to have extra health to survive and control the drones
public class Austin extends TeamRobot implements BorderSentry
{

    int numTeammates = 0;

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

        turnRight(normalRelativeAngleDegrees(0 - getHeading())); //Turn to face left

        do {

            // Turn the radar if we have no more turn, starts it if it stops and at the start of round
            if ( getRadarTurnRemaining() == 0.0 )
                setTurnRadarRightRadians( Double.POSITIVE_INFINITY );

            // Turn the gun if we have no more turn, starts it if it stops and at the start of round
            if ( getGunTurnRemaining() == 0.0 )
                setTurnGunRightRadians( Double.POSITIVE_INFINITY );

            execute();

            setAhead(1200);

        } while ( true );
    }
    public void onScannedRobot(ScannedRobotEvent e) { //When the robot's scanner sees an enemy

        //Stop if teammate
        if (isTeammate(e.getName())) {

            return;
        }

        //Absolute angle towards target
        double angleToEnemy = getHeadingRadians() + e.getBearingRadians();

        //Subtract current radar heading to get the turn required to face the enemy, be sure it is normalized
        double radarTurn = Utils.normalRelativeAngle( angleToEnemy - getRadarHeadingRadians() );

        //Subtract current gun heading to get the turn required to face the enemy, be sure it is normalized
        double gunTurn = Utils.normalRelativeAngle( angleToEnemy - getGunHeadingRadians() );

        //Distance we want to scan from middle of enemy to either side
        //The 18.0 is how many units from the center of the enemy robot it scans.
        double extraTurn = Math.min( Math.atan( 18.0 / e.getDistance() ), Rules.RADAR_TURN_RATE_RADIANS );

        //Adjust the radar turn so it goes that much further in the direction it is going to turn
        //Basically if we were going to turn it left, turn it even more left, if right, turn more right.
        //This allows us to overshoot our enemy so that we get a good sweep that will not slip.
        if (radarTurn < 0)
            radarTurn -= extraTurn;
        else
            radarTurn += extraTurn;

        //Turn the radar and gun
        setTurnRadarRightRadians(radarTurn);
        setTurnGunRightRadians(gunTurn);

        //Calculate enemy bearing
        double enemyDirection = this.getHeading() + e.getBearing();
        //Calculate enemy's position
        double enemyX = getX() + e.getDistance() * Math.sin(Math.toRadians(enemyDirection));
        double enemyY = getY() + e.getDistance() * Math.cos(Math.toRadians(enemyDirection));

        //Fire the gun if there are less than 2 teammates left
        if (numTeammates < 2)
            calculatedFire(e.getDistance());


        //Send the enemy's position to the drones on the team
        try {
            broadcastMessage(new Point(enemyX, enemyY));
        } catch (IOException ex) {
            out.println("Unable to send order: ");
            ex.printStackTrace(out);
        }
    }

    public void onHitRobot(HitRobotEvent e) { //If the robot hits another robot, or is hit by a robot

        //Back up
        setAhead(-1200);

    }

    public void onHitWall(HitWallEvent e) { //When the robot hits the wall at the other end of the battlefield

        //Turn left and move forwards
        turnRight(90);
        ahead(50);

    }

    public void onMessageReceived(MessageEvent e) {
        if (e.getMessage() instanceof String)
            numTeammates++;

    }

    public void onRobotDeath(DeathEvent e) {

        try {
            // Send Robot Name to our entire team to signify death
            broadcastMessage(this.getName());
        } catch (IOException ignored) {}

    }

    public void calculatedFire(double robotDistance) { //Smart targeting code inspired by Corners bot
        if (robotDistance > 50 || getEnergy() < 15) {
            fire(1);
        } else if (robotDistance > 25) {
            fire(2);
        } else {
            fire(3);
        }
    }
}
