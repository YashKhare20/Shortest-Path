package algo.project;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SamplePositiveGraph {

    public SampleGraph createGraph(String positiveGraphFile, String closeRedPinsFile) throws IOException {
        SampleGraph graph = new SampleGraph();

        // Read PositiveGraphAddresses.csv and add orange pin as garage
        BufferedReader positiveGraphReader = new BufferedReader(new FileReader(positiveGraphFile));
        String line;
        boolean isFirstLine = true;
        boolean isSecondLine = true;

        while ((line = positiveGraphReader.readLine()) != null) {
            if (isFirstLine) {
                isFirstLine = false;
                continue;
            }
            String[] parts = line.split(",");
            String name = parts[0];
            double latitude = Double.parseDouble(parts[1]);
            double longitude = Double.parseDouble(parts[2]);
            int mapRow = Integer.parseInt(parts[3]);
            int mapCol = Integer.parseInt(parts[4]);

            VertexType type = isSecondLine ? VertexType.GARAGE : VertexType.NORMAL;
            isSecondLine = false;

            SampleVertex vertex = new SampleVertex(name, type, latitude, longitude, mapRow, mapCol);
            graph.addVertex(vertex);
            // System.out.println("Added Normal or Garage Vertex: " + vertex);
        }
        positiveGraphReader.close();

        // Read pickup_dropoff_addresses.csv and add red pins as pickups and green pins as dropoffs
        BufferedReader redPinsReader = new BufferedReader(new FileReader(closeRedPinsFile));
        isFirstLine = true;
        List<SampleVertex> pickups = new ArrayList<>();
        List<SampleVertex> dropoffs = new ArrayList<>();
        int lineNumber = 0;

        while ((line = redPinsReader.readLine()) != null) {
            if (isFirstLine) {  // Skip header
                isFirstLine = false;
                continue;
            }
            String[] parts = line.split(",");
            String name = parts[19];
            double latitude = Double.parseDouble(parts[6]);  // pickup_latitude
            double longitude = Double.parseDouble(parts[5]);  // pickup_longitude

            if (lineNumber % 2 == 0) { // Even rows
                String dropoffName = name;
                SampleVertex dropoff = new SampleVertex(dropoffName, VertexType.DELIVERY, latitude, longitude, 0, 0);
                dropoffs.add(dropoff);
                graph.addVertex(dropoff);
                // System.out.println("Added Dropoff Vertex: " + dropoff);
            } else { // Odd rows
                String pickupName = name;
                SampleVertex pickup = new SampleVertex(pickupName, VertexType.PICKUP, latitude, longitude, 0, 0);
                pickups.add(pickup);
                graph.addVertex(pickup);
                // System.out.println("Added Pickup Vertex: " + pickup);
            }

            lineNumber++;
        }
        redPinsReader.close();

        // Create edges between red/green pins using blue pins as intermediaries
        List<SampleVertex> bluePins = graph.getVerticesByType(VertexType.NORMAL);
        for (SampleVertex pickup : pickups) {
            for (SampleVertex bluePin : bluePins) {
                SampleEdge edge = new SampleEdge(pickup, bluePin, calculateDistance(pickup, bluePin));
                graph.addEdge(edge);
            }
        }

        for (SampleVertex dropoff : dropoffs) {
            for (SampleVertex bluePin : bluePins) {
                SampleEdge edge = new SampleEdge(dropoff, bluePin, calculateDistance(dropoff, bluePin));
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
        return Math.sqrt(Math.pow(lat1 - lat2, 2) + Math.pow(lon1 - lon2, 2));
    }
}
