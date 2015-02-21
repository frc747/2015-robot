package com.frc869.robot.code2015.drive;

import edu.wpi.first.wpilibj.CANTalon;//Import necessary assets

/**
 * Mecanum Drive for the 2015 FRC season
 * 
 * Drive uses 4 motors that are controlled via Talon Motor Controllers over CAN
 * and are connected to encoders.
 * 
 * 
 */
public class Mecanum869 {

	private CANTalon[] drive = new CANTalon[4];
	private boolean enabled = false;
	private double[] old_speed, ratio;

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
	public Mecanum869(CANTalon rf, CANTalon rr, CANTalon lf, CANTalon lr) {
		drive[0] = rf;
		drive[1] = rr;
		drive[2] = lr;
		drive[3] = lf;

		this.old_speed = new double[4];
		this.ratio = new double[4];
		for (int i = 0; i < 4; i++) {
			ratio[i] = 1;
		}
	}

	public boolean isEnabled() {
		return this.enabled;
	}

	public void enable() {
		this.drive[0].enableControl();
		this.drive[1].enableControl();
		this.drive[2].enableControl();
		this.drive[3].enableControl();
		this.enabled = true;
	}

	public void disable() {
		this.enabled = false;
		this.drive[0].disable();
		this.drive[1].disable();
		this.drive[2].disable();
		this.drive[3].disable();
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
		if (!this.enabled)
			return;

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
		
		for (int i = 0; i < 4; i++) {
			this.ratio[i] = (power[i] / power[max_p_i])
					* (this.old_speed[i] / this.old_speed[max_p_i]);
		}
		int max_r_i = 0;
		for (int i = 1; i < 4; i++) {
			if (this.ratio[i] > this.ratio[max_r_i])
				max_r_i = i;
		}
		if (this.ratio[max_r_i] > 1) {
			for (int i = 0; i < 4; i++) {
				this.ratio[i] /= this.ratio[max_r_i];
			}
		}

		for (int i = 0; i < 4; i++) {
			this.old_speed[i] = this.drive[i].getSpeed();
		}
		// Applies previous calculations, see Mecanum Logic
		for (int i = 0; i < 4; i++) {
			// this.drive[i].set(power[i] * this.ratio[i]);
			this.drive[i].set(power[i]);
		}

	}

}
