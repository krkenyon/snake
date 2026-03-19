package verifier

import kotlin.random.Random

object SelectedBoardVerifier {

    fun verifyQuick(maxArea: Int = 1_000_000) {
        runSuite("selected_quick", buildList {
            // small degenerate boards
            for (s in 1..10_000) {
                add(1 to s)
                add(s to 1)
            }

            // squares / near-squares
            var d = 1
            while (d.toLong() * d.toLong() <= maxArea.toLong()) {
                add(d to d)
                if (d + 1 <= maxArea / d) add(d to (d + 1))
                if (d > 1) add(d to (d - 1))
                d++
            }

            // powers of two
            var w = 1
            while (w <= maxArea) {
                var h = 1
                while (h <= maxArea / w) {
                    add(w to h)
                    if (h > maxArea / 2) break
                    h *= 2
                }
                if (w > maxArea / 2) break
                w *= 2
            }

            // random boards
            val rng = Random(1)
            repeat(5000) {
                val wRand = rng.nextInt(1, 5000)
                val hMax = maxArea / wRand
                if (hMax >= 1) {
                    val hRand = rng.nextInt(1, hMax + 1)
                    add(wRand to hRand)
                }
            }
        })
    }

    private fun runSuite(runName: String, boards: List<Pair<Int, Int>>) {
        ResultWriter.clear(runName)
        ResultWriter.initCsv(runName, "width,height,area,success,coverage,stepsUsed,ratio")

        var total = 0
        var failures = 0
        var worstRatio = 0.0
        var worstCase: PrimeBoardChecker.Result? = null

        val seen = HashSet<Pair<Int, Int>>()

        for ((w, h) in boards) {
            if (!seen.add(w to h)) continue

            val area = w * h
            val result = PrimeBoardChecker.checkBoard(w, h, 35L * area)
            total++

            val ratio = result.stepsUsed.toDouble() / area.toDouble()
            ResultWriter.appendCsvRow(
                runName,
                "$w,$h,$area,${result.success},${result.coverage},${result.stepsUsed},$ratio"
            )

            if (!result.success) {
                failures++
                val line = "FAIL ${w}x$h coverage=${result.coverage}/$area steps=${result.stepsUsed}"
                println(line)
                ResultWriter.appendFailure(runName, line)
            } else if (ratio > worstRatio) {
                worstRatio = ratio
                worstCase = result
            }
        }

        val summary = buildString {
            appendLine("Boards tested: $total")
            appendLine("Failures: $failures")
            appendLine("Worst ratio: ${"%.6f".format(worstRatio)}")
            worstCase?.let {
                appendLine("Worst case board: ${it.width}x${it.height}")
                appendLine("Steps used: ${it.stepsUsed}")
            }
        }

        println(summary)
        ResultWriter.writeSummary(runName, summary)
    }
}