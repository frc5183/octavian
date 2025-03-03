package org.frc5183.subsystems.drive

import com.pathplanner.lib.auto.AutoBuilder
import com.pathplanner.lib.commands.PathfindingCommand
import com.pathplanner.lib.config.PIDConstants
import com.pathplanner.lib.controllers.PPHolonomicDriveController
import edu.wpi.first.math.geometry.Pose2d
import edu.wpi.first.math.geometry.Translation2d
import edu.wpi.first.math.kinematics.ChassisSpeeds
import edu.wpi.first.wpilibj.DriverStation
import edu.wpi.first.wpilibj.DriverStation.Alliance
import edu.wpi.first.wpilibj2.command.Subsystem
import org.frc5183.constants.*
import org.frc5183.constants.swerve.SwervePIDConstants
import org.frc5183.subsystems.drive.io.SwerveDriveIO
import org.frc5183.subsystems.vision.VisionSubsystem
import org.littletonrobotics.junction.Logger
import swervelib.telemetry.SwerveDriveTelemetry
import kotlin.jvm.optionals.getOrNull

class SwerveDriveSubsystem(
    val io: SwerveDriveIO,
    val vision: VisionSubsystem? = null,
) : Subsystem {
    private val ioInputs: SwerveDriveIO.SwerveDriveIOInputs = SwerveDriveIO.SwerveDriveIOInputs()

    val pose: Pose2d
        get() = io.pose

    val robotVelocity: ChassisSpeeds
        get() = io.robotVelocity

    init {
        SwerveDriveTelemetry.verbosity = LoggingConstants.SWERVE_VERBOSITY

        /*
        AutoBuilder.configure(
            { pose },
            this::resetPose,
            { robotVelocity },
            this::drive,
            PPHolonomicDriveController(
                PIDConstants(
                    SwervePIDConstants.DRIVE_PID.p,
                    SwervePIDConstants.DRIVE_PID.i,
                    SwervePIDConstants.DRIVE_PID.d,
                ),
                PIDConstants(
                    SwervePIDConstants.ANGLE_PID.p,
                    SwervePIDConstants.ANGLE_PID.i,
                    SwervePIDConstants.ANGLE_PID.d,
                ),
            ),
            AutoConstants.ROBOT_CONFIG,
            { DriverStation.getAlliance().getOrNull() == Alliance.Red },
            this,
            vision,
        )

         */

        // https://pathplanner.dev/pplib-follow-a-single-path.html#java-warmup
        PathfindingCommand.warmupCommand().schedule()

        if (Config.VISION_POSE_ESTIMATION && vision != null) {
            io.stopOdometryThread()
        }
    }

    override fun periodic() {
        io.updateInputs(ioInputs)
        Logger.processInputs("Swerve", ioInputs)

        if (Config.VISION_POSE_ESTIMATION && vision != null) {
            io.updateOdometry()
            updatePoseEstimationWithVision(vision)
            vision.updateRobotPose(pose)
        }
    }

    private fun updatePoseEstimationWithVision(vision: VisionSubsystem) {
        for (camera in vision.cameras) {
            val estimatedPose = vision.getEstimatedRobotPose(camera) ?: continue

            io.addVisionMeasurement(
                estimatedPose.estimatedPose.toPose2d(),
                estimatedPose.timestampSeconds,
                camera.currentStandardDeviations,
            )
        }
    }

    /**
     * Sets the drive motors to brake/coast mode.
     * @param brake Whether to set the motors to brake mode (true) or coast mode (false).
     */
    fun setMotorBrake(brake: Boolean) = io.setMotorBrake(brake)

    /**
     * Resets the robot's pose to [pose].
     * @param pose The pose to reset the odometry to.
     */
    fun resetPose(pose: Pose2d = Pose2d.kZero) = io.resetPose(pose)

    fun drive(
        translation: Translation2d,
        rotation: Double,
        fieldOriented: Boolean,
        openLoop: Boolean,
    ) = io.drive(translation, rotation, fieldOriented, openLoop)

    fun drive(speeds: ChassisSpeeds) = io.drive(speeds)
}
