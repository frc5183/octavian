package org.frc5183.subsystems.coral

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import edu.wpi.first.wpilibj2.command.Subsystem
import org.frc5183.subsystems.coral.io.CoralIO

class CoralSubsystem(private val io: CoralIO) : Subsystem {
    private val ioInputs = CoralIO.CoralIOInputs()

    var hasCoral: Boolean = false
        private set

    val seesCoral: Boolean
        get() = io.seesCoral

    private var visibleCoralBuffer: Boolean = false

    override fun periodic() {
      io.updateInputs(ioInputs, hasCoral)
        SmartDashboard.putBoolean("Has Coral", hasCoral)
        SmartDashboard.putBoolean("Sees Coral", seesCoral)

      if (seesCoral && !visibleCoralBuffer) visibleCoralBuffer = true
      if (!seesCoral && visibleCoralBuffer) {
        hasCoral = true
        visibleCoralBuffer = false
      }
    }

    fun runMotor() {
        io.runMotor()
    }

    fun stopMotor() {
        io.stopMotor()
    }

    fun clearCoral() {
        this.hasCoral = hasCoral
        visibleCoralBuffer = false
    }
}
