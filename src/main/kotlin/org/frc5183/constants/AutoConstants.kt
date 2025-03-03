package org.frc5183.constants

import com.pathplanner.lib.config.ModuleConfig
import com.pathplanner.lib.config.RobotConfig
import com.pathplanner.lib.path.PathConstraints
import edu.wpi.first.units.Units
import org.frc5183.constants.swerve.modules.BackLeftSwerveModuleConstants
import org.frc5183.constants.swerve.modules.BackRightSwerveModuleConstants
import org.frc5183.constants.swerve.modules.FrontLeftSwerveModuleConstants
import org.frc5183.constants.swerve.modules.FrontRightSwerveModuleConstants

object AutoConstants {
    /**
     * The speed limit factor to apply to the robot's translation and rotation speeds/accelerations when driving autonomously.
     */
    // todo: should we find a way to use this with curves? it might confuse pathplanner, but a basic limiter curve might work fine.
    const val SPEED_LIMIT_FACTOR = 0.75

    /**
     * The [RobotConfig] to be used in a [com.pathplanner.lib.auto.AutoBuilder].
     */
    val ROBOT_CONFIG =
        RobotConfig(
            PhysicalConstants.MASS,
            PhysicalConstants.MOI,
            ModuleConfig(
                PhysicalConstants.WHEEL_DIAMETER.div(2.0), // radius, not diameter
                Units.MetersPerSecond.of(Units.RadiansPerSecond.of(PhysicalConstants.DRIVE_MOTOR_TYPE.freeSpeedRadPerSec).`in`(Units.RotationsPerSecond).times(60).times(PhysicalConstants.DRIVE_GEAR_RATIO).times(PhysicalConstants.WHEEL_DIAMETER.`in`(Units.Meters))),
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
            PhysicalConstants.MAX_SPEED * SPEED_LIMIT_FACTOR,
            PhysicalConstants.MAX_ACCELERATION * SPEED_LIMIT_FACTOR,
            PhysicalConstants.MAX_ANGULAR_VELOCITY * SPEED_LIMIT_FACTOR,
            PhysicalConstants.MAX_ANGULAR_ACCELERATION * SPEED_LIMIT_FACTOR,
            PhysicalConstants.OPTIMAL_VOLTAGE
        )
}
