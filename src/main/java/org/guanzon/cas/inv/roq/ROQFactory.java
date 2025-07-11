package org.guanzon.cas.inv.roq;

import org.guanzon.appdriver.base.GRiderCAS;

public class ROQFactory {
    public static iROQ make(GRiderCAS applicationDriver, String categoryLevel1){
        iROQ roq;
        
        switch (categoryLevel1){
            case "0003": //motorcycle
                roq = new ROQ_MC();
                break;
            case "0004":
                roq = new ROQ_MCSP();
                break;
            default:
                return null;
        }
        
        roq.setGRider(applicationDriver);
        roq.setCategory(categoryLevel1);
        
        return roq;
    }
}
