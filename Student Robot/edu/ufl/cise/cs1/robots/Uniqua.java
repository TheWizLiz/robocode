package edu.ufl.cise.cs1.robots;

import robocode.ScannedRobotEvent;
import robocode.TeamRobot;

import java.awt.*;

public class Uniqua extends TeamRobot {
    public void setColors() {
        setGunColor(Color.CYAN);
        setBodyColor(Color.CYAN);
        setRadarColor(Color.CYAN);          //Team Color
        setBulletColor(Color.CYAN);         //Team Color
        setScanColor(Color.CYAN);
    }

    public void run() {
        while (true) {
            ahead(100);
            turnGunRight(360);
            back(100);
            turnGunRight(360);
        }
    }

    public void onScannedRobot(ScannedRobotEvent e) {
        if(isTeammate(e.getName())) {

        }
        else
            fire(1);
    }
}
