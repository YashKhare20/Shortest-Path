package algo.project;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class NegativeGraph {

    // Method to create a graph from a CSV file
    public static Graph createNegativeGraphFromCSV(String filePath, Graph positiveGraph) {
        Graph negativeGraph = new Graph(); // Create a new graph instance to store the negative graph

        // Add all vertices to the ArrayList
        // Create a new ArrayList to hold Vertex objects
        ArrayList<Vertex> vertices = new ArrayList<>(positiveGraph.getVertices()); // Get all vertices from the positive graph

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) { // Open the CSV file for reading
            String line; // Variable to hold each line read from the file
            boolean isFirstLine = true; // Flag to skip the header line

            while ((line = br.readLine()) != null) { // Read each line from the file until the end of the file
                if (isFirstLine) { // Check if it is the first line
                    isFirstLine = false; // Skip the header line
                    continue; // Continue to the next line
                }

                // Split the line into parts
                String[] parts = line.split(","); // Split the line by commas
                if (parts.length == 3) { // Check if the line has exactly 3 parts
                    // Extract vertex IDs and edge weight
                    String fromId = parts[0].trim(); // Get the 'from' vertex ID
                    String toId = parts[1].trim(); // Get the 'to' vertex ID
                    int weight = Integer.parseInt(parts[2].trim()); // Parse the weight as an integer

                    // Find the vertices by their IDs
                    Vertex fromVertex = findVertexById(vertices, fromId); // Find the 'from' vertex in the list
                    Vertex toVertex = findVertexById(vertices, toId); // Find the 'to' vertex in the list

                    // Add an edge between the vertices if they are found
                    if (fromVertex != null && toVertex != null) {
                        negativeGraph.addEdge(fromVertex, toVertex, -weight); // Add edge with negative weight
                    }
                }
            }
        } catch (IOException e) { // Catch any IO exceptions
            System.out.println("IOException occurred: " + e); // Print an error message if an exception occurs
        }

        // Ensure all pairs of vertices have bidirectional edges
        List<Vertex> selectedVertices = negativeGraph.getVertices(); // Get all vertices in the negative graph
        for (Vertex v1 : selectedVertices) { // Iterate over each vertex
            for (Vertex v2 : selectedVertices) { // Compare with every other vertex
                if (!v1.equals(v2)) { // Avoid self-loops
                    // Check if there is an edge from v1 to v2
                    boolean hasEdgeFromV1ToV2 = negativeGraph.checkEdgeExistence(v1, v2);
                    // Check if there is an edge from v2 to v1
                    boolean hasEdgeFromV2ToV1 = negativeGraph.checkEdgeExistence(v2, v1);

                    // If no edge exists from v1 to v2, add an edge with weight 0
                    if (!hasEdgeFromV1ToV2) {
                        negativeGraph.addEdge(v1, v2, 0);
                    }
                    // If no edge exists from v2 to v1, add an edge with weight 0
                    if (!hasEdgeFromV2ToV1) {
                        negativeGraph.addEdge(v2, v1, 0);
                    }
                }
            }
        }

        // Return the created graph
        return negativeGraph;
    }

    // Method to find a vertex by its ID
    private static Vertex findVertexById(List<Vertex> vertices, String id) {
        for (Vertex vertex : vertices) { // Iterate over the list of vertices
            // Check if the vertex ID matches
            if (vertex.getId().equals(id)) {
                return vertex; // Return the vertex if IDs match
            }
        }
        // Return null if vertex is not found
        return null;
    }

    // Method to convert custom graph to GraphStream graph for visualization
    public static org.graphstream.graph.Graph toGraphStreamGraph(Graph customGraph) {
        org.graphstream.graph.Graph gsGraph = new org.graphstream.graph.implementations.SingleGraph("NegativeGraph");

        for (Vertex vertex : customGraph.getVertices()) {
            gsGraph.addNode(vertex.getId()).setAttribute("ui.label", vertex.getName());
        }

        for (Vertex vertex : customGraph.getVertices()) {
            for (Edge edge : customGraph.getEdges(vertex)) {
                String edgeId = edge.getFromVertex().getId() + "-" + edge.getToVertex().getId();
                try {
                    gsGraph.addEdge(edgeId, edge.getFromVertex().getId(), edge.getToVertex().getId())
                           .setAttribute("ui.label", edge.getWeight());
                } catch (org.graphstream.graph.EdgeRejectedException e) {
                    // Handle duplicate edge exceptions
                    System.out.println("Duplicate edge rejected: " + edgeId);
                }
            }
        }

        return gsGraph;
    }

    // Method to print vertices and edges of the graph
    public static void printGraph(Graph graph) {
        System.out.println("Vertices:");
        for (Vertex vertex : graph.getVertices()) {
            System.out.println(vertex);
        }

        System.out.println("Edges:");
        for (Vertex vertex : graph.getVertices()) {
            for (Edge edge : graph.getEdges(vertex)) {
                System.out.println(edge);
            }
        }
    }
}

// import java.io.BufferedReader;
// import java.io.FileReader;
// import java.io.IOException;
// import java.util.HashMap;
// import java.util.List;
// import java.util.Map;

// public class NegativeGraph {

//     // Method to create a graph from a CSV file
//     public static Graph createNegativeGraphFromCSV(String filePath) {
//         Graph graph = new Graph();
//         Map<String, Vertex> vertexMap = new HashMap<>();

//         try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
//             String line;
//             int recordLimit = 10;
//             int recordCount = 0;
//             boolean isFirstLine = true;

//             while ((line = br.readLine()) != null && recordCount < recordLimit) {
//                 if (isFirstLine) {
//                     isFirstLine = false;
//                     continue;
//                 }

//                 String[] parts = line.split(",");
//                 if (parts.length >= 11) {
//                     double pickupLongitude = Double.parseDouble(parts[5].trim());
//                     double pickupLatitude = Double.parseDouble(parts[6].trim());
//                     double dropoffLongitude = Double.parseDouble(parts[9].trim());
//                     double dropoffLatitude = Double.parseDouble(parts[10].trim());

//                     String pickupId = String.format("%.4f,%.4f", pickupLatitude, pickupLongitude);
//                     String dropoffId = String.format("%.4f,%.4f", dropoffLatitude, dropoffLongitude);
//                     String pickupLabel = "Pickup(" + pickupId + ")";
//                     String dropoffLabel = "Dropoff(" + dropoffId + ")";

//                     Vertex pickupVertex = vertexMap.computeIfAbsent(pickupId, id -> new Vertex(pickupId, pickupLabel, "Pickup"));
//                     Vertex dropoffVertex = vertexMap.computeIfAbsent(dropoffId, id -> new Vertex(dropoffId, dropoffLabel, "Dropoff"));

//                     graph.addVertex(pickupVertex);
//                     graph.addVertex(dropoffVertex);

//                     int weight = 1;
//                     graph.addEdge(pickupVertex, dropoffVertex, -weight);

//                     recordCount++;
//                 }
//             }
//         } catch (IOException e) {
//             System.out.println("IOException occurred: " + e);
//         }

//         // Ensure all pairs of vertices have bidirectional edges with weight 0 if not connected
//         addZeroWeightEdges(graph);

//         return graph;
//     }

//     private static void addZeroWeightEdges(Graph graph) {
//         List<Vertex> vertices = graph.getVertices();
//         for (Vertex v1 : vertices) {
//             for (Vertex v2 : vertices) {
//                 if (!v1.equals(v2)) {
//                     // Check if there is an edge from v1 to v2
//                     boolean hasEdgeFromV1ToV2 = graph.checkEdgeExistence(v1, v2);
//                     // Check if there is an edge from v2 to v1
//                     boolean hasEdgeFromV2ToV1 = graph.checkEdgeExistence(v2, v1);

//                     // If no edge exists from v1 to v2, add an edge with weight 0
//                     if (!hasEdgeFromV1ToV2) {
//                         graph.addEdge(v1, v2, 0);
//                     }
//                     // If no edge exists from v2 to v1, add an edge with weight 0
//                     if (!hasEdgeFromV2ToV1) {
//                         graph.addEdge(v2, v1, 0);
//                     }
//                 }
//             }
//         }
//     }

//     // Method to convert custom graph to GraphStream graph for visualization
//     public static org.graphstream.graph.Graph toGraphStreamGraph(Graph customGraph) {
//         org.graphstream.graph.Graph gsGraph = new org.graphstream.graph.implementations.SingleGraph("NegativeGraph");

//         for (Vertex vertex : customGraph.getVertices()) {
//             gsGraph.addNode(vertex.getId()).setAttribute("ui.label", vertex.getName());
//         }

//         for (Vertex vertex : customGraph.getVertices()) {
//             for (Edge edge : customGraph.getEdges(vertex)) {
//                 String edgeId = edge.getFromVertex().getId() + "-" + edge.getToVertex().getId();
//                 try {
//                     gsGraph.addEdge(edgeId, edge.getFromVertex().getId(), edge.getToVertex().getId())
//                            .setAttribute("ui.label", edge.getWeight());
//                 } catch (org.graphstream.graph.EdgeRejectedException e) {
//                     // Handle duplicate edge exceptions
//                     System.out.println("Duplicate edge rejected: " + edgeId);
//                 }
//             }
//         }

//         return gsGraph;
//     }

//     // Method to print vertices and edges of the graph
//     public static void printGraph(Graph graph) {
//         System.out.println("Vertices:");
//         for (Vertex vertex : graph.getVertices()) {
//             System.out.println(vertex);
//         }

//         System.out.println("Edges:");
//         for (Vertex vertex : graph.getVertices()) {
//             for (Edge edge : graph.getEdges(vertex)) {
//                 System.out.println(edge);
//             }
//         }
//     }
// }
