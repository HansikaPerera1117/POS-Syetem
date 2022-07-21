package lk.ijse.pos.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import lk.ijse.pos.business.BOFactory;
import lk.ijse.pos.business.custom.CustomerBO;
import lk.ijse.pos.business.custom.impl.CustomerBOImpl;
import lk.ijse.pos.dto.CustomerDTO;
import lk.ijse.pos.validation.ValidationUtil;
import lk.ijse.pos.view.tm.CustomerTM;
import org.controlsfx.control.Notifications;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.*;
import java.util.regex.Pattern;

public class CustomerFormController {
    public AnchorPane ManageCustomerContext;
    public Label lblDate;
    public Label lblTime;

    private final CustomerBO customerBO = (CustomerBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.CUSTOMER);
    public TableView<CustomerTM> tblCustomer;
    public TableColumn colID;
    public TableColumn colTitle;
    public TableColumn colName;
    public TableColumn colAddress;
    public TableColumn colCity;
    public TableColumn colProvince;
    public TableColumn colPostalCode;
    public TableColumn colDelete;
    public JFXButton btnAddNewCustomer;
    public JFXButton btnSave;
    public JFXTextField txtCustomerId;
    public JFXTextField txtCustomerName;
    public JFXTextField txtCustomerAddress;
    public JFXTextField txtCity;
    public JFXTextField txtCustomerTitle;
    public JFXTextField txtProvince;
    public JFXTextField txtPostalCode;

    LinkedHashMap<TextField, Pattern> map = new LinkedHashMap<>();

    public void initialize(){
         colID.setCellValueFactory(new PropertyValueFactory<>("CustID"));
         colTitle.setCellValueFactory(new PropertyValueFactory<>("CustTitle"));
         colName.setCellValueFactory(new PropertyValueFactory<>("CustName"));
         colAddress.setCellValueFactory(new PropertyValueFactory<>("CustAddress"));
         colCity.setCellValueFactory(new PropertyValueFactory<>("City"));
         colProvince.setCellValueFactory(new PropertyValueFactory<>("Province"));
         colPostalCode.setCellValueFactory(new PropertyValueFactory<>("PostalCode"));
         colDelete.setCellValueFactory(new PropertyValueFactory<>("btn"));


        loadDateAndTime();

        initialUI();

        tblCustomer.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            btnSave.setText(newValue != null ? "Update" : "Save");
            btnSave.setDisable(newValue == null);

            if (newValue != null) {
                txtCustomerId.setText(newValue.getCustID());
                txtCustomerTitle.setText(newValue.getCustTitle());
                txtCustomerName.setText(newValue.getCustName());
                txtCustomerAddress.setText(newValue.getCustAddress());
                txtCity.setText(newValue.getCity());
                txtProvince.setText(newValue.getProvince());;
                txtPostalCode.setText(newValue.getPostalCode());

                txtCustomerId.setDisable(false);
                txtCustomerTitle.setDisable(false);
                txtCustomerName.setDisable(false);
                txtCustomerAddress.setDisable(false);
                txtCity.setDisable(false);
                txtProvince.setDisable(false);
                txtPostalCode.setDisable(false);
            }
        });

    //-------------------validation pattern---------------------------------
        Pattern titlePattern = Pattern.compile("^(Mr|Mrs|Mis)$");
        Pattern namePattern = Pattern.compile("^[A-z ]{3,25}$");
        Pattern addressPattern = Pattern.compile("^[A-z0-9 ,/]{4,40}$");
        Pattern cityPattern = Pattern.compile("^[A-z]{3,15}$");
        Pattern provincePattern = Pattern.compile("^[A-z ]{5,30}$");
        Pattern postalCodePattern = Pattern.compile("^[0-9]{5}$");


        map.put(txtCustomerTitle,titlePattern);
        map.put(txtCustomerName,namePattern);
        map.put(txtCustomerAddress,addressPattern);
        map.put(txtCity,cityPattern);
        map.put(txtProvince,provincePattern);
        map.put(txtPostalCode,postalCodePattern);


        loadAllCustomers();
    }

    private void initialUI() {
        txtCustomerId.clear();
        txtCustomerTitle.clear();
        txtCustomerName.clear();
        txtCustomerAddress.clear();
        txtCity.clear();
        txtProvince.clear();
        txtPostalCode.clear();
        txtCustomerId.setDisable(true);
        txtCustomerTitle.setDisable(true);
        txtCustomerName.setDisable(true);
        txtCustomerAddress.setDisable(true);
        txtCity.setDisable(true);
        txtProvince.setDisable(true);
        txtPostalCode.setDisable(true);
        txtCustomerId.setEditable(false);
        btnSave.setDisable(true);
    }

    public void btnNewCustomerOnAction(ActionEvent actionEvent) {
        txtCustomerId.setDisable(false);
        txtCustomerTitle.setDisable(false);
        txtCustomerName.setDisable(false);
        txtCustomerAddress.setDisable(false);
        txtCity.setDisable(false);
        txtProvince.setDisable(false);
        txtPostalCode.setDisable(false);
        txtCustomerId.clear();
        txtCustomerId.setText(generateNewId());
        txtCustomerTitle.clear();
        txtCustomerName.clear();
        txtCustomerAddress.clear();
        txtCity.clear();
        txtProvince.clear();
        txtPostalCode.clear();
        txtCustomerTitle.requestFocus();
        btnSave.setDisable(false);
        btnSave.setText("Save");
        tblCustomer.getSelectionModel().clearSelection();

    }

    private String generateNewId() {
        try {
            return customerBO.generateNewCustomerID();
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
          return "C001";
    }

    private void loadAllCustomers() {
        tblCustomer.getItems().clear();

        try {
            ArrayList<CustomerDTO> allCustomers = customerBO.getAllCustomers();
            for (CustomerDTO customer : allCustomers) {
                Button btn = new Button("Delete");
                tblCustomer.getItems().add(new CustomerTM(customer.getCID(),customer.getCTitle(),customer.getCName(),customer.getCAddress(),customer.getCity(),customer.getProvince(),customer.getPostalCode(),btn));

                btn.setOnAction(e -> {

                    //------------------Delete Customer------------------
                    String id = tblCustomer.getSelectionModel().getSelectedItem().getCustID();
                    try {
                        if (!existCustomer(id)) {
                            new Alert(Alert.AlertType.ERROR, "There is no such customer associated with the id " + id).show();
                        }

                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are You Sure?", ButtonType.YES, ButtonType.NO);
                        Optional<ButtonType> buttonType = alert.showAndWait();

                        if (buttonType.get().equals(ButtonType.YES)) {

                            customerBO.deleteCustomer(id);

                            tblCustomer.getItems().remove(tblCustomer.getSelectionModel().getSelectedItem());
                            tblCustomer.getSelectionModel().clearSelection();
                            initialUI();

                            Notifications notifications = Notifications.create().title("Successful !").text("Customer has been deleted successfully...").hideAfter(Duration.seconds(5)).position(Pos.BOTTOM_RIGHT);
                            notifications.darkStyle();
                            notifications.show();

                        }

                    } catch (SQLException er) {
                        new Alert(Alert.AlertType.ERROR, "Failed to delete the customer " + id).show();
                    } catch (ClassNotFoundException er) {
                        er.printStackTrace();
                    }
                });
            }

        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        } catch (ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    public void btnSaveCustomerOnAction(ActionEvent actionEvent) {
        String CId = txtCustomerId.getText();
        if (btnSave.getText().equalsIgnoreCase("Save")) {

           //----------------- Save Customer------------------------------
            try {
                if (existCustomer(txtCustomerId.getText())) {
                    new Alert(Alert.AlertType.ERROR, CId+ " already exists").show();
                }

                customerBO.saveCustomer(new CustomerDTO(txtCustomerId.getText(),txtCustomerTitle.getText(),txtCustomerName.getText(),txtCustomerAddress.getText(),txtCity.getText(),txtProvince.getText(),txtPostalCode.getText()));

                Notifications notifications = Notifications.create().title("Successful !").text("Customer has been saved successfully...").hideAfter(Duration.seconds(5)).position(Pos.BOTTOM_RIGHT);
                notifications.darkStyle();
                notifications.show();

            } catch (SQLException |ClassNotFoundException e) {
                new Alert(Alert.AlertType.ERROR, "Failed to save the customer " + e.getMessage()).show();
                e.printStackTrace();
            }
        } else {
           //--------------------- Update customer-----------------------------
            try {
                if (!existCustomer(txtCustomerId.getText())) {
                    new Alert(Alert.AlertType.ERROR, "There is no such customer associated with the id " + CId ).show();
                }

                customerBO.updateCustomer(new CustomerDTO(txtCustomerId.getText(),txtCustomerTitle.getText(),txtCustomerName.getText(),txtCustomerAddress.getText(),txtCity.getText(),txtProvince.getText(),txtPostalCode.getText()));

                Notifications notifications = Notifications.create().title("Successful !").text("Customer has been updated successfully...").hideAfter(Duration.seconds(5)).position(Pos.BOTTOM_RIGHT);
                notifications.darkStyle();
                notifications.show();

            } catch (SQLException | ClassNotFoundException e) {
                new Alert(Alert.AlertType.ERROR, "Failed to update the customer " + CId + e.getMessage()).show();
            }
        }
             loadAllCustomers();
    }

    boolean existCustomer(String id) throws SQLException, ClassNotFoundException {
        return customerBO.customerExist(id);
    }

    public void backOnAction(MouseEvent event) throws IOException {
        setUI(ManageCustomerContext,"PlaceOrderForm");
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

    public void textFields_Key_Released(KeyEvent keyEvent) {
        ValidationUtil.validate(map,btnSave);

        if (keyEvent.getCode() == KeyCode.ENTER) {
            Object response =  ValidationUtil.validate(map,btnSave);;

            if (response instanceof TextField) {
                TextField textField = (TextField) response;
                textField.requestFocus();
            }
        }
    }
}
