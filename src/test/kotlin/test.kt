import kotlin.random.Random

data class State(
    val width: Int,
    val height: Int,
    var x: Int,
    var y: Int,
    val appleX: Int,
    val appleY: Int,
    var steps: Int = 0,
    var found: Boolean = false
)

fun move(state: State, direction: Direction) {
    when (direction) {
        Direction.UP -> state.y = (state.y - 1 + state.height) % state.height
        Direction.DOWN -> state.y = (state.y + 1) % state.height
        Direction.LEFT -> state.x = (state.x - 1 + state.width) % state.width
        Direction.RIGHT -> state.x = (state.x + 1) % state.width
    }
}

fun makeSendSignal(state: State): (Direction) -> Boolean = { direction ->
    state.steps++
    move(state, direction)

    if (state.x == state.appleX && state.y == state.appleY) {
        state.found = true
        true
    } else {
        false
    }
}

fun runSingleTest(
    width: Int,
    height: Int,
    startX: Int,
    startY: Int,
    appleX: Int,
    appleY: Int
) {
    val state = State(width, height, startX, startY, appleX, appleY)
    val sendSignal = makeSendSignal(state)

    playGame(sendSignal)

    val area = width * height

    println(
        "Board=${width}x${height}, " +
                "start=($startX,$startY), apple=($appleX,$appleY), " +
                "found=${state.found}, steps=${state.steps}, area=$area"
    )
}

fun runRandomTests(testCount: Int, maxWidth: Int, maxHeight: Int, seed: Int = 42) {
    val random = Random(seed)

    repeat(testCount) { index ->
        val width = random.nextInt(1, maxWidth + 1)
        val height = random.nextInt(1, maxHeight + 1)

        val startX = random.nextInt(width)
        val startY = random.nextInt(height)
        val appleX = random.nextInt(width)
        val appleY = random.nextInt(height)

        val state = State(width, height, startX, startY, appleX, appleY)
        val sendSignal = makeSendSignal(state)

        playGame(sendSignal)

        val area = width * height
        println(
            "Random test ${index + 1}: " +
                    "Board=${width}x${height}, found=${state.found}, steps=${state.steps}, area=$area"
        )
    }
}

fun main() {
    // Fixed edge cases
    runSingleTest(width = 1, height = 20, startX = 0, startY = 7, appleX = 0, appleY = 13)
    runSingleTest(width = 20, height = 1, startX = 9, startY = 0, appleX = 3, appleY = 0)
    runSingleTest(width = 2, height = 2, startX = 0, startY = 0, appleX = 1, appleY = 1)
    runSingleTest(width = 7, height = 2, startX = 3, startY = 1, appleX = 6, appleY = 0)
    runSingleTest(width = 10, height = 10, startX = 4, startY = 4, appleX = 9, appleY = 2)

    println()
    println("Random tests:")
    runRandomTests(testCount = 10, maxWidth = 20, maxHeight = 20)
}