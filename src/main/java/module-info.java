module cashcash.com.cashcash {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires com.github.librepdf.openpdf;
    requires jakarta.activation;
    requires jakarta.mail;
    requires java.desktop;

    opens com.cashcash.cashcash2 to javafx.fxml;
    exports com.cashcash.cashcash2;
}