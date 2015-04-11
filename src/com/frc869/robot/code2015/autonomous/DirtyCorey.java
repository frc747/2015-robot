package com.frc869.robot.code2015.autonomous;

import com.frc869.robot.code2015.controllable.Lift;

public class DirtyCorey extends Autonomous {
	@Override
	public void run() {
		switch (getStep()) {
		default:
			stop();
			break;
		case 0:
			if (this.getMecanumDrive().autoDrive(-.15, -.1)) {
				increaseStep();
			}
			break;
		case 1:
			if (this.getLift().setPosition(Lift.Position.LITTER_BIN)) {
				increaseStep();
			}
			break;
		case 2:
			if (this.getMecanumDrive().autoDrive(-.5, 8)) {
				increaseStep();
			}
			break;
		}
	}
}
