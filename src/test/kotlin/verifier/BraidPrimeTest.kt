package verifier

import org.junit.jupiter.api.Test
import strategy.BraidPrimeStrategy

class BraidPrimeTest {

    @Test
    fun check45x45() {
        val result = StrategyBoardChecker.checkBoard(
            width = 45,
            height = 45,
            maxSteps = 35L * 45 * 45,
            generator = BraidPrimeStrategy::generateCommands
        )

        println(result)
    }
}