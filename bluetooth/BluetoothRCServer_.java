package kwm_bluetooth;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import lejos.hardware.Sound;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.motor.Motor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3IRSensor;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.hardware.sensor.SensorMode;
import lejos.robotics.Color;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.SampleProvider;
import lejos.utility.Delay;
import lejos.utility.Stopwatch;

public class BluetoothRCServer_ {
	public static void main(String args[]) throws IOException {
		int input;
		ServerSocket server = new ServerSocket(1111);
		EV3LargeRegulatedMotor motorA = new EV3LargeRegulatedMotor(MotorPort.A); //rechts
		EV3LargeRegulatedMotor motorB = new EV3LargeRegulatedMotor(MotorPort.B); //links
		IsEscapeDownChecker isEscapeDown = new IsEscapeDownChecker(server);
		isEscapeDown.setDaemon(true);
		isEscapeDown.start();

		//STANDARDGESCHWINDIGKEIT
		motorA.setSpeed(200);
		motorB.setSpeed(200);

		//STOPPUHREN
		Stopwatch TurnTimer = new Stopwatch();
		Stopwatch ObstacleTimer = new Stopwatch();

		//SENSOREN
		EV3TouchSensor touchleft = new EV3TouchSensor(SensorPort.S1);
		EV3TouchSensor touchright = new EV3TouchSensor(SensorPort.S2);
		EV3UltrasonicSensor ultrasensor = new EV3UltrasonicSensor(SensorPort.S4);

		//FERNBEDIENUNG EINGABE
		while (true) {
			Socket socket;
			try {
				socket = server.accept();
			} catch (IOException e) {
				break;
			}
			DataInputStream in = new DataInputStream(socket.getInputStream());
			input = in.readInt();

			//VORWÄRTS
			if (input == 1) {
				motorA.setAcceleration(6000);
				motorB.setAcceleration(6000);
				motorA.setSpeed(300);
				motorB.setSpeed(300);
				motorA.forward();
				motorB.forward();
			} 

			//ZURÜCK
			if (input == 2) {
				motorA.setAcceleration(6000);
				motorB.setAcceleration(6000);
				motorA.setSpeed(300);
				motorB.setSpeed(300);
				motorA.backward();
				motorB.backward();
			}

			//LINKS
			if (input == 3) {
				motorA.setAcceleration(20000);
				motorB.setAcceleration(20000);
				motorA.setSpeed(100);
				motorB.setSpeed(100);
				motorA.backward();
				motorB.forward();
			}

			//RECHTS
			if (input == 4) {
				motorA.setAcceleration(20000);
				motorB.setAcceleration(20000);
				motorA.setSpeed(100);
				motorB.setSpeed(100);
				motorA.forward();
				motorB.backward();
			}

			//STOPP
			if (input == 5) {
				motorA.stop();
				motorB.stop();
			}

			//EXIT
			if (input == 6) {
				Sound.setVolume(100);
				Sound.buzz();
				server.close();
				motorA.close();
				motorB.close();
				System.exit(0);
			}

			//HUPEN
			if (input == 7) {
				Sound.setVolume(100);
				Sound.beep();
			}

			//AUTONOM
			if (input == 8) {

				motorA.setAcceleration(6000);
				motorB.setAcceleration(6000);
				
				//STANDARDGESCHWINDIGKEIT
				motorA.setSpeed(200);
				motorB.setSpeed(200);
				
				//HINDERNISSE ERKENNEN UND NACH LINKS DREHEN
				boolean isActive = true;
				ObstacleTimer.reset();

				//Solange Druck auf Drucksensoren true (=1) ist
				while(isActive) {
					motorA.forward();
					motorB.forward();
					float[] distanceSample = missAbstand(ultrasensor);

					int i = 0;
					//Wenn Distanz zu Objekt kleiner als 9 cm -> nach links / rechts drehen
					while(distanceSample[0] <= 0.09) {				
						motorA.stop();
						motorB.stop();

						//Geschwindigkeit verringern
						motorA.setSpeed(150);
						motorB.setSpeed(150);

						TurnTimer.reset();
						int t = TurnTimer.elapsed();

						//4 Sekunden nach links oder rechts drehen
						double random = Math.random();
						if (random < 0.5) {
							while(t < 4000) { 
								t = TurnTimer.elapsed();
								motorA.forward();
								motorB.backward();
							}
						}
						else{
							while(t < 4000) { 
								t = TurnTimer.elapsed();
								motorA.backward();
								motorB.forward();
							}
						}
						motorA.setSpeed(200);
						motorB.setSpeed(200);

						//Neuen Wert holen
						distanceSample = missAbstand(ultrasensor);
						ultrasensor.fetchSample(distanceSample, 0);
						ObstacleTimer.reset();
					}
					int drivingTime = ObstacleTimer.elapsed();

					//Wenn Roboter länger als 10 Sekunden durchgängig fährt 
					while(drivingTime >= 11000) {
						TurnTimer.reset();
						int t = TurnTimer.elapsed();

						//Geschwindigkeit verringern
						motorA.setSpeed(100);
						motorB.setSpeed(100);

						//0,4 Sekunden zurückfahren
						while(t < 300) {
							t = TurnTimer.elapsed();
							motorA.backward();
							motorB.backward();
						}

						TurnTimer.reset();
						t = TurnTimer.elapsed();

						motorA.setSpeed(250);
						motorB.setSpeed(250);

						//1,5 Sekunden nach links oder rechts drehen
						double random = Math.random();
						if (random < 0.5) {
							while(t < 2000) {
								t = TurnTimer.elapsed();
								motorA.forward();
								motorB.backward();
							}
						}
						else {
							while(t < 2000) {
								t = TurnTimer.elapsed();
								motorA.backward();
								motorB.forward();
							}
						}
						drivingTime = 0;
						ObstacleTimer.reset();
						motorA.setSpeed(200);
						motorB.setSpeed(200);
					}
					//Berühren die Touchsensoren noch Tischboden?
					isActive = checkTouchSensor(motorA, motorB, touchleft, touchright);
				}
				//Roboter ist über der Kante - fertig
				Sound.beep();
			}
		}
	}

	//MISST DEN ABSTAND ZWISCHEN BODEN/OBJEKT UND ULTRASCHALLSENSOR
	public static float[] missAbstand(EV3UltrasonicSensor ultrasensor)
	{
		SampleProvider sp = ultrasensor.getDistanceMode();
		float [] sample = new float[sp.sampleSize()];
		sp.fetchSample(sample, 0);
		return sample;
	}

	//FAHR MIT WÜRFEL BIS ZUR KANTE
	public static boolean checkTouchSensor(EV3LargeRegulatedMotor motorA, EV3LargeRegulatedMotor motorB, EV3TouchSensor touchleft, EV3TouchSensor touchright) {
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

			//Berühren beide Drucksensoren noch den Tischboden?
			if(sample1[0]!=1 || sample2[0]!=1) {
				motorA.stop();
				motorB.stop();
				drive = false;
			}
			else return true;
		}
		return false;
	}
}