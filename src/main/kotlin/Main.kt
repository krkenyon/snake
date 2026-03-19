

import verifier.ExhaustiveVerifier
import verifier.PrimeBoardChecker
import verifier.SelectedBoardVerifier

fun main(args: Array<String>) {
    when (args.getOrNull(0)) {
        "verify-exhaustive" -> ExhaustiveVerifier.verifyAll()
        "verify-selected" -> SelectedBoardVerifier.verifyQuick()
        "check" -> {
            val w = args.getOrNull(1)?.toIntOrNull()
            val h = args.getOrNull(2)?.toIntOrNull()

            if (w == null || h == null) {
                println("Usage: check <width> <height>")
                return
            }

            val area = w * h
            val result = PrimeBoardChecker.checkBoard(w, h, 1000L * area)

            println("Board: ${w}x$h")
            println("Area: $area")
            println("Visited: ${result.coverage}/$area")
            println("Steps used: ${result.stepsUsed}")
            println("Success: ${result.success}")
            println("Ratio: ${"%.4f".format(result.stepsUsed.toDouble() / area)}")
        }
        else -> {
            println("Use one of:")
            println("  verify-exhaustive")
            println("  verify-selected")
            println("  check <width> <height>")
        }
    }
}