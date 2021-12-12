package seamcarving.fromsolution

object Helper {

    fun getRowWithIndex(array2D: Array<Array<Double>>, index: Int): Array<Double> {
        return Array(array2D.size) { x -> array2D[x][index] }
    }

    // Return index of cell with minimum value from upper three cells
    fun getMinUpper(weights: Array<Array<Double>>, x: Int, y: Int): Pair<Int, Int> {
        val uppers = when (x) {
            0 -> arrayOf(Pair(x, y - 1), Pair(x + 1, y - 1))
            weights.size - 1 -> arrayOf(Pair(x - 1, y - 1), Pair(x, y - 1))
            else -> arrayOf(Pair(x - 1, y - 1), Pair(x, y - 1), Pair(x + 1, y - 1))
        }
        var index = Pair(-1, -1)
        var min = Double.MAX_VALUE
        for (pair in uppers) {
            if (weights[pair.first][pair.second] < min) {
                min = weights[pair.first][pair.second]
                index = pair
            }
        }
        return index
    }
}