package org.frc5183.subsystems.drive.io

import edu.wpi.first.math.Matrix
import edu.wpi.first.math.geometry.Pose2d
import edu.wpi.first.math.geometry.Translation2d
import edu.wpi.first.math.kinematics.ChassisSpeeds
import edu.wpi.first.math.kinematics.SwerveModuleState
import edu.wpi.first.math.numbers.N1
import edu.wpi.first.math.numbers.N3
import org.frc5183.log.AutoLogInputs

interface SwerveDriveIO {
    class SwerveDriveIOInputs : AutoLogInputs() {
        var pose by log(Pose2d())
        var velocity by log(ChassisSpeeds())
        var moduleStates by log(arrayOf<SwerveModuleState>())
        var moduleSpeeds by log(doubleArrayOf())
    }

    fun updateInputs(inputs: SwerveDriveIOInputs)

    /**
     * The [Pose2d] of the robot according to swerve drive odometry and vision (if enabled).
     */
    val pose: Pose2d

    /**
     * The [ChassisSpeeds] of the robot according to swerve drive odometry.
     */
    val robotVelocity: ChassisSpeeds

    /**
     * Stops the swerve drive's odometry thread.
     * If you do this you need to manually call [updateOdometry]
     */
    fun stopOdometryThread()

    /**
     * Adds a vision measurement to the swerve drive.
     * @param pose The pose to add.
     * @param timestampSeconds The timestamp of the measurement.
     * @param standardDeviations The standard deviations of the measurement.
     */
    fun addVisionMeasurement(
        pose: Pose2d,
        timestampSeconds: Double,
        standardDeviations: Matrix<N3, N1>,
    )

    /**
     * Updates the robot's odometry using YAGSL.
     * This should only be run when the odometry thread is stopped (which should
     * only be when vision pose estimation is enabled).
     */
    fun updateOdometry()

    /**
     * Sets the drive motors to brake/coast mode.
     * @param brake Whether to set the motors to brake mode (true) or coast mode (false).
     */
    fun setMotorBrake(brake: Boolean)

    /**
     * Resets the robot's pose to [pose].
     */
    fun resetPose(pose: Pose2d = Pose2d.kZero)

    /**
     * Drives the robot with the given translation and rotation.
     * @param translation The translation to drive with.
     * @param rotation The rotation to drive with.
     * @param fieldOriented Whether the translation is field-oriented.
     * @param openLoop Whether the drive is open-loop.
     */
    fun drive(
        translation: Translation2d,
        rotation: Double,
        fieldOriented: Boolean,
        openLoop: Boolean,
    )

    /**
     * Drives the robot with the given chassis speeds.
     * @param speeds The chassis speeds to drive with.
     */
    fun drive(speeds: ChassisSpeeds)
}
