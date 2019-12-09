package edu.ufl.cise.cs1.robots;

import robocode.*;
import sampleteam.Point;
import sampleteam.RobotColors;
import sun.plugin2.message.Message;

import static robocode.util.Utils.*;
import java.awt.*;
import java.awt.geom.*;

public class Pablo extends TeamRobot {
    int turn;

    public void run() {
        setBodyColor(Color.PINK);
        setGunColor(Color.PINK);
        setRadarColor(Color.cyan);
        setBulletColor(Color.cyan);
        setScanColor(Color.PINK);

        turnRight(normalRelativeAngleDegrees(0 - getHeading()));
        ahead(5000);
        turnRight(90);
        ahead(5000);
        while (true) {
            turnRight(90);
            ahead (600);
            turnRight(180 - 18.41);
            ahead(632.5);
            turnRight(normalRelativeAngleDegrees(63.43 - getHeading()));
            ahead(447.2);
            turnRight(normalRelativeAngleDegrees(0 - getHeading()));
            turnLeft(90);
            ahead(400);
            turnRight(90);
            ahead(400);
            turnRight(normalRelativeAngleDegrees(26.57 - getHeading()));
            ahead(447.2);
            turnRight(normalRelativeAngleDegrees(71.57 - getHeading()));
            ahead(632.5);
            turnRight(normalRelativeAngleDegrees(180 - getHeading()));
            ahead(600);
        }
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

    public void onScannedRobot(ScannedRobotEvent e) {
        if (isTeammate(e.getName()) == false) {
            if (e.getDistance() < 600 && e.getDistance() > 400) {
                fire(.5);
            } else if (e.getDistance() < 600 && e.getDistance() > 200) {
                fire(1);
            } else if (e.getDistance() < 600 && e.getDistance() > 75) {
                fire(2);
            } else if (e.getDistance() < 600) {
                fire(3);
            } else if (e.getEnergy() < 15 && this.getEnergy() > 15) {
                fire(.5);
            }
        } else {
            scan();
        }
    }
}
