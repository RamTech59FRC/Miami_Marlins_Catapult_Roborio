package org.usfirst.frc.team59.robot;

import com.ni.vision.NIVision;
//import com.ni.vision.NIVision.DrawMode;
import com.ni.vision.NIVision.Image;
//import com.ni.vision.NIVision.ShapeMode;
import edu.wpi.first.wpilibj.CameraServer;
//import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Camera 
{
	
    static Image frame;
    static NIVision.Rect rect = new NIVision.Rect(10, 10, 100, 100);
   
    static int sessionfront;

	public static void CameraInit()
	{
		
        // the camera name ( "cam0") can be found through the roborio web interface
        
		frame = NIVision.imaqCreateImage(NIVision.ImageType.IMAGE_RGB, 0);

		sessionfront = NIVision.IMAQdxOpenCamera("cam0", NIVision.IMAQdxCameraControlMode.CameraControlModeController);

		NIVision.IMAQdxConfigureGrab(sessionfront);

      
	}
	
	public static void CameraTeleop()
	{
		NIVision.IMAQdxGrab(sessionfront, frame, 1);
        //NIVision.imaqDrawShapeOnImage(frame, frame, rect,DrawMode.DRAW_VALUE, ShapeMode.SHAPE_OVAL, 0.0f);
        
        CameraServer.getInstance().setImage(frame);
	}
	
}
