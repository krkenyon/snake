package baselines

import kotlin.math.sqrt

class ProvableStrategy {

    enum class Direction {
        UP, DOWN, LEFT, RIGHT
    }


    fun playGame(sendSignal: (Direction) -> Boolean) {
        var areaGuess = 1

        while (true) {
            val maxSmallDimension = sqrt(areaGuess.toDouble()).toInt()

            // Try all possible widths
            for (w in 1..maxSmallDimension) {
                val reps = (areaGuess + w - 1) / w

                repeat(reps) {
                    repeat(w - 1) {
                        if (sendSignal(Direction.RIGHT)) return
                    }
                    if (sendSignal(Direction.DOWN)) return
                }
            }

            // Try all possible heights
            for (h in 1..maxSmallDimension) {
                val reps = (areaGuess + h - 1) / h

                repeat(reps) {
                    repeat(h - 1) {
                        if (sendSignal(Direction.DOWN)) return
                    }
                    if (sendSignal(Direction.RIGHT)) return
                }
            }

            areaGuess *= 2
        }
    }
}