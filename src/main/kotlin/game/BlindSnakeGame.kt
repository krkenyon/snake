package game

/**
 * Minimal API expected from the hidden game engine.
 *
 * Our program may call a predefined method such as
 * `sendSignal(direction)` and receive a boolean telling us whether the apple has
 * just been found. This file makes that contract explicit so the repository has
 * a small, reusable boundary between:
 *
 * 1. solver code, and
 * 2. whatever external engine eventually runs the solver.
 */
enum class Command {
    LEFT, RIGHT, UP, DOWN
}

fun interface BlindSnakeGame {
    /**
     * Sends one move to the engine.
     *
     * @return true exactly when this move lands on the apple.
     */
    fun sendSignal(command: Command): Boolean
}
