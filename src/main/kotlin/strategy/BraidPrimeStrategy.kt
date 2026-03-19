package strategy

import game.BlindSnakeGame
import game.Command
import kotlin.math.min

/**
 * Braid-style variant of PrimeStrategy.
 *
 * For each active stride k, emit:
 *   RIGHT k
 *   DOWN 1
 *   RIGHT 2 (for asymmetry)
 *   DOWN k
 *
 * So across two stride lengths j and k the walk looks like:
 *   RIGHT j, DOWN, RIGHT 2, DOWN j,
 *   RIGHT k, DOWN, RIGHT 2, DOWN k, ...
 */
object BraidPrimeStrategy : BlindSnakeSolver {

    override fun play(game: BlindSnakeGame): Boolean = play(game, maxAreaBound = 1_000_000)

    fun play(game: BlindSnakeGame, maxAreaBound: Int = 1_000_000): Boolean {
        val maxMoves = 35L * maxAreaBound.toLong()
        var movesMade = 0L
        var commandIndex = 1L

        while (movesMade < maxMoves) {
            val activeRange = PrimeStrategy.getStrideIndices(commandIndex)

            for (idx in activeRange) {
                val stride = PrimeStrategy.getStrideAt(idx)

                repeat(stride) {
                    if (movesMade >= maxMoves) return false
                    if (game.sendSignal(Command.RIGHT)) return true
                    movesMade++
                    commandIndex++
                }

                if (movesMade >= maxMoves) return false
                if (game.sendSignal(Command.DOWN)) return true
                movesMade++
                commandIndex++

                if (movesMade >= maxMoves) return false
                if (game.sendSignal(Command.RIGHT)) return true
                movesMade++
                commandIndex++

                if (movesMade >= maxMoves) return false
                if (game.sendSignal(Command.RIGHT)) return true
                movesMade++
                commandIndex++

                repeat(stride) {
                    if (movesMade >= maxMoves) return false
                    if (game.sendSignal(Command.DOWN)) return true
                    movesMade++
                    commandIndex++
                }
            }
        }

        return false
    }

    /**
     * Deterministic command generator used by offline verifiers.
     */
    fun generateCommands(maxMoves: Long = 35_000_000L): List<Command> {
        val commands = ArrayList<Command>(min(maxMoves, 1_000_000L).toInt())
        var movesMade = 0L
        var commandIndex = 1L

        while (movesMade < maxMoves) {
            val activeRange = PrimeStrategy.getStrideIndices(commandIndex)

            for (idx in activeRange) {
                val stride = PrimeStrategy.getStrideAt(idx)

                repeat(stride) {
                    if (movesMade >= maxMoves) return commands
                    commands += Command.RIGHT
                    movesMade++
                    commandIndex++
                }

                if (movesMade >= maxMoves) return commands
                commands += Command.DOWN
                movesMade++
                commandIndex++

                if (movesMade >= maxMoves) return commands
                commands += Command.RIGHT
                movesMade++
                commandIndex++

                repeat(stride) {
                    if (movesMade >= maxMoves) return commands
                    commands += Command.DOWN
                    movesMade++
                    commandIndex++
                }
            }
        }

        return commands
    }
}