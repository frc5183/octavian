package org.frc5183.commands.elevator

import edu.wpi.first.wpilibj2.command.Command
import org.frc5183.constants.Config
import org.frc5183.subsystems.elevator.ElevatorSubsystem

class LowerElevatorCommand(
    val elevator: ElevatorSubsystem,
) : Command() {
    private var finished: Boolean = false

    init {
        addRequirements(elevator)
    }

    override fun initialize() {
        if (elevator.desiredStage <= 0) {
            finished = true
            return
        }

        elevator.desiredStage -= 1

        finished = true
    }

    override fun isFinished(): Boolean = finished
}
