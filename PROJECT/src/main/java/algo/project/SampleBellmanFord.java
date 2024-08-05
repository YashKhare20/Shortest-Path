package algo.project;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * This class implements the Bellman-Ford algorithm for finding the shortest paths
 * in a negative graph. It also includes methods to detect negative weight cycles.
 */
public class SampleBellmanFord {

    private SampleNegativeGraph negativeGraph;  // The negative graph on which Bellman-Ford is to be run.

    /**
     * Constructor to initialize the Bellman-Ford algorithm with a negative graph.
     *
     * @param negativeGraph the negative graph to be used for the Bellman-Ford algorithm.
     */
    public SampleBellmanFord(SampleNegativeGraph negativeGraph) {
        this.negativeGraph = new SampleNegativeGraph(negativeGraph);  // Make a deep copy of the current NegativeGraph.
    }

    /**
     * Executes the Bellman-Ford algorithm to find the shortest paths from a given source vertex.
     *
     * @param source the source vertex from which to find the shortest paths.
     * @return true if a negative weight cycle exists, false otherwise.
     */
    public boolean run(SampleVertex source) {
        // Initialize distances from the source
        for (SampleVertex vertex : negativeGraph.getAllVertices().values()) {
            vertex.setDistance(Double.POSITIVE_INFINITY);  // Set initial distance to positive infinity.
            vertex.setParent(null);  // No parent initially.
        }
        source.setDistance(0);  // Set the source vertex distance to 0.

        // Relax edges repeatedly
        for (int i = 1; i < negativeGraph.getAllVertices().size(); i++) {
            for (SampleEdge edge : negativeGraph.getEdges()) {
                SampleVertex u = edge.getVertexF();  // Get the source vertex of the edge.
                SampleVertex v = edge.getVertexT();  // Get the destination vertex of the edge.
                double weight = edge.getWeight();  // Get the weight of the edge.

                // If the distance to the destination vertex can be shortened via this edge
                if (u.getDistance() != Double.POSITIVE_INFINITY && u.getDistance() + weight < v.getDistance()) {
                    v.setDistance(u.getDistance() + weight);  // Update the distance of the destination vertex.
                    v.setParent(u);  // Update the parent of the destination vertex.
                }
            }
        }

        // Check for negative weight cycles
        boolean hasNegativeCycle = false;  // Flag to indicate if a negative weight cycle exists.
        for (SampleEdge edge : negativeGraph.getEdges()) {
            SampleVertex u = edge.getVertexF();  // Get the source vertex of the edge.
            SampleVertex v = edge.getVertexT();  // Get the destination vertex of the edge.
            double weight = edge.getWeight();  // Get the weight of the edge.

            // If we can still relax an edge, a negative cycle exists
            if (u.getDistance() != Double.POSITIVE_INFINITY && u.getDistance() + weight < v.getDistance()) {
                hasNegativeCycle = true;  // Set the flag to true indicating a negative cycle exists.
                break;  // No need to check further, exit the loop.
            }
        }

        return hasNegativeCycle;  // Return whether a negative weight cycle was found.
    }

    /**
     * Finds a negative weight cycle in the graph, if one exists.
     *
     * @return the list of vertices representing the negative cycle, or an empty list if no negative cycle exists.
     */
    public List<SampleVertex> getNegativeCycle() {
        List<SampleVertex> cycle = new ArrayList<>();  // List to store the vertices in the cycle.
        Set<SampleVertex> visited = new HashSet<>();  // Set to keep track of visited vertices.

        // Step 1: Find a vertex that is part of the negative cycle.
        SampleVertex cycleStart = null;  // Vertex that starts the negative cycle.
        for (SampleEdge edge : negativeGraph.getEdges()) {
            SampleVertex u = edge.getVertexF();  // Get the source vertex of the edge.
            SampleVertex v = edge.getVertexT();  // Get the destination vertex of the edge.
            double weight = edge.getWeight();  // Get the weight of the edge.

            // If the edge can still be relaxed, a negative cycle exists.
            if (u.getDistance() != Double.POSITIVE_INFINITY && u.getDistance() + weight < v.getDistance()) {
                cycleStart = u;  // Set this vertex as the start of the negative cycle.
                break;  // Exit the loop as we found a vertex part of the negative cycle.
            }
        }

        // Step 2: Trace back the cycle.
        if (cycleStart != null) {
            // Move `vertices.size()` steps back to ensure we are within the cycle.
            for (int i = 0; i < negativeGraph.getAllVertices().size(); i++) {
                cycleStart = cycleStart.getParent();  // Move to the parent vertex.
            }

            // Traverse the cycle and collect vertices.
            SampleVertex current = cycleStart;  // Start from the identified vertex in the cycle.
            do {
                if (!visited.contains(current)) {
                    cycle.add(current);  // Add the vertex to the cycle list.
                    visited.add(current);  // Mark this vertex as visited.
                }
                current = current.getParent();  // Move to the next vertex in the cycle.
            } while (current != cycleStart);  // Continue until we return to the start vertex.

            // Optionally, add the start vertex to complete the cycle.
            cycle.add(cycleStart);
        }

        return cycle;  // Return the list of vertices forming the negative cycle.
    }
}
