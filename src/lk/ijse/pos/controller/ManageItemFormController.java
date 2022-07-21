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
import lk.ijse.pos.business.custom.ItemBO;
import lk.ijse.pos.dto.ItemDTO;
import lk.ijse.pos.validation.ValidationUtil;
import lk.ijse.pos.view.tm.ItemTM;
import org.controlsfx.control.Notifications;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Optional;
import java.util.regex.Pattern;

public class ManageItemFormController {
    public AnchorPane ManageItemContext;
    public Label lblDate;
    public Label lblTime;
    public JFXTextField txtCode;
    public JFXTextField txtDescription;
    public JFXTextField txtPackSize;
    public JFXTextField txtUnitPrice;
    public JFXTextField txtQtyOnHand;
    public JFXTextField txtDiscount;
    public TableView<ItemTM> tblItem;
    public TableColumn colCode;
    public TableColumn colDescription;
    public TableColumn colPackSize;
    public TableColumn colUnitePrice;
    public TableColumn colQtyOnHand;
    public TableColumn colDiscount;
    public TableColumn colDelete;

    public JFXButton btnSaveItem;
    public JFXButton btnNewItem;

    LinkedHashMap<TextField, Pattern> map = new LinkedHashMap<>();

    private ItemBO itemBO = (ItemBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.ITEM);


    public void initialize(){

        colCode.setCellValueFactory(new PropertyValueFactory<>("ItemCode"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("Description"));
        colPackSize.setCellValueFactory(new PropertyValueFactory<>("PackSize"));
        colUnitePrice.setCellValueFactory(new PropertyValueFactory<>("UnitPrice"));
        colQtyOnHand.setCellValueFactory(new PropertyValueFactory<>("QtyOnHand"));
        colDiscount.setCellValueFactory(new PropertyValueFactory<>("Discount"));
        colDelete.setCellValueFactory(new PropertyValueFactory<>("btn"));

        loadDateAndTime();
        initialUI();

        tblItem.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            btnSaveItem.setText(newValue != null ? "Update" : "Save");
            btnSaveItem.setDisable(newValue == null);

            if (newValue != null) {
                txtCode.setText(newValue.getItemCode());
                txtDescription.setText(newValue.getDescription());
                txtPackSize.setText(newValue.getPackSize());
                txtUnitPrice.setText(newValue.getUnitPrice().setScale(2).toString());
                txtQtyOnHand.setText(newValue.getQtyOnHand() + "");
                txtDiscount.setText(newValue.getDiscount().setScale(2).toString());

                txtCode.setDisable(false);
                txtDescription.setDisable(false);
                txtPackSize.setDisable(false);
                txtUnitPrice.setDisable(false);
                txtQtyOnHand.setDisable(false);
                txtDiscount.setDisable(false);
            }
        });

        //-------------------validation pattern---------------------------------
        Pattern descriptionPattern = Pattern.compile("^[A-z / 0-9]{4,40}$");
        Pattern packetSizePattern = Pattern.compile("^[1-9][0-9]{0,5}(g|kg|ml|l)$");
        Pattern unitPricePattern = Pattern.compile("^[1-9][0-9]*(.[0-9]{1,2})?$");
        Pattern QtyOnHandPattern = Pattern.compile("^[1-9][0-9]{0,3}$");
        Pattern discountPattern = Pattern.compile("^[0-9]*(.[0-9]{1,2})?$");

        map.put(txtDescription,descriptionPattern);
        map.put(txtPackSize,packetSizePattern);
        map.put(txtUnitPrice,unitPricePattern);
        map.put(txtQtyOnHand,QtyOnHandPattern);
        map.put(txtDiscount,discountPattern);

        loadAllItems();
    }

    private void initialUI() {
        txtCode.clear();
        txtDescription.clear();
        txtPackSize.clear();
        txtUnitPrice.clear();
        txtQtyOnHand.clear();
        txtDiscount.clear();
        txtCode.setDisable(true);
        txtDescription.setDisable(true);
        txtPackSize.setDisable(true);
        txtUnitPrice.setDisable(true);
        txtQtyOnHand.setDisable(true);
        txtDiscount.setDisable(true);
        txtCode.setEditable(false);
        btnSaveItem.setDisable(true);
    }

    public void btnAddNewItemOnAction(ActionEvent actionEvent) {
        txtCode.setDisable(false);
        txtDescription.setDisable(false);
        txtPackSize.setDisable(false);
        txtUnitPrice.setDisable(false);
        txtQtyOnHand.setDisable(false);
        txtDiscount.setDisable(false);
        txtCode.clear();
        txtCode.setText(generateNewId());
        txtDescription.clear();
        txtPackSize.clear();
        txtUnitPrice.clear();
        txtQtyOnHand.clear();
        txtDiscount.clear();
        txtDescription.requestFocus();
        btnSaveItem.setDisable(false);
        btnSaveItem.setText("Save");
        tblItem.getSelectionModel().clearSelection();
    }

    private String generateNewId() {
        try {
            return itemBO.generateNewItemCode();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return "P001";
    }

    private void loadAllItems() {
        tblItem.getItems().clear();
        try {
            ArrayList<ItemDTO> allItems = itemBO.getAllItems();
            for (ItemDTO item : allItems) {
                Button btn = new Button("Delete");
                tblItem.getItems().add(new ItemTM(item.getItemCode(),item.getDescription(),item.getPackSize(),item.getUnitPrice(),item.getQtyOnHand(),item.getDiscount(),btn));

                btn.setOnAction(e -> {
                    //------------Delete Item----------------------------------
                    String code = tblItem.getSelectionModel().getSelectedItem().getItemCode();
                    try {
                        if (!existItem(code)) {
                            new Alert(Alert.AlertType.ERROR, "There is no such item associated with the id " + code).show();
                        }

                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are You Sure?", ButtonType.YES, ButtonType.NO);
                        Optional<ButtonType> buttonType = alert.showAndWait();

                        if (buttonType.get().equals(ButtonType.YES)) {

                            itemBO.deleteItem(code);
                            tblItem.getItems().remove(tblItem.getSelectionModel().getSelectedItem());
                            tblItem.getSelectionModel().clearSelection();
                            initialUI();

                            Notifications notifications = Notifications.create().title("Successful !").text("Item has been deleted successfully...").hideAfter(Duration.seconds(5)).position(Pos.BOTTOM_RIGHT);
                            notifications.darkStyle();
                            notifications.show();

                        }
                    } catch (SQLException er) {
                        new Alert(Alert.AlertType.ERROR, "Failed to delete the item " + code).show();
                    } catch (ClassNotFoundException er) {
                        er.printStackTrace();
                    }
                });

            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void btnSaveItemOnAction(ActionEvent actionEvent) {
        String code =txtCode.getText();
        String description = txtDescription.getText();
        String packSize = txtPackSize.getText();
        BigDecimal unitPrice = new BigDecimal(txtUnitPrice.getText()).setScale(2);
        int qtyOnHand = Integer.parseInt(txtQtyOnHand.getText());
        BigDecimal discount = new BigDecimal(txtDiscount.getText()).setScale(2);

        if (btnSaveItem.getText().equalsIgnoreCase("Save")) {
            try {
                if (existItem(code)) {
                    new Alert(Alert.AlertType.ERROR, code + " already exists").show();
                }
                //------------Save Item--------------------------------------

                itemBO.saveItem(new ItemDTO(code, description, packSize,unitPrice, qtyOnHand,discount));

                Notifications notifications = Notifications.create().title("Successful !").text("Item has been saved successfully...").hideAfter(Duration.seconds(5)).position(Pos.BOTTOM_RIGHT);
                notifications.darkStyle();
                notifications.show();

            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            try {

                if (!existItem(code)) {
                    new Alert(Alert.AlertType.ERROR, "There is no such item associated with the id " + code).show();
                }
                //------------------Update Item----------------------------------------------

                itemBO.updateItem(new ItemDTO(code, description,packSize, unitPrice, qtyOnHand,discount));

                Notifications notifications = Notifications.create().title("Successful !").text("Item has been updated successfully...").hideAfter(Duration.seconds(5)).position(Pos.BOTTOM_RIGHT);
                notifications.darkStyle();
                notifications.show();

            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        loadAllItems();
    }

    private boolean existItem(String code) throws SQLException, ClassNotFoundException {
        return itemBO.itemExist(code);
    }

    public void backToAdministratorMainOnAction(MouseEvent event) throws IOException {
        setUI(ManageItemContext,"AdministratorMainForm");
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
       ValidationUtil.validate(map,btnSaveItem);

        if (keyEvent.getCode() == KeyCode.ENTER) {
            Object response =  ValidationUtil.validate(map,btnSaveItem);;

            if (response instanceof TextField) {
                TextField textField = (TextField) response;
                textField.requestFocus();
            }
        }
    }
}
