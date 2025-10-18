module com.example.whgkswo.questionroulette {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.whgkswo.questionroulette to javafx.fxml;
    exports com.example.whgkswo.questionroulette;
}