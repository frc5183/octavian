package org.frc5183.constants

import edu.wpi.first.units.Units
import edu.wpi.first.units.measure.Time
import edu.wpi.first.wpilibj.event.EventLoop
import edu.wpi.first.wpilibj2.command.Command
import edu.wpi.first.wpilibj2.command.CommandScheduler
import edu.wpi.first.wpilibj2.command.InstantCommand
import edu.wpi.first.wpilibj2.command.button.CommandXboxController
import org.frc5183.commands.drive.AimCommand
import org.frc5183.commands.drive.TeleopDriveCommand
import org.frc5183.commands.teleop.AutoAimAndShoot
import org.frc5183.math.curve.*
import org.frc5183.subsystems.drive.SwerveDriveSubsystem
import org.frc5183.subsystems.vision.VisionSubsystem
import org.frc5183.target.FieldTarget
import kotlin.math.abs

/**
 * An object representing the controls of the robot.
 */
object Controls {
    val DRIVER = CommandXboxController(0)
    val OPERATOR = CommandXboxController(1)

    lateinit var TELEOP_DRIVE_COMMAND: Command

    /**
     * The deadband applied to the translation inputs, where absolute values less than this are considered zero.
     */
    val TRANSLATION_DEADBAND = 0.1

    /**
     * The deadband applied to the rotation input, where absolute values less than this are considered zero.
     */
    val ROTATION_DEADBAND = 0.1

    /**
     * The curve applied to the translation inputs, among other input filtering (deadband, range clamps, etc.)
     */
    val TRANSLATION_CURVE = LinearCurve(1.0, 0.0)

    /**
     * The curve applied to the rotation input, among other input filtering (deadband, range clamps, etc.)
     */
    val ROTATION_CURVE = ExponentialCurve(50.0)

    val BUTTON_DEBOUNCE_TIME: Time = Units.Seconds.of(1.0)
    val CONTROLS_EVENT_LOOP = EventLoop()

    /**
     * A function to be called during teleop init.
     * Registers all the buttons to their respective commands and the drive command.
     */
    fun teleopInit(
        drive: SwerveDriveSubsystem,
        vision: VisionSubsystem,
    ) {
        TELEOP_DRIVE_COMMAND =
            TeleopDriveCommand(
                drive,
                xInput = { DRIVER.leftX },
                yInput = { DRIVER.leftY },
                rotationInput = { DRIVER.rightX },
                translationCurve =
                    MultiCurve(
                        listOf(
                            PiecewiseCurve(
                                linkedMapOf(
                                    { input: Double -> abs(input) < TRANSLATION_DEADBAND } to NullCurve(), // Apply a deadband
                                    { input: Double -> abs(input) > TRANSLATION_DEADBAND } to TRANSLATION_CURVE, // Apply our actual curve.
                                ),
                            ),
                            LimitedCurve(-1.0, 1.0), // Clamp the output to [-1, 1]
                        ),
                    ),
                rotationCurve =
                    MultiCurve(
                        listOf(
                            PiecewiseCurve(
                                linkedMapOf(
                                    { input: Double -> abs(input) < ROTATION_DEADBAND } to NullCurve(), // Apply a deadband
                                    { input: Double -> abs(input) > ROTATION_DEADBAND } to ROTATION_CURVE, // Apply our actual curve.
                                ),
                            ),
                            LimitedCurve(-1.0, 1.0), // Clamp the output to [-1, 1]
                        ),
                    ),
                fieldRelative = true,
            )

        drive.defaultCommand = TELEOP_DRIVE_COMMAND

        // D-PAD Up
        DRIVER.povUp().debounce(BUTTON_DEBOUNCE_TIME.`in`(Units.Seconds)).onTrue(AimCommand(FieldTarget.Pipe, drive, vision))

        DRIVER.x().debounce(2.0).onTrue(
            InstantCommand({
                AutoAimAndShoot({ DRIVER.x().asBoolean }, drive, vision).schedule()
            }),
        )

        DRIVER.b().debounce(2.0).onTrue(
            InstantCommand({
                println("Driver cancelled all commands.")
                CommandScheduler.getInstance().cancelAll()
            }),
        )

        OPERATOR.b().debounce(2.0).onTrue(
            InstantCommand({
                println("Operator cancelled all commands.")
                CommandScheduler.getInstance().cancelAll()
            }),
        )
    }

    /**
     * A function to be called during teleop periodic.
     * Mainly for polling the [CONTROLS_EVENT_LOOP].
     */
    fun teleopPeriodic() {
        CONTROLS_EVENT_LOOP.poll()
    }
}
