package seamcarving

data class Seam(private val vertices: MutableList<Vertex>) {

    val energy: Double
        get() = vertices.sumByDouble { it.energy }

    fun appendVertex(vertex: Vertex) = vertices.add(vertex)

    fun getVertexAt(index: Int): Vertex = vertices[index]

    override fun toString(): String {
        return vertices.joinToString(" -> ") + " : " + energy
    }

}