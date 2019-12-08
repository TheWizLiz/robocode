package edu.ufl.cise.cs1.robots;

import robocode.*;

public class Austin extends TeamRobot
{

    public void run() {
        while (true) {
            ahead(100);
            turnGunRight(360);
            back(100);
            turnGunRight(360);
        }
    }

    public void onScannedRobot(ScannedRobotEvent e) {
        fire(1);
    }

}
