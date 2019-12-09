package edu.ufl.cise.cs1.robots;

import robocode.*;
import sampleteam.Point;
import sampleteam.RobotColors;
import sun.plugin2.message.Message;

import static robocode.util.Utils.*;
import java.awt.*;

public class Pablo extends TeamRobot implements Droid {
    public void run() {
        setBodyColor(Color.PINK);
        setGunColor(Color.PINK);

    }

    public void onMessageReceived(MessageEvent e) {
        if (e.getMessage() instanceof Point) {
            Point p = (Point) e.getMessage();
            double dx = p.getX() - this.getX();
            double dy = p.getY() - this.getY();
            double theta = Math.toDegrees(Math.atan2(dx, dy));

            turnGunRight(normalRelativeAngleDegrees(theta - getGunHeading()));
            fire(3);

        } else if (e.getMessage() instanceof RobotColors) {
            RobotColors c = (RobotColors) e.getMessage();

            setRadarColor(c.radarColor);
            setScanColor(c.scanColor);
            setBulletColor(c.bulletColor);
        }
    }
}
