module SE_Projcet {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.lee.ui to javafx.fxml;
    exports com.lee.ui;
}