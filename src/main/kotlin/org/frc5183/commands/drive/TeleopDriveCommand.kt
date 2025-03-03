package org.frc5183.commands.drive

import edu.wpi.first.math.kinematics.ChassisSpeeds
import edu.wpi.first.units.Units
import edu.wpi.first.wpilibj2.command.Command
import org.frc5183.Robot
import org.frc5183.constants.Controls
import org.frc5183.constants.PhysicalConstants
import org.frc5183.math.curve.Curve
import org.frc5183.subsystems.drive.SwerveDriveSubsystem

/**
 * A command that allows the driver to control the robot's translation and rotation using the joystick.
 */
class TeleopDriveCommand(
    /**
     * The [SwerveDriveSubsystem] to control.
     */
    val drive: SwerveDriveSubsystem,

    /**
     * A function that returns the X translation input for the robot. This should be in the range [-1, 1] and will
     * have [translationCurve] applied, then scaled to [PhysicalConstants.MAX_SPEED].
     */
    val xInput: () -> Double,

    /**
     * A function that returns the Y translation input for the robot. This should be in the range [-1, 1] and will
     * have [translationCurve] applied, then scaled to [PhysicalConstants.MAX_SPEED].
     */
    val yInput: () -> Double,

    /**
     * A function that returns the rotation input for the robot. This should be in the range [-1, 1] and will
     * have [rotationCurve] applied, then scaled to [PhysicalConstants.MAX_ANGULAR_VELOCITY].
     */
    val rotationInput: () -> Double,

    /**
     * The [Curve] to apply to the translation inputs.
     */
    val translationCurve: Curve,

    /**
     * The [Curve] to apply to the rotation input.
     */
    val rotationCurve: Curve,

    /**
     * Whether to use field-relative control.
     */
    val fieldRelative: Boolean
) : Command() {
    init {
        addRequirements(drive)
    }

    override fun initialize() {}

    override fun execute() {
        drive.drive(
            if (fieldRelative) ChassisSpeeds.fromFieldRelativeSpeeds(
                PhysicalConstants.MAX_SPEED * translationCurve.invoke(xInput()),
                PhysicalConstants.MAX_SPEED * translationCurve.invoke(yInput()),
                PhysicalConstants.MAX_ANGULAR_VELOCITY * rotationCurve.invoke(rotationInput()),
                drive.pose.rotation
            ) else ChassisSpeeds.fromRobotRelativeSpeeds(
                PhysicalConstants.MAX_SPEED * translationCurve.invoke(xInput()),
                PhysicalConstants.MAX_SPEED * translationCurve.invoke(yInput()),
                PhysicalConstants.MAX_ANGULAR_VELOCITY * rotationCurve.invoke(rotationInput()),
                drive.pose.rotation
            )
        )
    }

    override fun isFinished(): Boolean = false

    override fun end(interrupted: Boolean) {}
}
