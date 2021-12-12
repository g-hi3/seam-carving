package seamcarving

// Source: https://www.atomiccommits.io/dijkstras-algorithm-in-kotlin/

fun <T> List<Pair<T, T>>.getUniqueValuesFromPairs(): Set<T> = this
    .map { (a, b) -> listOf(a, b) }
    .flatten()
    .toSet()

fun <T> List<Pair<T, T>>.getUniqueValuesFromPairs(predicate: (T) -> Boolean): Set<T> = this
    .map { (a, b) -> listOf(a, b) }
    .flatten()
    .filter(predicate)
    .toSet()

data class Graph<T>(
    val vertices: Set<T>,
    val edges: Map<T, Set<T>>,
    val weights: Map<Pair<T, T>, Double>
) {

    constructor(weights: Map<Pair<T, T>, Double>): this(
        vertices = weights.keys.toList().getUniqueValuesFromPairs(),
        edges = weights.keys
            .groupBy { it.first }
            .mapValues { it.value.getUniqueValuesFromPairs { x -> x !== it.key } }
            .withDefault { emptySet() },
        weights = weights
    )
}

fun <T> dijkstra(graph: Graph<T>, start: T): Map<T, T?> {
    val subsetOfVerticesWithKnownTrueDistance: MutableSet<T> = mutableSetOf()

    val delta = graph.vertices
        .associateWith { Double.MAX_VALUE }
        .toMutableMap()
    delta[start] = 0.0

    val previous: MutableMap<T, T?> = graph.vertices
        .associateWith { null }
        .toMutableMap()

    while (subsetOfVerticesWithKnownTrueDistance != graph.vertices) {
        val v: T = delta
            .filter { !subsetOfVerticesWithKnownTrueDistance.contains(it.key) }
            .minByOrNull { it.value }!!
            .key

        graph.edges.getValue(v).minus(subsetOfVerticesWithKnownTrueDistance).forEach { neighbor ->
            val newPath = delta.getValue(v) + graph.weights.getValue(Pair(v, neighbor))

            if (newPath < delta.getValue(neighbor)) {
                delta[neighbor] = newPath
                previous[neighbor] = v
            }
        }

        subsetOfVerticesWithKnownTrueDistance.add(v)
    }

    return previous.toMap()
}

fun <T> shortestPath(shortestPathTree: Map<T, T?>, start: T, end: T): List<T> {
    fun pathTo(start: T, end: T): List<T> {
        if (shortestPathTree[end] == null) return listOf(end)
        return listOf(pathTo(start, shortestPathTree[end]!!), listOf(end)).flatten()
    }

    return pathTo(start, end)
}