package org.frc5183

import com.pathplanner.lib.auto.AutoBuilder
import com.pathplanner.lib.auto.NamedCommands
import com.revrobotics.ColorSensorV3
import com.revrobotics.spark.SparkMax
import edu.wpi.first.hal.FRCNetComm.tInstances
import edu.wpi.first.hal.FRCNetComm.tResourceType
import edu.wpi.first.hal.HAL
import edu.wpi.first.wpilibj.DigitalInput
import edu.wpi.first.wpilibj.Threads
import edu.wpi.first.wpilibj.Timer
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import edu.wpi.first.wpilibj.util.WPILibVersion
import edu.wpi.first.wpilibj2.command.Command
import edu.wpi.first.wpilibj2.command.CommandScheduler
import edu.wpi.first.wpilibj2.command.InstantCommand
import org.frc5183.commands.coral.IntakeCoralCommand
import org.frc5183.commands.coral.ShootCoralCommand
import org.frc5183.commands.elevator.GotoElevatorCommand
import org.frc5183.commands.elevator.HoldElevatorCommand
import org.frc5183.commands.elevator.LowerElevatorCommand
import org.frc5183.commands.elevator.RaiseElevatorCommand
import org.frc5183.constants.*
import org.frc5183.subsystems.climber.ClimberSubsystem
import org.frc5183.subsystems.climber.io.RealClimberIO
import org.frc5183.subsystems.coral.CoralSubsystem
import org.frc5183.subsystems.coral.io.RealCoralIO
import org.frc5183.subsystems.drive.SwerveDriveSubsystem
import org.frc5183.subsystems.drive.io.RealSwerveDriveIO
import org.frc5183.subsystems.drive.io.SimulatedSwerveDriveIO
import org.frc5183.subsystems.elevator.ElevatorSubsystem
import org.frc5183.subsystems.elevator.io.RealElevatorIO
import org.frc5183.subsystems.vision.VisionSubsystem
import org.frc5183.subsystems.vision.io.RealVisionIO
import org.frc5183.subsystems.vision.io.SimulatedVisionIO
import org.littletonrobotics.junction.LogFileUtil
import org.littletonrobotics.junction.LoggedRobot
import org.littletonrobotics.junction.Logger
import org.littletonrobotics.junction.networktables.NT4Publisher
import org.littletonrobotics.junction.wpilog.WPILOGReader
import org.littletonrobotics.junction.wpilog.WPILOGWriter

/**
 * The functions in this object (which basically functions as a singleton class) are called automatically
 * corresponding to each mode, as described in the TimedRobot documentation.
 * This is written as an object rather than a class since there should only ever be a single instance, and
 * it cannot take any constructor arguments. This makes it a natural fit to be an object in Kotlin.
 *
 * If you change the name of this object or its package after creating this project, you must also update
 * the `Main.kt` file in the project. (If you use the IDE's Rename or Move refactorings when renaming the
 * object or package, it will get changed everywhere.)
 */
object Robot : LoggedRobot() {
    private val vision: VisionSubsystem
    private val drive: SwerveDriveSubsystem
    private val climber: ClimberSubsystem
    private val elevator: ElevatorSubsystem
    private val coralSubsystem: CoralSubsystem

    val simulation: Boolean
        get() = isSimulation()

    /**
     * A timer to keep track of how long the robot has been disabled for so that
     * we can disable brake mode after a little bit of time.
     */
    private val brakeTimer = Timer()

    private val autoChooser: SendableChooser<Command>

    init
    {
        // Report usage of Kotlin and AdvantageKit
        HAL.report(tResourceType.kResourceType_Language, tInstances.kLanguage_Kotlin, 0, WPILibVersion.Version)
        HAL.report(tResourceType.kResourceType_Framework, tInstances.kFramework_AdvantageKit)

        // If these don't resolve then build or run the createVersionFile gradle task.
        Logger.recordMetadata("ProjectName", MAVEN_NAME)
        Logger.recordMetadata("BuildDate", BUILD_DATE)
        Logger.recordMetadata("GitSHA", GIT_SHA)
        Logger.recordMetadata("GitDate", GIT_DATE)
        Logger.recordMetadata("GitBranch", GIT_BRANCH)
        when (DIRTY) {
            0 -> Logger.recordMetadata("GitDirty", "All changes committed")
            1 -> Logger.recordMetadata("GitDirty", "Uncommitted changes")
            else -> Logger.recordMetadata("GitDirty", "Unknown")
        }

        when (State.mode) {
            State.Mode.REAL -> {
                Logger.addDataReceiver(WPILOGWriter())
                Logger.addDataReceiver(NT4Publisher())
            }
            State.Mode.SIM -> {
                // Logger.addDataReceiver(WPILOGWriter()) // Only enable when needed, can generate tons of logs when doing rapid iteration/testing.
                Logger.addDataReceiver(NT4Publisher())
            }
            State.Mode.REPLAY -> {
                setUseTiming(false)
                val logPath = LogFileUtil.findReplayLog()
                Logger.setReplaySource(WPILOGReader(logPath))
                Logger.addDataReceiver(WPILOGWriter(LogFileUtil.addPathSuffix(logPath, "_sim")))
            }
        }

        // if (State.mode != State.Mode.REAL) Logger.start() // todo: we don't have enough RAM to log on the RIO v1.0
        Logger.start()

        // Set the pathfinder to use the LocalADStarAK pathfinder so that
        //  we can use the AdvantageKit replay logging.

        vision =
            VisionSubsystem(
                if (State.mode ==
                    State.Mode.REAL
                ) {
                    RealVisionIO(listOf(DeviceConstants.FRONT_CAMERA))
                } else {
                    SimulatedVisionIO(listOf())
                },
            )

        drive = SwerveDriveSubsystem(if (State.mode == State.Mode.REAL) RealSwerveDriveIO() else SimulatedSwerveDriveIO(), vision)

        climber =
            ClimberSubsystem(
                RealClimberIO(
                    SparkMax(
                        DeviceConstants.CLIMBER_CAN,
                        DeviceConstants.CLIMBER_MOTOR_TYPE,
                    ),
                    DigitalInput(
                        DeviceConstants.CLIMBER_LIMIT_SWITCH_ID,
                    ),
                ),
            ) // todo: simulate this io

        elevator =
            ElevatorSubsystem(
                RealElevatorIO(
                    SparkMax(
                        DeviceConstants.ELEVATOR_MOTOR_ID,
                        DeviceConstants.ELEVATOR_MOTOR_TYPE,
                    ),
                    DigitalInput(DeviceConstants.ELEVATOR_BOTTOM_LIMIT_SWITCH_ID),
                    DigitalInput(DeviceConstants.ELEVATOR_TOP_LIMIT_SWITCH_ID),
                ),
            )

        coralSubsystem =
            CoralSubsystem(
                RealCoralIO(
                    SparkMax(DeviceConstants.CORAL_MOTOR_ID, DeviceConstants.CORAL_MOTOR_TYPE),
                    ColorSensorV3(DeviceConstants.CORAL_COLOR_SENSOR_PORT),
                ),
                elevator,
            )

        CommandScheduler.getInstance().registerSubsystem(
            vision,
            drive,
            climber,
            elevator,
            coralSubsystem,
        )

        if (State.mode == State.Mode.REAL) {
            NamedCommands.registerCommands(
                mapOf(
                    "Shoot Coral" to ShootCoralCommand(coralSubsystem),
                    "Intake Coral" to IntakeCoralCommand(coralSubsystem),
                    "Raise Elevator" to RaiseElevatorCommand(elevator),
                    "Lower Elevator" to LowerElevatorCommand(elevator),
                    "Goto Elevator" to GotoElevatorCommand(elevator),
                    "Hold Elevator" to HoldElevatorCommand(elevator),
                ),
            )
        } else {
            NamedCommands.registerCommands(
                mapOf(
                    "Shoot Coral" to InstantCommand({ println("Shoot Coral") }),
                    "Intake Coral" to InstantCommand({ println("Intake Coral") }),
                    "Raise Elevator" to InstantCommand({ println("Raise Elevator") }),
                    "Lower Elevator" to InstantCommand({ println("Lower Elevator") }),
                    "Goto Elevator" to InstantCommand({ println("Correct Elevator") }),
                    "Hold Elevator" to InstantCommand({ println("Hold Elevator") }),
                ),
            )
        }

        autoChooser = AutoBuilder.buildAutoChooser()
        SmartDashboard.putData("Auto choices", autoChooser)

        // todo: debug
        CommandScheduler.getInstance().onCommandInitialize {
            println("Command initialized: ${it.name}")
        }

        /**
         CommandScheduler.getInstance().onCommandExecute {
         if (it.name != "TeleopDriveCommand") {
         println("Command executed: ${it.name}")
         }
         }
         */

        CommandScheduler.getInstance().onCommandFinish {
            println("Command finished: ${it.name}")
        }

        CommandScheduler.getInstance().onCommandInterrupt { it: Command ->
            println("Command interrupted: ${it.name}")
        }
        // end todo
    }

    override fun robotPeriodic() {
        // Wrap the command scheduler in a high priority thread.
        //  (thanks AdvantageKit template)
        Threads.setCurrentThreadPriority(true, 99)

        CommandScheduler.getInstance().run()

        Threads.setCurrentThreadPriority(false, 10)
    }

    override fun autonomousInit() {
        CommandScheduler.getInstance().cancelAll()
        autoChooser.selected.schedule()
    }

    override fun autonomousPeriodic() {
        // Do Nothing
    }

    /** This method is called once when teleop is enabled.  */
    override fun teleopInit() {
        CommandScheduler.getInstance().cancelAll()
        Controls.teleopInit(drive, vision, climber, elevator, coralSubsystem) // Register all teleop controls.
    }

    /** This method is called periodically during operator control.  */
    override fun teleopPeriodic() {
    }

    /** This method is called once when the robot is disabled.  */
    override fun disabledInit() {
        CommandScheduler.getInstance().cancelAll()

        drive.setMotorBrake(true)
        brakeTimer.reset()
        brakeTimer.start()
    }

    /** This method is called periodically when disabled.  */
    override fun disabledPeriodic() {
        if (brakeTimer.advanceIfElapsed(Config.BREAK_TIME_AFTER_DISABLE.inWholeSeconds.toDouble())) {
            drive.setMotorBrake(false)
            brakeTimer.stop()
            brakeTimer.reset()
        }
    }

    /** This method is called once when test mode is enabled.  */
    override fun testInit() {}

    /** This method is called periodically during test mode.  */
    override fun testPeriodic() {}

    /** This method is called once when the robot is first started up.  */
    override fun simulationInit() {}

    /** This method is called periodically whilst in simulation.  */
    override fun simulationPeriodic() {}
}
