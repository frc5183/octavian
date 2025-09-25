package org.frc5183.constants

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class ConfigTest {
    @Test
    fun `ELEVATOR_STAGES is not empty`() {
        assertTrue(Config.ELEVATOR_STAGES.isNotEmpty())
    }

    @Test
    fun `ELEVATOR_STAGES are in ascending order`() {
        for (i in 0 until Config.ELEVATOR_STAGES.lastIndex) {
            assertTrue(Config.ELEVATOR_STAGES[i] < Config.ELEVATOR_STAGES[i + 1])
        }
    }

    @Test
    fun `ELEVATOR_STAGES size is 5`() {
        assertEquals(5, Config.ELEVATOR_STAGES.size)
    }

    @Test
    fun `ELEVATOR_MAX_ALLOWED_DRIFT is positive`() {
        assertTrue(Config.ELEVATOR_MAX_ALLOWED_DRIFT.`in`(edu.wpi.first.units.Units.Degrees) > 0)
    }

    @Test
    fun `ELEVATOR_MOVEMENT_SPEED is between -1 and 1`() {
        assertTrue(Config.ELEVATOR_MOVEMENT_SPEED in -1.0..1.0)
    }

    @Test
    fun `ELEVATOR_HOLD_SPEED is between -1 and 1`() {
        assertTrue(Config.ELEVATOR_HOLD_SPEED in -1.0..1.0)
    }

    @Test
    fun `CORAL_INTAKE_SPEED is between -1 and 1`() {
        assertTrue(Config.CORAL_INTAKE_SPEED in -1.0..1.0)
    }

    @Test
    fun `CORAL_SHOOT_SPEED is between -1 and 1`() {
        assertTrue(Config.CORAL_SHOOT_SPEED in -1.0..1.0)
    }

    @Test
    fun `CORAL_PROXIMITY_THRESHOLD is between 0 and 2047`() {
        assertTrue(Config.CORAL_PROXIMITY_THRESHOLD in 0..2047)
    }
}