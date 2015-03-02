package com.frc869.robot.code2015.endefector;

public class LiftListener implements EndefectorListener {

	private Lift lift;
	
	public LiftListener(Lift lift){
		this.lift = lift;
			
	}
	
	@Override
	public void run(String arg) {
		System.out.println("Lift " + arg);
		/*
		switch(arg){
		case "DOWN":
			this.lift.move(-0.5);
			break;
		case "UP":
			this.lift.move(0.5);
			break;
		case "STOP":
			this.lift.move(0);
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
		*/
	}
}