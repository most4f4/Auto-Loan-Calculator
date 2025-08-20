module ca.workshop3 {
    requires javafx.controls;
    requires javafx.fxml;

    opens ca.workshop3 to javafx.fxml;
    opens Controllers to javafx.fxml;

    exports ca.workshop3;
    exports Controllers;
    exports Models;
    exports Interfaces;
}