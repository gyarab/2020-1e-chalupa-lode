module com.mycompany.lode_rp {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.base;

    opens com.mycompany.lode_rp to javafx.fxml;
    exports com.mycompany.lode_rp;
}
