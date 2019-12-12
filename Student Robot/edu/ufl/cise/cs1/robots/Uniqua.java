package edu.ufl.cise.cs1.robots;

import robocode.*;
import sampleteam.Point;

import java.awt.*;

import static robocode.util.Utils.normalRelativeAngleDegrees;

public class Uniqua extends TeamRobot{
    public void setColors() {
        setGunColor(Color.cyan);
        setBodyColor(Color.cyan);
        setRadarColor(Color.cyan);          //Team Color
        setBulletColor(Color.cyan);         //Team Color
        setScanColor(Color.cyan);
    }

    public void run() {
        setColors();
        while (true) {

            execute();

            setTurnRight(90);
            setTurnGunRight(360);
            setAhead(100);
            setTurnRight(90);
            setAhead(100);
            setTurnGunRight(360);

        }
    }

    public void onBulletMissed(ScannedRobotEvent e) {
        if(e.getDistance() < 40) {
            turnRight(e.getBearing());
            ahead(20);
            fire(2);
        }
    }

    public void onScannedRobot(ScannedRobotEvent e) {
        if(isTeammate(e.getName())) {
            scan();
        }
        else {
            setAhead(100);
            fire(1);
        }
    }

    public void onHitRobot(HitRobotEvent event) {
        if(isTeammate(event.getName())) {
            back(20);
        }
        else {
            fireBullet(3);
        }
    }

    public void onRobotDeath(RobotDeathEvent death) {

        try {
            broadcastMessage(this.getName());
        } catch (Exception notWorking) {}

    }

    public void onMessageReceived(MessageEvent e) {
        if (e.getMessage() instanceof sampleteam.Point) {
            sampleteam.Point p = (Point) e.getMessage();
            double enemyX = p.getX() - this.getX();
            double enemyY = p.getY() - this.getY();
            double enemyBearing = Math.toDegrees(Math.atan2(enemyY, enemyY));

            setTurnRight(normalRelativeAngleDegrees(enemyBearing - getHeading()));
            setTurnGunRight(normalRelativeAngleDegrees(enemyBearing - getGunHeading()));

            if(this.getEnergy() > 25)
                setAhead(100);
        }
    }

    public void onHitByBullet(HitByBulletEvent e) {
        back(25);
    }

    public void onHitWall(HitWallEvent e)  {
        back(20);
    }
}