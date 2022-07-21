package lk.ijse.pos.controller;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;
import java.io.IOException;


public class LogInFormController {
    public JFXTextField txtCashierUserName;
    public JFXTextField txtAdminUsername;
    public JFXPasswordField pwdCashierPassword;
    public JFXPasswordField pwdAdminPassword;
    public AnchorPane LogInContext;
    int CashierAttempts=0;
    int AdminAttempts=0;


    public void btnCashierLogIn(ActionEvent actionEvent) throws IOException {
        CashierAttempts++;
        if (CashierAttempts<=3){
            if (txtCashierUserName.getText().equals("Cashier") && pwdCashierPassword.getText().equals("2468")){

                setUI(LogInContext,"cashierMainForm");

                Notifications notifications = Notifications.create().title("LogIn Successful !").text("You lodged in to the system  successfully...").hideAfter(Duration.seconds(5)).position(Pos.BOTTOM_RIGHT);
                notifications.darkStyle();
                notifications.show();

            }else if(txtCashierUserName.getText().equals("") && pwdCashierPassword.getText().equals("")){
                Notifications notifications = Notifications.create().title("Data Required !").text("Please enter your Username and Password...").hideAfter(Duration.seconds(5)).position(Pos.BOTTOM_RIGHT);
                notifications.darkStyle();
                notifications.show();
            }else{
                Notifications notifications = Notifications.create().title("LogIn Unsuccessful !").text("Please check and  Try again!").hideAfter(Duration.seconds(5)).position(Pos.BOTTOM_RIGHT);
                notifications.darkStyle();
                notifications.show();
            }
        }else{
            txtCashierUserName.setEditable(false);
            pwdCashierPassword.setEditable(false);
        }
    }

    public void btnAdminLogIn(ActionEvent actionEvent) throws IOException {
        AdminAttempts++;
        if (AdminAttempts<=3){
            if (txtAdminUsername.getText().equals("Admin") && pwdAdminPassword.getText().equals("1234")){

                setUI(LogInContext,"AdministratorMainForm");

                Notifications notifications = Notifications.create().title("LogIn Successful !").text("You lodged in to the system  successfully...").hideAfter(Duration.seconds(5)).position(Pos.BOTTOM_RIGHT);
                notifications.darkStyle();
                notifications.show();

            }else if(txtAdminUsername.getText().equals("") && pwdAdminPassword.getText().equals("")){
                Notifications notifications = Notifications.create().title("Data Required !").text("Please enter your Username and Password...").hideAfter(Duration.seconds(5)).position(Pos.BOTTOM_RIGHT);
                notifications.darkStyle();
                notifications.show();
            }else{
                Notifications notifications = Notifications.create().title("LogIn Unsuccessful !").text("Please check and  Try again!").hideAfter(Duration.seconds(5)).position(Pos.BOTTOM_RIGHT);
                notifications.darkStyle();
                notifications.show();
            }
        }else{
            txtAdminUsername.setEditable(false);
            pwdAdminPassword.setEditable(false);
        }
    }

    public void setUI(AnchorPane ap, String location) throws IOException {
        Stage stage = (Stage) ap.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/" + location + ".fxml"))));
        stage.setTitle(location);
        stage.centerOnScreen();
    }

}