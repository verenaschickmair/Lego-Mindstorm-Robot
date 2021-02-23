package kwm2020_roboter;

import lejos.hardware.*;
import lejos.hardware.sensor.EV3IRSensor;
import lejos.robotics.RegulatedMotor;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.motor.Motor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.utility.Delay;

public class kwmTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		RegulatedMotor leftMotor = new EV3LargeRegulatedMotor(MotorPort.A);
		RegulatedMotor rightMotor = new EV3LargeRegulatedMotor(MotorPort.B);
	
		
		combineMotorAndTouch(); 
		//testTouchSensor();
		
	}

	private static void testMotors() {
		//EV3MediumRegulatedMotor medMotor = new EV3MediumRegulatedMotor(MotorPort.A);
		RegulatedMotor leftMotor = new EV3LargeRegulatedMotor(MotorPort.B);
		RegulatedMotor rightMotor = new EV3LargeRegulatedMotor(MotorPort.C);

		leftMotor.synchronizeWith(new RegulatedMotor[] {rightMotor});
		leftMotor.resetTachoCount();
		rightMotor.resetTachoCount();
		leftMotor.rotateTo(0);
		rightMotor.rotateTo(0);
		leftMotor.setSpeed(400);
		rightMotor.setSpeed(400);
		leftMotor.setAcceleration(800);
		rightMotor.setAcceleration(800);
		leftMotor.startSynchronization();
		leftMotor.forward();
		rightMotor.forward();
		leftMotor.endSynchronization();

		 Delay.msDelay(5000);

		 leftMotor.startSynchronization();
		leftMotor.stop();
		rightMotor.stop();
		leftMotor.endSynchronization();
		//medMotor.close();
		leftMotor.close();
		rightMotor.close();
		}
	
	
	private static void testTouchSensor() {
		EV3TouchSensor touch1 = new EV3TouchSensor(SensorPort.S1);
		EV3TouchSensor touch2 = new EV3TouchSensor(SensorPort.S2);
		LCD.clear();
		while (!Button.ESCAPE.isDown()) {
		int sampleSize1 = touch1.sampleSize();
		int sampleSize2 = touch2.sampleSize();
		float[] sample1 = new float[sampleSize1];
		float[] sample2 = new float[sampleSize2];
		touch1.fetchSample(sample1, 0);
		touch2.fetchSample(sample2, 0);
		LCD.drawString("TSLinks: " + sample1[0], 0, 0);
		LCD.drawString("TSRechts: " + sample2[0], 0, 0);
		System.out.println("TSLinks: " + sample1[0]);
		System.out.println("TSRechts: " + sample2[0]);
		Delay.msDelay(2000);
		}
		touch1.close();
		touch2.close();
		LCD.clear();
		}
	
	
	private static void combineMotorAndTouch() {
		EV3TouchSensor touchleft = new EV3TouchSensor(SensorPort.S1);
		EV3TouchSensor touchright = new EV3TouchSensor(SensorPort.S2);
		RegulatedMotor leftMotor = Motor.B;
		RegulatedMotor rightMotor = Motor.A;
		leftMotor.setAcceleration(1000);
		rightMotor.setAcceleration(1000);
		boolean drive = true;
		while(drive) {
		leftMotor.forward();
		rightMotor.forward();
		int sampleSize1 = touchleft.sampleSize();
		int sampleSize2 = touchright.sampleSize();
		float[] sample1 = new float[sampleSize1];
		float[] sample2 = new float[sampleSize2];
		touchleft.fetchSample(sample1, 0);
		touchright.fetchSample(sample2, 0);
		if(sample1[0]!=1 && sample2[0]!=1) {
		drive = false;
		}
		}
		leftMotor.stop();
		rightMotor.stop();
		touchleft.close();
		touchright.close();
		leftMotor.close();
		rightMotor.close();
		}
}





