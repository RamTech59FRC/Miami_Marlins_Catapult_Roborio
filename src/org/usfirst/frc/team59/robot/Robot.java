
package org.usfirst.frc.team59.robot;

import org.usfirst.frc.team59.robot.Camera;
import org.usfirst.frc.team59.robot.subsystems.ExampleSubsystem;

import com.ni.vision.NIVision;
import com.ni.vision.NIVision.DrawMode;
import com.ni.vision.NIVision.Image;
import com.ni.vision.NIVision.ShapeMode;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {

	public static final ExampleSubsystem exampleSubsystem = new ExampleSubsystem();
	public static OI oi;

    Command autonomousCommand;
    SendableChooser chooser;

    AnalogInput pressureSensor = new AnalogInput(0);
	
	Compressor comp = new Compressor(1);
	
	Joystick trigger = new Joystick(0);
	
	Relay in = new Relay(0);
	Relay out = new Relay(1);
    
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
	
	public void robotInit()
	{
		//Camera.CameraInit();
	}
	
	public void disabledInit()
	{
		try {
	    	new Thread(new CamThread()).start();
	    } catch(Exception ex) {
	    	ex.printStackTrace();
	    }
	}
	
	public void disabledPeriodic() 
	{
		//Camera.CameraTeleop();
		double pressure = (Math.round(pressureSensor.getAverageVoltage()*100.0)/100.0);
		SmartDashboard.putString("DB/String 1","Pressure: " + pressure);
	}
	
	public void teleopInit()
	{
		comp.stop();
    	try {
    	new Thread(new CamThread()).start();
    } catch(Exception ex) {
    	ex.printStackTrace();
    }
	}
	
    public void teleopPeriodic() 
    {
    	//Camera.CameraTeleop();
    	double pressure = (Math.round(pressureSensor.getAverageVoltage()*100.0)/100.0);
        
        SmartDashboard.putString("DB/String 0","Not Fired");
        SmartDashboard.putString("DB/String 1","Pressure: " + pressure);
        
        if(trigger.getRawButton(1))
        {
        	SmartDashboard.putString("DB/String 0","FIRED");
        	in.set(Relay.Value.kOn);
        	Timer.delay(0.25);
        	//Camera.CameraTeleop();
        	in.set(Relay.Value.kOff);
        	out.set(Relay.Value.kOn);
        	Timer.delay(0.25);
        	out.set(Relay.Value.kOff);
        }
        
        if(trigger.getRawButton(3))
        {
        	comp.start();
        }
        else if(trigger.getRawButton(4) || pressure >= 1.75)
        {
        	comp.stop();
        }
    	
    }
    
    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
        LiveWindow.run();
    }
    class CamThread implements Runnable {

		@Override
		public void run() {
			
			//USBCamera cam = new USBCamera("cam0");
			//Timer.delay(2);

			Image frame = NIVision.imaqCreateImage(NIVision.ImageType.IMAGE_RGB, 0);
			//NIVision.RGBValue color = new RGBValue(0,255,0,255);

	       // cam.setExposureManual(1);
	        //cam.setFPS(6);
	       // cam.setBrightness(100);
	       // cam.startCapture();
			
			 //NIVision Crosshair
	        NIVision.Rect midLine = new NIVision.Rect(0, 317, 480, 6);
	        //crossLine = new NIVision.Rect(237, 270, 6, 100);
	        
	        //USBCamera Crosshair
	        
	        midLine = new NIVision.Rect(0, 128, 480, 2);
	        
	        
	        NIVision.Rect crossLineBottom = new NIVision.Rect(205, 109, 2, 40);
	        
	        
	        NIVision.Rect crossLineMid = new NIVision.Rect(175, 105, 2, 55);
	        NIVision.Rect crossLineTop = new NIVision.Rect(148, 90, 2, 75);
			
			while (true)
			{
				
				 ////SmartDashboard.putNumber("encoderVal", Robot.pivot.returnPIDInput());
//		        NIVision.IMAQdxGrab(session, frame, 1);
		        
		        frame = Camera.frame;
//		        //Vertical
		        NIVision.imaqDrawShapeOnImage(frame, frame, midLine,
		               DrawMode.PAINT_VALUE, ShapeMode.SHAPE_RECT, 255f);
		        //CrossMid
		        NIVision.imaqDrawShapeOnImage(frame, frame, crossLineMid,
		                DrawMode.PAINT_VALUE, ShapeMode.SHAPE_RECT, 0f);
		        //CrossBottom
		        NIVision.imaqDrawShapeOnImage(frame, frame, crossLineBottom,
		                DrawMode.PAINT_VALUE, ShapeMode.SHAPE_RECT, 255f);
		        //CrossTop
		        NIVision.imaqDrawShapeOnImage(frame, frame, crossLineTop,
		                DrawMode.PAINT_VALUE, ShapeMode.SHAPE_RECT, 255f);
		        
		        
//		        NIVision.imaqOverlayRect(frame, midLine, color, DrawMode.PAINT_VALUE, null );
		        CameraServer.getInstance().setImage(frame);
		        
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
    }
}
