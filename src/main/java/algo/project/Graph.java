package algo.project;

import java.util.*;

public class Graph {
    // Map to store the adjacency list of the graph
    private Map<Vertex, List<Edge>> adjacencyList;

    // Constructor to initialize the graph
    public Graph() {
        this.adjacencyList = new HashMap<>();
    }

    // Method to add a vertex to the graph
    public void addVertex(Vertex vertex) {
        // Add the vertex to the adjacency list if it's not already present
        adjacencyList.putIfAbsent(vertex, new ArrayList<>());
    }

    // Method to add an edge between two vertices
    public void addEdge(Vertex fromVertex, Vertex toVertex, double weight) {
        // Ensure both vertices are present in the adjacency list
        addVertex(fromVertex);
        addVertex(toVertex);
        // Add the edge from fromVertex to toVertex with the given weight
        adjacencyList.get(fromVertex).add(new Edge(fromVertex, toVertex, weight));
    }

    // Method to add a bidirectional edge between two vertices
    public void addBidirectionalEdge(Vertex fromVertex, Vertex toVertex, double weight) {
        // Ensure both vertices are present in the adjacency list
        addVertex(fromVertex);
        addVertex(toVertex);
        // Add the edge from fromVertex to toVertex with the given weight
        adjacencyList.get(fromVertex).add(new Edge(fromVertex, toVertex, weight));
        // Add the edge from toVertex to fromVertex with the given weight
        adjacencyList.get(toVertex).add(new Edge(toVertex, fromVertex, weight));
    }


    // Method to get all vertices in the graph
    public List<Vertex> getVertices() {
        // Return a list of all vertices from the keys of the adjacency list
        return new ArrayList<>(adjacencyList.keySet());
    }

    // Method to get all edges associated with a given vertex
    public List<Edge> getEdges(Vertex vertex) {
        // Return the list of edges for the vertex, or an empty list if the vertex is not found
        return adjacencyList.getOrDefault(vertex, new ArrayList<>());
    }

    // Method to check if there is an edge from a given vertex to another vertex
    public boolean checkEdgeExistence(Vertex from, Vertex to) {
        // Check if there is any edge from 'from' vertex to 'to' vertex
        return adjacencyList.getOrDefault(from, new ArrayList<>())
                .stream()
                .anyMatch(edge -> edge.getToVertex().equals(to));
    }

    // Method to get a string representation of the graph
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        // Iterate through each vertex in the adjacency list
        for (Vertex vertex : adjacencyList.keySet()) {
            // Append the vertex and its edges to the string builder
            sb.append(vertex).append(": ");
            for (Edge edge : adjacencyList.get(vertex)) {
                sb.append(edge).append(", ");
            }
            sb.append("\n");
        }
        // Return the string representation of the graph
        return sb.toString();
    }
}