package seamcarving

class Seam {

    private val vertices: MutableList<Vertex> = mutableListOf()

    val energy: Double
        get() = vertices.sumOf { it.energy }

    var isValid: Boolean
        private set

    init {
        isValid = true
    }

    fun appendVertex(vertex: Vertex) = vertices.add(vertex)

    fun getVertexAt(index: Int): Vertex = vertices[index]

    override fun toString(): String {
        return vertices.joinToString(" -> ") + " : " + energy
    }

    companion object {

        val Invalid: Seam = Seam()

        init {
            Invalid.isValid = false
        }
    }

}
