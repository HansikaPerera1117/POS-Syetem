package lk.ijse.pos.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class CustomerDTO {
    private String CID;
    private String CTitle;
    private String CName;
    private String CAddress;
    private String City;
    private String Province;
    private String PostalCode;

    public CustomerDTO() {
    }

    public CustomerDTO(String CID, String CTitle, String CName, String CAddress, String city, String province, String postalCode) {
        this.CID = CID;
        this.CTitle = CTitle;
        this.CName = CName;
        this.CAddress = CAddress;
        City = city;
        Province = province;
        PostalCode = postalCode;
    }



    public String getCID() {
        return CID;
    }

    public void setCID(String CID) {
        this.CID = CID;
    }

    public String getCTitle() {
        return CTitle;
    }

    public void setCTitle(String CTitle) {
        this.CTitle = CTitle;
    }

    public String getCName() {
        return CName;
    }

    public void setCName(String CName) {
        this.CName = CName;
    }

    public String getCAddress() {
        return CAddress;
    }

    public void setCAddress(String CAddress) {
        this.CAddress = CAddress;
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

    @Override
    public String toString() {
        return "CustomerDTO{" +
                "CID='" + CID + '\'' +
                ", CTitle='" + CTitle + '\'' +
                ", CName='" + CName + '\'' +
                ", CAddress='" + CAddress + '\'' +
                ", City='" + City + '\'' +
                ", Province='" + Province + '\'' +
                ", PostalCode='" + PostalCode + '\'' +
                '}';
    }
}
