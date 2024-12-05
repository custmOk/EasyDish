module org.easydish.easydish {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires com.google.gson;
    requires java.net.http;
    requires com.opencsv;
    requires java.desktop;

    opens org.easydish.easydish to javafx.fxml;
    exports org.easydish.easydish;
    exports org.easydish.easydish.records;
    opens org.easydish.easydish.records to javafx.fxml;
}