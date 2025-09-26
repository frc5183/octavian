package org.frc5183.commands.elevator

import edu.wpi.first.units.Units
import edu.wpi.first.wpilibj2.command.Command
import org.frc5183.constants.Config
import org.frc5183.subsystems.elevator.ElevatorSubsystem

/**
 * Can be ran before another command to move the elevator to the correct
 * stage to account for sag.
 */
@Deprecated(message = "GotoElevatorCommand goes both up and down with better checks.", ReplaceWith("GotoElevatorCommand"))
class CorrectElevatorCommand(
    val elevator: ElevatorSubsystem,
) : Command() {
    init {
        addRequirements(elevator)
    }

    override fun execute() {
        elevator.raiseElevator(Config.ELEVATOR_MOVEMENT_SPEED)
    }

    override fun end(interrupted: Boolean) {
        elevator.stopElevator()
    }

    override fun isFinished(): Boolean = elevator.stageDrift.abs(Units.Degrees) < Config.ELEVATOR_MAX_ALLOWED_DRIFT.`in`(Units.Degrees)
}
