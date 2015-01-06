package com.frc869.robot.code2015;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Timer;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {
    public void robotInit() {
        System.out.println("Start Robot");
        
        
        
        System.out.println("Robot Initialized");
    }
    
    public void commonPeriodic() {
        System.out.println("Start Common Periodic");

        
        
        System.out.println("Common Periodic Complete");
    }

    public void disabledInit() {
        System.out.println("Start Disabled");

        
        
        System.out.println("Disabled Initialized");
    }

    public void disabledPeriodic() {
        System.out.println("Start Disabled Periodic");
    	commonPeriodic();
    	
    	
    	
        Timer.delay(0.001);
        System.out.println("Disabled Periodic Complete");
    }

    public void autonomousInit() {
        System.out.println("Start Autonomous");

        
        
        System.out.println("Autonomous Initialized");
    }

    public void autonomousPeriodic() {
        System.out.println("Start Autonomous Periodic");
    	commonPeriodic();
    	
    	
    	
        Timer.delay(0.001);
        System.out.println("Autonomous Periodic Complete");
    }

    public void teleopInit() {
        System.out.println("Start Teleoperated");

        
        
        System.out.println("Teleoperated Initialized");
    }

    public void teleopPeriodic() {
        System.out.println("Start Teleoperated Periodic");
    	commonPeriodic();
    	
    	
    	
        Timer.delay(0.001);
        System.out.println("Teleoperated Periodic Complete");
    }

    public void testInit() {
        System.out.println("Start Test");

        
        
        System.out.println("Test Initialized");
    }

    public void testPeriodic() {
        System.out.println("Start Test Periodic");
    	commonPeriodic();
    	
    	
    	
        Timer.delay(0.001);
        System.out.println("Test Periodic Complete");
    }
    
}
