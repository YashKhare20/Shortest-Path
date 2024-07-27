// package algo.project;

// import org.jxmapviewer.JXMapViewer;
// import org.jxmapviewer.OSMTileFactoryInfo;
// import org.jxmapviewer.painter.CompoundPainter;
// import org.jxmapviewer.painter.Painter;
// import org.jxmapviewer.viewer.DefaultWaypoint;
// import org.jxmapviewer.viewer.GeoPosition;
// import org.jxmapviewer.viewer.TileFactoryInfo;
// import org.jxmapviewer.viewer.WaypointPainter;
// import org.jxmapviewer.input.CenterMapListener;
// import org.jxmapviewer.input.PanKeyListener;
// import org.jxmapviewer.input.PanMouseInputListener;
// import org.jxmapviewer.input.ZoomMouseWheelListenerCenter;

// import javax.swing.*;
// import java.awt.*;
// import java.awt.geom.Point2D;
// import java.util.ArrayList;
// import java.util.List;
// import java.util.stream.Collectors;

// public class MapVisualizer {

//     public static void visualize(Graph graph) {
//         // Create a JXMapViewer
//         JXMapViewer mapViewer = new JXMapViewer();

//         // Set the tile factory (OpenStreetMap)
//         TileFactoryInfo info = new OSMTileFactoryInfo();
//         mapViewer.setTileFactory(new org.jxmapviewer.viewer.DefaultTileFactory(info));

//         // Create waypoints from graph vertices
//         List<GeoPosition> waypoints = new ArrayList<>();
//         for (Vertex vertex : graph.getVertices()) {
//             String[] coordinates = vertex.getId().split(",");
//             try {
//                 double lon = Double.parseDouble(coordinates[0]);
//                 double lat = Double.parseDouble(coordinates[1]);
//                 waypoints.add(new GeoPosition(lat, lon));
//             } catch (NumberFormatException e) {
//                 e.printStackTrace();
//             }
//         }

//         // Set the initial focus and zoom level
//         if (!waypoints.isEmpty()) {
//             mapViewer.setZoom(10);
//             mapViewer.setAddressLocation(waypoints.get(0));
//         }

//         // Create waypoints painter
//         WaypointPainter<DefaultWaypoint> waypointPainter = new WaypointPainter<>();
//         waypointPainter.setWaypoints(waypoints.stream().map(DefaultWaypoint::new).collect(Collectors.toSet()));

//         // Create a custom painter for edges
//         Painter<JXMapViewer> edgePainter = new Painter<JXMapViewer>() {
//             @Override
//             public void paint(Graphics2D g, JXMapViewer map, int w, int h) {
//                 g = (Graphics2D) g.create();
//                 g.setStroke(new BasicStroke(2)); // Make edges more prominent

//                 // Convert map viewer to world bitmap coordinates
//                 Rectangle rect = map.getViewportBounds();
//                 int zoom = map.getZoom();

//                 for (Edge edge : graph.getEdges()) {
//                     Vertex start = edge.getFromVertex();
//                     Vertex end = edge.getToVertex();

//                     String[] startCoords = start.getId().split(",");
//                     String[] endCoords = end.getId().split(",");

//                     try {
//                         double startLon = Double.parseDouble(startCoords[0]);
//                         double startLat = Double.parseDouble(startCoords[1]);
//                         double endLon = Double.parseDouble(endCoords[0]);
//                         double endLat = Double.parseDouble(endCoords[1]);

//                         Point2D startPoint = map.getTileFactory().geoToPixel(new GeoPosition(startLat, startLon), zoom);
//                         Point2D endPoint = map.getTileFactory().geoToPixel(new GeoPosition(endLat, endLon), zoom);

//                         int x1 = (int) (startPoint.getX() - rect.getX());
//                         int y1 = (int) (startPoint.getY() - rect.getY());
//                         int x2 = (int) (endPoint.getX() - rect.getX());
//                         int y2 = (int) (endPoint.getY() - rect.getY());

//                         // Draw the edge in black
//                         g.setColor(Color.BLACK);
//                         g.drawLine(x1, y1, x2, y2);

//                         // Draw weight label on the edge
//                         String weightLabel = String.valueOf(edge.getWeight());
//                         g.setColor(Color.BLUE);
//                         g.drawString(weightLabel, (x1 + x2) / 2, (y1 + y2) / 2);
//                     } catch (NumberFormatException e) {
//                         e.printStackTrace();
//                     }
//                 }

//                 g.dispose();
//             }
//         };

//         // Create a custom painter for vertex labels
//         Painter<JXMapViewer> vertexLabelPainter = new Painter<JXMapViewer>() {
//             @Override
//             public void paint(Graphics2D g, JXMapViewer map, int w, int h) {
//                 g = (Graphics2D) g.create();
//                 g.setColor(Color.BLACK);

//                 // Convert map viewer to world bitmap coordinates
//                 Rectangle rect = map.getViewportBounds();
//                 int zoom = map.getZoom();

//                 for (Vertex vertex : graph.getVertices()) {
//                     String[] coords = vertex.getId().split(",");
//                     try {
//                         double lon = Double.parseDouble(coords[0]);
//                         double lat = Double.parseDouble(coords[1]);

//                         Point2D point = map.getTileFactory().geoToPixel(new GeoPosition(lat, lon), zoom);

//                         int x = (int) (point.getX() - rect.getX());
//                         int y = (int) (point.getY() - rect.getY());

//                         // Draw vertex ID with formatted coordinates and type
//                         String formattedId = String.format("%.2f,%.2f (%s)", lon, lat, vertex.getName());
//                         g.drawString(formattedId, x, y);
//                     } catch (NumberFormatException e) {
//                         e.printStackTrace();
//                     }
//                 }

//                 g.dispose();
//             }
//         };

//         // Combine painters
//         List<Painter<JXMapViewer>> painters = new ArrayList<>();
//         painters.add(waypointPainter);
//         painters.add(edgePainter);
//         painters.add(vertexLabelPainter);

//         CompoundPainter<JXMapViewer> compoundPainter = new CompoundPainter<>(painters);
//         mapViewer.setOverlayPainter(compoundPainter);

//         // Add mouse interactions
//         mapViewer.addMouseListener(new PanMouseInputListener(mapViewer));
//         mapViewer.addMouseMotionListener(new PanMouseInputListener(mapViewer));
//         mapViewer.addMouseWheelListener(new ZoomMouseWheelListenerCenter(mapViewer));
//         mapViewer.addKeyListener(new PanKeyListener(mapViewer));
//         mapViewer.addMouseListener(new CenterMapListener(mapViewer));

//         // Create a JFrame to display the map
//         JFrame frame = new JFrame("Graph Visualization on Map");
//         frame.setLayout(new BorderLayout());
//         frame.add(mapViewer, BorderLayout.CENTER);
//         frame.setSize(800, 600);
//         frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//         frame.setVisible(true);
//     }
// }
