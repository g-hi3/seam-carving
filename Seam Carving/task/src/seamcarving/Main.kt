package seamcarving

import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import java.io.FileOutputStream
import javax.imageio.ImageIO

fun main(args: Array<String>) {
    val inFile = args[args.indexOf("-in") + 1]
    val outFile = args[args.indexOf("-out") + 1]
    val inImage = ImageIO.read(File(inFile))
    val outImage = BufferedImage(inImage.width, inImage.height, BufferedImage.TYPE_INT_RGB)
    for (x in 0 until inImage.width) {
        for (y in 0 until inImage.height) {
            val color = Color(inImage.getRGB(x, y))
            val negativeColor = Color(255 - color.red, 255 - color.green, 255 - color.blue)
            outImage.setRGB(x, y, negativeColor.rgb)
        }
    }
    ImageIO.write(outImage, "png", File(outFile))
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