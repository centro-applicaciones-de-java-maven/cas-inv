package org.guanzon.cas.inv.roq;

import org.guanzon.appdriver.base.GRiderCAS;
import org.json.simple.JSONObject;

public interface iClassify {
    void setGRider(GRiderCAS applicationDriver);
    void setBranch(String branchCd);
    void setCategory(String categoryId);
    
    void setPeriodMonth(int value);
    void setPeriodYear(int value);
        
    JSONObject InitTransaction();
}
