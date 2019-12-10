package edu.ufl.cise.cs1.robots;

import robocode.*;
import sampleteam.Point;
import sampleteam.RobotColors;
import sun.plugin2.message.Message;

import static robocode.util.Utils.*;
import java.awt.*;
import java.io.IOException;

public class Pablo extends TeamRobot implements Droid {
    int turn;
    String enemyRam;
    double lastEnemyDirection;

    public void run() {
        setBodyColor(Color.PINK);
        setGunColor(Color.PINK);
    }

    public void onHitRobot(HitRobotEvent e) {
        if (isTeammate(e.getName())) {
            back(15);
        } else {
            turnRight(e.getBearing());
            fire(3);
            ahead(20);
        }
    }

    public void onHitWall(HitWallEvent e) {
        turnRight(lastEnemyDirection);
        fire(3);
        ahead(200);
    }

    public void onRobotDeath(RobotDeathEvent e) {

        try {
            broadcastMessage(this.getName());
        } catch (Exception f) {}

    }

    public void onMessageReceived(MessageEvent e) {
        if (e.getMessage() instanceof Point) {
            Point p = (Point) e.getMessage();
            double dx = p.getX() - this.getX();
            double dy = p.getY() - this.getY();
            double theta = Math.toDegrees(Math.atan2(dx, dy));
            lastEnemyDirection = theta;

            if (theta <= 180) {
                turnRight(normalRelativeAngleDegrees(theta - getHeading()));
                ahead(Math.sqrt(Math.pow((p.getX() - this.getX()), 2) - Math.pow((p.getY() - this.getY()), 2)) + 5);
                if (Math.sqrt((p.getY() - this.getY()) * (p.getY() - this.getY()) + (p.getX() - this.getX()) * (p.getX() - this.getX())) <= 10)
                    fire(1);
            } else {
                turnLeft(normalRelativeAngleDegrees(theta - getHeading()));
                ahead(Math.sqrt(Math.pow((p.getX() - this.getX()), 2) - Math.pow((p.getY() - this.getY()), 2)) + 5);
                if (Math.sqrt((p.getY() - this.getY()) * (p.getY() - this.getY()) + (p.getX() - this.getX()) * (p.getX() - this.getX())) <= 10)
                    fire(1);
            }
            
            if (Math.sqrt(Math.pow((p.getX() - this.getX()), 2) - Math.pow((p.getY() - this.getY()), 2)) > 600) {
                fire(.5);
            } else if (Math.sqrt(Math.pow((p.getX() - this.getX()), 2) - Math.pow((p.getY() - this.getY()), 2)) > 400) {
                fire(1);
            } else if (Math.sqrt(Math.pow((p.getX() - this.getX()), 2) - Math.pow((p.getY() - this.getY()), 2)) > 200) {
                fire(2);
            } else {
                fire(3);
            }

        } else if (e.getMessage() instanceof RobotColors) {
            RobotColors c = (RobotColors) e.getMessage();

            setRadarColor(c.radarColor);
            setScanColor(c.scanColor);
            setBulletColor(c.bulletColor);
        }
    }
}
