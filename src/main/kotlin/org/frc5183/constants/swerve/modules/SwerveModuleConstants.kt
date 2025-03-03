package org.frc5183.constants.swerve.modules

import edu.wpi.first.math.geometry.Translation2d
import edu.wpi.first.units.Units
import edu.wpi.first.units.measure.Angle
import org.frc5183.constants.swerve.SwerveModulePhysicalConstants
import org.frc5183.constants.swerve.SwervePIDConstants
import swervelib.encoders.SwerveAbsoluteEncoder
import swervelib.motors.SwerveMotor
import swervelib.parser.SwerveModuleConfiguration

interface SwerveModuleConstants {
    /**
     * A friendly name to identify the module.
     */
    val NAME: String

    /**
     * The [Translation2d] of the module relative to the center of the robot.
     */
    val LOCATION: Translation2d

    /**
     * The [SwerveAbsoluteEncoder] used to measure the angle of the module.
     */
    val ABSOLUTE_ENCODER: SwerveAbsoluteEncoder

    /**
     * The offset of the absolute encoder from the zero position.
     */
    val ABSOLUTE_ENCODER_OFFSET: Angle

    /**
     * Whether the absolute encoder is inverted.
     */
    val ABSOLUTE_ENCODER_INVERTED: Boolean

    /**
     * The [SwerveMotor] used to drive the module.
     */
    val DRIVE_MOTOR: SwerveMotor

    /**
     * Whether the drive motor is inverted.
     */
    val DRIVE_MOTOR_INVERTED: Boolean

    /**
     * The [SwerveMotor] used to rotate the module.
     */
    val ANGLE_MOTOR: SwerveMotor

    /**
     * Whether the angle motor is inverted.
     */
    val ANGLE_MOTOR_INVERTED: Boolean

    /**
     * A representation of this [SwerveModuleConstants] as a [SwerveModuleConfiguration]
     */
    val YAGSL: SwerveModuleConfiguration
        get() =
            SwerveModuleConfiguration(
                DRIVE_MOTOR,
                ANGLE_MOTOR,
                SwerveModulePhysicalConstants.CONVERSION_FACTORS,
                ABSOLUTE_ENCODER,
                ABSOLUTE_ENCODER_OFFSET.`in`(Units.Degrees),
                LOCATION.x,
                LOCATION.y,
                SwervePIDConstants.ANGLE_PID,
                SwervePIDConstants.DRIVE_PID,
                SwerveModulePhysicalConstants.YAGSL,
                ABSOLUTE_ENCODER_INVERTED,
                DRIVE_MOTOR_INVERTED,
                ANGLE_MOTOR_INVERTED,
                NAME,
                false,
            )
}
