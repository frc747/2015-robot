package com.frc869.robot.code2015.autonomous;

import java.util.ArrayList;
import java.util.List;

import com.frc869.robot.code2015.controllable.Lift;
import com.frc869.robot.code2015.controllable.Controllable;
import com.frc869.robot.code2015.controllable.Mecanum;
import com.frc869.robot.code2015.controllable.Tugger;

public class Autonomous implements Runnable {
	private Mecanum mecanumDrive;
	private Tugger tuggers;
	private Lift lift;
	private List<Controllable> controllable;
	private int step;

	public Autonomous() {
		step = 0;
		this.mecanumDrive = Mecanum.getInstance();
		this.lift = Lift.getInstance();
		this.tuggers = Tugger.getInstance();
		this.controllable = new ArrayList<>();
		this.controllable.add(this.mecanumDrive);
		this.controllable.add(this.lift);
		this.controllable.add(this.tuggers);
	}

	public Mecanum getMecanumDrive() {
		return mecanumDrive;
	}

	public Tugger getTuggers() {
		return tuggers;
	}

	public Lift getLift() {
		return lift;
	}

	public List<Controllable> getControllable() {
		return controllable;
	}
	
	public int getStep() {
		return step;
	}
	
	public void increaseStep() {
		step++;
	}
	
	public void stop() {
		for(Controllable component : this.controllable) {
			component.stop();
		}
	}
	
	@Override
	public void run() {
		stop();
	}
}
