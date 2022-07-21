package lk.ijse.pos.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.ReadOnlyObjectWrapper;
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
import lk.ijse.pos.business.custom.PlaceOrderBO;
import lk.ijse.pos.db.DBConnection;
import lk.ijse.pos.dto.CustomerDTO;
import lk.ijse.pos.dto.ItemDTO;
import lk.ijse.pos.dto.OrderDTO;
import lk.ijse.pos.dto.OrderDetailDTO;
import lk.ijse.pos.validation.ValidationUtil;
import lk.ijse.pos.view.tm.OrderDetailTM;
import org.controlsfx.control.Notifications;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class PlaceOrderFormController {
    public AnchorPane placeOrderContext;
    public Label lblTime;
    public Label lblDate;
    public Label lblDate1;
    private final PlaceOrderBO placeOrderBO = (PlaceOrderBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.PLACE_ORDER);
    public Label lblOrderId;
    public JFXComboBox<String> cmbCustomerId;
    public JFXTextField txtCustomerName;
    public JFXComboBox<String> cmbItemCode;
    public JFXTextField txtDescription;
    public JFXTextField txtQtyOnHand;
    public JFXTextField txtUnitPrice;
    public JFXTextField txtQty;
    public TableView<OrderDetailTM> tblOrderDetails;
    public JFXTextField txtDiscount;
    public JFXButton btnCancel;
    public Label lblDiscount;
    public Label lblTotal;
    public JFXButton btnPlaceOrder;
    public JFXButton btnAddToCart;
    public TableColumn colCode;
    public TableColumn colDescription;
    public TableColumn colQty;
    public TableColumn colUnitPrice;
    public TableColumn colTotal;
    public TableColumn colDiscount;
    public TableColumn colDelete;
    public Label lblFinalTotal;

    private String orderId;

    LinkedHashMap<TextField, Pattern> map = new LinkedHashMap<>();

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
                    tblOrderDetails.getItems().remove(param.getValue());
                    tblOrderDetails.getSelectionModel().clearSelection();
                    calculateTotalAndDiscount();
                    enableOrDisablePlaceOrderButton();
                }

            });
            return new ReadOnlyObjectWrapper<>(btnDelete);
        });

        initialUI();

        cmbCustomerId.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            enableOrDisablePlaceOrderButton();
            setCustomerDetails(newValue);
        });

        cmbItemCode.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newItemCode) -> {
            txtQty.setEditable(newItemCode != null);
            setItemDetails(newItemCode);
        });

        tblOrderDetails.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, selectedOrderDetail) -> {
                changeOrderDetails(selectedOrderDetail);
        });

        //-------------------validation pattern---------------------------------
        Pattern qtyPattern = Pattern.compile("^[1-9][0-9]{0,1}$");
        map.put(txtQty,qtyPattern);

        loadDateAndTime();
        loadAllCustomerIds();
        loadAllItemCodes();
    }

    private void changeOrderDetails(OrderDetailTM selectedOrderDetail) {
        if (selectedOrderDetail != null) {
            cmbItemCode.setDisable(true);
            cmbItemCode.setValue(selectedOrderDetail.getItemCode());
            btnAddToCart.setText("Update");
            txtQtyOnHand.setText(Integer.parseInt(txtQtyOnHand.getText()) + selectedOrderDetail.getQty() + "");
            txtQty.setText(selectedOrderDetail.getQty() + "");
        } else {
            btnAddToCart.setText("Add");
            cmbItemCode.setDisable(false);
            cmbItemCode.getSelectionModel().clearSelection();
            txtQty.clear();
        }

    }

    private void setItemDetails(String selectedItemCode) {
        if (selectedItemCode != null) {
            try {
                if (!existItem(selectedItemCode + "")) {
                    new Alert(Alert.AlertType.ERROR, "There is no such item associated with the code " + selectedItemCode + "").show();
                }

                ItemDTO item = placeOrderBO.searchItem(selectedItemCode + "");

                txtDescription.setText(item.getDescription());

                Optional<OrderDetailTM> optOrderDetail = tblOrderDetails.getItems().stream().filter(detail -> detail.getItemCode().equals(selectedItemCode)).findFirst();
                txtQtyOnHand.setText((optOrderDetail.isPresent() ? item.getQtyOnHand() - optOrderDetail.get().getQty() : item.getQtyOnHand()) + "");

                txtUnitPrice.setText(item.getUnitPrice().setScale(2).toString());
                txtDiscount.setText(item.getDiscount().setScale(2).toString());

            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
            }

        } else {
            txtDescription.clear();
            txtQtyOnHand.clear();
            txtUnitPrice.clear();
            txtDiscount.clear();
            txtQty.clear();
        }
    }

    private boolean existItem(String code) throws SQLException, ClassNotFoundException {
       return placeOrderBO.checkItemIsAvailable(code);
    }

    private void setCustomerDetails(String SelectedID) {
        if (SelectedID != null) {
            try {
                Connection connection = DBConnection.getInstance().getConnection();
                try {
                    if (!existCustomer(SelectedID + "")) {
                        new Alert(Alert.AlertType.ERROR, "There is no such customer associated with the id " + SelectedID + "").show();
                    }

                    CustomerDTO search = placeOrderBO.searchCustomer(SelectedID + "");
                    txtCustomerName.setText(search.getCName());

                } catch (SQLException e) {
                    new Alert(Alert.AlertType.ERROR, "Failed to find the customer " + SelectedID + "" + e).show();
                }

            } catch (SQLException |ClassNotFoundException e) {
                e.printStackTrace();
            }
        } else {
           txtCustomerName.clear();
        }
    }

    private boolean existCustomer(String id) throws SQLException, ClassNotFoundException {
        return placeOrderBO.checkCustomerIsAvailable(id);
    }

    private void enableOrDisablePlaceOrderButton() {
        btnPlaceOrder.setDisable(!(cmbCustomerId.getSelectionModel().getSelectedItem() != null && !tblOrderDetails.getItems().isEmpty()));
    }

    private void initialUI() {
        orderId = generateNewOrderId();
        lblOrderId.setText("Order ID: " + orderId);
        lblDate1.setText("Date :"+lblDate.getText());
        btnPlaceOrder.setDisable(true);
        txtCustomerName.setFocusTraversable(false);
        txtCustomerName.setEditable(false);
        txtDescription.setFocusTraversable(false);
        txtDescription.setEditable(false);
        txtUnitPrice.setFocusTraversable(false);
        txtUnitPrice.setEditable(false);
        txtQtyOnHand.setFocusTraversable(false);
        txtQtyOnHand.setEditable(false);
        txtDiscount.setFocusTraversable(false);
        txtDiscount.setEditable(false);
        txtQty.setOnAction(event -> btnAddToCart.fire());
        txtQty.setEditable(false);
        btnAddToCart.setDisable(true);
    }

    private String generateNewOrderId() {
        try {
            return placeOrderBO.generateNewOrderID();
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Failed to generate a new order id").show();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return "D001";
    }

    private void loadAllItemCodes() {
        try {
            ArrayList<ItemDTO> all = placeOrderBO.getAllItems();

            for (ItemDTO dto : all) {
                cmbItemCode.getItems().add(dto.getItemCode());
            }

        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void loadAllCustomerIds() {
        try {
            ArrayList<CustomerDTO> all = placeOrderBO.getAllCustomers();
            for (CustomerDTO customerDTO : all) {
                cmbCustomerId.getItems().add(customerDTO.getCID());
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Failed to load customer ids").show();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void btnAddOnAction(ActionEvent actionEvent) {
        String itemCode = cmbItemCode.getSelectionModel().getSelectedItem();
        String description = txtDescription.getText();
        int qty = Integer.parseInt(txtQty.getText());
        BigDecimal unitPrice = new BigDecimal(txtUnitPrice.getText()).setScale(2);
        BigDecimal total = unitPrice.multiply(new BigDecimal(qty)).setScale(2);
        BigDecimal discount = new BigDecimal(txtDiscount.getText()).setScale(2);
        Button btn = new Button("Delete");

        boolean exists = tblOrderDetails.getItems().stream().anyMatch(detail -> detail.getItemCode().equals(itemCode));

        if (exists) {
            OrderDetailTM orderDetailTM = tblOrderDetails.getItems().stream().filter(detail -> detail.getItemCode().equals(itemCode)).findFirst().get();

            if (btnAddToCart.getText().equalsIgnoreCase("Update")) {
                orderDetailTM.setQty(qty);
                orderDetailTM.setTotal(total);
                tblOrderDetails.getSelectionModel().clearSelection();
            } else {
                orderDetailTM.setQty(orderDetailTM.getQty() + qty);
                System.out.println(orderDetailTM.getQty());
                total = new BigDecimal(orderDetailTM.getQty()).multiply(unitPrice).setScale(2);
                orderDetailTM.setTotal(total);
            }
            tblOrderDetails.refresh();
        } else {
            tblOrderDetails.getItems().add(new OrderDetailTM(itemCode, description, qty, unitPrice, total,discount,btn));
        }
        cmbItemCode.getSelectionModel().clearSelection();
        cmbItemCode.requestFocus();
        calculateTotalAndDiscount();
        btnAddToCart.setDisable(true);
        enableOrDisablePlaceOrderButton();
    }

    private void calculateTotalAndDiscount() {
        BigDecimal total = new BigDecimal(0);
        for (OrderDetailTM detail : tblOrderDetails.getItems()) {
            total = total.add(detail.getTotal());
        }
        lblTotal.setText("Total: " + total);

        BigDecimal discount = new BigDecimal(0);
        for (OrderDetailTM detail : tblOrderDetails.getItems()) {
            discount = discount.add(detail.getUnitPrice().divide(BigDecimal.valueOf(100)).multiply(detail.getDiscount()).multiply(BigDecimal.valueOf(detail.getQty()))).setScale(2);
        }
        lblDiscount.setText("Full Discount: " + discount);

        BigDecimal finalTotal = new BigDecimal(0);

        finalTotal= total.subtract(discount);

        lblFinalTotal.setText("Final Total: " + finalTotal);

    }

    public void btnPlaceOderOnAction(ActionEvent actionEvent) {
        boolean b = saveOrder(orderId, LocalDate.now(), cmbCustomerId.getValue(),
                tblOrderDetails.getItems().stream().map(tm -> new OrderDetailDTO(orderId, tm.getItemCode(), tm.getQty(), tm.getDiscount())).collect(Collectors.toList()));
        if (b) {
            Notifications notifications = Notifications.create().title("Placed Order Successful !").text("Order has been placed successfully...").hideAfter(Duration.seconds(5)).position(Pos.BOTTOM_RIGHT);
            notifications.darkStyle();
            notifications.show();
        } else {
            new Alert(Alert.AlertType.ERROR, "Order has not been placed successfully").show();
        }
        orderId = generateNewOrderId();
        lblOrderId.setText("Order Id: " + orderId);
        cmbCustomerId.getSelectionModel().clearSelection();
        cmbItemCode.getSelectionModel().clearSelection();
        tblOrderDetails.getItems().clear();
        txtQty.clear();
        calculateTotalAndDiscount();
    }

    private boolean saveOrder(String orderId, LocalDate orderDate, String customerId, List<OrderDetailDTO> orderDetails) {
        try {
            return placeOrderBO.purchaseOrder(new OrderDTO(orderId, orderDate, customerId, orderDetails));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void btnCancelOnAction(ActionEvent actionEvent) {
        orderId = generateNewOrderId();
        lblOrderId.setText("Order Id: " + orderId);
        cmbCustomerId.getSelectionModel().clearSelection();
        cmbItemCode.getSelectionModel().clearSelection();
        tblOrderDetails.getItems().clear();
        txtQty.clear();
        calculateTotalAndDiscount();
    }

    public void btnAddNewCustomerOnAction(ActionEvent actionEvent) throws IOException {
        setUI(placeOrderContext,"CustomerForm");
    }

    public void backToCashierMainOnAction(MouseEvent event) throws IOException {
        setUI(placeOrderContext,"cashierMainForm");
    }

    public void setUI(AnchorPane ap, String location) throws IOException {
        Stage stage = (Stage) ap.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/lk/ijse/pos/view/"+location+".fxml"))));
        stage.setTitle(location);
        stage.centerOnScreen();
    }

    private void loadDateAndTime() {
        /* set date*/
        lblDate.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        /*set time*/
        Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            LocalTime currentTime = LocalTime.now();
            lblTime.setText("Time :"+currentTime.getHour() + ":" + currentTime.getMinute() + ":" + currentTime.getSecond());
        }), new KeyFrame(Duration.seconds(1))
        );
        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();

        lblDate1.setText("Date :"+lblDate.getText());
    }

    public void textFields_Key_Released(KeyEvent keyEvent) {
        ValidationUtil.validate(map,btnAddToCart);

        if (keyEvent.getCode() == KeyCode.ENTER) {
            Object response =  ValidationUtil.validate(map,btnAddToCart);;

            if (response instanceof TextField) {
                TextField textField = (TextField) response;
                textField.requestFocus();
            }
        }
    }
}
