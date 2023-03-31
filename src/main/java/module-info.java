module com.quizcreator.app {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires javafx.media;
    requires org.jdom2;

    opens com.quizcreator.app to javafx.fxml;
    opens com.quizcreator.app.userinterface to javafx.fxml;

    exports com.quizcreator.app;
}