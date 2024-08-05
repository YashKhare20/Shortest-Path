package algo.project;

import java.util.*;

/**
 * This class represents an implementation of Dijkstra's algorithm for finding
 * the shortest paths in a graph. It includes methods for running the algorithm,
 * getting the shortest path, finding the nearest vertex of a specified type,
 * and executing a sequence of paths based on a negative cycle.
 */
public class SampleDijkstra {

    private SamplePositiveGraph graph; // The graph on which Dijkstra's algorithm will be run.

    /**
     * Constructs a Dijkstra instance with a given graph.
     *
     * @param graph the graph to use.
     */
    public SampleDijkstra(SamplePositiveGraph graph) {
        this.graph = new SamplePositiveGraph(graph); // Deep copy the graph to ensure the original graph is not modified.
    }

    /**
     * Runs the Dijkstra algorithm from the given source vertex.
     *
     * @param source the source vertex to start from.
     */
    public void runDijkstra(SampleVertex source) {
        PriorityQueue<SampleVertex> priorityQueue = new PriorityQueue<>(Comparator.comparingDouble(SampleVertex::getDistance));

        // Initialize distances and status for all vertices
        for (SampleVertex vertex : graph.getAllVertices().values()) {
            vertex.setDistance(Double.MAX_VALUE); // Set initial distance to infinity
            vertex.setStatus(0); // Set vertex status to unvisited
            vertex.setParent(null); // No parent initially
        }
        source.setDistance(0); // Set distance for the source vertex to 0
        priorityQueue.add(source); // Add the source vertex to the priority queue

        while (!priorityQueue.isEmpty()) {
            SampleVertex u = priorityQueue.poll(); // Get the vertex with the smallest distance
            u.setStatus(1); // Mark the vertex as visited

            for (SampleEdge edge : u.getNeighbors()) {
                SampleVertex v = edge.getVertexT(); // Get the target vertex of the edge
                if (v.getStatus() == 1) continue; // Skip already visited vertices

                double newDist = u.getDistance() + edge.getWeight(); // Calculate the new distance
                if (newDist < v.getDistance()) {
                    priorityQueue.remove(v); // Remove the vertex from the queue to update its distance
                    v.setDistance(newDist); // Update the distance
                    v.setParent(u); // Set the parent to the current vertex
                    priorityQueue.add(v); // Re-add the vertex to the queue with the updated distance
                }
            }
        }
    }

    /**
     * Returns the shortest path from the source to the given target vertex.
     *
     * @param target the target vertex.
     * @return a list of vertices representing the shortest path.
     */
    public List<SampleVertex> getShortestPath(SampleVertex target) {
        List<SampleVertex> path = new ArrayList<>();
        for (SampleVertex at = target; at != null; at = at.getParent()) {
            path.add(at); // Add each vertex to the path list
        }
        Collections.reverse(path); // Reverse the list to get the path from source to target
        return path;
    }

    /**
     * Prints the shortest path from the source to the given target vertex.
     *
     * @param source the source vertex.
     * @param target the target vertex.
     */
    public void printShortestPath(SampleVertex source, SampleVertex target) {
        runDijkstra(source); // Ensure Dijkstra has been run from the source
        List<SampleVertex> path = getShortestPath(target); // Get the shortest path
        if (path.isEmpty() || !path.get(0).equals(source)) {
            System.out.println("No path from " + source.getName() + " to " + target.getName()); // If no path is found
        } else {
            StringBuilder pathString = new StringBuilder();
            for (SampleVertex vertex : path) {
                pathString.append(vertex.getName()).append(" "); // Build the path string
            }
            System.out.println("Shortest path from " + source.getName() + " to " + target.getName() + ": " + pathString.toString().trim()); // Print the path
        }
    }

    /**
     * Finds the nearest vertex of a specified type from a given vertex.
     *
     * @param from the starting vertex.
     * @param type the type of vertex to find (e.g., PICKUP, DELIVERY).
     * @return the nearest vertex of the specified type.
     */
    public SampleVertex findNearestVertex(SampleVertex from, VertexType type) {
        double minDistance = Double.MAX_VALUE; // Initialize minimum distance to infinity
        SampleVertex nearestVertex = null;

        for (SampleVertex vertex : graph.getVerticesByType(type)) {
            double distance = calculateEuclideanDistance(
                    from.getLatitude(), from.getLongitude(),
                    vertex.getLatitude(), vertex.getLongitude()
            );

            if (distance < minDistance) {
                minDistance = distance; // Update minimum distance
                nearestVertex = vertex; // Update nearest vertex
            }
        }

        return nearestVertex; // Return the nearest vertex
    }

    /**
     * Calculates the Euclidean distance between two points given their latitude and longitude.
     *
     * @param lat1 the latitude of the first point.
     * @param lon1 the longitude of the first point.
     * @param lat2 the latitude of the second point.
     * @param lon2 the longitude of the second point.
     * @return the distance between the two points.
     */
    private double calculateEuclideanDistance(double lat1, double lon1, double lat2, double lon2) {
        double latDiff = lat2 - lat1; // Difference in latitude
        double lonDiff = lon1 - lon2; // Difference in longitude
        return Math.sqrt(latDiff * latDiff + lonDiff * lonDiff); // Calculate and return the Euclidean distance
    }

    /**
     * Executes the sequence of finding paths from a Garage to all Pickups and Dropoffs and back to a Garage.
     *
     * @param garage the starting vertex for the cycle.
     * @param cycle the list of vertices in the cycle.
     */
    public void executeNegativeCycleAndPrintPath(SampleVertex garage, List<SampleVertex> cycle) {
        // Find the nearest pickup vertex from the starting vertex
        SampleVertex nearestPickup = findNearestVertex(garage, VertexType.PICKUP);

        if (nearestPickup == null) {
            System.out.println("No pickup vertex found."); // No pickup vertex found
            return;
        }

        // Find the index of the nearest pickup vertex in the cycle
        int nearestPickupIndex = cycle.indexOf(nearestPickup);
        if (nearestPickupIndex == -1) {
            System.out.println("Nearest pickup vertex not found in the cycle."); // Pickup vertex not found in the cycle
            return;
        }

        // Create a new order: garage -> nearestPickup -> rest of the cycle -> garage
        List<SampleVertex> orderedCycle = new ArrayList<>();
        orderedCycle.add(garage);

        // Add vertices from nearestPickupIndex + 1 to the end
        for (int i = nearestPickupIndex + 1; i < cycle.size(); i++) {
            orderedCycle.add(cycle.get(i));
        }

        // Add vertices from the beginning to nearestPickupIndex
        for (int i = 0; i <= nearestPickupIndex; i++) {
            orderedCycle.add(cycle.get(i));
        }

        // Add the garage at the end to complete the cycle
        orderedCycle.add(garage);

        // Print the paths between every two consecutive vertices in the ordered cycle
        for (int i = 0; i < orderedCycle.size() - 1; i++) {
            SampleVertex from = orderedCycle.get(i);
            SampleVertex to = orderedCycle.get(i + 1);
            printShortestPath(from, to); // Print the shortest path between each pair of consecutive vertices
        }
    }
}
