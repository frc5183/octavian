package org.frc5183.subsystems.elevator

import edu.wpi.first.units.Units
import edu.wpi.first.units.measure.Angle
import edu.wpi.first.wpilibj2.command.Subsystem
import org.frc5183.constants.Config
import org.frc5183.subsystems.elevator.io.ElevatorIO
import org.littletonrobotics.junction.Logger

class ElevatorSubsystem(
    val io: ElevatorIO,
) : Subsystem {
    private val ioInputs: ElevatorIO.ElevatorIOInputs = ElevatorIO.ElevatorIOInputs()

    // private val encoderZero: Angle = io.motorEncoder

    /**
     * The desired stage the elevator should be on.
     */
    var desiredStage: Int = 0

    /**
     * The current stage the elevator is on based on the motor's encoder.
     */
    var currentStage: Int = 0
        private set

    /**
     * The absolute difference between the current encoder value and the current stage's
     * desired encoder value.
     */
    val stageDrift: Angle
        get() = (Config.ELEVATOR_STAGES.getOrNull(desiredStage) ?: Units.Degrees.of(0.0)) - io.motorEncoder

    val topLimitSwitch: Boolean
        get() = io.topLimitSwitchTriggered

    val bottomLimitSwitch: Boolean
        get() = io.bottomLimitSwitchTriggered

    val motorRunningUp: Boolean
        get() = speedMovesUp(io.motorSpeed)

    val motorRunningDown: Boolean
        get() = speedMovesDown(io.motorSpeed)

    override fun periodic() {
        io.updateInputs(ioInputs, currentStage)
        Logger.processInputs("Elevator", ioInputs)
        Logger.recordOutput("Elevator/Current Stage", currentStage)
        Logger.recordOutput("Elevator/Desired Stage", desiredStage)

        Logger.recordOutput("Elevator/Current Stage (Readable)", stageToString(currentStage))
        Logger.recordOutput("Elevator/Desired Stage (Readable)", stageToString(desiredStage))

        Logger.recordOutput("Elevator/Stage Drift", stageDrift.`in`(Units.Rotations))

        currentStage = Config.ELEVATOR_STAGES.indexOfLast { it <= io.motorEncoder }.coerceAtLeast(0)

        if (bottomLimitSwitch) {
            currentStage = 0
            io.resetEncoder()
            if (motorRunningDown) stopElevator()
        }

        if (topLimitSwitch && motorRunningUp) stopElevator()
    }

    /**
     * Runs the elevator at [speed]
     */
    fun runElevator(speed: Double) {
        if (speedMovesUp(speed) && topLimitSwitch) return
        if (speedMovesDown(speed) && bottomLimitSwitch) return

        io.runElevator(speed)
    }

    /**
     * Runs the elevator motor up.
     */
    fun raiseElevator(speed: Double) = runElevator(-1.0 * speed)

    /**
     * Runs the elevator motor down.
     */
    fun lowerElevator(speed: Double) = runElevator(1.0 * speed)

    /**
     * Stops the elevator motor.
     */
    fun stopElevator() = io.stopElevator()

    /**
     * Resets the motor encoder to 0.
     * Should be called when the bottom limit switch is triggered.
     */
    fun resetEncoder() = io.resetEncoder()

    /**
     * Whether the given speed will move the elevator down.
     *
     * @param speed The speed to check.
     * @return Whether the speed will move the elevator down.
     */
    fun speedMovesDown(speed: Double): Boolean = speed > 0 && Config.ELEVATOR_MOTOR_INVERTED || speed < 0 && !Config.ELEVATOR_MOTOR_INVERTED

    /**
     * Whether the given speed will move the elevator up.
     *
     * @param speed The speed to check.
     * @return Whether the speed will move the elevator up.
     */
    fun speedMovesUp(speed: Double): Boolean = speed < 0 && Config.ELEVATOR_MOTOR_INVERTED || speed > 0 && !Config.ELEVATOR_MOTOR_INVERTED

    /**
     * Converts an elevator stage as an int to it's name as a string.
     *
     * @param stage The stage to convert.
     * @return The name of the stage, or "Unknown" if the stage is invalid
     */
    fun stageToString(stage: Int): String = when (stage) {
        0 -> "Bottom"
        1 -> "Trough (L1)"
        2 -> "L2 (First) Branch"
        3 -> "L3 (Second) Branch"
        4 -> "L4 (Third) Branch"
        else -> "Unknown"
    }
}
