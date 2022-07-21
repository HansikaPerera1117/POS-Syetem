package lk.ijse.pos.controller;

import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

public class AdministratorMainFormController {
    public ImageView ImgManageItems;
    public ImageView imgSystemReports;
    public Label lblMenu;
    public Label lblDescription;
    public AnchorPane AdminMainContext;


    public void MouseEnteredOnAction(MouseEvent event) {
        if (event.getSource() instanceof ImageView) {
            ImageView icon = (ImageView) event.getSource();

            switch (icon.getId()) {
                case "ImgManageItems":
                    lblMenu.setText("Manage Items");
                    lblDescription.setText("             Click to add, edit, delete, search or view items");
                    break;
                case "imgSystemReports":
                    lblMenu.setText("System Reports");
                    lblDescription.setText("                  Click if you want to see system reports");
                    break;
            }
            ScaleTransition scaleT = new ScaleTransition(Duration.millis(200), icon);
            scaleT.setToX(1.2);
            scaleT.setToY(1.2);
            scaleT.play();

            DropShadow glow = new DropShadow();
            glow.setColor(Color.CORNFLOWERBLUE);
            glow.setWidth(20);
            glow.setHeight(20);
            glow.setRadius(20);
            icon.setEffect(glow);
        }
    }

    public void MouseExitedOnAction(MouseEvent event) {
        if (event.getSource() instanceof ImageView) {
            ImageView icon = (ImageView) event.getSource();
            ScaleTransition scaleT = new ScaleTransition(Duration.millis(200), icon);
            scaleT.setToX(1);
            scaleT.setToY(1);
            scaleT.play();

            icon.setEffect(null);
            lblMenu.setText("Welcome");
            lblDescription.setText("Please select one of above main operations to proceed");
        }
    }

    public void LogoutOnAction(MouseEvent event) throws IOException {
        setUI(AdminMainContext,"LogInForm");
    }

    public void setUI(AnchorPane ap, String location) throws IOException {
        Stage stage = (Stage) ap.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/lk/ijse/pos/view/"+location+".fxml"))));
        stage.setTitle(location);
        stage.centerOnScreen();
    }

    public void navigationOnAction(MouseEvent event) throws IOException {
        if (event.getSource() instanceof ImageView) {
            ImageView icon = (ImageView) event.getSource();

            switch (icon.getId()) {
                case "ImgManageItems":
                    setUI(AdminMainContext,"ManageItemForm");
                    break;
                case "imgSystemReports":
                    setUI(AdminMainContext,"ReportsMainForm");
                    break;
            }
        }
    }
}
