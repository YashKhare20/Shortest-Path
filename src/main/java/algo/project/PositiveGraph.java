package algo.project;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class PositiveGraph {

    private Graph graph;

    // Constructor to initialize the PositiveGraph with an empty Graph
    public PositiveGraph() {
        this.graph = new Graph();
    }

    // Method to generate a grid graph with vertices from the CSV file and random edge weights between 80 and 100
    public Graph generatePositiveGraphFromCSV(String filePath) {
        Map<String, Vertex> vertexMap = new HashMap<>();
        ArrayList<Vertex> vertices = new ArrayList<>();
        int pickupCount = 1;
        int dropoffCount = 1;

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            int recordLimit = 49; // Limit to the first 49 records each for pickup/delivery to leave space for 2 garages
            int recordCount = 0;
            boolean isFirstLine = true;

            while ((line = br.readLine()) != null && recordCount < recordLimit) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }

                String[] parts = line.split(",");
                if (parts.length >= 11) {
                    double pickupLongitude = Double.parseDouble(parts[5].trim());
                    double pickupLatitude = Double.parseDouble(parts[6].trim());
                    double dropoffLongitude = Double.parseDouble(parts[9].trim());
                    double dropoffLatitude = Double.parseDouble(parts[10].trim());

                    String pickupId = "Pickup" + pickupCount++;
                    String dropoffId = "Dropoff" + dropoffCount++;
                    String pickupLabel = String.format("Pickup(%.3f,%.3f)", pickupLatitude, pickupLongitude);
                    String dropoffLabel = String.format("Dropoff(%.3f,%.3f)", dropoffLatitude, dropoffLongitude);

                    Vertex pickupVertex = vertexMap.computeIfAbsent(pickupId, id -> new Vertex(pickupId, pickupLabel, "Pickup"));
                    Vertex dropoffVertex = vertexMap.computeIfAbsent(dropoffId, id -> new Vertex(dropoffId, dropoffLabel, "Dropoff"));

                    vertices.add(pickupVertex);
                    vertices.add(dropoffVertex);

                    recordCount++;
                }
            }
        } catch (IOException e) {
            System.out.println("IOException occurred: " + e);
        }

        // Add 2 garages
        Vertex garage1 = new Vertex("Garage1", "Garage(40.0,-73.0)", "Garage");
        Vertex garage2 = new Vertex("Garage2", "Garage(41.0,-73.5)", "Garage");
        vertices.add(garage1);
        vertices.add(garage2);

        // Debug print statement
        System.out.println("Added garages: " + garage1 + ", " + garage2);

        // Add garages to vertexMap
        vertexMap.put(garage1.getId(), garage1);
        vertexMap.put(garage2.getId(), garage2);

        // Ensure we have exactly 100 vertices
        while (vertices.size() < 100) {
            vertices.add(vertices.get(vertices.size() % vertexMap.size()));
        }

        // Convert the list to a 2D array for the grid structure
        Vertex[][] grid = new Vertex[10][10];
        for (int i = 0; i < 100; i++) {
            grid[i / 10][i % 10] = vertices.get(i);
            graph.addVertex(vertices.get(i));
        }

        // Add edges to create a grid-like structure
        addGridEdges(grid);

        // Debug print statement
        System.out.println("Total vertices after adding to grid: " + vertices.size());

        return graph;
    }

    private void addGridEdges(Vertex[][] grid) {
        Random random = new Random();
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                Vertex fromVertex = grid[i][j];
                if (fromVertex == null) continue;

                // Connect to the right vertex
                if (j < grid[i].length - 1) {
                    Vertex toVertex = grid[i][j + 1];
                    if (toVertex != null) {
                        int weight = 80 + random.nextInt(21);
                        graph.addEdge(fromVertex, toVertex, weight);
                    }
                }

                // Connect to the bottom vertex
                if (i < grid.length - 1) {
                    Vertex toVertex = grid[i + 1][j];
                    if (toVertex != null) {
                        int weight = 80 + random.nextInt(21);
                        graph.addEdge(fromVertex, toVertex, weight);
                    }
                }
            }
        }
    }

    // Method to convert custom graph to GraphStream graph for visualization
    public org.graphstream.graph.Graph toGraphStreamGraph() {
        org.graphstream.graph.Graph gsGraph = new org.graphstream.graph.implementations.SingleGraph("PositiveGraph");

        for (Vertex vertex : graph.getVertices()) {
            gsGraph.addNode(vertex.getId()).setAttribute("ui.label", vertex.getName());
        }

        for (Vertex vertex : graph.getVertices()) {
            for (Edge edge : graph.getEdges(vertex)) {
                String edgeId = edge.getFromVertex().getId() + "-" + edge.getToVertex().getId();
                gsGraph.addEdge(edgeId, edge.getFromVertex().getId(), edge.getToVertex().getId())
                        .setAttribute("ui.label", edge.getWeight());
            }
        }

        return gsGraph;
    }

    // Method to print vertices and edges of the graph
    public void printGraph(Graph graph) {
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


