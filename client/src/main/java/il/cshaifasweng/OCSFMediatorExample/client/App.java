package il.cshaifasweng.OCSFMediatorExample.client;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import java.io.IOException;

public class App extends Application {
    private static Scene scene;
    public static SimpleClient client;
    public static Stage primaryStage;

    public App() {
    }

    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;

        // רישום לאירועי EventBus
        EventBus.getDefault().register(this);
        // טעינת המסך הראשוני
        scene = new Scene(loadFXML("init"), 800, 600); // שינוי ל-"menuList.fxml"
        primaryStage.setScene(scene);
        primaryStage.setTitle("Restaurant Menu Management");
        primaryStage.show();
    }

    static void setRoot(String fxml) throws Exception {
        scene.setRoot(loadFXML(fxml));
        primaryStage.setTitle(fxml); // עדכון כותרת בהתאם למסך הנוכחי
    }

    private static Parent loadFXML(String fxml) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    @Override
    public void stop() throws Exception {
        // ביטול רישום ל-EventBus
        EventBus.getDefault().unregister(this);
        super.stop();
    }

    @Subscribe
    public void onWarningEvent(WarningEvent event) {
        // טיפול באירועי אזהרה מהשרת
        Platform.runLater(() -> {
            Alert alert = new Alert(AlertType.WARNING, String.format("Message: %s\nTimestamp: %s\n",
                    event.getWarning().getMessage(),
                    event.getWarning().getTime().toString()), ButtonType.OK);
            alert.showAndWait();
        });
    }

    private void showError(String title, String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle(title);
            alert.setContentText(message);
            alert.showAndWait();
        });
    }

    public static Scene getScene() {
        return scene;
    }

    public static Stage getStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch(args);
    }


    public static void setWindowTitle(String title) {
        primaryStage.setTitle(title);
    }

    public static void setContent(String pageName) throws IOException {
        Parent root = null;
        try {
            root = loadFXML(pageName);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void switchScreen(String screenName) {
        switch (screenName) {
            case "Menu List":
                Platform.runLater(() -> {
                    setWindowTitle("Menu List");
                    try {
                        setContent("primary");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                break;
            case "Tables":
                Platform.runLater(() -> {
                    setWindowTitle("Tables");
                    try {
                        setContent("secondary");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                break;
        }
    }
}

