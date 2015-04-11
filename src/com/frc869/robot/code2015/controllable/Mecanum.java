package com.frc869.robot.code2015.controllable;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CANTalon.FeedbackDevice;
import edu.wpi.first.wpilibj.Gyro;

/**
 * Mecanum Drive for the 2015 FRC season
 * 
 * Drive uses 4 motors that are controlled via Talon Motor Controllers over CAN
 * and are connected to encoders.
 * 
 * 
 */
public class Mecanum extends Controllable {
//	private double driveGear = 12.75;
	private double driveGear = 1;
	private double wheelGear = 1;
	private double wheelDiameter = 6;
	private double wheelBase = 32.625;

	private CANTalon[] drive = new CANTalon[4];
	private CANTalon rightForward;
	private CANTalon rightRear;
	private CANTalon leftForward;
	private CANTalon leftRear;
	private double[] old_speed, ratio;
	private Gyro gyro;
	
	private static Mecanum instance;

	/**
	 * 
	 * @param rf
	 *            right front CANTalon
	 * @param rr
	 *            right rear CANTalon
	 * @param lf
	 *            left front CANTalon
	 * @param lr
	 *            left rear CANTalon
	 */
	private Mecanum(CAN rf, CAN rr, CAN lf, CAN lr, Analog gyro) {
		drive[0] = rightForward = new CANTalon(rf.getValue());
		drive[1] = rightRear = new CANTalon(rr.getValue());
		drive[2] = leftForward = new CANTalon(lf.getValue());
		drive[3] = leftRear = new CANTalon(lr.getValue());
		this.gyro = new Gyro(gyro.getValue());
		this.gyro.reset();

		this.old_speed = new double[4];
		this.ratio = new double[] {1,1,1,1};
	}
	
	public static Mecanum getInstance() {
		if(null==instance) {
			instance = new Mecanum(
					CAN.MECANUM_RIGHT_FORWARD,
					CAN.MECANUM_RIGHT_REAR,
					CAN.MECANUM_LEFT_FORWARD,
					CAN.MECANUM_LEFT_REAR,
					Analog.GYRO
				);
		}
		return instance;
	}
	
//	public double getRightForwardPosition() {
//		return rightForward.getPosition();
//	}
//
//	public double getRightRearPosition() {
//		return rightRear.getPosition();
//	}
//
//	public double getLeftForwardPosition() {
//		return leftForward.getPosition();
//	}
//
//	public double getLeftRearPosition() {
//		return leftRear.getPosition();
//	}

	public boolean isEnabled() {
		boolean enabled = true;
		for(CANTalon talon : drive) {
			if(!talon.isControlEnabled()) {
				enabled = false;
			}
		}
		return enabled;
	}

	public void enable() {
		for(CANTalon talon : drive) {
			talon.setFeedbackDevice(FeedbackDevice.QuadEncoder);
			talon.enableControl();
			talon.setPosition(0);
		}
	}

	public void disable() {
		for(CANTalon talon : drive) {
			talon.disable();
		}
	}

	public void drive(double x, double y, double z) {
		drive(x,y,z,0);
	}
	
	/**
	 * Controls the robot based on the position of a joystick and the robot.
	 * 
	 * direction vector (x,y) is based off of the coordinates of the robot's
	 * starting position.
	 * 
	 * @param x
	 *            x component of the direction vector
	 * @param y
	 *            y component of the direction vector
	 * @param z
	 *            rotation amount
	 * @param gyroAng
	 *            current angle of the robot
	 */
	public void drive(double x, double y, double z, double gyroAng) {
		if (!this.isEnabled()) {
			return;
		}
		double theta = (Math.PI / 4) + gyroAng;
		/*
		 * Mecanum Logic, calculations which determine the direction that each
		 * motor will move so that the robot will move in the desired direction
		 */

		double xr = Math.cos(theta) * x - Math.sin(theta) * y;
		double yr = Math.sin(theta) * x + Math.cos(theta) * y;

		// Adds rotation value (z)

		double[] power = new double[4];
		power[0] = yr + z;
		power[1] = -xr + z;
		power[2] = -yr + z;
		power[3] = xr + z;

		// Normalizes vector, preventing any value from exceeding 1 by dividing
		// all values by the greatest value
		int max_p_i = 0;
		for (int i = 1; i < 4; i++) {
			if (power[i] > power[max_p_i])
				max_p_i = i;
		}

		if (power[max_p_i] > 1) {
			for (int i = 0; i < 4; i++) {
				power[i] /= power[max_p_i];
			}
		}
		// applying encoder normalization
		
//		for (int i = 0; i < 4; i++) {
//			this.ratio[i] = (power[i] / power[max_p_i]) * (this.old_speed[i] / this.old_speed[max_p_i]);
//		}
//		int max_r_i = 0;
//		for (int i = 1; i < 4; i++) {
//			if (this.ratio[i] > this.ratio[max_r_i]) {
//				max_r_i = i;
//			}
//		}
//		if (this.ratio[max_r_i] > 1) {
//			for (int i = 0; i < 4; i++) {
//				this.ratio[i] /= this.ratio[max_r_i];
//			}
//		}

//		for (int i = 0; i < 4; i++) {
//			this.old_speed[i] = this.drive[i].getSpeed();
//		}
		// Applies previous calculations, see Mecanum Logic
		for (int i = 0; i < 4; i++) {
			 this.drive[i].set(power[i] * this.ratio[i]);
		}
	}
	
	public boolean autoDrive(double speed, double inches) {
		return autoDrive(speed,inches,true);
	}
	
	public boolean autoDrive(double speed, double inches, boolean resetAfter) {
		this.drive(0,speed,0,0);
		double averagePosition = (this.rightForward.getPosition()
			+this.leftForward.getPosition()
			-this.rightRear.getPosition()
			-this.leftRear.getPosition())/4;
		if(averagePosition > this.getClicksForDrive(inches)) {
			this.drive(0,0,0,0);
			if(resetAfter) {
				this.resetPosition();
			}
			return true;
		} else {
			return false;
		}
	}
	
	public boolean autoStrafe(double speed, double inches) {
		return autoStrafe(speed,inches,true);
	}
	
	public boolean autoStrafe(double speed, double inches, boolean resetAfter) {
		this.drive(speed,0,0,0);
		double averagePosition = (this.rightForward.getPosition()
			-this.leftForward.getPosition()
			-this.rightRear.getPosition()
			+this.leftRear.getPosition())/4;
		if(averagePosition > this.getClicksForDrive(inches)) {
			this.drive(0,0,0,0);
			if(resetAfter) {
				this.resetPosition();
			}
			return true;
		} else {
			return false;
		}
	}
	
	public boolean autoTurn(double speed, double inches) {
		return autoTurn(speed,inches,true);
	}
	
	public boolean autoTurn(double speed, double inches, boolean resetAfter) {
		this.drive(0,0,speed,0);
		double averagePosition = (this.rightForward.getPosition()
				+this.leftForward.getPosition()
				+this.rightRear.getPosition()
				+this.leftRear.getPosition())/4;
		if(averagePosition > this.getClicksForAngle(inches)) {
			this.drive(0,0,0,0);
			if(resetAfter) {
				this.resetPosition();
			}
			return true;
		} else {
			return false;
		}
	}
	
	public double getClicksForDrive(double inches) {
//		int clicksPerRotation = 250;
		int clicksPerRotation = 1000;
		return (((driveGear / wheelGear) * clicksPerRotation) / (Math.PI * wheelDiameter)) * inches;
	}

	public double getClicksForAngle(double angle) {
		double clicksPerInch = getClicksForDrive(1);
		return ((angle / 360F) * (clicksPerInch * Math.PI * wheelBase));
	}
	
	public void resetPosition() {
		for(CANTalon talon : drive) {
			talon.setPosition(0);
		}
	}

	@Override
	public void stop() {
		drive(0,0,0,0);
	}
}
