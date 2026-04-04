import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.swing.*;
import java.nio.file.Files;
import java.nio.file.Path;

public class BGR_StoicJournal extends JPanel {
    private static final String QUOTE_API_URL = "https://zenquotes.io/api/today";
    private static final HttpClient HTTP_CLIENT = HttpClient.newHttpClient();

    JLabel dateJLabel, quoteJLabel;
    SwingWorker<String, Void> activeWorker;
    JTextArea journalTextArea;
    String rawQuote = "";


    public BGR_StoicJournal() {
        setLayout(new BorderLayout());
        setBackground(Color.BLACK);


        JPanel northPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        northPanel.setBackground(Color.gray);
        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("MMMM d, yyyy"));
        dateJLabel = new JLabel("Date: " + today);
        dateJLabel.setForeground(Color.WHITE);
        northPanel.add(dateJLabel);
        add(northPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridLayout(2, 1));
        centerPanel.setBackground(Color.WHITE);

        // Quote
        JPanel quotePanel = new JPanel(new BorderLayout());
        quotePanel.setBackground(Color.WHITE);


        quoteJLabel = new JLabel("Quote: --", SwingConstants.CENTER);
        quoteJLabel.setFont(new Font("Georgia", Font.ITALIC, 18));
        quoteJLabel.setForeground(Color.DARK_GRAY);

        quotePanel.add(quoteJLabel, BorderLayout.CENTER);

        // Text Area
        JPanel textAreaPanel = new JPanel(new BorderLayout());
        textAreaPanel.setBackground(Color.WHITE);

        String placeholder = "Write your reflection here...";
        journalTextArea = new JTextArea(placeholder, 8, 30);
        journalTextArea.setLineWrap(true);
        journalTextArea.setWrapStyleWord(true);
        journalTextArea.setFont(new Font("Serif", Font.PLAIN, 13));
        journalTextArea.setForeground(Color.GRAY);
        journalTextArea.setFocusable(false);

        journalTextArea.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                journalTextArea.setFocusable(true);
                journalTextArea.requestFocusInWindow();
            }
        });

        journalTextArea.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (journalTextArea.getText().equals(placeholder)) {
                    journalTextArea.setText("");
                    journalTextArea.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (journalTextArea.getText().isEmpty()) {
                    journalTextArea.setText(placeholder);
                    journalTextArea.setForeground(Color.GRAY);
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(journalTextArea);
        textAreaPanel.add(scrollPane, BorderLayout.CENTER);

        centerPanel.add(quotePanel);
        centerPanel.add(textAreaPanel);
        add(centerPanel, BorderLayout.CENTER);

        // South panel with Save Reflection button
        JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        southPanel.setBackground(Color.DARK_GRAY);

        JButton saveButton = new JButton("Save Reflection");
        saveButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        saveButton.setBackground(new Color(60, 120, 80));
        saveButton.setForeground(Color.WHITE);
        saveButton.setFocusPainted(false);

        southPanel.add(saveButton);
        saveButton.addActionListener (e -> { 
            String reflection = journalTextArea.getText();
            if (reflection.isEmpty() || reflection.equals(placeholder)) {
                JOptionPane.showMessageDialog(this, "Please write a reflection before saving.", "Empty Reflection", JOptionPane.WARNING_MESSAGE);
                return;
            }
            JsonObject entry = new JsonObject();
            entry.addProperty("date", dateJLabel.getText().replace("Date: ", ""));
            entry.addProperty("quote", rawQuote);
            entry.addProperty("reflection", reflection);

            try {
                saveJournalEntry(Path.of("journal_log.json"), entry);
                JOptionPane.showMessageDialog(this, "Reflection saved successfully!", "Saved", JOptionPane.INFORMATION_MESSAGE);
                journalTextArea.setText(placeholder);
                journalTextArea.setForeground(Color.GRAY);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Failed to save reflection: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }

        });
        add(southPanel, BorderLayout.SOUTH);

        fetchAndUpdate();
    }

        //  Fetches a random stoic quote and updates the UI
        private void fetchAndUpdate() {
            if (activeWorker != null && !activeWorker.isDone()) return;

            activeWorker = new SwingWorker<String,Void>() {
                @Override
                protected String doInBackground() throws Exception {
                    return fetchRandomQuote();
                }

                @Override
                protected void done() {
                    try {
                        String quote = get();
                        rawQuote = quote;
                        quoteJLabel.setText("<html><div style='text-align:center;'>" + quote + "</div></html>");
                    }
                    catch (Exception ex) {
                        quoteJLabel.setText("Failed to fetch quote");
                    }


            };


        };        activeWorker.execute();
    }

    private String fetchRandomQuote() throws IOException {
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(QUOTE_API_URL))
            .timeout(Duration.ofSeconds(10))
            .GET()
            .build();
        try {
            HttpResponse<String> response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                throw new IOException("API returned non-OK status: " + response.statusCode());
            }
            JsonArray jsonArray = new Gson().fromJson(response.body(), JsonArray.class);
            try {
                JsonObject json = jsonArray.get(0).getAsJsonObject();
                String quote = json.get("q").getAsString();
                String author = json.get("a").getAsString();
                return "\"" + quote + "\" — " + author;
            } catch (JsonSyntaxException | NullPointerException | IndexOutOfBoundsException e) {
                throw new IOException("Invalid JSON structure: " + e.getMessage());
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IOException("Request interrupted", e);
        }
    }

    // method to read in json file and return a Json Array
    private JsonArray readJournalEntries(Path filePath) throws IOException {
        try (BufferedReader reader = Files.newBufferedReader(filePath)) {
            return new Gson().fromJson(reader, JsonArray.class);
        }
    }

    private void saveJournalEntry (Path filePath, JsonObject entry) throws IOException {
        JsonArray entries;
        if (Files.exists(filePath)) {
            entries = readJournalEntries(filePath);
        } else {
            entries = new JsonArray();
        }
        entries.add(entry);
        try (BufferedWriter writer = Files.newBufferedWriter(filePath)) {
            new GsonBuilder().setPrettyPrinting().create().toJson(entries, writer);
        }
    }
}