package seamcarving

data class Vertex(val x: Int, val y: Int, val energy: Double) {

    override fun toString(): String {
        return "{ $x, $y : $energy }"
    }

}
