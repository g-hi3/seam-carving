package seamcarving

import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import java.io.FileOutputStream
import javax.imageio.ImageIO
import kotlin.math.pow
import kotlin.math.sqrt

fun main(args: Array<String>) {
    val inFile = args[args.indexOf("-in") + 1]
    val outFile = args[args.indexOf("-out") + 1]
    val inImage = ImageIO.read(File(inFile))
    val outImage = BufferedImage(inImage.width, inImage.height, BufferedImage.TYPE_INT_RGB)
    val maxEnergyValue = getMaxEnergy(inImage)
    for (x in 0 until inImage.width) {
        for (y in 0 until inImage.height) {
            val pixelEnergy = getPixelEnergy(inImage, x, y)
            val intensity = (255.0 * pixelEnergy / maxEnergyValue).toInt()
            val intensityColor = Color(intensity, intensity, intensity)
            outImage.setRGB(x, y, intensityColor.rgb)
        }
    }
    ImageIO.write(outImage, "png", File(outFile))
}

fun getMaxEnergy(image: BufferedImage): Double {
    var highestEnergy = 0.0
    for (x in 0 until image.width) {
        for (y in 0 until image.height) {
            val energy = getPixelEnergy(image, x, y)
            if (energy > highestEnergy) {
                highestEnergy = energy
            }
        }
    }
    return highestEnergy
}

fun getPixelEnergy(image: BufferedImage, x: Int, y: Int): Double {
    val horizontalDelta = getHorizontalDelta(image, x, y)
    val verticalDelta = getVerticalDelta(image, x, y)
    return sqrt(horizontalDelta + verticalDelta)
}

fun getHorizontalDelta(image: BufferedImage, x: Int, y: Int): Double {
    val actualX = when (x) { 0 -> 1; image.width - 1 -> image.width - 2; else -> x }
    val before = Color(image.getRGB(actualX - 1, y))
    val after = Color(image.getRGB(actualX + 1, y))
    return (before.red - after.red).toDouble().pow(2) +
            (before.green - after.green).toDouble().pow(2) +
            (before.blue - after.blue).toDouble().pow(2)
}

fun getVerticalDelta(image: BufferedImage, x: Int, y: Int): Double {
    val actualY = when (y) { 0 -> 1; image.height - 1 -> image.height - 2; else -> y }
    val before = Color(image.getRGB(x, actualY - 1))
    val after = Color(image.getRGB(x, actualY + 1))
    return (before.red - after.red).toDouble().pow(2) +
            (before.green - after.green).toDouble().pow(2) +
            (before.blue - after.blue).toDouble().pow(2)
}

fun getNegativeColor(image: BufferedImage, x: Int, y: Int): Color {
    val color = Color(image.getRGB(x, y))
    return Color(255 - color.red, 255 - color.green, 255 - color.blue)
}

fun createRectangle() {
    println("Enter rectangle width:")
    val width = readLine()!!.toInt()
    println("Enter rectangle height:")
    val height = readLine()!!.toInt()
    println("Enter output image name:")
    val outputImageName = readLine()!!
    val bufferedImage = BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)
    bufferedImage.setRGB(0, 0, width, height, IntArray(width * height) { Color.BLACK.rgb }, 0, 0)
    for (x in 0 until width) {
        bufferedImage.setRGB(x, x * height / width, Color.RED.rgb)
        bufferedImage.setRGB(x, height - x * height / width - 1, Color.RED.rgb)
    }
    val output = FileOutputStream(outputImageName)
    ImageIO.write(bufferedImage, "png", output)
}