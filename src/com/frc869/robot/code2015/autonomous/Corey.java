package com.frc869.robot.code2015.autonomous;

import com.frc869.robot.code2015.controllable.Lift;
import com.frc869.robot.code2015.controllable.Tugger;

public class Corey extends Autonomous {
	@Override
	public void run() {
		switch (getStep()) {
		default:
			stop();
			break;
		case 0:
			if (this.getMecanumDrive().autoDrive(-.15, -.5)) {
				increaseStep();
			}
			break;
		case 1:
			if (this.getLift().setPosition(Lift.Position.PICKUP_BIN)) {
				increaseStep();
			}
			break;
		case 2:
			if (this.getMecanumDrive().autoDrive(.5, 75.398223688, false)) {
				increaseStep();
			}
			break;
		case 3:
			if (this.getLift().setPosition(Lift.Position.DROP_BIN)) {
				increaseStep();
			}
			break;
		case 4:
			if (this.getTuggers().setPosition(Tugger.Position.OPEN)) {
				increaseStep();
			}
			break;
		}
	}
}
