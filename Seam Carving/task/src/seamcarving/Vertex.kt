package seamcarving

data class Vertex(val x: Int, val y: Int, val energy: Double) {

    val isValid: Boolean
    get() = this != InvalidVertex

    override fun toString(): String {
        return "{ $x, $y : $energy }"
    }

    companion object {

        val InvalidVertex = Vertex(-1, -1, Double.MAX_VALUE)

        fun getVertex(energyMatrix: EnergyMatrix, x: Int, y: Int): Vertex {
            if (x < 0 || x > energyMatrix.width - 1 || y < 0 || y > energyMatrix.height - 1) {
                return InvalidVertex
            }
            return Vertex(x, y, energyMatrix.getEnergyAt(x, y))
        }

    }

}