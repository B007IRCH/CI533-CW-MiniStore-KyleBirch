package clients.admin;

import dbAccess.DerbyAccess; // Import the specific DBAccess implementation
import dbAccess.DBAccess;

public class AdminApp {
    public static void main(String[] args) {
        AdminView adminView = new AdminView(); // Create the admin view
        DBAccess dbAccess = new DerbyAccess(); // Create an instance of DBAccess (e.g., DerbyAccess)
        AdminController adminController = new AdminController(adminView, dbAccess); // Pass both arguments to AdminController
        adminView.displayMenu(); // Start the admin UI
    }
}
