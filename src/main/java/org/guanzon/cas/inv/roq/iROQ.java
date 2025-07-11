package org.guanzon.cas.inv.roq;

import java.sql.SQLException;
import javax.sql.rowset.CachedRowSet;
import org.guanzon.appdriver.base.GRiderCAS;
import org.json.simple.JSONObject;

public interface iROQ {
    void setGRider(GRiderCAS applicationDriver);
    void setBranch(String branchCd);
    void setCategory(String categoryId);
    
    CachedRowSet getRecommendations();
    
    JSONObject InitTransaction();
    JSONObject LoadRecommendedOrder() throws SQLException;
    JSONObject LoadRecommendedOrder(boolean isSerialized) throws SQLException;
}
