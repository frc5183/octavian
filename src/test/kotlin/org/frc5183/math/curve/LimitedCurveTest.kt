package org.frc5183.math.curve

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class LimitedCurveTest {
    private companion object {
        private const val DELTA = 0.0001
    }
    
    @Test
    fun `test limited curve with positive range`() {
        val min = 0.0
        val max = 1.0
        val curve = LimitedCurve(min, max)

        assertEquals(0.5, curve(0.5), DELTA)
        assertEquals(0.0, curve(0.0), DELTA)
        assertEquals(1.0, curve(1.0), DELTA)

        assertEquals(0.0, curve(-0.5), DELTA)
        assertEquals(1.0, curve(1.5), DELTA)
    }

    @Test
    fun `test limited curve with negative range`() {
        val min = -2.0
        val max = -1.0
        val curve = LimitedCurve(min, max)

        assertEquals(-1.5, curve(-1.5), DELTA)
        assertEquals(-2.0, curve(-2.0), DELTA)
        assertEquals(-1.0, curve(-1.0), DELTA)

        assertEquals(-2.0, curve(-3.0), DELTA)
        assertEquals(-1.0, curve(0.0), DELTA)
    }

    @Test
    fun `test limited curve with mixed range`() {
        val min = -1.0
        val max = 1.0
        val curve = LimitedCurve(min, max)

        assertEquals(0.0, curve(0.0), DELTA)
        assertEquals(-0.5, curve(-0.5), DELTA)
        assertEquals(0.5, curve(0.5), DELTA)

        assertEquals(-1.0, curve(-2.0), DELTA)
        assertEquals(1.0, curve(2.0), DELTA)
    }
}
