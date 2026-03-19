package game

/**
 * Minimal API expected from the hidden game engine.
 *
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
