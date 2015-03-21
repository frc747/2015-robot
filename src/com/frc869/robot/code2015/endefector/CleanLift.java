package com.frc869.robot.code2015.endefector;

//Import necessary assets

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import com.frc869.robot.code2015.Robot;

/**
 * Code responsible for commanding the lift, which lifts the totes after they've been "Tugged"
 * 
 * Code uses one motor, limit switches, a CANTalon with an encoder, and a set of static positions as well as a reverse direction variable
 */

//Establishing all variables to be used in later code

public class CleanLift implements Runnable {
	private CANTalon talonLift1, talonLift2;
	private Encoder encoder, liftEncoder;
	private DigitalInput lowerLimit, upperLimit;
	private double speed;
	private Robot robot;
	
	
	
//	private boolean moveToDest = false;
//	private boolean running = false;
//	private int positionNum = 0;
	private int encoderMin, encoderMax;
	private double posi1 = 1000;
	private double posi2 = 2000;
	private double posi3 = 3000;
	private double posi4 = 4000;
	public boolean liftIsHome;
	
	
	
	
	//Creates object "Lift"
	
	public CleanLift(CANTalon talonLift1, CANTalon talonLift2, Encoder encoder, DigitalInput lowerLimit, DigitalInput upperLimit, int encMin, int encMax){
		this.talonLift1 = talonLift1;
		this.talonLift2 = talonLift2;
		this.encoder = encoder;
		this.lowerLimit = lowerLimit;
		this.upperLimit = upperLimit;
		
		this.encoderMin = encMin;
		this.encoderMax = encMax;
	}
	
	//Moves lift
	
	public void move(double speed){
		if(speed>0){
				this.talonLift1.set(speed);
				this.talonLift2.set(speed);
		} else if(speed < 0) {
				this.talonLift1.set(speed);
				this.talonLift2.set(speed);
		} else {
			this.talonLift1.set(0);
			this.talonLift2.set(0);
		}
	}
	
	public void setPosition(double position, Encoder liftEncoder) {
		
		double slowSpeed = .15;
		double fastSpeed = .7;
		double buffer = 100;
		this.liftEncoder = liftEncoder;
	
		
		
		if (this.liftEncoder.get() < position + buffer && this.liftEncoder.get() > position - buffer) {
			this.talonLift1.set(0);
			this.talonLift2.set(0);
		} else if (this.liftEncoder.get() > position) {
				this.talonLift1.set((fastSpeed * (-1)));
				this.talonLift2.set((fastSpeed * (-1)));
				this.robot.rumbleController(1);
		} else if (this.liftEncoder.get() < position) {
				this.talonLift1.set(fastSpeed);
				this.talonLift2.set(fastSpeed);
				this.robot.rumbleController(1);
		} else {
			this.talonLift1.set(0);
			this.talonLift2.set(0);
		}
		
		
		
		
		
	}

	
	
	//Calibrate lower position
	
	public void calibrateDown(DigitalInput liftLowerLimit, Encoder liftEncoder, Double speed){

		
		this.liftEncoder = liftEncoder;
		
		
		if (this.lowerLimit.get()){
			this.talonLift1.set(speed);
			this.talonLift2.set(speed);
			System.out.println("LIMIT");
			System.out.println(this.liftEncoder.get());
			liftIsHome = false;
		} else {
			
			if (this.liftEncoder.get() <= (-125) && this.liftEncoder.get() >= (-135)){
				this.talonLift1.set(0);
				this.talonLift2.set(0);
				System.out.println(this.liftEncoder.get());
				System.out.println("third");
				this.liftEncoder.reset();
				liftIsHome = true;
			} else if (this.liftEncoder.get() <= 0 && this.liftEncoder.get() > (-125) && !liftIsHome){
				this.talonLift1.set(speed);
				this.talonLift2.set(speed);
				System.out.println(this.liftEncoder.get());
				System.out.println("second");
			} else if (this.liftEncoder.get() > 0 || this.liftEncoder.get() < (-135) && !liftIsHome){
				this.liftEncoder.reset();
				this.talonLift1.set(0);
				this.talonLift2.set(0);	
				System.out.println(this.liftEncoder.get());
				System.out.println("first"); 
			}

		}
		
			
				
				
		
		
		//TO DO reset encoder
	}

	
	@Override
	public void run() {

	}
}
