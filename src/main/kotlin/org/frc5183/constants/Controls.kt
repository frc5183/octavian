package org.frc5183.constants

import edu.wpi.first.math.filter.Debouncer
import edu.wpi.first.wpilibj2.command.Command
import edu.wpi.first.wpilibj2.command.CommandScheduler
import edu.wpi.first.wpilibj2.command.InstantCommand
import edu.wpi.first.wpilibj2.command.button.CommandXboxController
import org.frc5183.commands.climber.DriveClimberCommand
import org.frc5183.commands.climber.PullClimberCommand
import org.frc5183.commands.coral.IntakeCoralCommand
import org.frc5183.commands.coral.ShootCoralCommand
import org.frc5183.commands.drive.AimCommand
import org.frc5183.commands.drive.TeleopDriveCommand
import org.frc5183.commands.elevator.*
import org.frc5183.commands.teleop.AutoAimAndShoot
import org.frc5183.math.curve.*
import org.frc5183.subsystems.climber.ClimberSubsystem
import org.frc5183.subsystems.coral.CoralSubsystem
import org.frc5183.subsystems.drive.SwerveDriveSubsystem
import org.frc5183.subsystems.elevator.ElevatorSubsystem
import org.frc5183.subsystems.vision.VisionSubsystem
import org.frc5183.target.FieldTarget
import kotlin.math.abs
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import kotlin.time.DurationUnit

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
    val TRANSLATION_CURVE = LogisticCurve(8.5, 0.6)

    /**
     * The curve applied to the rotation input, among other input filtering (deadband, range clamps, etc.)
     */
    val ROTATION_CURVE = ExponentialCurve(50.0, 35.0)

    /**
     * The curve applied to the climb input, among other input filtering (deadband, range clamps, etc.)
     */
    val CLIMB_CURVE = LinearCurve(1.0, 0.0)

    /**
     * The curve applied to manual elevator control with joystick.
     */
    val ELEVATOR_CURVE = LinearCurve(1.0, 0.0)

    val BUTTON_DEBOUNCE_TIME: Duration = 0.5.seconds

    /**
     * A function to be called during teleop init.
     * Registers all the buttons to their respective commands and the drive command.
     */
    fun teleopInit(
        drive: SwerveDriveSubsystem,
        vision: VisionSubsystem,
        climber: ClimberSubsystem,
        elevator: ElevatorSubsystem,
        coralSubsystem: CoralSubsystem,
    ) {
        CommandScheduler.getInstance().activeButtonLoop.clear()

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
        DRIVER
            .povUp()
            .debounce(
                BUTTON_DEBOUNCE_TIME.toDouble(DurationUnit.SECONDS),
                Debouncer.DebounceType.kFalling,
            ).onTrue(AimCommand(FieldTarget.Pipe, drive, vision))

        DRIVER.x().debounce(BUTTON_DEBOUNCE_TIME.toDouble(DurationUnit.SECONDS), Debouncer.DebounceType.kFalling).onTrue(
            InstantCommand({
                AutoAimAndShoot({ DRIVER.x().asBoolean }, drive, vision).schedule()
            }),
        )

        // Operator Commands Start

        // Coral Commands Start
        OPERATOR
            .y()
            .debounce(
                BUTTON_DEBOUNCE_TIME.toDouble(DurationUnit.SECONDS),
                Debouncer.DebounceType.kFalling,
            ).onTrue(IntakeCoralCommand(coralSubsystem))
        /*
        OPERATOR
            .a()
            .debounce(
                BUTTON_DEBOUNCE_TIME.toDouble(DurationUnit.SECONDS),
                Debouncer.DebounceType.kFalling,
            ).onTrue(ShootCoralCommand(coralSubsystem))

         */

        OPERATOR.a().debounce(BUTTON_DEBOUNCE_TIME.toDouble(DurationUnit.SECONDS), Debouncer.DebounceType.kFalling).onTrue(
            GotoElevatorCommand(elevator)
                .andThen(
                    ShootCoralCommand(coralSubsystem)
                        .raceWith(HoldElevatorCommand(elevator)),
                ), // First correct the elevator, then shoot the coral while holding the elevator.
        )

        // Reset Coral State
        OPERATOR.x().debounce(BUTTON_DEBOUNCE_TIME.toDouble(DurationUnit.SECONDS), Debouncer.DebounceType.kFalling).onTrue(
            InstantCommand({
                coralSubsystem.clearCoral()
            }),
        )

        // Coral Commands End

        // Climber Command Start
        OPERATOR.rightTrigger().whileTrue(PullClimberCommand(climber))
        OPERATOR.rightStick().toggleOnTrue(
            DriveClimberCommand(
                climber,
                input = { OPERATOR.rightY },
                inputCurve =
                    MultiCurve(
                        listOf(
                            PiecewiseCurve(
                                linkedMapOf(
                                    { input: Double -> abs(input) < TRANSLATION_DEADBAND } to NullCurve(), // Apply a deadband
                                    { input: Double -> abs(input) > TRANSLATION_DEADBAND } to CLIMB_CURVE, // Apply our actual curve.
                                ),
                            ),
                            LimitedCurve(-1.0, 1.0), // Clamp the output to [-1, 1]
                        ),
                    ),
            ),
        )

        // Elevator Commands Start

        OPERATOR
            .povUp()
            .debounce(
                BUTTON_DEBOUNCE_TIME.toDouble(DurationUnit.SECONDS),
                Debouncer.DebounceType.kFalling,
            ).onTrue(RaiseElevatorCommand(elevator))
        OPERATOR
            .povDown()
            .debounce(
                BUTTON_DEBOUNCE_TIME.toDouble(DurationUnit.SECONDS),
                Debouncer.DebounceType.kFalling,
            ).onTrue(LowerElevatorCommand(elevator))

        val elevatorDriveCommand =
            DriveElevatorCommand(
                elevator,
                input = { OPERATOR.leftY },
                inputCurve =
                    MultiCurve(
                        listOf(
                            PiecewiseCurve(
                                linkedMapOf(
                                    { input: Double -> abs(input) < ROTATION_DEADBAND } to NullCurve(), // Apply a deadband.
                                    { input: Double -> abs(input) > ROTATION_DEADBAND } to ELEVATOR_CURVE, // Apply our actual curve.
                                ),
                            ),
                            LimitedCurve(-1.0, 1.0), // Clamp the output to [-1, 1]
                        ),
                    ),
            )

        OPERATOR
            .leftStick()
            .debounce(
                BUTTON_DEBOUNCE_TIME.toDouble(DurationUnit.SECONDS),
                Debouncer.DebounceType.kFalling,
            ).toggleOnTrue(elevatorDriveCommand)

        // Elevator Commands Stop

        // Operator Commands End

        DRIVER.b().debounce(BUTTON_DEBOUNCE_TIME.toDouble(DurationUnit.SECONDS), Debouncer.DebounceType.kFalling).onTrue(
            InstantCommand({
                println("Driver cancelled all commands.")
                CommandScheduler.getInstance().cancelAll()
            }),
        )

        OPERATOR.b().debounce(BUTTON_DEBOUNCE_TIME.toDouble(DurationUnit.SECONDS), Debouncer.DebounceType.kFalling).onTrue(
            InstantCommand({
                println("Operator cancelled all commands.")
                CommandScheduler.getInstance().cancelAll()
            }),
        )
    }
}
