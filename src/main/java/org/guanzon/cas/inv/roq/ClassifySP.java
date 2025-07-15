package org.guanzon.cas.inv.roq;

import com.sun.rowset.CachedRowSetImpl;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import javax.sql.rowset.CachedRowSet;
import org.guanzon.appdriver.base.CommonUtils;
import org.guanzon.appdriver.base.GRiderCAS;
import org.guanzon.appdriver.base.GuanzonException;
import org.guanzon.appdriver.base.MiscUtil;
import org.guanzon.appdriver.base.SQLUtil;
import org.json.simple.JSONObject;

public class ClassifySP implements iClassify{
    private GRiderCAS poGRider;
    private String psBranchCd;
    private String psCategrCd;
    
    private String[] pasPeriod ;
    private ClassificationConfig poOthers;
    
    private boolean pbInitTran;
    private boolean pbProcessed;
    private boolean pbMinMax;
    
    private CachedRowSet poROQ;
    private CachedRowSet poDetail;
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
    
    public JSONObject Classify() throws SQLException, GuanzonException{
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
    
    private JSONObject firstClassify() throws SQLException, GuanzonException{       
        String lsSQL;
        String lsSQL_ULock = "UNLOCK TABLES";
        String lsSQL_Lock = "LOCK TABLES TABLENAME READ/WRITE";
        
        System.out.println("Setting Required Information");
        
        System.out.println("Retrieving Computation Variable...");
        if (!getOthers()){
            poJSON = new JSONObject();
            poJSON.put("result", "error");
            poJSON.put("message", "Unable to retrieve configuration.");
            return poJSON;
        }
        
        System.out.println("Retriving period to process...");
        poJSON = checkPeriod();
        
        if ("error".equals((String) poJSON.get("result"))) return poJSON;
        
        poGRider.beginTrans("Inventory Classification", psBranchCd + "-" + psCategrCd, "CLASS", "");
        
        lsSQL = "UPDATE Inv_Master SET" +
                    "  nMaxLevel = 0" +
                    ", nAveMonSl = 0" +
                    ", nResvOrdr = 0" +
                    ", nBackOrdr = 0" +
                    ", nFloatQty = 0" +
                " WHERE sBranchCd = " + SQLUtil.toSQL(psBranchCd) +
                    " AND sStockIDx IN (SELECT sStockIDx" +
                                        " FROM Inventory" +
                                        " WHERE sCategCd1 = " + SQLUtil.toSQL(psCategrCd) +")";
        
        System.out.println("Setting initial values...");
        poGRider.executeQuery(lsSQL, "Inv_Master", psBranchCd, "", "");
        
        if (poGRider.isWarehouse()){
            //for warehouse, post all confirm PO and cancel all unconfirm PO
            
            //todo:
            System.out.println("Removing all Unconfirmed Order...");
            System.out.println("Removing all Pending Order...");
            System.out.println("Posting all Confirmed Order...");
        } else {
            lsSQL = "UPDATE Inv_Stock_Transfer_Master SET " +
                        "  cTranStat = '3'" +
                    " WHERE sTransNox LIKE " + SQLUtil.toSQL(psBranchCd + "%") +
                        " AND cTranStat = '0'";
            
            System.out.println("Removing all Unconfirmed Order...");
            poGRider.executeQuery(lsSQL, "Inv_Stock_Transfer_Master", psBranchCd, "", "");
            
            //todo:
            System.out.println("Removing all Pending Order...");
        }
        
        lsSQL = "SELECT" +
                    "  a.sStockIDx" +
                    ", a.sBarCodex" +
                    ", a.sDescript" +
                    ", b.dBegInvxx" +
                    ", b.dAcquired" +
                    ", b.nQtyOnHnd" +
                    ", b.cRecdStat" +
                " FROM Inventory a" +
                    ", Inv_Master b" +
                " WHERE a.sStockIDx = b.sStockIDx" +
                    " AND a.sCategCd1 = " + SQLUtil.toSQL(psCategrCd) +
                    " AND b.sBranchCd = " + SQLUtil.toSQL(psBranchCd) +
                " ORDER BY b.dAcquired"; 
        
        System.out.println("Retrieving items...");
        ResultSet loRS = poGRider.executeQuery(lsSQL);
        
        ResultSet loTmp;
        LocalDate ldAcquired;
        LocalDate ldHistory;
        int lnPeriod;
        int lnYear;
        int lnMonth;
        double lnTotal = 0.00;
        double lnPerTotal = 0.00;
        double lnRunTotal = 0.00;
                
        if (MiscUtil.RecordCount(loRS) > 0){
            System.out.println("Assigning date acquired for the existing items.");
            while (loRS.next()){
                System.out.println(loRS.getString("sBarCodex") +  " - " + loRS.getString("sDesript"));
                lsSQL = "SELECT dTransact" +
                        " FROM Inv_Ledger" +
                        " WHERE sStockIDx = " + SQLUtil.toSQL(loRS.getString("sStockIDx")) +
                            " AND sBranchCd = " + SQLUtil.toSQL(psBranchCd) +
                            " AND sSourceCd <> 'xxxx'" + //purchase order source code
                        " ORDER BY dTransact LIMIT 1";
                
                loTmp = poGRider.executeQuery(lsSQL);
                
                ldHistory = null;
                
                if (!loTmp.next()){
                    ldAcquired = null;
                } else {
                    ldAcquired = LocalDate.parse(loTmp.getString("dTransact"));
                    ldHistory = LocalDate.parse(loTmp.getString("dTransact"));
                }
                
                lsSQL = "";
                if (loRS.getInt("nQtyOnHnd") == 0){
                    if (ldAcquired == null){
                        if (!loRS.getString("cRecdStat").equals("0")){
                            lsSQL = "UPDATE Inv_Master SET " +
                                        "  cRecdStat = '0'" +
                                    " WHERE sStockIDx  = " + SQLUtil.toSQL(loRS.getString("sStockIDx")) +
                                        " AND sBranchCd = " + SQLUtil.toSQL(psBranchCd);
                        }
                    } else {
                        //deactivate parts that didn't move for more than 2 years and has no inventory
                        if (CommonUtils.dateDiff(LocalDate.now(), ldHistory, ChronoUnit.YEARS) >= 2) {
                            lsSQL = "UPDATE Inv_Master SET" +
                                        "  cRecdStat = '0'" +
                                        ", dAcquired = " + SQLUtil.toSQL(ldAcquired) +
                                        loRS.getDate("dBegInvxx") == null ? ", dBegInvxx = " + SQLUtil.toSQL(ldAcquired) : "" +
                                    " WHERE sStockIDx  = " + SQLUtil.toSQL(loRS.getString("sStockIDx")) +
                                        " AND sBranchCd = " + SQLUtil.toSQL(psBranchCd);
                        } else {
                            lsSQL = "UPDATE Inv_Master SET" +
                                        "  dAcquired = " + SQLUtil.toSQL(ldAcquired) +
                                        (loRS.getDate("dBegInvxx") == null ? ", dBegInvxx = " + SQLUtil.toSQL(ldAcquired) : "") +
                                        (loRS.getString("cRecdStat").equals("0") ? ", cRecdStat = '1'" : "") +
                                    " WHERE sStockIDx  = " + SQLUtil.toSQL(loRS.getString("sStockIDx")) +
                                        " AND sBranchCd = " + SQLUtil.toSQL(psBranchCd);
                        }
                    }
                } else {
                    if (ldAcquired == null) {
                        ldAcquired = LocalDate.of(2010, 1, 1);
                    }
                    
                    lsSQL = "UPDATE Inv_Master SET" +
                                "  dAcquired = " + SQLUtil.toSQL(ldAcquired) +
                                (loRS.getDate("dBegInvxx") == null ? ", dBegInvxx = " + SQLUtil.toSQL(ldAcquired) : "") +
                                (loRS.getString("cRecdStat").equals("0") ? ", cRecdStat = '1'" : "") +
                            " WHERE sStockIDx  = " + SQLUtil.toSQL(loRS.getString("sStockIDx")) +
                                " AND sBranchCd = " + SQLUtil.toSQL(psBranchCd);
                }
                
                if (!lsSQL.isEmpty()){
                    if (poGRider.executeQuery(lsSQL, "Inv_Master", psBranchCd, "", "") < 0){
                        poJSON = new JSONObject();
                        poJSON.put("result", "error");
                        poJSON.put("message", "Unable to update acquisition date!\n" +
                                                        "Please Inform GMC SEG/SSG for this matter!");
                        return poJSON;
                    }
                }
            }
        } else {
            lsSQL = "SELECT sPeriodxx" +
                    " FROM Inv_Classificaton_Master" +
                    " WHERE sBranchCd = " + SQLUtil.toSQL(psBranchCd) +
                        " AND sCategrCd = " + SQLUtil.toSQL(psCategrCd) +
                    " ORDER BY sPeriodxx DESC LIMIT 1";
            
            loTmp = poGRider.executeQuery(lsSQL);
            
            if (MiscUtil.RecordCount(loTmp) == 0) {
                ldAcquired = CommonUtils.dateAdd(LocalDate.now(), -13, ChronoUnit.MONTHS);
                pnYear = ldAcquired.getYear();
                pnMonth = ldAcquired.getMonthValue();
            } else {
                lnPeriod = Integer.parseInt(loRS.getString("sPeriodxx"));
                lnYear = lnPeriod / 100;       //2025
                lnMonth = lnPeriod % 100;      //07
                
                ldAcquired = LocalDate.of(lnYear, lnMonth, 28);
                ldAcquired = CommonUtils.dateAdd(ldAcquired, 1, ChronoUnit.MONTHS);
                pnYear = ldAcquired.getYear();
                pnMonth = ldAcquired.getMonthValue();
            }
            
            lnPeriod = Integer.parseInt(loRS.getString("sPeriodxx"));
            lnYear = lnPeriod / 100;       //2025
            lnMonth = lnPeriod % 100;      //07            

            while (CommonUtils.dateDiff(LocalDate.now(), LocalDate.of(pnYear, pnMonth, 28), ChronoUnit.MONTHS) > 0) {
                System.out.println("Initializing period info...");
                
                //initialize period
                initPeriod();
                
                //after initializing period, check if Min Max must be calculated
                pbMinMax = isComputeMinMax();
                System.out.println("Finalizing...");
                
                System.out.println("Retrieving statement information...");
                if (pbProcessed){
                    lsSQL = getCompDemandSQL();
                } else {
                    lsSQL = getDemand4ClassSQL();
                }
                System.out.println("Finalizing...");
                
                System.out.println("Retreiving sales total...");
                lnTotal = getTotal();
                
                if (lnTotal == 0.00) {
                    //trap the processing if no sales exist
                    if (poGRider.getClientID().equals("GGC_BGMO1")) {
                        poJSON = updateClassification(lnPerTotal);
                        
                        if (!"success".equals((String) poJSON.get("result"))){
                            poGRider.rollbackTrans();
                            return poJSON;
                        }
                    }
                } else {
                    ResultSet loDetail = poGRider.executeQuery(lsSQL);
                    
                    if (MiscUtil.RecordCount(loRS) == 0){
                        poGRider.rollbackTrans();
                        
                        poJSON = new JSONObject();
                        poJSON.put("result", "error");
                        poJSON.put("message", "Unable to retrieve items for classification!\n" +
                                                        "Please Inform SEG/SSG of Guanzon Group of Companies on this Matter!!!");
                        
                        return poJSON;
                    }                   
                    
                    poDetail = new CachedRowSetImpl();
                    poDetail.populate(loDetail);
                    
                    lnRunTotal = 0.00;
                    lnPerTotal = 0.00;
                    while (poDetail.next()){
                        System.out.println(poDetail.getString("sBarCodex") +  " - " + poDetail.getString("sDesript"));
                        lnRunTotal += poDetail.getDouble("xTotlSold");
                        lnPerTotal += poDetail.getDouble("nSoldQty1");
                        poDetail.setObject("nTotlSumx", lnRunTotal);
                        poDetail.setObject("nTotlSumP", lnRunTotal / lnTotal);
                        
                        //todo: if classify parts
                    }
                    
                    poJSON = updateClassification(lnPerTotal);

                    if (!"success".equals((String) poJSON.get("result"))){
                        poGRider.rollbackTrans();
                        return poJSON;
                    }
                }
                
                //increment period
                ldAcquired = LocalDate.of(pnYear, pnMonth, 28);
                ldAcquired = CommonUtils.dateAdd(ldAcquired, 1, ChronoUnit.MONTHS);
                pnYear = ldAcquired.getYear();
                pnMonth = ldAcquired.getMonthValue();
            }
        }
        
        return poJSON;
    }
    
    private JSONObject updateClassification(double lnPerTotal) throws SQLException, GuanzonException{
        String lsSQL;
        
        if (pbProcessed){
            lsSQL = "UPDATE Inv_Classification_Master SET" +
                        "  nTotlSale = " + lnPerTotal +
                        ", sPostedxx = " + SQLUtil.toSQL(poGRider.getUserID()) +
                        ", dPostedxx = " + SQLUtil.toSQL(poGRider.getServerDate()) +
                        ", cTranStat = " + SQLUtil.toSQL("2") +
                    " WHERE sCategrCd = " + SQLUtil.toSQL(psCategrCd) +
                        " AND sBranchCd = " + SQLUtil.toSQL(psBranchCd) +
                        " AND sPeriodxx = " + SQLUtil.toSQL(pasPeriod[0]);
        } else {
            lsSQL = "INSERT INTO Inv_Classification_Master SET" +
                        "  sCategrCd = " + SQLUtil.toSQL(psCategrCd) +
                        ", sBranchCd = " + SQLUtil.toSQL(psBranchCd) +
                        ", sPeriodxx = " + SQLUtil.toSQL(pasPeriod[0]) +
                        ", nTotlSale = " + lnPerTotal + 
                        ", sProcessd = " + SQLUtil.toSQL(poGRider.getUserID()) +
                        ", dProcessd = " + SQLUtil.toSQL(poGRider.getServerDate()) +
                        ", sPostedxx = " + SQLUtil.toSQL(poGRider.getUserID()) +
                        ", dPostedxx = " + SQLUtil.toSQL(poGRider.getServerDate()) +
                        ", cTranStat = " + SQLUtil.toSQL("2");
        }
        
        poJSON = new JSONObject();
        
        if (poGRider.executeQuery(lsSQL, "Inv_Classification_Master", psBranchCd, "", "") <= 0){
            poJSON.put("result", "error");
            poJSON.put("message", "Unable to Finalize Spareparts Classification Info!\n" +
                                        "Please Inform SEG/SSG of Guanzon Group of Companies on this Matter!!!");
            return poJSON;
        }
        
        poJSON.put("result", "success");
        return poJSON;
    }
    
    
    
    private double getTotal() throws SQLException{
        String lsSQL;
        
        if (pbProcessed){
            lsSQL = "SELECT SUM(nTotlSale) xTotlSold" +
                    " FROM Inv_Classification_Master" +
                    " WHERE sBranchCd = " + SQLUtil.toSQL(psBranchCd) +
                        " AND sCategrCd = " + SQLUtil.toSQL(psCategrCd) +
                        " AND sPeriodxx BETWEEN " + SQLUtil.toSQL(pasPeriod[pasPeriod.length - 1]) +
                        " AND " + SQLUtil.toSQL(pasPeriod[0]);
        } else {
            lsSQL = "SELECT IFNULL(SUM(xTotlSold), 0) xTotlSold" +
                    " FROM (SELECT SUM(nTotlSale) xTotlSold" +
                            " FROM Inv_Classification_Master" +
                            " WHERE sBranchCd = " + SQLUtil.toSQL(psBranchCd) +
                                " AND sCategrCd = " + SQLUtil.toSQL(psCategrCd) +
                                " AND sPeriodxx BETWEEN " + SQLUtil.toSQL(pasPeriod[pasPeriod.length - 1]) +
                                " AND " + SQLUtil.toSQL(pasPeriod[1]) +
                            " UNION SELECT SUM(nQtyOutxx - nQtyInxxx) xTotlSold" +
                            " FROM Inv_Ledger" +
                                " WHERE sBranchCd = " + SQLUtil.toSQL(psBranchCd) +
                                   " AND sSourceCd IN ('', '', '', '', '', '', '', '')" +
                                   " AND DATE_FORMAT(dTransact, '%Y%m') = " + SQLUtil.toSQL(pasPeriod[1]) + ") x";
//                    strParm(pxeSPJobOrder) & ", " & _
//                    strParm(pxeSPJobOrder) & ", " & _
//                    strParm(pxeSPSales) & ", " & _
//                    strParm(pxeSPWholesale) & ", " & _
//                    strParm(pxeSPSalesGiveAway) & ", " & _
//                    strParm(pxeSPWarrantyRelease) & ", " & _
//                    strParm(pxeSPWholesaleReturn) & ", " & _
//                    strParm(pxeSPSalesReturn) & ")" & _
        }
        
        ResultSet loRS = poGRider.executeQuery(lsSQL);
        
        if (loRS.next()){
            return loRS.getDouble("xTotlSold");
        } else {
            return 0.00;
        }
    }
    
    private void initPeriod(){
        pasPeriod = new String[poOthers.getModel().getNoOfMonths()];
        
        LocalDate ldDate = LocalDate.of(pnYear, pnMonth, 1);
        DateTimeFormatter lsFormat = DateTimeFormatter.ofPattern("yyyyMM");
        
        for (int lnCtr = 0; lnCtr <= poOthers.getModel().getNoOfMonths() - 1; lnCtr++){
            pasPeriod[lnCtr] = ldDate.format(lsFormat);
            
            ldDate = CommonUtils.dateAdd(ldDate, -1, ChronoUnit.MONTHS);
        }
    }
    
    private boolean getOthers() throws SQLException, GuanzonException{
        poOthers.setApplicationDriver(poGRider);
        poOthers.setWithParentClass(false);
        poOthers.initialize();
        return true;
    }
    
    private JSONObject checkPeriod() throws SQLException{
        poJSON = new JSONObject();
        
        String lsSQL = "SELECT sPeriodxx, sPostedxx" +
                        " FROM Inv_Classificaton_Master" +
                        " WHERE sBranchCd = " + SQLUtil.toSQL(psBranchCd) +
                            " AND sCategrCd = " + SQLUtil.toSQL(psCategrCd) +
                        " ORDER BY sPeriodxx DESC LIMIT 1";
        
        ResultSet loRS = poGRider.executeQuery(lsSQL);
        
        LocalDate ldPeriod = CommonUtils.dateAdd(poGRider.getServerDate(), -1, ChronoUnit.MONTHS);
        int lnPeriod1, lnPeriod2;
        
        if (!loRS.next()){
            lnPeriod1 = Integer.parseInt(SQLUtil.dateFormat(poGRider.getServerDate(), SQLUtil.FORMAT_SHORT_YEAR_MONTH));
            lnPeriod2 = Integer.parseInt(String.format("%04d", pnYear) + String.format("%02d", pnMonth));
            
            if (!(pnYear != 0 && pnMonth != 0 && lnPeriod1 > lnPeriod2)){
                pnYear = ldPeriod.getYear();
                pnMonth = ldPeriod.getMonthValue();
            }
        } else {
            lnPeriod1 = Integer.parseInt(loRS.getString("sPeriodxx"));
            lnPeriod2 = Integer.parseInt(SQLUtil.dateFormat(poGRider.getServerDate(), SQLUtil.FORMAT_SHORT_YEAR_MONTH));
            
            if (lnPeriod1 < lnPeriod2){
                lnPeriod1 = Integer.parseInt(loRS.getString("sPeriodxx").substring(0, 4));
                lnPeriod2 = Integer.parseInt(loRS.getString("sPeriodxx").substring(4, 6));
                
                ldPeriod = LocalDate.of(lnPeriod1, lnPeriod2, 1);
                ldPeriod = CommonUtils.dateAdd(ldPeriod, 1, ChronoUnit.MONTHS);
                pnYear = ldPeriod.getYear();
                pnMonth = ldPeriod.getMonthValue();
            } else {
                if (!loRS.getString("sPostedxx").isEmpty()){
                    poJSON.put("result", "error");
                    poJSON.put("message", "SP Classification was already posted for the criteria month!\n" +
                                                "Re-processing of classified parts is not Allowed!!!");
                    return poJSON;
                }
                
                pbProcessed = true;
            }
        }
                
        pbMinMax = isComputeMinMax();
        
        poJSON.put("result", "error");
        return poJSON;
    }
    
    private boolean isComputeMinMax(){
        int lnMonth  = poOthers.getModel().getStartMinMax();
        
        while (lnMonth > pnMonth){
            if (lnMonth == pnMonth) return true;
            
            lnMonth += poOthers.getModel().getStartMinMax();
        }
        
        return false;
    }
    
    private String getCompDemandSQL(){
        return "";
    }
    
    private String getDemand4ClassSQL(){
        return "";
    }
}