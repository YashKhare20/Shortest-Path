package algo.project;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;

public class PositiveGraph {

    private Graph graph;
    private static final String[] ADDRESSES = {
        "1st Ave & E 11th St", "2nd Ave & E 22nd St", "3rd Ave & E 33rd St", "4th Ave & E 44th St", "5th Ave & E 55th St",
        "6th Ave & E 66th St", "7th Ave & E 77th St", "8th Ave & E 88th St", "9th Ave & E 99th St", "10th Ave & E 110th St",
        "11th Ave & E 121st St", "12th Ave & E 132nd St", "13th Ave & E 143rd St", "14th Ave & E 154th St", "15th Ave & E 165th St",
        "16th Ave & E 176th St", "17th Ave & E 187th St", "18th Ave & E 198th St", "19th Ave & E 209th St", "20th Ave & E 220th St",
        "21st Ave & E 231st St", "22nd Ave & E 242nd St", "23rd Ave & E 253rd St", "24th Ave & E 264th St", "25th Ave & E 275th St",
        "26th Ave & E 286th St", "27th Ave & E 297th St", "28th Ave & E 308th St", "29th Ave & E 319th St", "30th Ave & E 330th St",
        "31st Ave & E 341st St", "32nd Ave & E 352nd St", "33rd Ave & E 363rd St", "34th Ave & E 374th St", "35th Ave & E 385th St",
        "36th Ave & E 396th St", "37th Ave & E 407th St", "38th Ave & E 418th St", "39th Ave & E 429th St", "40th Ave & E 440th St",
        "41st Ave & E 451st St", "42nd Ave & E 462nd St", "43rd Ave & E 473rd St", "44th Ave & E 484th St", "45th Ave & E 495th St",
        "46th Ave & E 506th St", "47th Ave & E 517th St", "48th Ave & E 528th St", "49th Ave & E 539th St", "50th Ave & E 550th St",
        "51st Ave & E 561st St", "52nd Ave & E 572nd St", "53rd Ave & E 583rd St", "54th Ave & E 594th St", "55th Ave & E 605th St",
        "56th Ave & E 616th St", "57th Ave & E 627th St", "58th Ave & E 638th St", "59th Ave & E 649th St", "60th Ave & E 660th St",
        "61st Ave & E 671st St", "62nd Ave & E 682nd St", "63rd Ave & E 693rd St", "64th Ave & E 704th St", "65th Ave & E 715th St",
        "66th Ave & E 726th St", "67th Ave & E 737th St", "68th Ave & E 748th St", "69th Ave & E 759th St", "70th Ave & E 770th St",
        "71st Ave & E 781st St", "72nd Ave & E 792nd St", "73rd Ave & E 803rd St", "74th Ave & E 814th St", "75th Ave & E 825th St",
        "76th Ave & E 836th St", "77th Ave & E 847th St", "78th Ave & E 858th St", "79th Ave & E 869th St", "80th Ave & E 880th St",
        "81st Ave & E 891st St", "82nd Ave & E 902nd St", "83rd Ave & E 913th St", "84th Ave & E 924th St", "85th Ave & E 935th St",
        "86th Ave & E 946th St", "87th Ave & E 957th St", "88th Ave & E 968th St", "89th Ave & E 979th St", "90th Ave & E 990th St",
        "91st Ave & E 1001st St", "92nd Ave & E 1012th St", "93rd Ave & E 1023rd St", "94th Ave & E 1034th St", "95th Ave & E 1045th St",
        "96th Ave & E 1056th St", "97th Ave & E 1067th St", "98th Ave & E 1078th St", "99th Ave & E 1089th St", "100th Ave & E 1100th St"
    };

    // Constructor to initialize the PositiveGraph with an empty Graph
    public PositiveGraph() {
        this.graph = new Graph();
    }

    // Method to generate a grid graph with vertices from the CSV file and calculate edge weights based on Euclidean distance
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
                if (parts.length >= 11) { // Assuming the CSV file has at least 11 columns
                    String pickupId = "Pickup" + pickupCount++;
                    String dropoffId = "Dropoff" + dropoffCount++;
                    String pickupLabel = "Pickup(" + ADDRESSES[recordCount] + ")";
                    String dropoffLabel = "Dropoff(" + ADDRESSES[recordCount + 1] + ")";

                    double pickupLat = Double.parseDouble(parts[6].trim());
                    double pickupLon = Double.parseDouble(parts[5].trim());
                    double dropoffLat = Double.parseDouble(parts[10].trim());
                    double dropoffLon = Double.parseDouble(parts[9].trim());

                    Vertex pickupVertex = new Vertex(pickupId, pickupLabel, "Pickup", pickupLat, pickupLon);
                    Vertex dropoffVertex = new Vertex(dropoffId, dropoffLabel, "Dropoff", dropoffLat, dropoffLon);

                    vertexMap.put(pickupId, pickupVertex);
                    vertexMap.put(dropoffId, dropoffVertex);

                    vertices.add(pickupVertex);
                    vertices.add(dropoffVertex);

                    System.out.println("Added vertex: " + pickupVertex);
                    System.out.println("Added vertex: " + dropoffVertex);

                    recordCount++;
                }
            }
        } catch (IOException e) {
            System.out.println("IOException occurred: " + e);
        }

        // Add 2 garages
        Vertex garage1 = new Vertex("Garage1", "Garage(40.0,-73.0)", "Garage", 40.0, -73.0);
        Vertex garage2 = new Vertex("Garage2", "Garage(41.0,-73.5)", "Garage", 41.0, -73.5);
        vertices.add(garage1);
        vertices.add(garage2);

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

    // Method to calculate the Euclidean distance between two points given their latitude and longitude
    private double calculateEuclideanDistance(double lat1, double lon1, double lat2, double lon2) {
        double latDiff = lat2 - lat1;
        double lonDiff = lon2 - lon1;
        double distance = Math.sqrt(latDiff * latDiff + lonDiff * lonDiff);
        return formatDistance(distance);
    }

    // Method to format the distance to 2 decimal places
    private double formatDistance(double distance) {
        DecimalFormat df = new DecimalFormat("#.###");
        return Double.parseDouble(df.format(distance));
    }

    private void addGridEdges(Vertex[][] grid) {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                Vertex fromVertex = grid[i][j];
                if (fromVertex == null) continue;

                // Connect to the right vertex
                if (j < grid[i].length - 1) {
                    Vertex toVertex = grid[i][j + 1];
                    if (toVertex != null) {
                        double weight = calculateEuclideanDistance(fromVertex.getLatitude(), fromVertex.getLongitude(), toVertex.getLatitude(), toVertex.getLongitude());
                        graph.addEdge(fromVertex, toVertex, weight);
                        System.out.println("Added edge from " + fromVertex.getId() + " to " + toVertex.getId() + " with weight " + weight);
                    }
                }

                // Connect to the bottom vertex
                if (i < grid.length - 1) {
                    Vertex toVertex = grid[i + 1][j];
                    if (toVertex != null) {
                        double weight = calculateEuclideanDistance(fromVertex.getLatitude(), fromVertex.getLongitude(), toVertex.getLatitude(), toVertex.getLongitude());
                        graph.addEdge(fromVertex, toVertex, weight);
                        System.out.println("Added edge from " + fromVertex.getId() + " to " + toVertex.getId() + " with weight " + weight);
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

        // Keep track of added edges to avoid duplicates
        Set<String> addedEdges = new HashSet<>();

        // Add edges
        for (Vertex vertex : graph.getVertices()) {
            for (Edge edge : graph.getEdges(vertex)) {
                String edgeId = edge.getFromVertex().getId() + "-" + edge.getToVertex().getId();
                if (!addedEdges.contains(edgeId)) {
                    try {
                        gsGraph.addEdge(edgeId, edge.getFromVertex().getId(), edge.getToVertex().getId())
                               .setAttribute("ui.label", edge.getWeight());
                        addedEdges.add(edgeId);
                    } catch (org.graphstream.graph.EdgeRejectedException e) {
                        // Handle duplicate edge exceptions
                        System.out.println("Duplicate edge rejected: " + edgeId);
                    }
                }
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

