import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Test extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // Create a button
        Button button = new Button("Click me!");

        // Create a layout and add the button to it
        StackPane root = new StackPane();
        root.getChildren().add(button);

        // Create a scene with the layout
        Scene scene = new Scene(root, 300, 200);

        // Set the scene on the primary stage and show the stage
        primaryStage.setScene(scene);
        primaryStage.setTitle("Simple JavaFX Window");
        primaryStage.show();
    }
}
