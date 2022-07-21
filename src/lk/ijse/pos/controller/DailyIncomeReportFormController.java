package lk.ijse.pos.controller;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import lk.ijse.pos.business.BOFactory;
import lk.ijse.pos.business.custom.DailyIncomeReportBO;
import lk.ijse.pos.dto.CustomDTO;
import lk.ijse.pos.view.tm.CustomTM;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;

public class DailyIncomeReportFormController {
    public Label lblDate;
    public Label lblTime;
    public TableView<CustomTM> tblDailyIncome;
    public TableColumn colDate;
    public TableColumn colIncome;
    public AnchorPane DailyIncomeContext;

   private final DailyIncomeReportBO dailyIncomeReportBO = (DailyIncomeReportBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.DAILY_INCOME);

    public void initialize(){

        colDate.setCellValueFactory(new PropertyValueFactory<>("OrderDate"));
        colIncome.setCellValueFactory(new PropertyValueFactory<>("UnitPrice"));

        loadAddDailyIncome();
        loadDateAndTime();
    }

    private void loadAddDailyIncome() {
        tblDailyIncome.getItems().clear();
        try {
            ArrayList<CustomDTO> dailyIncome = dailyIncomeReportBO.getDailyIncome();
            for (CustomDTO dto : dailyIncome) {
                tblDailyIncome.getItems().add(new CustomTM(dto.getOrderDate(),dto.getUnitPrice()));
            }
        } catch (SQLException | ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    public void backOnAction(MouseEvent event) throws IOException {
        setUI(DailyIncomeContext,"ReportsMainForm");
    }

    public void setUI(AnchorPane ap, String location) throws IOException {
        Stage stage = (Stage) ap.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/lk/ijse/pos/view/"+location+".fxml"))));
        stage.setTitle(location);
        stage.centerOnScreen();
    }

    private void loadDateAndTime() {
        /* set date*/
        lblDate.setText("Date :"+new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        /*set time*/
        Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            LocalTime currentTime = LocalTime.now();
            lblTime.setText("Time :"+currentTime.getHour() + ":" + currentTime.getMinute() + ":" + currentTime.getSecond());
        }), new KeyFrame(Duration.seconds(1))
        );
        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();
    }
}
