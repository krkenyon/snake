package verifier

import game.Command

object StrategyBoardChecker {

    data class Result(
        val width: Int,
        val height: Int,
        val success: Boolean,
        val stepsUsed: Long,
        val coverage: Int
    )

    fun checkBoard(
        width: Int,
        height: Int,
        maxSteps: Long,
        generator: (Long) -> List<Command>
    ): Result {
        val visited = BooleanArray(width * height)

        var x = 0
        var y = 0
        var visitedCount = 0
        var steps = 0L

        fun visit() {
            val idx = y * width + x
            if (!visited[idx]) {
                visited[idx] = true
                visitedCount++
            }
        }

        visit()

        val commands = generator(maxSteps)

        for (command in commands) {
            if (steps >= maxSteps) break

            when (command) {
                Command.RIGHT -> x = (x + 1) % width
                Command.LEFT -> x = (x - 1 + width) % width
                Command.DOWN -> y = (y + 1) % height
                Command.UP -> y = (y - 1 + height) % height
            }

            visit()
            steps++

            if (visitedCount == width * height) {
                return Result(width, height, true, steps, visitedCount)
            }
        }

        return Result(width, height, false, steps, visitedCount)
    }
}