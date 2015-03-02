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
	private double speed;
	
	private boolean moveToDest = false;
	private boolean running = false;
	private int positionNum = 0;
	private int encoderMin = 0;
	private int encoderMax = 0;
	private Thread thread;
	
	//Creates object "Tugger"
	
	public Tugger(CANTalon motor, DigitalInput lowerLimit, DigitalInput upperLimit, double[] positions, double direction, int encMin, int encMax){
		this.motor = motor;
		this.innerLimit = lowerLimit;
		this.outterLimit = upperLimit;
		this.direction = direction;
		
		this.positions = positions;
		
		this.encoderMax = encMin;
		this.encoderMax = encMax;
		
		this.speed = 0;
		
		this.thread = new Thread(this);
	}
	
	//Moving out at a given speed
	
	public void move(double speed){
		this.speed = speed;
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
					double reduction = 0;

					if (this.motor.getPosition() - this.encoderMin > .9*(encoderMax - encoderMin) && speed > 0){
						reduction = (this.motor.getPosition() - encoderMin) / (.1 * (encoderMax - encoderMin));
					}else if((this.motor.getPosition() - this.positions[positionNum]) < .1 * (encoderMax - encoderMin)){
						reduction = (this.motor.getPosition() - this.positions[positionNum]) / (.1 * (encoderMax - encoderMin));
					}
					reduction *= -.9;
					this.motor.set(-0.5 * this.direction - reduction);
				}else if(!this.outterLimit.get() && this.positions[positionNum] < this.motor.getPosition()){
					double reduction = 0;

					if(this.motor.getPosition() - this.encoderMin  < .1 *(encoderMax - encoderMin) && speed < 0){
						reduction = (this.motor.getPosition() - encoderMin) / (.1 * (encoderMax - encoderMin));
					}else if((this.positions[positionNum] - this.motor.getPosition()) < .1 * (encoderMax - encoderMin)){
						reduction = (this.positions[positionNum] - this.motor.getPosition()) / (.1 * (encoderMax - encoderMin));
					}
					reduction *= .9;
					this.motor.set(0.5 * this.direction - reduction);
				}else{
					this.moveToDest = false;
				}
			}else{
				double reduction = 0;
				if (this.motor.getPosition() -  this.encoderMin > .9*(encoderMax - encoderMin) && speed > 0){
					reduction = (encoderMax - this.motor.getPosition()) / (.1 * (encoderMax - encoderMin));
					reduction *= .9;
				}else if(this.motor.getPosition() - this.encoderMin  < .1 *(encoderMax - encoderMin) && speed < 0){
					reduction = (this.motor.getPosition() - encoderMin) / (.1 * (encoderMax - encoderMin));
					reduction *= -.9;
				}	
				this.motor.set(this.speed - reduction);
			}
			
		}
	}
	
	public void on(){
		this.running = true;
		this.motor.enableControl();
	}

	public void off() {
		this.running = false;
		this.motor.disable();
	}

}
