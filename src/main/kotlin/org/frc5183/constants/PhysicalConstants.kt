package org.frc5183.constants

import edu.wpi.first.math.system.plant.DCMotor
import edu.wpi.first.units.*
import edu.wpi.first.units.measure.*

object PhysicalConstants {
    /**
     * The [Mass] of the robot.
     */
    val MASS: Mass = Units.Kilograms.of(35.0) // todo: this was taken before the robot was fully built

    /**
     * The [MomentOfInertia] of the robot.
     */
    val MOI: MomentOfInertia = Units.KilogramSquareMeters.of(6.883)

    /**
     * A measure of [Voltage] over [Time] the swerve module's drive motor can ramp at.
     */
    const val DRIVE_MOTOR_RAMP_RATE = 0.25

    /**
     * A measure of [Voltage] over [Time] the swerve module's angle motor can ramp at.
     */
    const val ANGLE_MOTOR_RAMP_RATE = 0.25

    /**
     * The type of [DCMotor] used for each swerve module's drive motor.
     */
    val DRIVE_MOTOR_TYPE: DCMotor = DCMotor.getKrakenX60(1)

    /**
     * The type of [DCMotor] used for each swerve module's angle motor.
     */
    val ANGLE_MOTOR_TYPE: DCMotor = DCMotor.getKrakenX60(1)

    /**
     * The minimum [Voltage] each swerve module's drive motor can run at.
     */
    val DRIVE_MINIMUM_VOLTAGE: Voltage = Units.Millivolts.of(300.0)

    /**
     * The minimum [Voltage] each swerve module's angle motor can run at.
     */
    val ANGLE_MINIMUM_VOLTAGE: Voltage = Units.Millivolts.of(200.0)

    /**
     * The rotational inertia of each swerve module's drive motor.
     */
    val STEER_ROTATIONAL_INERTIA: MomentOfInertia = Units.KilogramSquareMeters.of(0.03)

    /**
     * The optimal [Voltage] the robot should run at.
     */
    val OPTIMAL_VOLTAGE: Voltage = Units.Volts.of(12.0)

    /**
     * The gear ratio of each swerve module's angle motor.
     */
    const val ANGLE_GEAR_RATIO: Double = 18.75

    /**
     * The gear ratio of each swerve module's drive motor.
     */
    const val DRIVE_GEAR_RATIO: Double = 5.9

    /**
     * The [Current] limit of each swerve module's angle motor.
     */
    val ANGLE_CURRENT_LIMIT: Current = Units.Amps.of(30.0)

    /**
     * The [Current] limit of each swerve module's drive motor.
     */
    val DRIVE_CURRENT_LIMIT: Current = Units.Amps.of(40.0)

    /**
     * The coefficient of friction of each swerve module's wheels.
     */
    const val WHEEL_COF: Double = 1.19

    /**
     * The [Distance] of each swerve module's wheel diameter.
     */
    val WHEEL_DIAMETER: Distance = Units.Inches.of(4.0)

    // <editor-fold desc="Maximum Constraints">

    /**
     * The maximum [LinearVelocity] of the robot.
     */
    val MAX_SPEED: LinearVelocity =
        /*
         * (W/G) * (π*d)
         * where W = free speed (rev/s), G = gear ratio, d = wheel diameter (meters)
         */
        Units.MetersPerSecond.of(
            (
                Units.RadiansPerSecond
                    .of(DRIVE_MOTOR_TYPE.freeSpeedRadPerSec)
                    .`in`(Units.RevolutionsPerSecond)
                    .div(DRIVE_GEAR_RATIO)
            ) *
                (Math.PI * WHEEL_DIAMETER.`in`(Units.Meters)),
        )

    /**
     * The maximum [LinearAcceleration] of the robot.
     */
    val MAX_ACCELERATION: LinearAcceleration = Units.MetersPerSecondPerSecond.of(8.0)

    /**
     * The maximum [AngularVelocity] of the robot.
     */
    val MAX_ANGULAR_VELOCITY: AngularVelocity = Units.DegreesPerSecond.of(360.0)

    /**
     * The maximum [AngularAcceleration] of the robot.
     */
    val MAX_ANGULAR_ACCELERATION: AngularAcceleration = Units.DegreesPerSecondPerSecond.of(360.0)

    // </editor-fold>
}
