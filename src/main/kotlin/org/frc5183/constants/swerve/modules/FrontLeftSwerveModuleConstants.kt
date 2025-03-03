package org.frc5183.constants.swerve.modules

import edu.wpi.first.math.geometry.Translation2d
import edu.wpi.first.units.Units
import edu.wpi.first.units.measure.Angle
import org.frc5183.constants.PhysicalConstants
import swervelib.encoders.CANCoderSwerve
import swervelib.encoders.SwerveAbsoluteEncoder
import swervelib.motors.SwerveMotor
import swervelib.motors.TalonFXSwerve

object FrontLeftSwerveModuleConstants : SwerveModuleConstants {
    override val NAME: String
        = "frontleft"

    override val LOCATION: Translation2d
        = Translation2d(Units.Inches.of(12.25), Units.Inches.of(15.0))

    override val ABSOLUTE_ENCODER: SwerveAbsoluteEncoder
        = CANCoderSwerve(10)

    override val ABSOLUTE_ENCODER_OFFSET: Angle
        = Units.Degrees.of(266.045)

    override val ABSOLUTE_ENCODER_INVERTED: Boolean
        = false

    override val DRIVE_MOTOR: SwerveMotor
        = TalonFXSwerve(11, true, PhysicalConstants.DRIVE_MOTOR_TYPE)

    override val DRIVE_MOTOR_INVERTED: Boolean
        = false

    override val ANGLE_MOTOR: SwerveMotor
        = TalonFXSwerve(12, false, PhysicalConstants.ANGLE_MOTOR_TYPE)

    override val ANGLE_MOTOR_INVERTED: Boolean
        = false
}
