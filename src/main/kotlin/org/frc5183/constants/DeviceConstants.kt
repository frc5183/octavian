package org.frc5183.constants

import com.studica.frc.AHRS.NavXComType
import edu.wpi.first.math.VecBuilder
import edu.wpi.first.math.geometry.Rotation3d
import edu.wpi.first.math.geometry.Transform3d
import org.frc5183.hardware.vision.Camera

/**
 * Device IDs connected to the robot.
 */
object DeviceConstants {
    /*
    val FRONT_CAMERA: Camera =
        Camera(
            "Front",
            Transform3d(0.0, 0.0, 1.0, Rotation3d(0.0, 0.0, 0.0)),
            VecBuilder.fill(4.0, 4.0, 8.0),
            VecBuilder.fill(0.5, 0.5, 1.0),
        )
    val BACK_CAMERA: Camera =
        Camera(
            "Back",
            Transform3d(0.0, 0.0, 1.0, Rotation3d(0.0, 0.0, 180.0)),
            VecBuilder.fill(4.0, 4.0, 8.0),
            VecBuilder.fill(0.5, 0.5, 1.0),
        )

     */
    val IMU_PORT: NavXComType = NavXComType.kMXP_SPI
}
