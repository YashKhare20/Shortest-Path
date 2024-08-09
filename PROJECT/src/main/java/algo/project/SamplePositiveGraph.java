package algo.project;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SamplePositiveGraph {

    public SampleGraph createGraph(String positiveGraphFile) throws IOException {
        SampleGraph graph = new SampleGraph();

        // Read positive_file.csv and add vertices based on the 'type' column
        BufferedReader positiveGraphReader = new BufferedReader(new FileReader(positiveGraphFile));
        String line;
        boolean isFirstLine = true;

        SampleVertex garageVertex = null;
        List<SampleVertex> pickups = new ArrayList<>();
        List<SampleVertex> dropoffs = new ArrayList<>();
        List<SampleVertex> normals = new ArrayList<>();

        while ((line = positiveGraphReader.readLine()) != null) {
            if (isFirstLine) {  // Skip header
                isFirstLine = false;
                continue;
            }
            String[] parts = line.split(",");
            String address = parts[2];
            double latitude = Double.parseDouble(parts[1]);
            double longitude = Double.parseDouble(parts[0]);
            String type = parts[3];

            SampleVertex vertex;

            switch (type.toLowerCase()) {
                case "garage":
                    vertex = new SampleVertex(address, VertexType.GARAGE, latitude, longitude);
                    garageVertex = vertex;
                    graph.addVertex(vertex);
                    break;

                case "normal":
                    vertex = new SampleVertex(address, VertexType.NORMAL, latitude, longitude);
                    normals.add(vertex);
                    graph.addVertex(vertex);
                    break;

                case "pickup":
                    vertex = new SampleVertex(address, VertexType.PICKUP, latitude, longitude);
                    pickups.add(vertex);
                    graph.addVertex(vertex);
                    break;

                case "dropoff":
                    vertex = new SampleVertex(address, VertexType.DELIVERY, latitude, longitude);
                    dropoffs.add(vertex);
                    graph.addVertex(vertex);
                    break;

                default:
                    System.out.println("Unknown type: " + type);
            }
        }
        positiveGraphReader.close();

        // Create edges between garage and all normal vertices
        if (garageVertex != null) {
            for (SampleVertex normal : normals) {
                SampleEdge edge = new SampleEdge(garageVertex, normal, calculateDistance(garageVertex, normal));
                graph.addEdge(edge);
            }
        }

        // Create edges between pickups and normal vertices
        for (SampleVertex pickup : pickups) {
            for (SampleVertex normal : normals) {
                SampleEdge edge = new SampleEdge(pickup, normal, calculateDistance(pickup, normal));
                graph.addEdge(edge);
            }
        }

        // Create edges between dropoffs and normal vertices
        for (SampleVertex dropoff : dropoffs) {
            for (SampleVertex normal : normals) {
                SampleEdge edge = new SampleEdge(dropoff, normal, calculateDistance(dropoff, normal));
                graph.addEdge(edge);
            }
        }

        return graph;
    }

    private double calculateDistance(SampleVertex v1, SampleVertex v2) {
        double lat1 = v1.getLatitude();
        double lon1 = v1.getLongitude();
        double lat2 = v2.getLatitude();
        double lon2 = v2.getLongitude();

        // Use Euclidean distance
        // Multiplying by 100 to make distances more realistic (consider distances in miles)
        return 100*Math.sqrt(Math.pow(lat1 - lat2, 2) + Math.pow(lon1 - lon2, 2));
    }
}
