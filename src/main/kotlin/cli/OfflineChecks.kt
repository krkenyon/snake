package cli

import baselines.ProvableStrategy
import game.BlindSnakeGame
import game.Command
import experiments.BraidPrimeStrategy
import strategy.PrimeStrategy

object OfflineChecks {

    data class CheckResult(
        val strategy: String,
        val width: Int,
        val height: Int,
        val success: Boolean,
        val stepsUsed: Long,
        val coverage: Int,
        val area: Int,
        val appleX: Int? = null,
        val appleY: Int? = null
    )

    private class BudgetExceeded : RuntimeException()
    private class CoverageComplete : RuntimeException()

    private class SimulatedGame(
        private val width: Int,
        private val height: Int,
        private val maxSteps: Long,
        private val appleX: Int? = null,
        private val appleY: Int? = null
    ) : BlindSnakeGame {

        var x = 0
        var y = 0
        var steps = 0L
        var visitedCount = 0
            private set

        private val visited = BooleanArray(width * height)

        init {
            visitCurrentCell()
        }

        fun hasFullCoverage(): Boolean = visitedCount == width * height

        private fun visitCurrentCell() {
            val idx = y * width + x
            if (!visited[idx]) {
                visited[idx] = true
                visitedCount++
            }
        }

        override fun sendSignal(command: Command): Boolean {
            if (steps >= maxSteps) throw BudgetExceeded()

            when (command) {
                Command.RIGHT -> x = (x + 1) % width
                Command.LEFT -> x = (x - 1 + width) % width
                Command.DOWN -> y = (y + 1) % height
                Command.UP -> y = (y - 1 + height) % height
            }

            steps++
            visitCurrentCell()

            if (appleX == null && appleY == null && hasFullCoverage()) {
                throw CoverageComplete()
            }

            return appleX != null && appleY != null && x == appleX && y == appleY
        }
    }

    private fun runStrategy(strategy: String, game: BlindSnakeGame, area: Int): Boolean {
        return when (strategy.lowercase()) {
            "prime" -> PrimeStrategy.play(game, maxAreaBound = area)
            "braid", "braid-prime" -> BraidPrimeStrategy.play(game, maxAreaBound = area)
            "baseline", "provable" -> ProvableStrategy().play(game)
            else -> error("Unknown strategy: $strategy")
        }
    }

    fun checkCoverage(strategy: String, width: Int, height: Int): CheckResult {
        require(width > 0) { "width must be positive" }
        require(height > 0) { "height must be positive" }

        val area = width * height
        val maxSteps = 35L * area
        val game = SimulatedGame(width, height, maxSteps)

        if (game.hasFullCoverage()) {
            return CheckResult(
                strategy = strategy,
                width = width,
                height = height,
                success = true,
                stepsUsed = game.steps,
                coverage = game.visitedCount,
                area = area
            )
        }

        val success = try {
            runStrategy(strategy, game, area)
            false
        } catch (_: CoverageComplete) {
            true
        } catch (_: BudgetExceeded) {
            false
        }

        return CheckResult(
            strategy = strategy,
            width = width,
            height = height,
            success = success,
            stepsUsed = game.steps,
            coverage = game.visitedCount,
            area = area
        )
    }

    fun checkApple(strategy: String, width: Int, height: Int, appleX: Int, appleY: Int): CheckResult {
        require(width > 0) { "width must be positive" }
        require(height > 0) { "height must be positive" }
        require(appleX in 0 until width) { "appleX must be in 0 until width" }
        require(appleY in 0 until height) { "appleY must be in 0 until height" }

        val area = width * height
        val maxSteps = 35L * area
        val game = SimulatedGame(width, height, maxSteps, appleX, appleY)

        val success = try {
            runStrategy(strategy, game, area)
        } catch (_: BudgetExceeded) {
            false
        }

        return CheckResult(
            strategy = strategy,
            width = width,
            height = height,
            success = success,
            stepsUsed = game.steps,
            coverage = game.visitedCount,
            area = area,
            appleX = appleX,
            appleY = appleY
        )
    }
}