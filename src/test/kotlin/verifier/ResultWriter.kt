package verifier

import java.io.File

/**
 * Writes verifier artifacts to the local results directory.
 *
 * Keeping this in test code avoids implying that result-file generation is part
 * of the submission itself.
 */
object ResultWriter {
    private val resultsDir = File("results").apply { mkdirs() }

    fun clear(runName: String) {
        File(resultsDir, "${runName}_summary.txt").writeText("")
        File(resultsDir, "${runName}_failures.txt").writeText("")
    }

    fun appendFailure(runName: String, line: String) {
        File(resultsDir, "${runName}_failures.txt").appendText(line + "\n")
    }

    fun writeSummary(runName: String, text: String) {
        File(resultsDir, "${runName}_summary.txt").writeText(text)
    }

    fun appendCsvRow(runName: String, row: String) {
        File(resultsDir, "${runName}_data.csv").appendText(row + "\n")
    }

    fun initCsv(runName: String, header: String) {
        File(resultsDir, "${runName}_data.csv").writeText(header + "\n")
    }
}
