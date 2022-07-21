package lk.ijse.pos.view.tm;

import javafx.scene.control.Button;

public class CustomerTM {
    private String CustID;
    private String CustTitle;
    private String CustName;
    private String CustAddress;
    private String City;
    private String Province;
    private String PostalCode;
    private Button btn;

    public CustomerTM() {
    }

    public CustomerTM(String custID, String custTitle, String custName, String custAddress, String city, String province, String postalCode, Button btn) {
        CustID = custID;
        CustTitle = custTitle;
        CustName = custName;
        CustAddress = custAddress;
        City = city;
        Province = province;
        PostalCode = postalCode;
        this.btn = btn;
    }

    public String getCustID() {
        return CustID;
    }

    public void setCustID(String custID) {
        CustID = custID;
    }

    public String getCustTitle() {
        return CustTitle;
    }

    public void setCustTitle(String custTitle) {
        CustTitle = custTitle;
    }

    public String getCustName() {
        return CustName;
    }

    public void setCustName(String custName) {
        CustName = custName;
    }

    public String getCustAddress() {
        return CustAddress;
    }

    public void setCustAddress(String custAddress) {
        CustAddress = custAddress;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getProvince() {
        return Province;
    }

    public void setProvince(String province) {
        Province = province;
    }

    public String getPostalCode() {
        return PostalCode;
    }

    public void setPostalCode(String postalCode) {
        PostalCode = postalCode;
    }

    public Button getBtn() {
        return btn;
    }

    public void setBtn(Button btn) {
        this.btn = btn;
    }

    @Override
    public String toString() {
        return "CustomerTM{" +
                "CustID='" + CustID + '\'' +
                ", CustTitle='" + CustTitle + '\'' +
                ", CustName='" + CustName + '\'' +
                ", CustAddress='" + CustAddress + '\'' +
                ", City='" + City + '\'' +
                ", Province='" + Province + '\'' +
                ", PostalCode='" + PostalCode + '\'' +
                ", btn=" + btn +
                '}';
    }
}
