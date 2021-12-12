package seamcarving

import seamcarving.fromsolution.ImageEnergy
import java.awt.Color
import java.io.File
import javax.imageio.ImageIO

fun main(args: Array<String>) {
    val inFile = args[args.indexOf("-in") + 1]
    val outFile = args[args.indexOf("-out") + 1]
    val image = ImageIO.read(File(inFile))
    val energyMatrix = EnergyMatrix(image)
    val optimalSeam = getOptimalSeam(energyMatrix)
    for (y in 0 until image.height) {
        val vertex = optimalSeam.getVertexAt(y)
        image.setRGB(vertex.x, y, Color.RED.rgb)
    }
    /*val weights = getWeights(energyMatrix)
    val start = getStart()
    val end = getEnd()
    val shortestPathTree = dijkstra(Graph(weights), start)
    val shortestPath = shortestPath(shortestPathTree, start, end)
    for ((x, y) in shortestPath) {
        if (x >= 0 && x < image.width
            && y >= 0 && y < image.width)
        image.setRGB(x, y, Color.RED.rgb)
    }*/
    /*for (y in 0 until image.height) {
        val shortestPathX = shortestPath[y + 1].first
        println("y($y): $shortestPathX")
        for (x in 0 until image.width) {
            val pixelEnergy = energyMatrix.getEnergyAt(x, y)
            val intensity = (255.0 * pixelEnergy / energyMatrix.maxEnergy).toInt()
            if (x != shortestPathX) {
                val intensityColor = Color(intensity, intensity, intensity)
                image.setRGB(x, y, intensityColor.rgb)
            } else {
                image.setRGB(x, y, Color.RED.rgb)
            }
        }
    }*/
    //val energyOfImage = ImageEnergy(image).calcEnergyOfImage()
    //val seam = seamcarving.fromsolution.Seam(energyOfImage)
    //seam.markSeam(image)
    ImageIO.write(image, "png", File(outFile))
}

private fun getWeights(matrix: EnergyMatrix): Map<Pair<Pair<Int, Int>, Pair<Int, Int>>, Double> {
    val firstWeights = getFirstWeights(matrix.width)
    val finalWeights = getFinalWeights(matrix.width, matrix.height)
    val intermediateWeights = mutableMapOf<Pair<Pair<Int, Int>, Pair<Int, Int>>, Double>()
    for (y in 0 until matrix.height) {
        for (x in 0 until matrix.width) {
            intermediateWeights += getWeightsAt(matrix, x, y)
        }
    }
    return firstWeights + intermediateWeights + finalWeights
}

private fun getFirstWeights(width: Int): Map<Pair<Pair<Int, Int>, Pair<Int, Int>>, Double> {
    val firstNode = getStart()
    val firstWeights = mutableMapOf<Pair<Pair<Int, Int>, Pair<Int, Int>>, Double>()
    for (x in 0 until width) {
        val startNode = Pair(x, 0)
        val edge = Pair(firstNode, startNode)
        firstWeights[edge] = 0.0
    }
    return firstWeights
}

private fun getWeightsAt(matrix: EnergyMatrix, x: Int, y: Int): Map<Pair<Pair<Int, Int>, Pair<Int, Int>>, Double> {
    if (y == matrix.height - 1) {
        return emptyMap()
    }
    val fromNode = Pair(x, y)
    val weights = mutableMapOf<Pair<Pair<Int, Int>, Pair<Int, Int>>, Double>()
    val centerNode = Pair(x, y + 1)
    val centerEdge = Pair(fromNode, centerNode)
    weights[centerEdge] = matrix.getEnergyAt(x, y + 1)
    if (x > 0) {
        val leftNode = Pair(x - 1, y + 1)
        val leftEdge = Pair(fromNode, leftNode)
        weights[leftEdge] = matrix.getEnergyAt(x - 1, y + 1)
    }
    if (x < matrix.width - 1) {
        val rightNode = Pair(x + 1, y + 1)
        val rightEdge = Pair(fromNode, rightNode)
        weights[rightEdge] = matrix.getEnergyAt(x + 1, y + 1)
    }
    return weights
}

private fun getFinalWeights(width: Int, height: Int): Map<Pair<Pair<Int, Int>, Pair<Int, Int>>, Double> {
    val finalNode = getEnd()
    val finalWeights = mutableMapOf<Pair<Pair<Int, Int>, Pair<Int, Int>>, Double>()
    for (x in 0 until width) {
        val endNode = Pair(x, height - 1)
        val edge = Pair(endNode, finalNode)
        finalWeights[edge] = 0.0
    }
    return finalWeights
}

private fun getStart() = Pair(Int.MIN_VALUE, Int.MIN_VALUE)

private fun getEnd() = Pair(Int.MAX_VALUE, Int.MAX_VALUE)

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