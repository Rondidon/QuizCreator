module com.quizcreator.app {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires javafx.media;

    opens com.quizcreator.app to javafx.fxml;
    exports com.quizcreator.app;
}