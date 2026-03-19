package verifier

import org.junit.jupiter.api.Test
import strategy.BraidPrimeStrategy
import strategy.PrimeStrategy

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

    @Test
    fun verifySelectedQuickBraid() {
        SelectedBoardVerifier.verifyQuick(
            runName = "selected_quick_braid",
            generator = BraidPrimeStrategy::generateCommands
        )
    }

    @Test
    fun verifySelectedQuickPrime() {
        SelectedBoardVerifier.verifyQuick(
            runName = "selected_quick_prime",
            generator = PrimeStrategy::generateCommands
        )
    }
}