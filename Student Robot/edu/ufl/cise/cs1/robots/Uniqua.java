package edu.ufl.cise.cs1.robots;

import robocode.ScannedRobotEvent;
import robocode.TeamRobot;
import robocode.*;
import sampleteam.Point;
import sampleteam.RobotColors;

import java.awt.*;

import static robocode.util.Utils.normalRelativeAngleDegrees;

public class Uniqua extends TeamRobot {
    public void setColors() {
        setGunColor(Color.CYAN);
        setBodyColor(Color.CYAN);
        setRadarColor(Color.RED);          //Team Color
        setGunColor(Color.BLACK);
        setBodyColor(Color.GREEN);
        setRadarColor(Color.CYAN);          //Team Color
        setBulletColor(Color.CYAN);         //Team Color
        setScanColor(Color.CYAN);
        setScanColor(Color.BLUE);
    }

    public void run() {
        while (true) {
            ahead(100);
            turnGunRight(360);
            back(100);
            turnGunRight(360);
            turnRight(90);
        }
    }
    public void onHItByBullet(HitByBulletEvent e) {
        back(10);
    }

    public void onHitWall(HitWallEvent e)  {
        back(20);
    }
}