package edu.ufl.cise.cs1.robots;

import robocode.*;
import sampleteam.Point;
import sampleteam.RobotColors;
import sun.plugin2.message.Message;

import static robocode.util.Utils.*;
import java.awt.*;

public class Uniqua extends TeamRobot implements Droid {
    int turn;
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
            ahead(5);
        }
    }

    public void onMessageReceived(MessageEvent e) {
        if (e.getMessage() instanceof Point) {
            Point p = (Point) e.getMessage();
            double dx = p.getX() - this.getX();
            double dy = p.getY() - this.getY();
            double theta = Math.toDegrees(Math.atan2(dx, dy));

            if (theta <= 180) {
                turnRight(normalRelativeAngleDegrees(theta - getGunHeading()));
                ahead(Math.sqrt(Math.pow((p.getX() - this.getX()), 2) - Math.pow((p.getY() - this.getY()), 2)) + 5);
                if ((Math.sqrt(Math.pow((p.getX() - this.getX()), 2) - Math.pow((p.getY() - this.getY()), 2)) + 5) <= 10) {
                    fire(1);
                }
            } else {
                turnLeft(normalRelativeAngleDegrees(theta - getGunHeading()));
                ahead(Math.sqrt(Math.pow((p.getX() - this.getX()), 2) - Math.pow((p.getY() - this.getY()), 2)) + 5);
                if ((Math.sqrt(Math.pow((p.getX() - this.getX()), 2) - Math.pow((p.getY() - this.getY()), 2)) + 5) <= 10) {
                    fire(1);
                }
            }

        } else if (e.getMessage() instanceof RobotColors) {
            RobotColors c = (RobotColors) e.getMessage();

            setRadarColor(c.radarColor);
            setScanColor(c.scanColor);
            setBulletColor(c.bulletColor);
        }
    }
}
