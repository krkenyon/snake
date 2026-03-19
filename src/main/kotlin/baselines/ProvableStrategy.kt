package baselines

import game.BlindSnakeGame
import game.Command
import strategy.BlindSnakeSolver
import kotlin.math.sqrt

/**
 * Structured baseline intended as a comparison point, not as the main submission.
 *
 * This baseline grows an area guess and, for each guess, tries simple sweep
 * patterns corresponding to candidate small dimensions. The high-level logic is:
 *
 * 1. guess an area bound G
 * 2. for each candidate width w up to sqrt(G), run a row sweep shaped like a
 *    w-column board
 * 3. for each candidate height h up to sqrt(G), run a column sweep shaped like
 *    an h-row board
 * 4. double G and repeat
 *
 */
class ProvableStrategy : BlindSnakeSolver {

    override fun play(game: BlindSnakeGame): Boolean {
        var areaGuess = 1

        while (true) {
            val maxSmallDimension = sqrt(areaGuess.toDouble()).toInt()

            for (w in 1..maxSmallDimension) {
                val repetitions = (areaGuess + w - 1) / w

                repeat(repetitions) {
                    repeat(w - 1) {
                        if (game.sendSignal(Command.RIGHT)) return true
                    }
                    if (game.sendSignal(Command.DOWN)) return true
                }
            }

            for (h in 1..maxSmallDimension) {
                val repetitions = (areaGuess + h - 1) / h

                repeat(repetitions) {
                    repeat(h - 1) {
                        if (game.sendSignal(Command.DOWN)) return true
                    }
                    if (game.sendSignal(Command.RIGHT)) return true
                }
            }

            areaGuess *= 2
        }
    }
}
