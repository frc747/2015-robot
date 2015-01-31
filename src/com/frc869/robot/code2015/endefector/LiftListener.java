package com.frc869.robot.code2015.endefector;

public class LiftListener implements EndefectorListener {

	private Lift lift;
	
	public LiftListener(Lift lift){
		this.lift = lift;
			
	}
	
	@Override
	public void run(String arg) {
		switch(arg){
		case "DOWN":
			this.lift.moveDown(-0.5);
			break;
		case "IN":
			this.lift.moveUp(0.5);
			break;
		case "NEXTDOWN":
		case "PREV":
			this.lift.nextDown();
			break;
		case "NEXTUP":
		case "NEXT":
			this.lift.nextUp();
			break;
		default:
			break;
		}
	}
}