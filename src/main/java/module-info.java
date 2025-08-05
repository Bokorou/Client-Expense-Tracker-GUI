module expense.tracker.client {
    requires javafx.controls;
    requires com.google.gson;
    requires jdk.jshell;
    //crucial to be able to read data from models and store them in tables
    opens  org.example.Models to javafx.base;
    exports org.example;
}