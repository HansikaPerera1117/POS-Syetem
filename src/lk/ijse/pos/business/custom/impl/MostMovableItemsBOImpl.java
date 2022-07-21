package lk.ijse.pos.business.custom.impl;

import lk.ijse.pos.business.custom.MostMovableItemsBO;
import lk.ijse.pos.dto.CustomDTO;
import lk.ijse.pos.entity.CustomEntity;
import lk.ijse.pos.repository.DAOFactory;
import lk.ijse.pos.repository.custom.QueryDAO;

import java.sql.SQLException;
import java.util.ArrayList;

public class MostMovableItemsBOImpl implements MostMovableItemsBO {

    private final QueryDAO queryDAO = (QueryDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.QUERYDAO);

    @Override
    public ArrayList<CustomDTO> getAllMostMovableItems() throws SQLException, ClassNotFoundException {
        ArrayList<CustomEntity> all = queryDAO.mostMovableItems();
        ArrayList<CustomDTO> mostMovable= new ArrayList<>();
        for (CustomEntity entity: all) {
            mostMovable.add(new CustomDTO(entity.getItemCode(),entity.getDescription(),entity.getUnitPrice(),entity.getQtyOnHand(),entity.getOrderQTY()));
        }
        return mostMovable;
    }
}
