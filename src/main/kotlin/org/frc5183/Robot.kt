package org.frc5183

import com.pathplanner.lib.commands.PathPlannerAuto
import com.pathplanner.lib.path.GoalEndState
import com.pathplanner.lib.path.PathConstraints
import com.pathplanner.lib.path.PathPlannerPath
import com.pathplanner.lib.pathfinding.Pathfinder
import com.pathplanner.lib.pathfinding.Pathfinding
import edu.wpi.first.hal.FRCNetComm.tInstances
import edu.wpi.first.hal.FRCNetComm.tResourceType
import edu.wpi.first.hal.HAL
import edu.wpi.first.math.Pair
import edu.wpi.first.math.geometry.Pose2d
import edu.wpi.first.math.geometry.Rotation2d
import edu.wpi.first.math.geometry.Translation2d
import edu.wpi.first.math.kinematics.ChassisSpeeds
import edu.wpi.first.units.Units
import edu.wpi.first.wpilibj.Threads
import edu.wpi.first.wpilibj.Timer
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import edu.wpi.first.wpilibj.util.WPILibVersion
import edu.wpi.first.wpilibj2.command.Command
import edu.wpi.first.wpilibj2.command.CommandScheduler
import org.frc5183.commands.drive.DriveToPose2d
import org.frc5183.constants.*
import org.frc5183.math.auto.pathfinding.DummyPathfinder
import org.frc5183.math.auto.pathfinding.LocalADStarAK
import org.frc5183.subsystems.drive.SwerveDriveSubsystem
import org.frc5183.subsystems.drive.io.RealSwerveDriveIO
import org.frc5183.subsystems.drive.io.SimulatedSwerveDriveIO
import org.frc5183.subsystems.vision.VisionSubsystem
import org.frc5183.subsystems.vision.io.RealVisionIO
import org.frc5183.subsystems.vision.io.SimulatedVisionIO
import org.littletonrobotics.junction.LogFileUtil
import org.littletonrobotics.junction.LoggedRobot
import org.littletonrobotics.junction.Logger
import org.littletonrobotics.junction.networktables.LoggedDashboardChooser
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

    val simulation: Boolean
        get() = isSimulation()

    val brakeTimer = Timer()

    private var selectedAutoMode = AutoMode.default
    private val autoModeChooser =
        LoggedDashboardChooser<AutoMode>("Auto Mode").also { chooser ->
            AutoMode.entries.forEach { chooser.addOption(it.optionName, it) }
            chooser.addDefaultOption(AutoMode.default.optionName, AutoMode.default)
        }

    /**
     * A enumeration of the available autonomous modes.
     *
     * @param optionName The name for the [autoModeChooser] option.
     * @param periodicFunction The function that is called in the [autonomousPeriodic] function each time it is called.
     * @param autoInitFunction An optional function that is called in the [autonomousInit] function.
     */
    private enum class AutoMode(
        val optionName: String,
        val autoInitFunction: (() -> Unit)?,
        val periodicFunction: (() -> Unit)? = null,
    ) {
        CUSTOM_AUTO_1("Custom Auto Mode 1", ::autoMode1),
        CUSTOM_AUTO_2("Custom Auto Mode 2", ::autoMode2),
        ;

        companion object {
            /** The default auto mode. */
            val default = CUSTOM_AUTO_1
        }
    }

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


        if (State.mode != State.Mode.REAL) Logger.start() // todo: we don't have enough RAM to log on the RIO v1.0

        // Set the pathfinder to use the LocalADStarAK pathfinder so that
        //  we can use the AdvantageKit replay logging.
        Pathfinding.setPathfinder(DummyPathfinder("Example Path.path"))

        vision =
            VisionSubsystem(
                if (State.mode ==
                    State.Mode.REAL
                ) {
                    RealVisionIO(listOf())
                } else {
                    SimulatedVisionIO(listOf())
                },
            )

        drive = SwerveDriveSubsystem(if (State.mode == State.Mode.REAL) RealSwerveDriveIO() else SimulatedSwerveDriveIO(), vision)

        CommandScheduler.getInstance().registerSubsystem(
            vision,
            drive,
        )

        SmartDashboard.putData("Auto choices", autoModeChooser.sendableChooser)

        // todo: debug
        CommandScheduler.getInstance().onCommandInitialize {
            println("Command initialized: ${it.name}")
        }

        CommandScheduler.getInstance().onCommandExecute {
            if (it.name != "TeleopDriveCommand") {
                println("Command executed: ${it.name}")
            }
        }

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
        selectedAutoMode = autoModeChooser.get() ?: AutoMode.default
        println("Selected auto mode: ${selectedAutoMode.optionName}")
        selectedAutoMode.autoInitFunction?.invoke()
    }

    override fun autonomousPeriodic() {
        selectedAutoMode.periodicFunction?.invoke()
    }

    private fun autoMode1() {
        DriveToPose2d(Pose2d(15.0, 3.0, Rotation2d(0.0, 0.0)), drive).schedule()
        /*
        val pose = Pose2d(15.0, 3.0, Rotation2d(0.0, 0.0))

        AutoBuilder
            .pathfindThenFollowPath(
                PathPlannerPath(
                    PathPlannerPath.waypointsFromPoses(
                        Pose2d(SwerveDriveSubsystem.pose.translation, Rotation2d.kZero),
                        Pose2d(pose.translation, Rotation2d.kZero),
                    ),
                    AutoConstants.PATH_CONSTRAINTS,
                    null,
                    GoalEndState(0.0, pose.rotation),
                ),
                AutoConstants.PATH_CONSTRAINTS,
            ).schedule()
         */
    }

    private fun autoMode2() {
        PathPlannerAuto("Auto 2").schedule()
    }

    /** This method is called once when teleop is enabled.  */
    override fun teleopInit() {
        CommandScheduler.getInstance().cancelAll()
        Controls.teleopInit(drive, vision) // Register all teleop controls.

        // todo debug sets the pose2d to into the field in sim
        drive.resetPose(Pose2d(3.0, 2.0, Rotation2d(0.0, 0.0)))
    }

    /** This method is called periodically during operator control.  */
    override fun teleopPeriodic() {
        Controls.teleopPeriodic()
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
