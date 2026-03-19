package strategy

/*
Strategy:

We do not know:
- grid size (A, B)
- current position
- apple position

We only receive feedback when the apple is found.

Key idea:
At least one dimension of the grid is <= sqrt(S), where S = A * B.
However, we do not know whether the smaller dimension is the width or the height,
so in each phase we try both width guesses and height guesses.

We iterate over increasing guesses of S (doubling M),
and for each phase we try all possible small widths and heights.

For each width guess w:
- move RIGHT (w - 1) times, then DOWN once
- repeat enough times to cover all rows

If w equals the true width, this scans the entire grid.

Similarly for height.

Once M >= S, we are guaranteed to try the correct dimension,
so the entire grid is covered and the apple is found.

Complexity:
Total number of moves is O(S * sqrt(S)).

This guarantees finding the apple without knowing any grid information.
*/

/*
Correctness:

We do not know the grid dimensions A, B, or the current position.
We only receive feedback when the apple is found, so the algorithm must
follow a predetermined sequence of moves.

Let S = A * B. At least one of A or B is ≤ sqrt(S).

The algorithm proceeds in phases with area guesses M = 1, 2, 4, 8, ...

In phase M, we try all width guesses w = 1..⌊sqrt(M)⌋ and height guesses h = 1..⌊sqrt(M)⌋.

If M ≥ S, then the smaller dimension (either A or B) is included among the guesses.
If w = A is tried, the movement pattern:
    (RIGHT repeated A-1 times, then DOWN once)
repeated enough times will visit every cell in the grid.

Thus, once M ≥ S, the algorithm must find the apple.

Complexity:

For a fixed width guess w, the number of moves is:
    ceil(M / w) * w ≤ M + w

Summing over all w ≤ sqrt(M):
    total work per phase = O(M * sqrt(M))

Since M doubles each phase, the total work is dominated by the last phase:
    total steps = O(S * sqrt(S))

Thus, the algorithm always finds the apple using O(S√S) moves.
*/

class ProvableStrategy {

    enum class Direction {
        UP, DOWN, LEFT, RIGHT
    }


    fun playGame(sendSignal: (Direction) -> Boolean) {
        var areaGuess = 1

        while (true) {
            val maxSmallDimension = kotlin.math.sqrt(areaGuess.toDouble()).toInt()

            // Try all possible widths
            for (w in 1..maxSmallDimension) {
                val reps = (areaGuess + w - 1) / w

                repeat(reps) {
                    repeat(w - 1) {
                        if (sendSignal(Direction.RIGHT)) return
                    }
                    if (sendSignal(Direction.DOWN)) return
                }
            }

            // Try all possible heights
            for (h in 1..maxSmallDimension) {
                val reps = (areaGuess + h - 1) / h

                repeat(reps) {
                    repeat(h - 1) {
                        if (sendSignal(Direction.DOWN)) return
                    }
                    if (sendSignal(Direction.RIGHT)) return
                }
            }

            areaGuess *= 2
        }
    }
}