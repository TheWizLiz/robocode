//The student package
package edu.ufl.cise.cs1.robots;

//Import project code
import robocode.*;
import robocode.util.Utils;
import sampleteam.Point;
import sampleteam.RobotColors;
import static robocode.util.Utils.normalRelativeAngleDegrees;


//Import java code
import java.awt.*;
import java.io.IOException;

/*

Austin, the BorderSentry Team Leader Robot coded by Gabriel Turmail.

Code created with heavy inspiration and adaption of the BorderGuard, Walls, MyFirstLeader and TrackFire sample robots.

 */

//The team leader robot, uses Border Sentry to have extra health to survive and control the drones
public class Austin extends TeamRobot implements BorderSentry
{

    int numTeammates = 3; //Variable to track number of alive teammates

    public void run() { //Called at the start of every turn

        //Create a map of the colors
        RobotColors colorMap = new RobotColors();

        colorMap.bodyColor = Color.cyan;
        colorMap.gunColor = Color.cyan;
        colorMap.radarColor = Color.cyan;
        colorMap.scanColor = Color.cyan;
        colorMap.bulletColor = Color.cyan;

        //Set the robot's colors
        setColors(colorMap.bodyColor, colorMap.gunColor, colorMap.radarColor, colorMap.bulletColor, colorMap.scanColor);

        //Broadcast the colors to the drones
        try {
            broadcastMessage(colorMap);
        } catch (IOException ignored) {}

        setAdjustRadarForGunTurn(true); //Allow the radar to turn separate from the gun

        turnRight(normalRelativeAngleDegrees(0 - getHeading())); //Turn to face left

        //Do while loop to control constant game functions
        do {

            // Turn the radar to the right forever if it has nowhere left to turn (when it looses its target)
            if ( getRadarTurnRemaining() == 0.0 )
                setTurnRadarRightRadians( Double.POSITIVE_INFINITY );

            // Turn the gun to the right forever if it has nowhere left to turn (when it looses its target)
            if ( getGunTurnRemaining() == 0.0 )
                setTurnGunRightRadians( Double.POSITIVE_INFINITY );

            execute(); //Execute the turn commands of all functions

            setAhead(1200); //Set the robot to move forwards across the whole map

        } while ( true );
    }
    public void onScannedRobot(ScannedRobotEvent e) { //When the robot's scanner sees an enemy

        //Stop if teammate
        if (isTeammate(e.getName())) {

            return;
        }

        //Calculate the absolute angle towards the scanned robot
        double angleToEnemy = getHeadingRadians() + e.getBearingRadians();

        //Subtract the current radar heading from the absolute heading to get the turn required to face the enemy
        double radarTurn = Utils.normalRelativeAngle( angleToEnemy - getRadarHeadingRadians() );

        //Subtract the current gun heading from the absolute heading to get the turn required to face the enemy
        double gunTurn = Utils.normalRelativeAngle( angleToEnemy - getGunHeadingRadians() );

        //Add extra distance to the side of the enemy to keep track of it as it moves
        double extraTurn = Math.min( Math.atan( 36.0 / e.getDistance() ), Rules.RADAR_TURN_RATE_RADIANS );

        //Add the extra distance if positive, subtract if negative
        if (radarTurn < 0)
            radarTurn -= extraTurn;
        else
            radarTurn += extraTurn;

        //Turn the radar and gun their respective degrees
        setTurnRadarRightRadians(radarTurn);
        setTurnGunRightRadians(gunTurn);

        //Calculate enemy's x and y positions
        double enemyX = getX() + e.getDistance() * Math.sin(angleToEnemy);
        double enemyY = getY() + e.getDistance() * Math.cos(angleToEnemy);

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

    public void onMessageReceived(MessageEvent e) { //If a teammate dies
        if (e.getMessage() instanceof String)
            numTeammates--; //Decrease the number of teammates

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
