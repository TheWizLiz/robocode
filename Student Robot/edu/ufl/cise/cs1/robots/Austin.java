package edu.ufl.cise.cs1.robots;

import robocode.*;
import robocode.util.Utils;
import sampleteam.Point;
import sampleteam.RobotColors;

import java.awt.*;
import java.io.IOException;
import java.util.Vector;

public class Austin extends TeamRobot implements BorderSentry
{

    double moveAmount; // How much to move

    public void run() {

        RobotColors c = new RobotColors();

        c.bodyColor = Color.cyan;
        c.gunColor = Color.cyan;
        c.radarColor = Color.cyan;
        c.scanColor = Color.cyan;
        c.bulletColor = Color.cyan;

        setColors(c.bodyColor, c.gunColor, c.radarColor, c.bulletColor, c.scanColor);

        setAdjustRadarForGunTurn(true);

        // Initialize moveAmount to the maximum possible for this battlefield.
        moveAmount = Math.max(getBattleFieldWidth(), getBattleFieldHeight());

        try {
            // Send RobotColors object to our entire team
            broadcastMessage(c);
        } catch (IOException ignored) {}

        turnLeft(getHeading() % 90);

        do {
            // ...
            // Turn the radar if we have no more turn, starts it if it stops and at the start of round
            if ( getRadarTurnRemaining() == 0.0 )
                setTurnRadarRightRadians( Double.POSITIVE_INFINITY );

            // Move up the wall
            ahead(moveAmount);
            // Turn to the next wall
            turnRight(90);

            execute();

        } while ( true );
    }
    public void onScannedRobot(ScannedRobotEvent e) {
        // Don't fire on teammates
        if (isTeammate(e.getName())) {
            return;
        }

        // Absolute angle towards target
        double angleToEnemy = getHeadingRadians() + e.getBearingRadians();

        // Subtract current radar heading to get the turn required to face the enemy, be sure it is normalized
        double radarTurn = Utils.normalRelativeAngle( angleToEnemy - getRadarHeadingRadians() );

        // Distance we want to scan from middle of enemy to either side
        // The 36.0 is how many units from the center of the enemy robot it scans.
        double extraTurn = Math.min( Math.atan( 18.0 / e.getDistance() ), Rules.RADAR_TURN_RATE_RADIANS );

        // Adjust the radar turn so it goes that much further in the direction it is going to turn
        // Basically if we were going to turn it left, turn it even more left, if right, turn more right.
        // This allows us to overshoot our enemy so that we get a good sweep that will not slip.
        if (radarTurn < 0)
            radarTurn -= extraTurn;
        else
            radarTurn += extraTurn;

        //Turn the radar
        setTurnRadarRightRadians(radarTurn);

        // Calculate enemy bearing
        double enemyBearing = this.getHeading() + e.getBearing();
        // Calculate enemy's position
        double enemyX = getX() + e.getDistance() * Math.sin(Math.toRadians(enemyBearing));
        double enemyY = getY() + e.getDistance() * Math.cos(Math.toRadians(enemyBearing));

        setTurnGunRight(enemyBearing);

        smartFire(e.getDistance());

        try {
            // Send enemy position to teammates
            broadcastMessage(new Point(enemyX, enemyY));
        } catch (IOException ex) {
            out.println("Unable to send order: ");
            ex.printStackTrace(out);
        }
    }

    public void onHitByBullet(HitByBulletEvent e) {
        turnLeft(90 - e.getBearing());
    }


    public void onHitRobot(HitRobotEvent e) {

        back(50);

    }

    public void smartFire(double robotDistance) {
        if (robotDistance > 100 || getEnergy() < 15) {
            fire(1);
        } else if (robotDistance > 50) {
            fire(2);
        } else {
            fire(3);
        }
    }
}
