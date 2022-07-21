package lk.ijse.pos.business.custom;

import lk.ijse.pos.business.SuperBO;
import lk.ijse.pos.dto.CustomDTO;

import java.sql.SQLException;
import java.util.ArrayList;

public interface DailyIncomeReportBO extends SuperBO {
    ArrayList<CustomDTO> getDailyIncome() throws SQLException, ClassNotFoundException;

}
