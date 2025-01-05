package dbAccess;

import debug.DEBUG;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class SimpleCSVReader {

    private static final String CSV_FILE_PATH = "dbAccess/Item_Database.csv"; // Path to your CSV
    private static final String DATABASE_URL = "jdbc:sqlite:dbAccess/catshop.db"; // SQLite Database Path

    public static void populateDatabaseFromCSV() {
        String insertQuery = "INSERT OR REPLACE INTO products (product_id, description, price, stock, company, category, description_details) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(DATABASE_URL);
             BufferedReader br = new BufferedReader(new FileReader(CSV_FILE_PATH))) {

            DEBUG.trace("Connected to the database. Starting CSV population...");
            String line;
            boolean isHeader = true;

            while ((line = br.readLine()) != null) {
                // Skip the header row
                if (isHeader) {
                    isHeader = false;
                    DEBUG.trace("Skipping header row: %s", line);
                    continue;
                }

                // Split the CSV line by tab
                String[] values = line.split("\t");
                if (values.length == 7) { // Ensure the line has all required fields
                    try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
                        preparedStatement.setString(1, values[0].trim()); // product_id
                        preparedStatement.setString(2, values[1].trim()); // description
                        preparedStatement.setDouble(3, Double.parseDouble(values[2].trim())); // price
                        preparedStatement.setInt(4, Integer.parseInt(values[3].trim())); // stock
                        preparedStatement.setString(5, values[4].trim()); // company
                        preparedStatement.setString(6, values[5].trim()); // category
                        preparedStatement.setString(7, values[6].trim()); // description_details
                        preparedStatement.executeUpdate();
                        DEBUG.trace("Inserted row: %s", String.join(", ", values));
                    } catch (Exception e) {
                        DEBUG.error("Error inserting row: %s\nError: %s", line, e.getMessage());
                    }
                } else {
                    DEBUG.error("Malformed line: %s", line);
                }
            }

            DEBUG.trace("Database successfully populated from CSV!");

        } catch (Exception e) {
            DEBUG.error("Error reading or populating database: %s", e.getMessage());
        }
    }

    public static void main(String[] args) {
        DEBUG.set(true); // Enable debugging
        populateDatabaseFromCSV();
    }
}
