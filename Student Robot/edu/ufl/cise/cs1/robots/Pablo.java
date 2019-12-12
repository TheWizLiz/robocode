package edu.ufl.cise.cs1.robots;

import robocode.*;
import sampleteam.Point;
import sampleteam.RobotColors;
import sun.plugin2.message.Message;

import static robocode.util.Utils.*;
import java.awt.*;
import java.io.IOException;

public class Pablo extends TeamRobot implements Droid { //Droid makes my robot have no radar, but a higher starting energy. Scanning comes from messages from the sentry bot, Austin.
    double lastEnemyDirection = 0;

    public void run() { //runs while the game is running
        execute();

        setTurnRight(lastEnemyDirection);
        setAhead(100);

    }

    public void onHitRobot(HitRobotEvent e) { //when a robot hits my robot
        if (isTeammate(e.getName())) { //checks if it is a teammate
            setTurnRight(15);
            back(15); //backs away
        } else {
            if (this.getEnergy() > 30) {
                turnGunRight(e.getBearing()); //turns toward it
                fire(3);
                ahead(25); //attempts to ram (if enough energy)
            } else {
                fire(3);
                back(50);
            }
        }
    }

    public void onHitWall(HitWallEvent e) { //when my robot hits a wall
        turnRight(lastEnemyDirection); //turns toward the last known enemy direction
        fire(3);
        ahead(200);
    }

    public void onRobotDeath(RobotDeathEvent e) { //when my robot dies
        try {
            broadcastMessage(this.getName()); //broadcasts the name of my robot
        } catch (Exception f) {}
    }

    public void onMessageReceived(MessageEvent e) { //when my robot receives a message from the sentry bot
        if (e.getMessage() instanceof Point) { //when the message is an enemy robot
            Point p = (Point) e.getMessage(); //assigns p to the point
            double dx = p.getX() - this.getX(); //gets the x-coordinate
            double dy = p.getY() - this.getY(); //gets the y-coordinate
            double theta = Math.toDegrees(Math.atan2(dx, dy)); //finds the angle towards the enemy
            lastEnemyDirection = theta; //stores the last known enemy direction for onHitWall

            if (theta <= 180) { //if it is faster to turn right or left
                setTurnRight(normalRelativeAngleDegrees(theta - getHeading())); //turns towards the enemy
                setAhead(Math.sqrt(Math.pow((p.getX() - this.getX()), 2) - Math.pow((p.getY() - this.getY()), 2)) + 5); //goes ahead the distancce between my robot and the enemy (distance formula)
                if (Math.sqrt((p.getY() - this.getY()) * (p.getY() - this.getY()) + (p.getX() - this.getX()) * (p.getX() - this.getX())) <= 10) //if my robot is close enough, it fires
                    fire(1);
            } else {
                setTurnLeft(normalRelativeAngleDegrees(theta - getHeading()));
                setAhead(Math.sqrt(Math.pow((p.getX() - this.getX()), 2) - Math.pow((p.getY() - this.getY()), 2)) + 5);
                if (Math.sqrt((p.getY() - this.getY()) * (p.getY() - this.getY()) + (p.getX() - this.getX()) * (p.getX() - this.getX())) <= 10)
                    fire(1);
            }

            if (Math.sqrt(Math.pow((p.getX() - this.getX()), 2) - Math.pow((p.getY() - this.getY()), 2)) > 600) {
                fire(.5); //depending on the distance between my robot and the enemy, my robot fires at different powers
            } else if (Math.sqrt(Math.pow((p.getX() - this.getX()), 2) - Math.pow((p.getY() - this.getY()), 2)) > 400) {
                fire(1); //this is done to better manage my robots energy
            } else if (Math.sqrt(Math.pow((p.getX() - this.getX()), 2) - Math.pow((p.getY() - this.getY()), 2)) > 200) {
                fire(2);
            } else {
                fire(3);
            }

        } else if (e.getMessage() instanceof RobotColors) { //the sentry bot also broadcasts the team colors for the robots
            RobotColors c = (RobotColors) e.getMessage();

            setRadarColor(c.radarColor); //team color is cyan
            setScanColor(c.scanColor);
            setBulletColor(c.bulletColor);
            setBodyColor(c.bodyColor);
            setGunColor(c.gunColor);
        }
    }
}
