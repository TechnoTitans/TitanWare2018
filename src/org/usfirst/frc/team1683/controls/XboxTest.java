package org.usfirst.frc.team1683.controls;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;
import org.usfirst.frc.team1683.driverStation.SmartDashboard;

public class XboxTest {

    XboxController controller = new XboxController(0);

    public void run() {
        SmartDashboard.putString("kLeft_y", "INPUT y is kLeft" + controller.getY(GenericHID.Hand.kLeft));
        SmartDashboard.putString("kRight_y", "INPUT y is kRight" + controller.getY(GenericHID.Hand.kRight));
        SmartDashboard.putString("abtn", "INPUT getAButton" + controller.getAButton());
        
        
        // forward is -1.0, backwards (down is +1.0), so always multiply by one 
        
        
    }
}
