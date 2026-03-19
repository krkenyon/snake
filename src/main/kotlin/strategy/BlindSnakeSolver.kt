package strategy

import game.BlindSnakeGame

/**
 * Common entry point for any blind-snake strategy.
 */
fun interface BlindSnakeSolver {
    /**
     * Runs the strategy until it finds the apple or decides to stop.
     *
     * Returning false means the strategy exhausted its own budget or gave up;
     * it does not necessarily mean the board is impossible.
     */
    fun play(game: BlindSnakeGame): Boolean
}
