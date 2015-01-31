package com.frc869.robot.code2015.endefector;

//Import necesary assets

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.DigitalInput;

/**
 * Code responsible for commanding the tuggers (component which grabs and pulls the totes
 * 
 * Code uses one motor, limit switches, a CANTalon with an encoder, and a set of static positions as well as a reverse direction variable
 */

public class Tugger implements Runnable{
	
	//Establishing all variables that will be used in later code
	
	private CANTalon motor;
	private DigitalInput innerLimit, outterLimit;
	private double[] positions;
	private double direction;
	
	private boolean moveToDest = false;
	private int positionNum = 0;
	private boolean running = false;
	private Thread thread;
	
	//Creates object "Tugger"
	
	public Tugger(CANTalon motor, DigitalInput lowerLimit, DigitalInput upperLimit, double[] positions, double direction){
		this.motor = motor;
		this.innerLimit = lowerLimit;
		this.outterLimit = upperLimit;
		this.direction = direction;
		
		this.positions = positions;
		this.thread = new Thread(this);
		this.thread.start();
	}
	
	//Moving out at a given speed
	
	public void moveOut(double speed){
		if(!this.outterLimit.get())
			this.motor.set(speed * this.direction);
		this.moveToDest = false;
	}
	
	//Moving in at a given speed
	
	public void moveIn(double speed){
		if(!this.innerLimit.get())
			this.motor.set(-speed * this.direction);
		this.moveToDest = false;
	}
	
	//Moving to next outer position
	
	public void nextOut(){
		if(this.positionNum < this.positions.length - 1){
			this.positionNum++;
			this.moveToDest = true;
		}
	}
	
	//Moving to next inner position
	
	public void nextIn(){
		if(this.positionNum > 0){
			this.positionNum--;
			this.moveToDest = true;
		}
	}
	
	//Calibrate outer position
	
	public void calibrateOut(){
		this.moveToDest = false;
		while(this.outterLimit.get()){
			this.motor.set(1 * this.direction);
		}
		
		this.positionNum = this.positions.length - 1;
		
		//TODO reset encoder
	}
	
	//Calibrate inner position
	
	public void calibrateIn(){
		this.moveToDest = false;
		while(this.innerLimit.get()){
			this.motor.set(-1 * this.direction);
		}
		
		//Gives calibrating process a starting point/default position
		
		this.positionNum = 0;
		
		//TODO reset encoder
	}

	//Automated moving control, sets to preset positions
	
	@Override
	public void run() {
		while(this.running){
			if(this.moveToDest){
				if(!this.innerLimit.get() && this.positions[positionNum] > this.motor.getPosition()){
					this.motor.set(-0.5 * this.direction);
				}else if(!this.outterLimit.get() && this.positions[positionNum] < this.motor.getPosition()){
					this.motor.set(0.5 * this.direction);
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
