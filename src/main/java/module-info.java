module com.nonstick.autosuggestjavafx {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires java.desktop;

    opens com.nonstick.autosuggestjavafx to javafx.fxml;
    exports com.nonstick.autosuggestjavafx;
}