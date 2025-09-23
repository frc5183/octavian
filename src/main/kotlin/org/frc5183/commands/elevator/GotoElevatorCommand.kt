package org.frc5183.commands.elevator

import edu.wpi.first.wpilibj2.command.Command
import org.frc5183.constants.Config
import org.frc5183.subsystems.elevator.ElevatorSubsystem

/**
 * Sends the elevator to the desired stage.
 *
 * This command will stop as soon as it reaches the desired stage, so it should
 * be followed with a [HoldElevatorCommand] to keep it in place.
 */
class GotoElevatorCommand(
    val elevator: ElevatorSubsystem,
) : Command() {
    var invalid = false

    init {
        addRequirements(elevator)
    }

    override fun execute() {
        // If we're below the desired stage, move up
        if (elevator.currentStage < elevator.desiredStage) elevator.raiseElevator(Config.ELEVATOR_MOVEMENT_SPEED)

        // If we're above the current stage, move down
        if (elevator.currentStage > elevator.desiredStage) elevator.lowerElevator(Config.ELEVATOR_MOVEMENT_SPEED)
    }

    override fun end(interrupted: Boolean) {
        elevator.stopElevator()
    }

    override fun isFinished(): Boolean {
        // If we're either at or above the max stage, and the top limit switch is hit, we're done no matter what.
        if ((elevator.desiredStage >= Config.ELEVATOR_STAGES.size || elevator.currentStage >= Config.ELEVATOR_STAGES.size) || elevator.topLimitSwitch) {
            return true
        }

        // If we're at or below the min stage, and the bottom limit switch is hit, we're done no matter what.
        if ((elevator.desiredStage <= 0 || elevator.currentStage <= 0) || elevator.bottomLimitSwitch) {
            return true
        }

        // Otherwise, we're done once we're at the desired stage.
        return elevator.currentStage == elevator.desiredStage
    }
}