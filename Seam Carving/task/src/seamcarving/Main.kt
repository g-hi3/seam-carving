package seamcarving

import java.awt.Color
import java.awt.image.BufferedImage
import java.io.FileOutputStream
import javax.imageio.ImageIO

fun main() {
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
