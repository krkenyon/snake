package verifier

/**
 * Heavy verification suite.
 *
 */
object ExhaustiveVerifier {

    fun verifyAll(maxArea: Int = 1_000_000) {
        val runName = "exhaustive"
        ResultWriter.clear(runName)
        ResultWriter.initCsv(runName, "width,height,area,success,coverage,stepsUsed,ratio")

        var total = 0
        var failures = 0
        var worstRatio = 0.0
        var worstCase: PrimeBoardChecker.Result? = null

        for (w in 1..maxArea) {
            for (h in 1..(maxArea / w)) {
                val area = w * h
                val maxSteps = 35L * area
                val result = PrimeBoardChecker.checkBoard(w, h, maxSteps)

                total++

                val ratio = result.stepsUsed.toDouble() / area.toDouble()
                ResultWriter.appendCsvRow(
                    runName,
                    "${w},${h},${area},${result.success},${result.coverage},${result.stepsUsed},$ratio"
                )

                if (!result.success) {
                    failures++
                    val line = "FAIL ${w}x${h} coverage=${result.coverage}/$area steps=${result.stepsUsed}"
                    println(line)
                    ResultWriter.appendFailure(runName, line)
                } else if (ratio > worstRatio) {
                    worstRatio = ratio
                    worstCase = result
                }

                if (total % 1000 == 0) {
                    println("Checked $total boards, failures=$failures")
                }
            }
        }

        val summary = buildString {
            appendLine("==== EXHAUSTIVE VERIFICATION SUMMARY ====")
            appendLine("Boards tested: $total")
            appendLine("Failures: $failures")
            appendLine("Worst ratio: ${"%.6f".format(worstRatio)}")
            worstCase?.let {
                appendLine("Worst case board: ${it.width}x${it.height}")
                appendLine("Coverage: ${it.coverage}/${it.width * it.height}")
                appendLine("Steps used: ${it.stepsUsed}")
            }
        }

        println(summary)
        ResultWriter.writeSummary(runName, summary)
    }
}
