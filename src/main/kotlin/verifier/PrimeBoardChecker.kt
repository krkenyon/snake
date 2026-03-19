package verifier

import strategy.PrimeStrategy

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
        var i = 1L

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
            val range = PrimeStrategy.getPrimesIndices(i)

            for (idx in range) {
                val p = PrimeStrategy.getPrimeAt(idx)

                repeat(p) {
                    if (steps >= maxSteps) {
                        return Result(width, height, false, steps, visitedCount)
                    }

                    x = (x + 1) % width
                    visit()
                    steps++
                    i++

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
                i++

                if (visitedCount == width * height) {
                    return Result(width, height, true, steps, visitedCount)
                }
            }
        }

        return Result(
            width = width,
            height = height,
            success = visitedCount == width * height,
            stepsUsed = steps,
            coverage = visitedCount
        )
    }
}