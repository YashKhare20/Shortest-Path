package algo.project;

public class Vertex {
    private String id;
    private String name;
    private String type;  // Type can be "Pickup", "Delivery", or "Garage"

    public Vertex(String id, String name, String type) {
        this.id = id;
        this.name = name;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return "Vertex(" + id + ", " + name + ", " + type + ")";
    }
}
