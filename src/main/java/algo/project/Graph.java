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
    public void addEdge(Vertex fromVertex, Vertex toVertex, int weight) {
        // Ensure both vertices are present in the adjacency list
        addVertex(fromVertex);
        addVertex(toVertex);
        // Add the edge from fromVertex to toVertex with the given weight
        adjacencyList.get(fromVertex).add(new Edge(fromVertex, toVertex, weight));
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


// import java.util.ArrayList;
// import java.util.HashMap;
// import java.util.List;
// import java.util.Map;

// public class Graph {
//     private Map<Vertex, List<Edge>> adjacencyList;

//     public Graph() {
//         this.adjacencyList = new HashMap<>();
//     }

//     public void addVertex(Vertex vertex) {
//         adjacencyList.putIfAbsent(vertex, new ArrayList<>());
//     }

//     public void addEdge(Vertex fromVertex, Vertex toVertex, int weight) {
//         addVertex(fromVertex);
//         addVertex(toVertex);
//         adjacencyList.get(fromVertex).add(new Edge(fromVertex, toVertex, weight));
//     }

//     public List<Edge> getEdges(Vertex vertex) {
//         return adjacencyList.get(vertex);
//     }

//     public Map<Vertex, List<Edge>> getAdjacencyList() {
//         return adjacencyList;
//     }

//     @Override
//     public String toString() {
//         StringBuilder sb = new StringBuilder();
//         for (Vertex vertex : adjacencyList.keySet()) {
//             sb.append(vertex).append(": ");
//             for (Edge edge : adjacencyList.get(vertex)) {
//                 sb.append(edge).append(", ");
//             }
//             sb.append("\n");
//         }
//         return sb.toString();
//     }

//     public static void main(String[] args) {
//         Graph graph = new Graph();

//         // Creating vertices
//         Vertex garage = new Vertex(0, "Garage");
//         Vertex pickupA = new Vertex(1, "Pick-up A");
//         Vertex deliverA = new Vertex(2, "Deliver A");
//         Vertex pickupB = new Vertex(3, "Pick-up B");
//         Vertex deliverB = new Vertex(4, "Deliver B");

//         // Adding edges
//         graph.addEdge(garage, pickupA, 10);
//         graph.addEdge(pickupA, deliverA, 20);
//         graph.addEdge(deliverA, pickupB, 30);
//         graph.addEdge(pickupB, deliverB, 40);
//         graph.addEdge(deliverB, garage, 50);

//         // Adding zero weight edges to complete the graph
//         List<Vertex> vertices = List.of(garage, pickupA, deliverA, pickupB, deliverB);
//         for (Vertex from : vertices) {
//             for (Vertex to : vertices) {
//                 if (from != to && graph.getEdges(from).stream().noneMatch(edge -> edge.getToVertex().equals(to))) {
//                     graph.addEdge(from, to, 0);
//                 }
//             }
//         }

//         System.out.println(graph);
//     }
// }
