package kwm_bluetooth;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;

import lejos.hardware.Sound;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3IRSensor;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.hardware.sensor.SensorMode;
import lejos.robotics.Color;
import lejos.robotics.RegulatedMotor;
import lejos.utility.Delay;


public class InfraredSignalCheckerThread extends Thread {
	private EV3IRSensor infraredSensor;
	private RegulatedMotor leftMotor,rightMotor;

	public InfraredSignalCheckerThread(final EV3IRSensor infraredSensor, RegulatedMotor left, RegulatedMotor right){
		this.infraredSensor = infraredSensor;
		this.leftMotor = left;
		this.rightMotor = right;
	}

	public void run() {
		
	}
}


/* switch (mode){
                case 0: //Checkt nix
                	leftMotor.startSynchronization();	    
            		leftMotor.stop();
            		rightMotor.stop();
            		leftMotor.endSynchronization();
                    break;
                case 1: //Nach links
                    //pilot.rotateLeft();
                	System.out.println("Should turn left");
                    break;
                case 2: //Nach rechts
                    //pilot.rotateRight();
                    System.out.println("Should turn right");
                    break;
                case 3: //nach vorne
    				motorA.setAcceleration(1000);
    				motorB.setAcceleration(1000);
    				boolean drive = true;
    				while(drive) {
    					motorA.forward();
    					motorB.forward();
    					int sampleSize1 = touchleft.sampleSize();
    					int sampleSize2 = touchright.sampleSize();
    					float[] sample1 = new float[sampleSize1];
    					float[] sample2 = new float[sampleSize2];
    					touchleft.fetchSample(sample1, 0);
    					touchright.fetchSample(sample2, 0);
    					if(sample1[0]!=1 || sample2[0]!=1) {
    						drive = false;
    					}
    				}
    				motorA.stop();
    				motorB.stop();
                	leftMotor.startSynchronization();	    
            		leftMotor.forward();
            		rightMotor.forward();
            		leftMotor.endSynchronization();
                    break;

                case 4: //zurück
                    //pilot.backward();
                	leftMotor.startSynchronization();	    
            		leftMotor.backward();
            		rightMotor.backward();
            		leftMotor.endSynchronization();
                    break;
                default:
                    System.out.println("not sure what to do...");
                    System.exit(0);*/

