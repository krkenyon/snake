import cli.OfflineChecks

fun main(args: Array<String>) {
    when (args.getOrNull(0)?.lowercase()) {
        "check" -> runCheck(args)
        else -> printUsage()
    }
}

private fun runCheck(args: Array<String>) {
    if (args.size != 4 && args.size != 6) {
        printUsage()
        return
    }

    val strategy = args[1]
    val width = args[2].toInt()
    val height = args[3].toInt()

    if (args.size == 4) {
        val result = OfflineChecks.checkCoverage(strategy, width, height)
        println("strategy=${result.strategy}")
        println("board=${result.width}x${result.height}")
        println("mode=coverage")
        println("success=${result.success}")
        println("coverage=${result.coverage}/${result.area}")
        println("stepsUsed=${result.stepsUsed}")
        println("ratio=${result.stepsUsed/result.area}")
        return
    }

    val appleX = args[4].toInt()
    val appleY = args[5].toInt()

    val result = OfflineChecks.checkApple(strategy, width, height, appleX, appleY)
    println("strategy=${result.strategy}")
    println("board=${result.width}x${result.height}")
    println("mode=apple")
    println("apple=(${result.appleX},${result.appleY})")
    println("success=${result.success}")
    println("stepsUsed=${result.stepsUsed}")
}

private fun printUsage() {
    println("Usage:")
    println("  check <strategy> <width> <height>")
    println("  check <strategy> <width> <height> <appleX> <appleY>")
    println()
    println("Strategies:")
    println("  prime")
    println("  braid")
    println("  baseline")
    println()
    println("Examples:")
    println("  check prime 7 5")
    println("  check braid 7 5 3 4")
    println("  check baseline 9 9 0 0")
}