package il.cshaifasweng.OCSFMediatorExample.client;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

import java.awt.*;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Main extends Application {

	private static Scene scene;
	public static SimpleClient client;
	public static Stage primaryStage;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		// Create the root group for the scene
		Parent root = FXMLLoader.load(getClass().getResource("init.fxml"));
		Main.primaryStage = primaryStage;
		Scene scene = new Scene(root);

		Image icon = new Image("file:/D:/Learning/resturant-project/Images/restaurantLogo.jpg");
		primaryStage.getIcons().add(icon);
		// Set the title and scene for the stage
		primaryStage.setTitle("Restaurant");
		primaryStage.setWidth(700);
		primaryStage.setHeight(600);
		primaryStage.setResizable(false);
		primaryStage.setScene(scene);

		// Show the stage
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
			case "SelectBranchScreen":
				Platform.runLater(() -> {
					setWindowTitle("Select Branch");
					try {
						setContent("selected-branch");
					} catch (IOException e) {
						e.printStackTrace();
					}
				});
				break;
		}
	}

	public static void setWindowTitle(String title) {
		primaryStage.setTitle(title);
	}

	public static void setContent(String pageName) throws IOException {
		Parent root = null;
		try {
			root = FXMLLoader.load(Main.class.getResource("/il/cshaifasweng/OCSFMediatorExample/client/" + pageName + ".fxml"));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
}