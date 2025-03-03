package org.frc5183.constants

import edu.wpi.first.math.system.plant.DCMotor
import edu.wpi.first.units.*
import edu.wpi.first.units.measure.*

object PhysicalConstants {
    /**
     * The [Mass] of the robot.
     */
    val MASS: Mass = Units.Pounds.of(80.0) // todo: this was taken before the robot was fully built

    /**
     * The [MomentOfInertia] of the robot.
     */
    val MOI: MomentOfInertia = Units.KilogramSquareMeters.of(6.883)

    // <editor-fold desc="Maximum Constraints">
    // todo: all constraints
    /**
     * The maximum [LinearVelocity] of the robot.
     */
    val MAX_SPEED: LinearVelocity = Units.MetersPerSecond.of(5.0)

    /**
     * The maximum [LinearAcceleration] of the robot.
     */
    val MAX_ACCELERATION: LinearAcceleration = Units.MetersPerSecondPerSecond.of(8.0)

    /**
     * The maximum [AngularVelocity] of the robot.
     */
    val MAX_ANGULAR_VELOCITY: AngularVelocity = Units.DegreesPerSecond.of(180.0)

    /**
     * The maximum [AngularAcceleration] of the robot.
     */
    val MAX_ANGULAR_ACCELERATION: AngularAcceleration = Units.DegreesPerSecondPerSecond.of(360.0)

    /**
     * A measure of [Voltage] over [Time] the swerve module's drive motor can ramp at.
     */
    // todo: find a way to use PerUnit to represent ramp rates better.
    const val DRIVE_MOTOR_RAMP_RATE = 0.25
    // val DRIVE_MOTOR_RAMP_RATE: Measure<out PerUnit<VoltageUnit, TimeUnit>> = PerUnit.combine(Units.Volts, Units.Seconds).of(0.25)

    /**
     * A measure of [Voltage] over [Time] the swerve module's angle motor can ramp at.
     */
    const val ANGLE_MOTOR_RAMP_RATE = 0.25
    // val ANGLE_MOTOR_RAMP_RATE: Measure<out PerUnit<VoltageUnit, TimeUnit>> = PerUnit.combine(Units.Volts, Units.Seconds).of(0.25)

    // </editor-fold>

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
}
