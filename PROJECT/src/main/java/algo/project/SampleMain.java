package algo.project;

import java.io.IOException;

public class SampleMain {
    public static void main(String[] args) {
        SamplePositiveGraph graphCreator = new SamplePositiveGraph();
        try {
            SampleGraph graph = graphCreator.createGraph("Project/PROJECT/positive_file.csv");

            // Print the total number of vertices and edges
            System.out.println("Total number of vertices: " + graph.getAllVertices().size());
            System.out.println("Total number of edges: " + graph.getEdges().size());

            // Print Graph
            System.out.println(graph);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
