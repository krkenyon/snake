package verifier

import strategy.PrimeStrategy

/**
 * Offline simulator for a specific torus board.
 *
 * This does not know where the apple is. Instead, it asks a stronger question:
 * "How many distinct cells does the strategy visit within a fixed step budget?"
 *
 * If the simulated walk covers every cell, then regardless of the unknown start
 * cell and unknown apple cell, the strategy would eventually hit the apple on
 * that board within the same number of moves.
 */
object PrimeBoardChecker {

    data class Result(
        val width: Int,
        val height: Int,
        val success: Boolean,
        val stepsUsed: Long,
        val coverage: Int
    )

    fun checkBoard(width: Int, height: Int, maxSteps: Long): Result {
        val visited = BooleanArray(width * height)

        var x = 0
        var y = 0
        var visitedCount = 0

        var steps = 0L
        var commandIndex = 1L

        fun visit() {
            val idx = y * width + x
            if (!visited[idx]) {
                visited[idx] = true
                visitedCount++
            }
        }

        visit()
        if (visitedCount == width * height) {
            return Result(width, height, true, steps, visitedCount)
        }

        while (steps < maxSteps) {
            val range = PrimeStrategy.getStrideIndices(commandIndex)

            for (idx in range) {
                val stride = PrimeStrategy.getStrideAt(idx)

                repeat(stride) {
                    if (steps >= maxSteps) {
                        return Result(width, height, false, steps, visitedCount)
                    }

                    x = (x + 1) % width
                    visit()
                    steps++
                    commandIndex++

                    if (visitedCount == width * height) {
                        return Result(width, height, true, steps, visitedCount)
                    }
                }

                if (steps >= maxSteps) {
                    return Result(width, height, false, steps, visitedCount)
                }

                y = (y + 1) % height
                visit()
                steps++
                commandIndex++

                if (visitedCount == width * height) {
                    return Result(width, height, true, steps, visitedCount)
                }
            }
        }

        return Result(width, height, visitedCount == width * height, steps, visitedCount)
    }
}
