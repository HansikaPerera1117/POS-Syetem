package lk.ijse.pos.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.ObservableList;
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
import lk.ijse.pos.business.custom.ManageOrderBO;
import lk.ijse.pos.db.DBConnection;
import lk.ijse.pos.dto.CustomerDTO;
import lk.ijse.pos.dto.OrderDTO;
import lk.ijse.pos.dto.OrderDetailDTO;
import lk.ijse.pos.validation.ValidationUtil;
import lk.ijse.pos.view.tm.CustomerTM;
import lk.ijse.pos.view.tm.OrderDetailTM;
import org.controlsfx.control.Notifications;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ManageOrderFormController {
    public AnchorPane ManageOrderContext;
    public Label lblDate;
    public Label lblTime;
    public Label lblFinalTotal;
    public Label lblTotal;
    public Label lblDiscount;
    public Label lblOrderDate;
    public JFXComboBox <String>cmbCustomerID;
    public Label lblOrderID;
    public JFXTextField txtItemCode;
    public Label lblCustomerID;
    public JFXTextField txtDiscount;
    public TableColumn colDelete;
    public TableColumn colDiscount;
    public TableColumn colTotal;
    public TableColumn colUnitPrice;
    public TableColumn colQty;
    public TableColumn colDescription;
    public TableColumn colCode;
    public TableView <OrderDetailTM>tblOrderDetails;
    public JFXButton btnRemoveOrder;
    public JFXButton btnConfirmEdits;
    public JFXButton btnChangeQTY;
    public JFXTextField txtQty;
    public JFXTextField txtUnitPrice;
    public JFXTextField txtDescription;
    public JFXComboBox<String> cmbOrderID;

    LinkedHashMap<TextField, Pattern> map = new LinkedHashMap<>();

    private final ManageOrderBO manageOrderBO = (ManageOrderBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.MANAGE_ORDER);

    private String deleteItemCode=null;
    private int nonUpdatedQty=0;
    private String selectedOrderId=null;


    public void initialize(){

        colCode.setCellValueFactory(new PropertyValueFactory<>("ItemCode"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("Description"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("Qty"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("UnitPrice"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("Total"));
        colDiscount.setCellValueFactory(new PropertyValueFactory<>("Discount"));

        TableColumn<OrderDetailTM, Button> lastCol = (TableColumn<OrderDetailTM, Button>) tblOrderDetails.getColumns().get(6);
        lastCol.setCellValueFactory(param -> {
            Button btnDelete = new Button("Delete");
            btnDelete.setOnAction(event -> {

                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are You Sure?", ButtonType.YES, ButtonType.NO);
                Optional<ButtonType> buttonType = alert.showAndWait();

                if (buttonType.get().equals(ButtonType.YES)) {

                    deleteItemCode = tblOrderDetails.getSelectionModel().getSelectedItem().getItemCode();
                    tblOrderDetails.getItems().remove(param.getValue());
                    calculateTotalAndDiscount();
                    btnConfirmEdits.setDisable(false);

                }

            });
            return new ReadOnlyObjectWrapper<>(btnDelete);
        });

        initialUI();

        cmbOrderID.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            selectedOrderId=newValue;
            enableOrDisableRemoveOrderButton();
            setOrderDetailsToTable(newValue);
            lblOrderID.setText("Order ID: "+newValue);
        });

        cmbCustomerID.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            cmbOrderID.getItems().clear();
            setOrderIdsAccordingToCustomerId(newValue);
            lblOrderID.setText("Order ID: ");
            lblCustomerID.setText("Customer ID: "+newValue);
            lblOrderDate.setText("Order Date: ");
        });

        tblOrderDetails.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, selectedOrderDetail) -> {
            changeOrderDetails(selectedOrderDetail);
        });

        //-------------------validation pattern---------------------------------
        Pattern qtyPattern = Pattern.compile("^[1-9][0-9]{0,1}$");
        map.put(txtQty,qtyPattern);

        loadDateAndTime();
        loadAllOrderIds();
        loadAllCustomerIds();
    }

    private void changeOrderDetails(OrderDetailTM selectedOrderDetail) {
        if (selectedOrderDetail != null) {
            cmbOrderID.setDisable(true);
            cmbCustomerID.setDisable(true);
            txtItemCode.setText(selectedOrderDetail.getItemCode());
            txtDescription.setText(selectedOrderDetail.getDescription());
            txtUnitPrice.setText(selectedOrderDetail.getUnitPrice().toString());
            txtDiscount.setText(selectedOrderDetail.getDiscount().toString());
            txtQty.setText(selectedOrderDetail.getQty() + "");
            txtQty.setEditable(true);
            btnChangeQTY.setDisable(false);
        } else {
            cmbOrderID.setDisable(false);
            cmbCustomerID.setDisable(false);
            cmbOrderID.getSelectionModel().clearSelection();
            cmbCustomerID.getSelectionModel().clearSelection();
            txtItemCode.clear();
            txtDescription.clear();
            txtUnitPrice.clear();
            txtDiscount.clear();
            txtQty.clear();
        }
    }

    private void setOrderIdsAccordingToCustomerId(String selectedCustomerId) {
        try {
            ArrayList<OrderDTO> all = manageOrderBO.getAllOrdersAccordingToCustomerID(selectedCustomerId);
            for (OrderDTO orderDTO : all) {
                cmbOrderID.getItems().add(orderDTO.getOrderID());
            }

        } catch (SQLException | ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR, "Failed to load order ids").show();
        }
    }

    private void setOrderDetailsToTable(String selectOrderId) {
        tblOrderDetails.getItems().clear();

        if (selectOrderId != null) {
            try {
                Connection connection = DBConnection.getInstance().getConnection();
                try {
                    if (!existOrder(selectOrderId + "")) {
                        new Alert(Alert.AlertType.ERROR, "There is no such order associated with the id " + selectOrderId + "").show();
                        btnRemoveOrder.setDisable(true);
                    }
                        btnRemoveOrder.setDisable(false);
                     ArrayList<OrderDetailDTO> orderDetail = manageOrderBO.searchOrderDetail(selectOrderId + "");

                    for (OrderDetailDTO dto : orderDetail) {

                        lblCustomerID.setText("Customer ID: "+dto.getCustID());
                        lblOrderDate.setText("Order Date: "+dto.getOrderDate().toString());

                        int qty = dto.getOrderQTY();
                        BigDecimal unitPrice = new BigDecimal(String.valueOf(dto.getUnitePrice())).setScale(2);
                        BigDecimal total = unitPrice.multiply(new BigDecimal(qty)).setScale(2);
                        String itemCode = dto.getItemCode();
                        String itemDescription = dto.getItemDescription();
                        BigDecimal discount = new BigDecimal(String.valueOf(dto.getDiscountPriceForOneItem())).setScale(2);

                        Button btn = new Button("Delete");
                        tblOrderDetails.getItems().add(new OrderDetailTM(itemCode, itemDescription, qty, unitPrice, total, discount, btn));
                        calculateTotalAndDiscount();
                    }

                } catch (SQLException e) {
                    new Alert(Alert.AlertType.ERROR, "Failed to find the order " + selectOrderId + "" + e).show();
                }

            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            tblOrderDetails.getItems().clear();
        }
    }

    private void calculateTotalAndDiscount() {
        BigDecimal total = new BigDecimal(0);
        for (OrderDetailTM detail : tblOrderDetails.getItems()) {
            total = total.add(detail.getTotal());
        }
        lblTotal.setText("Total: " + total);

        BigDecimal discount = new BigDecimal(0);
        for (OrderDetailTM detail : tblOrderDetails.getItems()) {
            discount = discount.add(detail.getDiscount().multiply(BigDecimal.valueOf(detail.getQty()))).setScale(2);
        }
        lblDiscount.setText("Full Discount: " + discount);

        BigDecimal finalTotal = new BigDecimal(0);

        finalTotal= total.subtract(discount);

        lblFinalTotal.setText("Final Total: " + finalTotal);
    }

    private boolean existOrder(String id) throws SQLException, ClassNotFoundException {
       return manageOrderBO.orderExist(id);
    }

    private void enableOrDisableRemoveOrderButton() {
        btnRemoveOrder.setDisable(!(cmbOrderID.getSelectionModel().getSelectedItem() != null && !tblOrderDetails.getItems().isEmpty()));
    }

    private void initialUI() {
        btnConfirmEdits.setDisable(true);
        btnRemoveOrder.setDisable(true);
        btnChangeQTY.setDisable(true);
        txtItemCode.setFocusTraversable(false);
        txtItemCode.setEditable(false);
        txtDescription.setFocusTraversable(false);
        txtDescription.setEditable(false);
        txtUnitPrice.setFocusTraversable(false);
        txtUnitPrice.setEditable(false);
        txtDiscount.setFocusTraversable(false);
        txtDiscount.setEditable(false);
        txtQty.setOnAction(event -> btnChangeQTY.fire());
        txtQty.setEditable(false);
    }

    private void loadAllCustomerIds() {
        try {
            ArrayList<CustomerDTO> all = manageOrderBO.getAllCustomers();
            for (CustomerDTO customerDTO : all) {
                cmbCustomerID.getItems().add(customerDTO.getCID());
            }

        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Failed to load customer ids").show();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void loadAllOrderIds() {
        try {
            ArrayList<OrderDTO> all = manageOrderBO.getAllOrders();
            for (OrderDTO orderDTO : all) {
                cmbOrderID.getItems().add(orderDTO.getOrderID());
            }

        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Failed to load order ids").show();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void btnChangeQTYOnAction(ActionEvent actionEvent) {
        String itemCode = txtItemCode.getText();
        int qty = Integer.parseInt(txtQty.getText());
        BigDecimal unitPrice = new BigDecimal(txtUnitPrice.getText()).setScale(2);
        BigDecimal total = unitPrice.multiply(new BigDecimal(qty)).setScale(2);

        boolean exists = tblOrderDetails.getItems().stream().anyMatch(detail -> detail.getItemCode().equals(itemCode));

        if (exists) {
            if (tblOrderDetails.getSelectionModel().getSelectedItem() != null) {
                OrderDetailTM selectedItem = tblOrderDetails.getSelectionModel().getSelectedItem();
                nonUpdatedQty=selectedItem.getQty();
                selectedItem.setQty(qty);
                selectedItem.setTotal(total);
                tblOrderDetails.refresh();
                btnConfirmEdits.setDisable(false);

                Notifications notifications = Notifications.create().title("Successful !").text("Item QTY has been changed successfully...").hideAfter(Duration.seconds(5)).position(Pos.BOTTOM_RIGHT);
                notifications.darkStyle();
                notifications.show();
            }
            tblOrderDetails.refresh();
        } else{
            new Alert(Alert.AlertType.ERROR, "Item not exists").show();
        }
        calculateTotalAndDiscount();
        txtItemCode.clear();
        txtDescription.clear();
        txtUnitPrice.clear();;
        txtDiscount.clear();
        txtQty.clear();
    }

    public void btnConfirmEditsOnAction(ActionEvent actionEvent) {
        try {
            if (!existOrder(selectedOrderId)) {
                new Alert(Alert.AlertType.ERROR, "There is no such order associated with the id " + selectedOrderId ).show();
            }

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are You Sure?", ButtonType.YES, ButtonType.NO);
            Optional<ButtonType> buttonType = alert.showAndWait();
            if (buttonType.get().equals(ButtonType.YES)) {

                ObservableList<OrderDetailTM> items = tblOrderDetails.getItems();
                boolean b= false;
                for (OrderDetailTM orderDetailTM: items) {
                    b = manageOrderBO.updateOrderDetails(new OrderDetailDTO(selectedOrderId, orderDetailTM.getItemCode(), orderDetailTM.getQty(), orderDetailTM.getDiscount(),nonUpdatedQty));
                }

                if (deleteItemCode != null){
                    boolean b1 = manageOrderBO.removeOrderDetails(selectedOrderId,deleteItemCode);
                    if (b1) {
                        new Alert(Alert.AlertType.INFORMATION, "Order details has been updated successfully").show();
                    }else {
                        new Alert(Alert.AlertType.ERROR, "Order details has not been updated successfully").show();
                    }
                    deleteItemCode=null;
                }

                if (b) {
                    new Alert(Alert.AlertType.INFORMATION, "Order details has been updated successfully").show();
                }

            }

        } catch (SQLException | ClassNotFoundException e ) {
            e.printStackTrace();
        }
        btnConfirmEdits.setDisable(true);
    }

    public void btnRemoveOrderOnAction(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are You Sure?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> buttonType = alert.showAndWait();

        if (buttonType.get().equals(ButtonType.YES)) {

            boolean b = removeOrder(selectedOrderId);
            if (b) {
                new Alert(Alert.AlertType.INFORMATION, "Order has been removed successfully").show();
                cancelSearching();
            } else {
                new Alert(Alert.AlertType.ERROR, "Order has not been removed successfully").show();
            }
        }
    }

    private boolean removeOrder(String id) {
        try {
            return manageOrderBO.removeOrder(id);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void btnCancelOnAction(ActionEvent actionEvent) {
        cancelSearching();
    }

    private void cancelSearching() {
        btnChangeQTY.setDisable(true);
        btnConfirmEdits.setDisable(true);
        btnRemoveOrder.setDisable(true);
        cmbOrderID.setDisable(false);
        cmbCustomerID.setDisable(false);
        cmbOrderID.getSelectionModel().clearSelection();
        cmbCustomerID.getSelectionModel().clearSelection();
        lblOrderID.setText("Order ID: ");
        lblOrderDate.setText("Order Date: ");
        lblCustomerID.setText("Customer ID: ");
        txtItemCode.clear();
        txtDescription.clear();
        txtUnitPrice.clear();;
        txtDiscount.clear();
        txtQty.clear();
        tblOrderDetails.getItems().clear();
        calculateTotalAndDiscount();
        loadAllOrderIds();
    }

    public void backToCashierMainOnAction(MouseEvent event) throws IOException {
        setUI(ManageOrderContext,"cashierMainForm");
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
        ValidationUtil.validate(map,btnChangeQTY);

        if (keyEvent.getCode() == KeyCode.ENTER) {
            Object response =  ValidationUtil.validate(map,btnChangeQTY);;

            if (response instanceof TextField) {
                TextField textField = (TextField) response;
                textField.requestFocus();
            }
        }
    }
}
