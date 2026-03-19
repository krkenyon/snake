import baselines.ProvableStrategy
import game.BlindSnakeGame
import strategy.PrimeStrategy

/**
 * Small command-line entry point for lightweight local demos.
 *
 * This file should stay focused on code that belongs to the actual solution:
 * - wiring a strategy to the game-facing API
 * - running tiny smoke tests with a stub game engine
 *
 * Offline verification, board checking, and result generation belong under
 * src/test/kotlin and should be run from dedicated test runners instead.
 */
fun main(args: Array<String>) {
    when (args.getOrNull(0)) {
        "demo-prime" -> {
            val won = PrimeStrategy.play(BlindSnakeGame { false }, maxAreaBound = 10)
            println("Prime demo finished. Won=$won")
        }

        "demo-baseline" -> {
            val won = ProvableStrategy().play(BlindSnakeGame { false })
            println("Baseline demo finished. Won=$won")
        }

        else -> {
            println("Use one of:")
            println("  demo-prime")
            println("  demo-baseline")
            println()
            println("Verification utilities were moved to src/test/kotlin/verifier.")
            println("Run them from dedicated tests instead of Main.kt.")
        }
    }
}