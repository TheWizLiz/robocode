package edu.ufl.cise.cs1.robots;

import robocode.*;

import java.awt.*;

public class Austin extends TeamRobot
{

    public void run() {

        setColors(Color.cyan, Color.cyan, Color.cyan, Color.cyan, Color.cyan);

        while (true) {
            ahead(100);
            turnGunRight(360);
            back(100);
            turnGunRight(360);
        }
    }

    public void onScannedRobot(ScannedRobotEvent e) {
        if (!isTeammate(e.getName())) {
            fire(1);
        } else {
            scan();
        }
    }

}
