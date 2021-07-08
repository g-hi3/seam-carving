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
    val allSeams = getAllSeams(energyMatrix)
    val optimalSeam = getOptimalSeam(allSeams)
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

fun getOptimalSeam(seams: List<Seam>): Seam {
    return seams.minByOrNull { it.energy }!!
}

fun getAllSeams(energyMatrix: EnergyMatrix): List<Seam> {
    val allSeams = mutableListOf<Seam>()
    for (x in 0 until energyMatrix.width) {
        val seams = getSeams(energyMatrix, x, energyMatrix.height - 1)
        allSeams.addAll(seams)
    }
    return allSeams
}

// TODO: This function returns way too many (and also invalid) seams
fun getSeams(energyMatrix: EnergyMatrix, x: Int, y: Int): List<Seam> {
    val energy = energyMatrix.getEnergyAt(x, y)
    val vertex = Vertex(x, y, energy)
    val seams = mutableListOf<Seam>()
    if (y == 0) {
        seams.add(Seam(mutableListOf(vertex)))
        return seams
    }
    if (x > 0) {
        for (seam in getSeams(energyMatrix, x - 1, y - 1)) {
            seam.appendVertex(vertex)
            seams.add(seam)
        }
    }
    for (seam in getSeams(energyMatrix, x, y - 1)) {
        seam.appendVertex(vertex)
        seams.add(seam)
    }
    if (x < energyMatrix.width - 1) {
        for (seam in getSeams(energyMatrix, x + 1, y - 1)) {
            seam.appendVertex(vertex)
            seams.add(seam)
        }
    }
    return seams
}

