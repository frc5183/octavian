package org.frc5183.math.curve

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class LinearCurveTest {
    private companion object {
        private const val DELTA = 0.0001
    }

    @Test
    fun `test linear curve with positive slope`() {
        val m = 2.0
        val b = 1.0
        val curve = LinearCurve(m, b)

        assertEquals(1.0, curve(0.0), DELTA) // b = 1.0
        assertEquals(3.0, curve(1.0), DELTA) // 2*1 + 1 = 3
        assertEquals(-1.0, curve(-1.0), DELTA) // 2*(-1) + 1 = -1
        assertEquals(11.0, curve(5.0), DELTA) // 2*5 + 1 = 11
    }

    @Test
    fun `test linear curve with negative slope`() {
        val m = -1.5
        val b = 0.0
        val curve = LinearCurve(m, b)

        assertEquals(0.0, curve(0.0), DELTA) // b = 0.0
        assertEquals(-1.5, curve(1.0), DELTA) // -1.5*1 + 0 = -1.5
        assertEquals(1.5, curve(-1.0), DELTA) // -1.5*(-1) + 0 = 1.5
        assertEquals(-7.5, curve(5.0), DELTA) // -1.5*5 + 0 = -7.5
    }

    @Test
    fun `test linear curve with zero slope`() {
        val m = 0.0
        val b = 3.0
        val curve = LinearCurve(m, b)

        assertEquals(3.0, curve(0.0), DELTA) // b = 3.0
        assertEquals(3.0, curve(1.0), DELTA) // 0*1 + 3 = 3
        assertEquals(3.0, curve(-1.0), DELTA) // 0*(-1) + 3 = 3
        assertEquals(3.0, curve(5.0), DELTA) // 0*5 + 3 = 3
    }
}
