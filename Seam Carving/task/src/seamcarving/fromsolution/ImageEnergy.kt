package seamcarving.fromsolution

import java.awt.Color
import java.awt.image.BufferedImage
import kotlin.math.pow
import kotlin.math.sqrt

class ImageEnergy(private val image: BufferedImage) {

    fun calcEnergyOfImage(): Array<Array<Double>> {
        val energyOfImage = Array(image.width) { Array(image.height) { 0.0 } }
        for (x in 0 until image.width) {
            for (y in 0 until image.height) {
                energyOfImage[x][y] = computePixelEnergy(x, y, image)
            }
        }
        return energyOfImage
    }

    // Compute energy of pixel at (x, y) via dual-gradient energy function
    private fun computePixelEnergy(x: Int, y: Int, image: BufferedImage): Double {
        val cx = when (x) {
            0 -> 1
            image.width - 1 -> image.width - 2
            else -> x
        }
        val cy = when (y) {
            0 -> 1
            image.height - 1 -> image.height - 2
            else -> y
        }
        val leftPixel = Color(image.getRGB(cx - 1, y))
        val rightPixel = Color(image.getRGB(cx + 1, y))
        val upperPixel = Color(image.getRGB(x, cy - 1))
        val bottomPixel = Color(image.getRGB(x, cy + 1))
        val xGrad = (leftPixel.red - rightPixel.red.toDouble()).pow(2.0) +
                (leftPixel.green - rightPixel.green.toDouble()).pow(2.0) +
                (leftPixel.blue - rightPixel.blue.toDouble()).pow(2.0)
        val yGrad = (upperPixel.red - bottomPixel.red.toDouble()).pow(2.0) +
                (upperPixel.green - bottomPixel.green.toDouble()).pow(2.0) +
                (upperPixel.blue - bottomPixel.blue.toDouble()).pow(2.0)

        return sqrt(xGrad + yGrad)
    }
}