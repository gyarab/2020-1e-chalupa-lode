package com.mycompany.lode_rp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import javafx.scene.Node;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("menu", ""), 800, 800);
        stage.setScene(scene);
        stage.setTitle("Lodě");
        stage.show();
    }

    static void setRoot(String fxml, String name) throws IOException {
        scene.setRoot(loadFXML(fxml, name));
    }

    private static Parent loadFXML(String fxml, String name) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        Parent parent = (Parent) fxmlLoader.load();
        if (name != "") {
            ResultsController controller = fxmlLoader.getController();
            controller.passName(name);
        }
        return parent;
    }

    public static void main(String[] args) {
        launch();
    }

}
