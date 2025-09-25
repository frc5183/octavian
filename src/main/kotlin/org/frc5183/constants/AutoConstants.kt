package org.frc5183.constants

import com.pathplanner.lib.config.ModuleConfig
import com.pathplanner.lib.config.RobotConfig
import com.pathplanner.lib.path.PathConstraints
import edu.wpi.first.units.Units
import edu.wpi.first.units.measure.LinearVelocity
import org.frc5183.constants.swerve.modules.BackLeftSwerveModuleConstants
import org.frc5183.constants.swerve.modules.BackRightSwerveModuleConstants
import org.frc5183.constants.swerve.modules.FrontLeftSwerveModuleConstants
import org.frc5183.constants.swerve.modules.FrontRightSwerveModuleConstants

object AutoConstants {
    /**
     * The speed limit factor to apply to the robot's translation and rotation speeds/accelerations when driving autonomously.
     */
    const val SPEED_LIMIT_FACTOR = 1.0

    /**
     * The [RobotConfig] to be used in a [com.pathplanner.lib.auto.AutoBuilder].
     */
    val ROBOT_CONFIG =
        RobotConfig(
            PhysicalConstants.MASS,
            PhysicalConstants.MOI,
            ModuleConfig(
                PhysicalConstants.WHEEL_DIAMETER.div(2.0), // radius, not diameter
                PhysicalConstants.MODULE_MAXIMUM_SPEED,
                PhysicalConstants.WHEEL_COF,
                PhysicalConstants.DRIVE_MOTOR_TYPE,
                PhysicalConstants.DRIVE_CURRENT_LIMIT,
                1, // 1 drive motor per module in swerve.
            ),
            FrontLeftSwerveModuleConstants.LOCATION,
            FrontRightSwerveModuleConstants.LOCATION,
            BackLeftSwerveModuleConstants.LOCATION,
            BackRightSwerveModuleConstants.LOCATION,
        )

    /**
     * Speed constraints to follow when driving autonomously.
     */
    val PATH_CONSTRAINTS =
        PathConstraints(
            Units.MetersPerSecond.of(4.0),
            Units.MetersPerSecondPerSecond.of(4.0),
            Units.DegreesPerSecond.of(540.0),
            Units.DegreesPerSecondPerSecond.of(720.0),
            PhysicalConstants.OPTIMAL_VOLTAGE,
        )
}
