package clients.admin;

public class AdminApp {
    public static void main(String[] args) {
        AdminView adminView = new AdminView(); // Create the admin view
        AdminController adminController = new AdminController(adminView); // Link the controller with the view
        adminView.displayMenu(); // Start the admin UI
    }
}
