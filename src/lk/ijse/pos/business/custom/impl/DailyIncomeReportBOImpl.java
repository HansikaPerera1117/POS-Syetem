package lk.ijse.pos.business.custom.impl;

import lk.ijse.pos.business.custom.DailyIncomeReportBO;
import lk.ijse.pos.dto.CustomDTO;
import lk.ijse.pos.entity.CustomEntity;
import lk.ijse.pos.repository.DAOFactory;
import lk.ijse.pos.repository.custom.QueryDAO;

import java.sql.SQLException;
import java.util.ArrayList;

public class DailyIncomeReportBOImpl implements DailyIncomeReportBO {

    private final QueryDAO queryDAO = (QueryDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.QUERYDAO);

    @Override
    public ArrayList<CustomDTO> getDailyIncome() throws SQLException, ClassNotFoundException {
        ArrayList<CustomEntity> all = queryDAO.dailyIncomeReport();
        ArrayList<CustomDTO> dailyIncome= new ArrayList<>();
        for (CustomEntity entity: all) {
            dailyIncome.add(new CustomDTO(entity.getOrderDate(),entity.getUnitPrice()));
        }
        return dailyIncome;
    }

}
