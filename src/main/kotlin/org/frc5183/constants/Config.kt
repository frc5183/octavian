package org.frc5183.constants

import edu.wpi.first.apriltag.AprilTagFieldLayout
import edu.wpi.first.apriltag.AprilTagFields
import edu.wpi.first.units.Units
import edu.wpi.first.units.measure.Angle
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

/**
 * Miscellaneous configuration constants.
 */
object Config {
    val ELEVATOR_STAGES: List<Angle> =
        listOf(
            Units.Rotations.of(0.0), // Bottom
            Units.Rotations.of(10.0), // Trough (L1)
            Units.Rotations.of(20.0), // L2 Branch
            Units.Rotations.of(30.0), // L3 Branch
            Units.Rotations.of(90.0), // L4 Branch
        )
    val ELEVATOR_MAX_ALLOWED_DRIFT: Angle = Units.Degrees.of(1.0)
    const val ELEVATOR_MOTOR_INVERTED: Boolean = true
    const val ELEVATOR_MOVEMENT_SPEED = 0.3
    const val ELEVATOR_HOLD_SPEED = 0.05

    val CORAL_SHOOT_TIME: Duration = 1.seconds
    const val CORAL_PROXIMITY_THRESHOLD: Int = 200

    /**
     * The amount of time to continue running the intake motor for after
     * the coral has been detected.
     */
    val CORAL_INTAKE_TIME: Duration = 1.seconds
    const val CORAL_INTAKE_SPEED: Double = 0.5
    const val CORAL_SHOOT_SPEED: Double = 1.0

    const val VISION_POSE_ESTIMATION = false
    val FIELD_LAYOUT: AprilTagFieldLayout = AprilTagFieldLayout.loadField(AprilTagFields.k2025ReefscapeWelded)
    val BREAK_TIME_AFTER_DISABLE: Duration = 1.seconds
}
