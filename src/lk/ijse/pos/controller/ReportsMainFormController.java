package lk.ijse.pos.controller;

import javafx.animation.ScaleTransition;
import javafx.fxml.FXMLLoader;
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


public class ReportsMainFormController {

    public ImageView imgIncome;
    public ImageView imgMostMovableItems;
    public ImageView imgLessMovableItems;
    public Label lblMenu;
    public Label lblDescription;
    public AnchorPane reportsMainContext;


    public void backToAdministratorMainOnAction(MouseEvent event) throws IOException {
        setUI(reportsMainContext,"AdministratorMainForm");
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
                case "imgIncome":
                    setUI(reportsMainContext,"DailyIncomeReportForm");
                    break;
                case "imgMostMovableItems":
                    setUI(reportsMainContext,"MostMovableItemReportForm");
                    break;
                case "imgLessMovableItems":
                     setUI(reportsMainContext,"LessMovableItemReportForm");
                    break;
            }
        }
    }

    public void MouseEnteredOnAction(MouseEvent event) {
        if (event.getSource() instanceof ImageView) {
            ImageView icon = (ImageView) event.getSource();

            switch (icon.getId()) {
                case "imgIncome":
                    lblMenu.setText("       Income Reports");
                    lblDescription.setText("            Click if you want to see income reports");
                    break;
                case "imgMostMovableItems":
                    lblMenu.setText("Most Movable Item Report");
                    lblDescription.setText("      Click if you want to see most movable item report");
                    break;
                case "imgLessMovableItems":
                    lblMenu.setText("Less Movable Item Report");
                    lblDescription.setText("      Click if you want to see les movable item report");
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
            lblMenu.setText("            Welcome");
            lblDescription.setText("Please select one of above main operations to proceed");
        }
    }
}
