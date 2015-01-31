package com.frc869.robot.code2015.endefector;

//Import necessary assets

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.DigitalInput;

/**
 * Code responsible for commanding the lift, which lifts the totes after they've been "Tugged"
 * 
 * Code uses one motor, limit switches, a CANTalon with an encoder, and a set of static positions as well as a reverse direction variable
 */

//Establishing all variables to be used in later code

public class Lift implements Runnable {
	private CANTalon motor;
	private DigitalInput lowerLimit, upperLimit;
	private double[] positions;
	
	private boolean moveToDest = false;
	private int positionNum = 0;
	private boolean running = false;
	private Thread thread;
	
	//Creates object "Lift"
	
	public Lift(CANTalon motor, DigitalInput lowerLimit, DigitalInput upperLimit, double[] positions){
		this.motor = motor;
		this.lowerLimit = lowerLimit;
		this.upperLimit = upperLimit;
		
		this.positions = positions;
		this.thread = new Thread(this);
		this.thread.start();
	}
	
	//Moves Lift up
	
	public void moveUp(double speed){
		if(!this.upperLimit.get())
			this.motor.set(speed);
		this.moveToDest = false;
	}
	
	//Moves Lift down
	
	public void moveDown(double speed){
		if(!this.lowerLimit.get())
			this.motor.set(-speed);
		this.moveToDest = false;
	}
	
	//Moves Lift up to next set position
	
	public void nextUp(){
		if(this.positionNum < this.positions.length - 1){
			this.positionNum++;
			this.moveToDest = true;
		}
	}
	
	//Moves Lift down to next set position
	
	public void nextDown(){
		if(this.positionNum > 0){
			this.positionNum--;
			this.moveToDest = true;
		}
	}
	
	//Calibrate upper position
	
	public void calibrateUp(){
		this.moveToDest = false;
		while(this.upperLimit.get()){
			this.motor.set(1);
		}
		
		this.positionNum = this.positions.length - 1;
		
		//TODO reset encoder
		
	}
	
	//Calibrate lower postion
	
	public void calibrateDown(){
		this.moveToDest = false;
		while(this.lowerLimit.get()){
			this.motor.set(-1);
		}
		
		this.positionNum = 0;
		
		//TODO reset encoder
	}

	//Autmoated moving control to preset conditions
	@Override
	public void run() {
		while(this.running){
			if(this.moveToDest){
				if(!this.lowerLimit.get() && this.positions[positionNum] > this.motor.getPosition()){
					this.motor.set(-0.5);
				}else if(!this.upperLimit.get() && this.positions[positionNum] < this.motor.getPosition()){
					this.motor.set(0.5);
				}else{
					this.moveToDest = false;
				}
			}else{
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
		}
	}
}
