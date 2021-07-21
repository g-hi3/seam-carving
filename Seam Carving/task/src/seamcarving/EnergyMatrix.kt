package seamcarving

import java.awt.Color
import java.awt.image.BufferedImage
import kotlin.math.pow
import kotlin.math.sqrt

class EnergyMatrix(image: BufferedImage) {

    val width: Int = image.width
    val height: Int = image.height
    private val vertices: Array<Array<Vertex>>

    init {
        vertices = Array(height) { y ->
            Array(width) { x ->
                val energy = getPixelEnergy(image, x, y)
                Vertex(x, y, energy)
            }
        }
    }

    val maxEnergy: Double
        get() = vertices.flatten().maxOfOrNull { it.energy }!!

    fun getVertexAt(x: Int, y: Int): Vertex = vertices[y][x]

    fun getEnergyAt(x: Int, y: Int): Double = getVertexAt(x, y).energy

    private fun getPixelEnergy(image: BufferedImage, x: Int, y: Int): Double {
        val horizontalDelta = getHorizontalDelta(image, x, y)
        val verticalDelta = getVerticalDelta(image, x, y)
        return sqrt(horizontalDelta + verticalDelta)
    }

    private fun getHorizontalDelta(image: BufferedImage, x: Int, y: Int): Double {
        val actualX = getPositionWithRespectToBoundary(x, image.width)
        val before = Color(image.getRGB(actualX - 1, y))
        val after = Color(image.getRGB(actualX + 1, y))
        return getSquaredDelta(before, after)
    }

    private fun getVerticalDelta(image: BufferedImage, x: Int, y: Int): Double {
        val actualY = getPositionWithRespectToBoundary(y, image.height)
        val before = Color(image.getRGB(x, actualY - 1))
        val after = Color(image.getRGB(x, actualY + 1))
        return getSquaredDelta(before, after)
    }

    private fun getPositionWithRespectToBoundary(pos: Int, boundary: Int): Int {
        return when (pos) {
            0 -> 1
            boundary - 1 -> boundary - 2
            else -> pos
        }
    }

    private fun getSquaredDelta(before: Color, after: Color): Double {
        return getSquaredDelta(before.red, after.red) +
                getSquaredDelta(before.green, after.green) +
                getSquaredDelta(before.blue, after.blue)
    }

    private fun getSquaredDelta(before: Int, after: Int): Double {
        return (before - after).toDouble().pow(2)
    }

}
