package org.frc5183.commands.elevator

import edu.wpi.first.wpilibj2.command.Command
import org.frc5183.constants.Config
import org.frc5183.subsystems.elevator.ElevatorSubsystem

class RaiseElevatorCommand(
    val elevator: ElevatorSubsystem,
) : Command() {
    private var finished: Boolean = false

    init {
        addRequirements(elevator)
    }

    override fun initialize() {
        if (elevator.desiredStage >= Config.ELEVATOR_STAGES.lastIndex) {
            finished = true
            return
        }

        elevator.desiredStage += 1

        finished = true
    }

    override fun isFinished(): Boolean = finished
}
