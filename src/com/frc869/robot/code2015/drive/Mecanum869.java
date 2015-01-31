package com.frc869.robot.code2015.drive;

import edu.wpi.first.wpilibj.CANTalon;

public class Mecanum869 {
	
	private CANTalon[] drive = new CANTalon[4];
	private boolean enabled = false;
	
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
	
	public void drive(double x, double y, double z, double gyroAng){
		if(!this.enabled)
			return;
		
		double theta = (Math.PI / 4) + gyroAng;
		
		double xr = Math.cos(theta) * x - Math.sin(theta) * y;
		double yr = Math.sin(theta) * x + Math.cos(theta) * y;
		
		double a = -yr - z;
		double b = -xr - z;
		double c = yr - z;
		double d = xr - z;
		
		double n = Math.max(Math.max(Math.abs(a), Math.abs(b)), Math.max(Math.abs(c), Math.abs(d)));
		
		if(n > 1){
			a /= n;
			b /= n;
			c /= n;
			d /= n;
		}

		drive[0].set(a);
		drive[1].set(b);
		drive[2].set(c);
		drive[3].set(d);
		
	}

}
