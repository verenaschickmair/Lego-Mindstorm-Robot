package kwm2020_roboter;

import lejos.hardware.Button;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.motor.Motor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3IRSensor;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.chassis.Chassis;
import lejos.robotics.chassis.Wheel;
import lejos.robotics.chassis.WheeledChassis;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.navigation.MovePilot;

//http://www.rapidpm.org/2014/02/lego-mindstorms-ev3-components-infrared.html
public class RemoteTest {


    public static void main(String[] args) throws InterruptedException {
        final EV3IRSensor infraredSensor = new EV3IRSensor(SensorPort.S4);
        RegulatedMotor leftMotor = new EV3LargeRegulatedMotor(MotorPort.B);
	    RegulatedMotor rightMotor = new EV3LargeRegulatedMotor(MotorPort.A);	   	    		
	    leftMotor.synchronizeWith(new RegulatedMotor[] {rightMotor});
        final InfraredSignalCheckerThread checkerThread = new InfraredSignalCheckerThread(infraredSensor,leftMotor,rightMotor);
        
        leftMotor.setSpeed(400);
	    rightMotor.setSpeed(400);
	    leftMotor.setAcceleration(800);
	    rightMotor.setAcceleration(800);

        checkerThread.start();
        Button.waitForAnyPress();
    }
}

