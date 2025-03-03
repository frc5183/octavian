package org.frc5183.math.curve

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

class TimedCurveTest {
    private companion object {
        private const val DELTA = 0.0001
    }
    
    @Test
    fun `test timed curve returns input before time expires`() {
        val curve = TimedCurve(1.seconds)

        assertEquals(0.0, curve(0.0), DELTA)
        assertEquals(1.0, curve(1.0), DELTA)
        assertEquals(-1.0, curve(-1.0), DELTA)
    }

    @Test
    fun `test timed curve returns zero after time expires`() {
        val curve = TimedCurve(1.milliseconds)

        Thread.sleep(5)

        assertEquals(0.0, curve(0.0), DELTA)
        assertEquals(0.0, curve(1.0), DELTA)
        assertEquals(0.0, curve(-1.0), DELTA)
    }
}
