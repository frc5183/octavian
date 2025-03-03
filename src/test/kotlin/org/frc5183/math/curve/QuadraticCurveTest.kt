package org.frc5183.math.curve

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class QuadraticCurveTest {
    private companion object {
        private const val DELTA = 0.0001
    }
    
    @Test
    fun `test quadratic curve with positive a`() {
        val a = 1.0
        val b = 0.0
        val c = 0.0
        val curve = QuadraticCurve(a, b, c)

        assertEquals(0.0, curve(0.0), DELTA) // 1.0 * 0.0^2 + 0.0 * 0.0 + 0.0 = 0.0
        assertEquals(1.0, curve(1.0), DELTA) // 1.0 * 1.0^2 + 0.0 * 1.0 + 0.0 = 1.0
        assertEquals(1.0, curve(-1.0), DELTA) // 1.0 * (-1.0)^2 + 0.0 * (-1.0) + 0.0 = 1.0
        assertEquals(4.0, curve(2.0), DELTA) // 1.0 * 2.0^2 + 0.0 * 2.0 + 0.0 = 4.0
    }

    @Test
    fun `test quadratic curve with negative a`() {
        val a = -2.0
        val b = 0.0
        val c = 1.0
        val curve = QuadraticCurve(a, b, c)

        assertEquals(1.0, curve(0.0), DELTA) // -2.0 * 0.0^2 + 0.0 * 0.0 + 1.0 = 1.0
        assertEquals(-1.0, curve(1.0), DELTA) // -2.0 * 1.0^2 + 0.0 * 1.0 + 1.0 = -1.0
        assertEquals(-1.0, curve(-1.0), DELTA) // -2.0 * (-1.0)^2 + 0.0 * (-1.0) + 1.0 = -1.0
        assertEquals(-7.0, curve(2.0), DELTA) // -2.0 * 2.0^2 + 0.0 * 2.0 + 1.0 = -7.0
    }

    @Test
    fun `test quadratic curve with all parameters`() {
        val a = 1.0
        val b = -3.0
        val c = 2.0
        val curve = QuadraticCurve(a, b, c)

        assertEquals(2.0, curve(0.0), DELTA) // 1.0 * 0.0^2 + (-3.0) * 0.0 + 2.0 = 2.0
        assertEquals(0.0, curve(1.0), DELTA) // 1.0 * 1.0^2 + (-3.0) * 1.0 + 2.0 = 0.0
        assertEquals(6.0, curve(-1.0), DELTA) // 1.0 * (-1.0)^2 + (-3.0) * (-1.0) + 2.0 = 6.0
        assertEquals(0.0, curve(2.0), DELTA) // 1.0 * 2.0^2 + (-3.0) * 2.0 + 2.0 = (4.0) + (-6.0) + 2.0 = 0.0
    }

    @Test
    fun `test quadratic curve special points`() {
        // Create curve with roots at x=1 and x=2: y = (x-1)(x-2) = x² - 3x + 2
        val curve = QuadraticCurve(1.0, -3.0, 2.0)

        // Test roots
        assertEquals(0.0, curve(1.0), DELTA) // First root
        assertEquals(0.0, curve(2.0), DELTA) // Second root

        // Test vertex (at x = -b/2a = 1.5)
        assertEquals(-0.25, curve(1.5), DELTA) // Minimum value
    }
}
