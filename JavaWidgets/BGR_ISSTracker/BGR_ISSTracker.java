import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.ExecutionException;
import javax.imageio.ImageIO;
import javax.swing.*;

public class BGR_ISSTracker extends JPanel {
    private static final String ISS_API_URL = "https://api.wheretheiss.at/v1/satellites/25544";

    private static final HttpClient HTTP_CLIENT = HttpClient.newHttpClient();
    private double latitude, longitude;
    private Timer timer;
    private SwingWorker<double[], Void> activeWorker;
    private JLabel latJLabel, lngJLabel, statusMessageJLabel;
    private BufferedImage worldMap;

    public BGR_ISSTracker() {
        setLayout(new BorderLayout());
        setBackground(Color.BLACK);

        try {
            worldMap = ImageIO.read(new File("BGR_Media/world_map.png"));
        } catch (IOException ex) {
            System.err.println("Failed to load world map: " + ex.getMessage());
        }

        JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        southPanel.setBackground(Color.BLACK);
        latJLabel = new JLabel("Latitude: --");
        latJLabel.setForeground(Color.WHITE);
        lngJLabel = new JLabel("Longitude: --");
        lngJLabel.setForeground(Color.WHITE);
        statusMessageJLabel = new JLabel("Waiting for ISS data...");
        statusMessageJLabel.setForeground(Color.WHITE);

        southPanel.add(latJLabel);
        southPanel.add(lngJLabel);
        southPanel.add(statusMessageJLabel);
        add(southPanel, BorderLayout.SOUTH);

        timer = new Timer(5000, e -> fetchAndUpdate());
        timer.setInitialDelay(0);
        timer.start();
    }

    public void stop() {
        timer.stop();
    }

    // Fetches ISS position in a background thread and updates the UI
    private void fetchAndUpdate() {
        if (activeWorker != null && !activeWorker.isDone()) return;

        statusMessageJLabel.setText("Fetching ISS data...");
        activeWorker = new SwingWorker<>() {
            @Override
            protected double[] doInBackground() throws Exception {
                return fetchIssPosition();
            }

            @Override
            protected void done() {
                try {
                    double[] position = get();
                    latitude = position[0];
                    longitude = position[1];
                    latJLabel.setText(String.format("Latitude: %.4f", latitude));
                    lngJLabel.setText(String.format("Longitude: %.4f", longitude));
                    statusMessageJLabel.setText("ISS data updated");
                    repaint();
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                    statusMessageJLabel.setText("ISS data unavailable");
                } catch (ExecutionException ex) {
                    statusMessageJLabel.setText("ISS data unavailable");
                }
            }
        };

        activeWorker.execute();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        if (worldMap != null) {
            g2d.drawImage(worldMap, 0, 0, getWidth(), getHeight(), null);
        }
        g2d.setColor(Color.RED);
        g2d.fillOval(lonToX(longitude) - 5, latToY(latitude) - 5, 10, 10);
    }

    // Converts longitude (-180 to 180) to pixel X coordinate
    private int lonToX(double longitude) {
        return (int) ((longitude + 180) * (getWidth() / 360.0));
    }

    // Converts latitude (90 to -90) to pixel Y coordinate
    private int latToY(double latitude) {
        return (int) ((90 - latitude) * (getHeight() / 180.0));
    }

    // Calls the ISS API and returns [latitude, longitude]
    private double[] fetchIssPosition() throws IOException {
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(ISS_API_URL))
            .timeout(Duration.ofSeconds(5))
            .GET()
            .build();
        try {
            HttpResponse<String> response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                throw new IOException("ISS API returned HTTP " + response.statusCode());
            }
            JsonObject json = new Gson().fromJson(response.body(), JsonObject.class);
            double lat = json.get("latitude").getAsDouble();
            double lon = json.get("longitude").getAsDouble();
            return new double[]{ lat, lon };
        } catch (JsonSyntaxException | NullPointerException ex) {
            throw new IOException("Invalid ISS API response", ex);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            throw new IOException("Request interrupted", ex);
        }
    }
}