/**
 * IK1004
 * Labb 2 och 3
 * @author: Ida Funk
 */

package sample;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    public void start(Stage primaryStage) {

        Scene scene = new Scene(new GUISkiduthyrning());
        primaryStage.setScene(scene);
        primaryStage.setTitle("Skiduthyrningn AB");
        primaryStage.show();
    }

    public static void main(String[] args) {

        launch(args);
    }
}
