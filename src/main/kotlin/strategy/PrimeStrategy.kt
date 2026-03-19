package strategy

import game.BlindSnakeGame
import game.Command
import kotlin.math.min

/**
 * Practical blind-search heuristic for the toroidal snake problem.
 *
 * Problem setting recap:
 * - width A is unknown
 * - height B is unknown
 * - area S = A * B is unknown, but guaranteed to satisfy S < 1_000_000
 * - the snake has no positional feedback
 * - movement wraps around both dimensions
 * - the only feedback is whether the current move has hit the apple
 *
 * Main idea:
 * We keep repeating blocks of the form
 *
 *     RIGHT p times, then DOWN once
 *
 * where p comes from a changing pool of mostly prime run lengths.
 *
 * Why this is a plausible heuristic:
 * - A fixed horizontal stride can resonate badly with an unknown board width.
 * - Using many different run lengths keeps changing the horizontal offset modulo
 *   the true width, so the walk is much less likely to get trapped in a tiny
 *   repeating subset of cells.
 * - Small values appear early, which helps on tiny boards and thin boards.
 * - Larger values appear gradually, which increases mixing on wider boards.
 *
 * Important limitation:
 * This is still a heuristic. It is designed to perform well empirically, but it
 * does not currently come with a proof that every board is solved within 35 * S
 * moves.
 */
object PrimeStrategy : BlindSnakeSolver {

    /**
     * Pool of candidate horizontal sweep lengths.
     *
     * This intentionally mirrors the experimental sequence already used in the
     * project so we keep continuity with prior verification runs.
     */
    private val stridePool = intArrayOf(
        1, 2, 3, 5, 7, 11, 13, 5, 17, 19, 23, 3, 29,
        31, 37, 41, 43, 47, 53, 59, 61, 67, 71,
        73, 79, 83, 89, 97, 101, 103, 107, 109, 113,
        127, 131, 137, 139, 149, 151, 157, 163, 167, 173,
        179, 181, 191, 193, 197, 199, 211, 223, 227, 229,
        233, 239, 241, 251, 257, 263, 269, 271, 277, 281,
        283, 293, 307, 311, 313, 317, 331, 337, 347, 349,
        353, 359, 367, 373, 379, 383, 389, 397, 401, 409,
        419, 421, 431, 433, 439, 443, 449, 457, 461, 463,
        467, 479, 487, 491, 499, 503, 509, 521, 523, 541,
        547, 557, 563, 569, 571, 577, 587, 593, 599, 601,
        607, 613, 617, 619, 631, 641, 643, 647, 653, 659,
        661, 673, 677, 683, 691, 701, 709, 719, 727, 733,
        739, 743, 751, 757, 761, 769, 773, 787, 797, 809,
        811, 821, 823, 827, 829, 839, 853, 857, 859, 863,
        877, 881, 883, 887, 907, 911, 919, 929, 937, 941,
        947, 953, 967, 971, 977, 983, 991, 997, 1009, 1013,
        1019, 1021, 1031, 1033, 1039, 1049, 1051, 1061, 1063,
        1069, 1087, 1091, 1093, 1097, 1103, 1109, 1117, 1123
    )

    fun getStrideAt(index: Int): Int = stridePool[index]

    /**
     * Returns the currently active prefix of the stride pool.
     *
     * The prefix grows slowly with time:
     * - early on we mostly use short runs
     * - later on we admit a broader variety of run lengths
     */
    fun getStrideIndices(commandIndex: Long): IntRange {
        val lastIndex = (commandIndex / 200_000L + 14L).toInt()
        return 0..min(lastIndex, stridePool.lastIndex)
    }

    /**
     * Plays the heuristic using the absolute safe cap implied by the brief.
     *
     * Because S < 1_000_000, a universal cap of 35_000_000 moves can never
     * exceed the allowed 35 * S limit for a legal board only if S were known to
     * equal the maximum bound. In the actual contest setting, a submission would
     * need a strategy-specific proof with respect to the *true* unknown S.
     *
     * In this repository the cap is used only for offline simulation and to keep
     * the heuristic bounded during experiments.
     */
    override fun play(game: BlindSnakeGame): Boolean = play(game, maxAreaBound = 1_000_000)

    fun play(game: BlindSnakeGame, maxAreaBound: Int = 1_000_000): Boolean {
        val maxMoves = 35L * maxAreaBound.toLong()
        var movesMade = 0L
        var commandIndex = 1L

        while (movesMade < maxMoves) {
            val activeRange = getStrideIndices(commandIndex)

            for (idx in activeRange) {
                val stride = stridePool[idx]

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
            }
        }

        return false
    }

    /**
     * Deterministic command generator used by the offline verifiers.
     */
    fun generateCommands(maxMoves: Long = 35_000_000L): List<Command> {
        val commands = ArrayList<Command>(min(maxMoves, 1_000_000L).toInt())
        var movesMade = 0L
        var commandIndex = 1L

        while (movesMade < maxMoves) {
            val activeRange = getStrideIndices(commandIndex)

            for (idx in activeRange) {
                val stride = stridePool[idx]

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
            }
        }

        return commands
    }
}
