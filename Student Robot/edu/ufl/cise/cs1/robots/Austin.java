package edu.ufl.cise.cs1.robots;

import robocode.*;
import robocode.util.Utils;
import sampleteam.Point;
import sampleteam.RobotColors;

import java.awt.*;
import java.io.IOException;

public class Austin extends TeamRobot implements BorderSentry
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

        do {
            // ...
            // Turn the radar if we have no more turn, starts it if it stops and at the start of round
            if ( getRadarTurnRemaining() == 0.0 )
                setTurnRadarRightRadians( Double.POSITIVE_INFINITY );

                setTurnRight(5);
                ahead(5);

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

}
