package seamcarving

import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

fun main(args: Array<String>) {
    val inFile = args[args.indexOf("-in") + 1]
    val outFile = args[args.indexOf("-out") + 1]
    val inImage = ImageIO.read(File(inFile))
    val outImage = BufferedImage(inImage.width, inImage.height, BufferedImage.TYPE_INT_RGB)
    val energyMatrix = EnergyMatrix(inImage)
    val optimalSeam = getOptimalSeam(energyMatrix)
    for (y in 0 until inImage.height) {
        val vertex = optimalSeam.getVertexAt(y)
        for (x in 0 until inImage.width) {
            val pixelEnergy = energyMatrix.getEnergyAt(x, y)
            val intensity = (255.0 * pixelEnergy / energyMatrix.maxEnergy).toInt()
            if (x != vertex.x) {
                val intensityColor = Color(intensity, intensity, intensity)
                outImage.setRGB(x, y, intensityColor.rgb)
            } else {
                outImage.setRGB(x, y, Color.RED.rgb)
            }
        }
    }
    ImageIO.write(outImage, "png", File(outFile))
}

private fun getOptimalSeam(energyMatrix: EnergyMatrix): Seam {
    val allSeams = mutableListOf<Seam>()
    val lastY = energyMatrix.height - 1
    for (x in 0 until energyMatrix.width) {
        val seam = getGoodSeam(energyMatrix, x, lastY)
        allSeams.add(seam)
    }
    return allSeams.minByOrNull { it.energy }!!
}

private fun getGoodSeam(energyMatrix: EnergyMatrix, x: Int, y: Int): Seam {
    if (x < 0 || x > energyMatrix.width - 1) {
        return Seam.Invalid
    }
    val vertex = energyMatrix.getVertexAt(x, y)
    if (y == 0) {
        val seam = Seam()
        seam.appendVertex(vertex)
        return seam
    }
    val centerSeam = getGoodSeam(energyMatrix, x, y - 1)
    var optimalSeam = centerSeam
    val leftSeam = getGoodSeam(energyMatrix, x - 1, y - 1)
    optimalSeam = getBetterSeam(optimalSeam, leftSeam)
    val rightSeam = getGoodSeam(energyMatrix, x + 1, y - 1)
    optimalSeam = getBetterSeam(optimalSeam, rightSeam)
    optimalSeam.appendVertex(vertex)
    return optimalSeam
}

private fun getBetterSeam(seamA: Seam, seamB: Seam): Seam {
    var optimalSeam = seamA
    if (seamB.isValid && seamB.energy < optimalSeam.energy) {
        optimalSeam = seamB
    }
    return optimalSeam
}