package seamcarving.fromsolution

import seamcarving.fromsolution.Helper.getMinUpper
import seamcarving.fromsolution.Helper.getRowWithIndex
import java.awt.Color
import java.awt.image.BufferedImage

class Seam(private val energies: Array<Array<Double>>) {
    private var coordinates: Array<Pair<Int, Int>>

    // Create 2D array of weights by dynamic programming approach
    // https://en.m.wikipedia.org/wiki/Seam_carving#Dynamic_programming
    init {
        val weights = Array(energies.size) { Array(energies[0].size) { -1.0 } }
        for (x in energies.indices) {
            weights[x][0] = energies[x][0]
        }
        for (y in 1 until energies[0].size) {
            for (x in energies.indices) {
                val minUpper = getMinUpper(weights, x, y)
                weights[x][y] = weights[minUpper.first][minUpper.second] + energies[x][y]
            }
        }

        coordinates = constructSeam(weights)
    }

    // Constructs minimum seam from given weights by greedy alg from bottom minimum to top
    private fun constructSeam(weights: Array<Array<Double>>): Array<Pair<Int, Int>> {
        val seam = Array(weights[0].size) { Pair(-1, -1) }
        var indexOfMin = getRowWithIndex(weights, weights[0].size - 1)
            .withIndex().minByOrNull { (_, w) -> w }!!.index
        seam[weights[0].size - 1] = Pair(indexOfMin, weights[0].size - 1)

        for (y in weights[0].size - 1 downTo 1) {
            indexOfMin = getMinUpper(weights, indexOfMin, y).first
            seam[y - 1] = Pair(indexOfMin, y - 1)
        }
        return seam
    }

    fun markSeam(image: BufferedImage) {
        for (pair in coordinates) {
            image.setRGB(pair.first, pair.second, Color.RED.rgb)
        }
    }
}