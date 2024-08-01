package algo.project;

public class Vertex {
    private String id;
    private String name;
    private String type;  // Type can be "Pickup", "Delivery", or "Garage"
    private double latitude;
    private double longitude;

    public Vertex(String id, String name, String type, double latitude, double longitude) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.latitude = latitude;
        this.longitude = longitude;
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

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    @Override
    public String toString() {
        return "Vertex(" + id + ", " + name + ", " + type + ", " + latitude + ", " + longitude + ")";
    }
}
