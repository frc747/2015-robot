package com.frc869.robot.code2015.drive;

import edu.wpi.first.wpilibj.CANTalon;//Import necessary assets

/**
 * Mechanum Drive for the 2015 FRC season
 * 
 * Drive uses 4 motors that are controlled via Talon Motor Controllers over CAN and are connected to ecoders.
 * 
 * 
 */
public class Mecanum869 {
	
	private CANTalon[] drive = new CANTalon[4];
	private boolean enabled = false;
	
	/**
	 * 
	 * @param rf right front CANTalon
	 * @param rr right rear CANTalon
	 * @param lf left front CANTalon
	 * @param lr left rear CANTalon
	 */
	public Mecanum869(CANTalon rf, CANTalon rr, CANTalon lf, CANTalon lr){
		drive[0] = rf;
		drive[1] = rr;
		drive[2] = lr;
		drive[3] = lf;
		
		//TODO Calculate max can speed
	}
	
	public boolean isEnabled(){
		return this.enabled ;
	}
	
	public void enable(){
		this.drive[0].enableControl();
		this.drive[1].enableControl();
		this.drive[2].enableControl();
		this.drive[3].enableControl();		
		this.enabled = true;
	}
	public void disable(){
		this.enabled = false;
		this.drive[0].disable();
		this.drive[1].disable();
		this.drive[2].disable();
		this.drive[3].disable();
	}
	
	/**
	 * Controls the robot based on the position of a joystick and the robot.
	 * 
	 * direction vector (x,y) is based off of the coordinates of the robot's starting position.
	 * 
	 * @param x x component of the direction vector
	 * @param y y component of the direction vector
	 * @param z rotation amount
	 * @param gyroAng current angle of the robot
	 */
	public void drive(double x, double y, double z, double gyroAng){
		if(!this.enabled)
			return;
		
		double theta = (Math.PI / 4) + gyroAng;
		
		/*
		 * Mecanum Logic, calculations which determine the direction that each
		 * motor will move so that the robot will move in the desired direction 
		 */
		
		double xr = Math.cos(theta) * x - Math.sin(theta) * y;
		double yr = Math.sin(theta) * x + Math.cos(theta) * y;
		
		//Adds rotation value (z)
		
		double a = -yr - z;
		double b = -xr - z;
		double c = yr - z;
		double d = xr - z;
		
		//Normalizes vector, preventing any value from exceeding ±1 by dividing all values by the greatest value
		double n = Math.max(Math.max(Math.abs(a), Math.abs(b)), Math.max(Math.abs(c), Math.abs(d)));
		
		if(n > 1){
			a /= n;
			b /= n;
			c /= n;
			d /= n;
		}
		//Applies previous calculations, see Mecanum Logic
		drive[0].set(a);
		drive[1].set(b);
		drive[2].set(c);
		drive[3].set(d);
		
	}

}
