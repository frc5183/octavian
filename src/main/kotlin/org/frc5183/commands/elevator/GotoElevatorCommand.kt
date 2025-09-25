package org.frc5183.commands.elevator

import edu.wpi.first.units.Units
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

    override fun initialize() {
        elevator.desiredStage = elevator.desiredStage.coerceIn(0, Config.ELEVATOR_STAGES.lastIndex)
    }

    override fun execute() {
        if (elevator.stageDrift.gt(Units.Degrees.zero())) { // If we're below the desired stage, move up
            elevator.raiseElevator(Config.ELEVATOR_MOVEMENT_SPEED)
        } else if (elevator.stageDrift.lt(Units.Degrees.zero())) { // If we're above the current stage, move down
            elevator.lowerElevator(Config.ELEVATOR_MOVEMENT_SPEED)
        }
    }

    override fun end(interrupted: Boolean) {
        elevator.stopElevator()
    }

    override fun isFinished(): Boolean {
        // If we're either at or above the max stage, and the top limit switch is hit, we're done no matter what.
        if ((elevator.desiredStage >= Config.ELEVATOR_STAGES.lastIndex || elevator.currentStage >= Config.ELEVATOR_STAGES.lastIndex) ||
            elevator.topLimitSwitch
        ) {
            return true
        }

        // If we're at or below the min stage, and the bottom limit switch is hit, we're done no matter what.
        if ((elevator.desiredStage <= 0 || elevator.currentStage <= 0) || elevator.bottomLimitSwitch) {
            return true
        }

        // Otherwise, we're done once we're within the allowed drift of the desired stage.
        return elevator.stageDrift.abs(Units.Degrees) <= Config.ELEVATOR_MAX_ALLOWED_DRIFT.`in`(Units.Degrees)
    }
}
