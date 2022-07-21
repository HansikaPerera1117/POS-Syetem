package lk.ijse.pos.business;

import lk.ijse.pos.business.custom.impl.*;

public class BOFactory {

    private static BOFactory boFactory;

    private BOFactory() {
    }

    public static BOFactory getBoFactory() {
        if (boFactory == null) {
            boFactory = new BOFactory();
        }
        return boFactory;
    }

    public enum BOTypes {
        CUSTOMER, ITEM, PLACE_ORDER, MOST_MOVABLE_ITEMS, LESS_MOVABLE_ITEMS,MANAGE_ORDER,DAILY_INCOME
    }

    public SuperBO getBO(BOTypes types) {
        switch (types) {
            case CUSTOMER:
                return new CustomerBOImpl();
            case ITEM:
                return new ItemBOImpl();
            case PLACE_ORDER:
                return new PlaceOrderBOImpl();
            case MOST_MOVABLE_ITEMS:
                return new MostMovableItemsBOImpl();
            case LESS_MOVABLE_ITEMS:
                return new LessMovableItemsBOImpl();
            case MANAGE_ORDER:
                return new ManageOrderBOImpl();
            case DAILY_INCOME:
                return new DailyIncomeReportBOImpl();
            default:
                return null;
        }
    }

}
