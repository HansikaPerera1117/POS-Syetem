package lk.ijse.pos.business.custom;

import lk.ijse.pos.business.SuperBO;
import lk.ijse.pos.dto.CustomDTO;
import lk.ijse.pos.dto.CustomerDTO;

import java.sql.SQLException;
import java.util.ArrayList;

public interface MostMovableItemsBO extends SuperBO {
    ArrayList<CustomDTO> getAllMostMovableItems() throws SQLException, ClassNotFoundException;
}
