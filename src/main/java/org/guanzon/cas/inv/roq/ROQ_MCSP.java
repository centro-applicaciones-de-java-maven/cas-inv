package org.guanzon.cas.inv.roq;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ROQ_MCSP extends ROQ_MC{
    @Override
    protected double computeROQ(ResultSet foRS) throws SQLException{
        double lnROQ;
        
        double lnAvailQty = foRS.getDouble("nQtyOnHnd") + 
                                foRS.getDouble("nBackOrdr") + 
                                foRS.getDouble("nFloatQty") + 
                                foRS.getDouble("nUnconfrm");   
                
        if ("ABC".contains(foRS.getString("cClassify"))){
            if (foRS.getDouble("nMinLevel") < foRS.getDouble("nQtyOnHnd")){
                lnROQ = 0.00;
            } else {
                lnROQ = foRS.getDouble("nMaxLevel") - 
                        foRS.getDouble("nResvOrdr") -
                        lnAvailQty;
                
                lnROQ = lnROQ < 0 ? 0 : lnROQ;
            }
        } else {
            lnROQ = foRS.getDouble("nResvOrdr") - lnAvailQty;
            
            lnROQ = lnROQ < 0 ? 0 : lnROQ;
        }
        
        return lnROQ;
    }
}
