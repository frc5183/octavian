package org.frc5183.commands.coral

import edu.wpi.first.wpilibj.Timer
import edu.wpi.first.wpilibj2.command.Command
import org.frc5183.constants.Config
import org.frc5183.subsystems.coral.CoralSubsystem
import kotlin.time.DurationUnit

class ShootCoralCommand(
    private val coralSubsystem: CoralSubsystem,
) : Command() {
    private val timer: Timer = Timer()

    init {
        addRequirements(coralSubsystem)
    }

    override fun initialize() {
        timer.reset()
        timer.start()
        coralSubsystem.runMotor(Config.CORAL_SHOOT_SPEED)
    }

    override fun end(interrupted: Boolean) {
        coralSubsystem.stopMotor()
        coralSubsystem.clearCoral()
    }

    override fun isFinished(): Boolean = timer.hasElapsed(Config.CORAL_SHOOT_TIME.toDouble(DurationUnit.SECONDS))
}
