import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

public class BGR_WikiRabbitHole extends JPanel {

    JTextField landingSearchField, resultsSearchField;
    JButton landingSearchButton, resultsSearchButton;
    CardLayout cardLayout;
    JTextArea summaryTextArea;
    GraphPanel graphPanel;
    private static final HttpClient HTTP_CLIENT = HttpClient.newHttpClient();
    SwingWorker<Void, Void> activeWorker;
    String centerArticle;
    List<String> linkedArticles = new ArrayList<>();

    public BGR_WikiRabbitHole() {
        cardLayout = new CardLayout();
        setLayout(cardLayout);

        //  Landing Card 
        JPanel landingCard = new JPanel(new GridBagLayout());
        landingCard.setBackground(new Color(30, 30, 30));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.insets = new Insets(8, 0, 8, 0);
        gbc.anchor = GridBagConstraints.CENTER;

        JLabel titleLabel = new JLabel("Wikipedia Rabbit Hole");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        gbc.gridy = 0;
        landingCard.add(titleLabel, gbc);

        landingSearchField = new JTextField(25);
        landingSearchField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        gbc.gridy = 1;
        landingCard.add(landingSearchField, gbc);

        landingSearchButton = new JButton("Search");
        landingSearchButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        landingSearchButton.setBackground(new Color(50, 100, 180));
        landingSearchButton.setForeground(Color.WHITE);
        landingSearchButton.setFocusPainted(false);
        gbc.gridy = 2;
        landingCard.add(landingSearchButton, gbc);

        add(landingCard, "landing");

        // Results Card 
        JPanel resultsCard = new JPanel(new BorderLayout());
        resultsCard.setBackground(Color.WHITE);

        resultsSearchField = new JTextField(25);
        resultsSearchField.setFont(new Font("SansSerif", Font.PLAIN, 14));

        resultsSearchButton = new JButton("Search");
        resultsSearchButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        resultsSearchButton.setBackground(new Color(50, 100, 180));
        resultsSearchButton.setForeground(Color.WHITE);
        resultsSearchButton.setFocusPainted(false);

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        topPanel.setBackground(new Color(30, 30, 30));
        topPanel.add(resultsSearchField);
        topPanel.add(resultsSearchButton);
        resultsCard.add(topPanel, BorderLayout.NORTH);

        graphPanel = new GraphPanel();
        graphPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                for (int[] pos : graphPanel.nodePositions) {
                    int dx = e.getX() - pos[0];
                    int dy = e.getY() - pos[1];
                    if (dx * dx + dy * dy <= GraphPanel.HIT_RADIUS * GraphPanel.HIT_RADIUS) {
                        String clicked = linkedArticles.get(pos[2]);
                        landingSearchField.setText(clicked);
                        resultsSearchField.setText(clicked);
                        searchArticle(clicked);
                        return;
                    }
                }
            }
        });
        summaryTextArea = new JTextArea("Article summary will appear here...");
        summaryTextArea.setLineWrap(true);
        summaryTextArea.setWrapStyleWord(true);
        summaryTextArea.setEditable(false);
        summaryTextArea.setFont(new Font("Serif", Font.PLAIN, 14));
        JScrollPane summaryScroll = new JScrollPane(summaryTextArea);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, graphPanel, summaryScroll);
        splitPane.setDividerLocation(600);
        splitPane.setResizeWeight(0.65);
        resultsCard.add(splitPane, BorderLayout.CENTER);

        add(resultsCard, "results");

        //  Search Action 
        ActionListener searchAction = e -> {
            String term = landingSearchField.isShowing()
                ? landingSearchField.getText().trim()
                : resultsSearchField.getText().trim();
            if (term.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a search term.", "Empty Search", JOptionPane.WARNING_MESSAGE);
                return;
            }
            landingSearchField.setText(term);
            resultsSearchField.setText(term);
            searchArticle(term);
        };

        landingSearchButton.addActionListener(searchAction);
        resultsSearchButton.addActionListener(searchAction);
        landingSearchField.addActionListener(searchAction);
        resultsSearchField.addActionListener(searchAction);

        cardLayout.show(this, "landing");
    }

    private void searchArticle(String term) {
        if (activeWorker != null && !activeWorker.isDone()) return;

        summaryTextArea.setText("Searching for: " + term + "...");
        cardLayout.show(this, "results");

        activeWorker = new SwingWorker<Void, Void>() {
            String summary;
            List<String> links;

            @Override
            protected Void doInBackground() throws Exception {
                summary = fetchSummary(term);
                links = fetchLinks(term);
                return null;
            }

            @Override
            protected void done() {
                try {
                    get();
                    centerArticle = term;
                    linkedArticles = links;
                    summaryTextArea.setText(summary);
                    graphPanel.repaint();
                } catch (Exception ex) {
                    centerArticle = null;
                    linkedArticles.clear();
                    summaryTextArea.setText("Article not found or failed to fetch: " + term);
                    graphPanel.repaint();
                }
            }
        };
        activeWorker.execute();
    }

    private String fetchUrl(String url) throws IOException {
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .header("User-Agent", "BGR_WikiRabbitHole/1.0")
            .timeout(Duration.ofSeconds(10))
            .GET()
            .build();
        try {
            HttpResponse<String> response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                throw new IOException("API returned status: " + response.statusCode());
            }
            return response.body();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IOException("Request interrupted", e);
        }
    }

    private String fetchSummary(String title) throws IOException {
        String url = "https://en.wikipedia.org/api/rest_v1/page/summary/"
            + URLEncoder.encode(title, StandardCharsets.UTF_8).replace("+", "%20");
        String response = fetchUrl(url);
        JsonObject json = new Gson().fromJson(response, JsonObject.class);
        return json.get("extract").getAsString();
    }

    private List<String> fetchLinks(String title) throws IOException {
        String url = "https://en.wikipedia.org/w/api.php?action=query&titles="
            + URLEncoder.encode(title, StandardCharsets.UTF_8).replace("+", "%20")
            + "&prop=links&pllimit=20&format=json";
        String response = fetchUrl(url);
        JsonObject json = new Gson().fromJson(response, JsonObject.class);
        JsonObject pages = json.getAsJsonObject("query").getAsJsonObject("pages");
        JsonObject page = pages.entrySet().iterator().next().getValue().getAsJsonObject();
        JsonArray linksArray = page.getAsJsonArray("links");

        List<String> titles = new ArrayList<>();
        if (linksArray != null) {
            for (int i = 0; i < linksArray.size(); i++) {
                titles.add(linksArray.get(i).getAsJsonObject().get("title").getAsString());
            }
        }
        return titles;
    }

    // Inner class for the graph drawing area
    class GraphPanel extends JPanel {
        static final int DOT_RADIUS = 6;
        static final int HIT_RADIUS = 12;
        List<int[]> nodePositions = new ArrayList<>(); // {x, y, index}
        int hoveredIndex = -1;

        GraphPanel() {
            setBackground(new Color(245, 245, 245));

            addMouseMotionListener(new MouseMotionAdapter() {
                @Override
                public void mouseMoved(MouseEvent e) {
                    int oldHovered = hoveredIndex;
                    hoveredIndex = -1;
                    for (int[] pos : nodePositions) {
                        int dx = e.getX() - pos[0];
                        int dy = e.getY() - pos[1];
                        if (dx * dx + dy * dy <= HIT_RADIUS * HIT_RADIUS) {
                            hoveredIndex = pos[2];
                            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                            break;
                        }
                    }
                    if (hoveredIndex == -1) {
                        setCursor(Cursor.getDefaultCursor());
                    }
                    if (hoveredIndex != oldHovered) repaint();
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            nodePositions.clear();

            if (centerArticle == null || linkedArticles.isEmpty()) {
                g2d.setColor(Color.GRAY);
                g2d.setFont(new Font("SansSerif", Font.ITALIC, 16));
                String msg = "Graph will appear here";
                FontMetrics fm = g2d.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(msg)) / 2;
                int y = getHeight() / 2;
                g2d.drawString(msg, x, y);
                return;
            }

            int centerX = getWidth() / 2;
            int centerY = getHeight() / 2;
            int radius = Math.min(getWidth(), getHeight()) * 2 / 5;

            // Draw lines from center to each linked node
            g2d.setColor(new Color(200, 200, 200));
            g2d.setStroke(new BasicStroke(1.0f));
            for (int i = 0; i < linkedArticles.size(); i++) {
                double angle = 2 * Math.PI * i / linkedArticles.size();
                int nx = centerX + (int) (radius * Math.cos(angle));
                int ny = centerY + (int) (radius * Math.sin(angle));
                g2d.drawLine(centerX, centerY, nx, ny);
            }

            // Draw linked nodes as small dots with names
            g2d.setFont(new Font("SansSerif", Font.PLAIN, 11));
            for (int i = 0; i < linkedArticles.size(); i++) {
                double angle = 2 * Math.PI * i / linkedArticles.size();
                int nx = centerX + (int) (radius * Math.cos(angle));
                int ny = centerY + (int) (radius * Math.sin(angle));

                nodePositions.add(new int[]{nx, ny, i});

                // Black dot, blue on hover
                boolean hovered = (i == hoveredIndex);
                g2d.setColor(hovered ? new Color(50, 100, 180) : Color.BLACK);
                g2d.fillOval(nx - DOT_RADIUS, ny - DOT_RADIUS, DOT_RADIUS * 2, DOT_RADIUS * 2);

                // Name next to the dot
                g2d.setColor(hovered ? new Color(50, 100, 180) : Color.DARK_GRAY);
                String title = linkedArticles.get(i);
                FontMetrics fm = g2d.getFontMetrics();
                // Place text to the right or left depending on position
                if (nx >= centerX) {
                    g2d.drawString(title, nx + DOT_RADIUS + 4, ny + fm.getAscent() / 2);
                } else {
                    g2d.drawString(title, nx - DOT_RADIUS - 4 - fm.stringWidth(title), ny + fm.getAscent() / 2);
                }
            }

            // Draw center node (slightly larger)
            g2d.setColor(new Color(50, 100, 180));
            g2d.fillOval(centerX - 10, centerY - 10, 20, 20);
            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("SansSerif", Font.BOLD, 11));
            FontMetrics fm = g2d.getFontMetrics();
            g2d.drawString(centerArticle, centerX - fm.stringWidth(centerArticle) / 2, centerY - 14);
        }
    }
}
