package org.guanzon.cas.inv.roq;

import com.sun.org.apache.bcel.internal.Const;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import javax.sql.rowset.CachedRowSet;
import org.guanzon.appdriver.base.GRiderCAS;
import org.guanzon.appdriver.base.SQLUtil;
import org.json.simple.JSONObject;

public class ClassifySP implements iClassify{
    private GRiderCAS poGRider;
    private String psBranchCd;
    private String psCategrCd;
    
    private boolean pbInitTran;
    private CachedRowSet poROQ;
    private JSONObject poJSON;
    
    private int pnYear;
    private int pnMonth;
       
    @Override
    public void setGRider(GRiderCAS applicationDriver) {
        poGRider = applicationDriver;
    }

    @Override
    public void setBranch(String branchCd) {
        psBranchCd = branchCd;
    }
    
    @Override
    public void setCategory(String categoryId) {
        psCategrCd = categoryId;
    }
    
    @Override
    public void setPeriodMonth(int value){
        pnMonth = value;
    }
    
    @Override
    public void setPeriodYear(int value){
        pnYear = value;
    }

    @Override
    public JSONObject InitTransaction() {
        poJSON = new JSONObject();
        
        if (poGRider == null) {
            poJSON.put("result", "error");
            poJSON.put("message", "Application driver is not set.");
            return poJSON;
        }
        
        if (psCategrCd == null || psCategrCd.isEmpty()){
            poJSON.put("result", "error");
            poJSON.put("message", "Category is not set.");
            return poJSON;
        }
        
        if (psBranchCd == null || psBranchCd.isEmpty()) psBranchCd = poGRider.getBranchCode();
        
        if (pnMonth < 1 ||pnMonth > 12) {
            poJSON.put("result", "error");
            poJSON.put("message", "Invalid classification month.");
            return poJSON;
        }
        
        if (pnYear < 2025 || pnYear > LocalDate.now().getYear()) {
            poJSON.put("result", "error");
            poJSON.put("message", "Invalid classification year.");
            return poJSON;
        }
        
        pbInitTran = true;
        
        poJSON.put("result", "success");
        return poJSON;
    }
    
    public JSONObject Classify() throws SQLException{
        poJSON = new JSONObject();
        
        if (!pbInitTran) {
            poJSON.put("result", "error");
            poJSON.put("message", "Transaction is not initialized.");
            return poJSON;
        }
    
        //check if first time to run classification
        if (isFirstClassify()){
            return firstClassify();
        }
        
        
        poJSON.put("result", "success");
        return poJSON;
    }
    
    private boolean isFirstClassify() throws SQLException{
        String lsSQL = "SELECT sPeriodxx" +
                        " FROM Inv_Classificaton_Master" +
                        " WHERE sBranchCd = " + SQLUtil.toSQL(psBranchCd) +
                            " AND sCategrCd = " + SQLUtil.toSQL(psCategrCd) +
                        " ORDER BY sPeriodxx DESC LIMIT 1";
        
        ResultSet loRS = poGRider.executeQuery(lsSQL);
        
        if (loRS.next()){
            return true;
        } else {
            int lnPeriod = Integer.parseInt(loRS.getString("sPeriodxx"));
            int lnYear = lnPeriod / 100;       //2025
            int lnMonth = lnPeriod % 100;      //07

            LocalDate ldPeriod = LocalDate.of(lnYear, lnMonth, 1);
            LocalDate ldCurrent = LocalDate.now();
            
            if (ChronoUnit.MONTHS.between(ldCurrent, ldPeriod) > 4){
                return true;
            }
        }
            
        return false;
    }
    
    private JSONObject firstClassify(){
        String lsSQL_ULock = "UNLOCK TABLES";
        String lsSQL_Lock = "LOCK TABLES TABLENAME READ/WRITE";
        
        
        
        return poJSON;
    }
    
    private boolean getOthers(){
        return true;
    }
}
