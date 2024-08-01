package algo.project;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BellmanFord {
    private Graph graph;
    private Map<Vertex, Integer> distances; // Distance from the source vertex to each vertex
    private Map<Vertex, Vertex> predecessors; // Predecessor of each vertex in the shortest path

    public BellmanFord(Graph graph) {
        this.graph = graph;
        distances = new HashMap<>();
        predecessors = new HashMap<>();
    }

    // Runs the Bellman-Ford algorithm from the given source vertex
    public boolean run(Vertex source) {
        initialize(source);

        // Relax all edges |V| - 1 times, where |V| is the number of vertices
        for (int i = 1; i < graph.getVertices().size(); i++) {
            for (Vertex u : graph.getVertices()) {
                for (Edge e : graph.getEdges(u)) {
                    relax(u, e.getToVertex(), e.getWeight());
                }
            }
        }

        // Check for negative weight cycles
        for (Vertex u : graph.getVertices()) {
            for (Edge e : graph.getEdges(u)) {
                if (distances.get(e.getToVertex()) > distances.get(u) + e.getWeight()) {
                    return true; // Negative cycle detected
                }
            }
        }

        return false; // No negative cycle found
    }

    // Initializes the distances and predecessors for the Bellman-Ford algorithm
    private void initialize(Vertex source) {
        for (Vertex v : graph.getVertices()) {
            distances.put(v, Integer.MAX_VALUE); // Set initial distances to infinity
            predecessors.put(v, null); // Set initial predecessors to null
        }
        distances.put(source, 0); // Distance to source is 0
    }

    // Relaxes the edge (u, v) with weight
    private void relax(Vertex u, Vertex v, int weight) {
        if (distances.get(u) != Integer.MAX_VALUE && distances.get(v) > distances.get(u) + weight) {
            distances.put(v, distances.get(u) + weight); // Update the distance to v
            predecessors.put(v, u); // Update the predecessor of v
        }
    }

    // Returns the vertices in the detected negative cycle, if any
    public List<Vertex> getNegativeCycle() {
        for (Vertex u : graph.getVertices()) {
            for (Edge e : graph.getEdges(u)) {
                if (distances.get(e.getToVertex()) > distances.get(u) + e.getWeight()) {
                    // Negative cycle detected, reconstruct the cycle
                    List<Vertex> cycle = new ArrayList<>();
                    Vertex v = e.getToVertex();

                    // Move one step back in the cycle to find the starting vertex
                    for (int i = 0; i < graph.getVertices().size(); i++) {
                        v = predecessors.get(v);
                    }

                    Vertex start = v;
                    do {
                        cycle.add(v);
                        v = predecessors.get(v);
                    } while (!v.equals(start));
                    cycle.add(start);
                    return cycle;
                }
            }
        }
        return null; // No negative cycle found
    }
}
