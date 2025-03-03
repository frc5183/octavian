package org.frc5183.math.curve

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class NullCurveTest {
    private companion object {
        private const val DELTA = 0.0001
    }
    
    @Test
    fun `test null curve always returns zero`() {
        val curve = NullCurve()

        assertEquals(0.0, curve(0.0), DELTA)
        assertEquals(0.0, curve(1.0), DELTA)
        assertEquals(0.0, curve(-1.0), DELTA)
        assertEquals(0.0, curve(100.0), DELTA)
        assertEquals(0.0, curve(-100.0), DELTA)
    }
}
