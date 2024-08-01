package algo.project;

public class Edge {
    private Vertex fromVertex;
    private Vertex toVertex;
    private double weight;

    public Edge(Vertex fromVertex, Vertex toVertex, double weight) {
        this.fromVertex = fromVertex;
        this.toVertex = toVertex;
        this.weight = weight;
    }

    public Vertex getFromVertex() {
        return fromVertex;
    }

    public Vertex getToVertex() {
        return toVertex;
    }

    public double getWeight() {
        return weight;
    }

    @Override
    public String toString() {
        return "Edge(from " + fromVertex + " to " + toVertex + ", weight=" + weight + ")";
    }
}
