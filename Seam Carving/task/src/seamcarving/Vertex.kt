package seamcarving

data class Vertex(val x: Int, val y: Int, val energy: Double) {

    var connectedVertices: Set<Vertex> = setOf()

    override fun toString(): String {
        return "{ $x, $y : $energy }"
    }

}
