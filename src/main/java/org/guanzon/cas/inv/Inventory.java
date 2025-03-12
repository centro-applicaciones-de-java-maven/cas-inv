package org.guanzon.cas.inv;

import java.sql.SQLException;
import org.guanzon.appdriver.agent.ShowDialogFX;
import org.guanzon.appdriver.agent.services.Parameter;
import org.guanzon.appdriver.base.GuanzonException;
import org.guanzon.appdriver.base.MiscUtil;
import org.guanzon.appdriver.base.SQLUtil;
import org.guanzon.appdriver.constant.Logical;
import org.guanzon.appdriver.constant.UserRight;
import org.guanzon.cas.inv.model.Model_Inventory;
import org.json.simple.JSONObject;

public class Inventory extends Parameter{
    Model_Inventory poModel;
    
    @Override
    public void initialize() {
        psRecdStat = Logical.YES;
        
        poModel = new Model_Inventory();
        poModel.setApplicationDriver(poGRider);
        poModel.setXML("Model_Inventory");
        poModel.setTableName("Inventory");
        poModel.initialize();
    }
    
    @Override
    public JSONObject isEntryOkay() throws SQLException{
        poJSON = new JSONObject();
        
        if (poGRider.getUserLevel() < UserRight.SYSADMIN){
            poJSON.put("result", "error");
            poJSON.put("message", "User is not allowed to save record.");
            return poJSON;
        } else {
            poJSON = new JSONObject();
            
            if (poModel.getBarCode().isEmpty()){
                poJSON.put("result", "error");
                poJSON.put("message", "Item bar code must not be empty.");
                return poJSON;
            }
            
            if (poModel.getDescription().isEmpty()){
                poJSON.put("result", "error");
                poJSON.put("message", "Item description must not be empty.");
                return poJSON;
            }
            
            //todo:
            //  more validations/use of validators per category
            
            poModel.setModifyingId(poGRider.Encrypt(poGRider.getUserID()));
            poModel.setModifiedDate(poGRider.getServerDate());
        }
        
        poJSON.put("result", "success");
        return poJSON;
    }
    
    @Override
    public Model_Inventory getModel() {
        return poModel;
    }
    
    @Override
    public JSONObject searchRecord(String value, boolean byCode) throws SQLException, GuanzonException {
        poJSON = ShowDialogFX.Search(poGRider,
                getSQ_Browse(),
                value,
                "Bar Code»Description»Brand»Model»Color»Selling Price»ID",
                "sBarCodex»sDescript»xBrandNme»xModelNme»xColorNme»nSelPrice»sStockIDx",
                "a.sBarCodex»a.sDescript»IFNULL(b.sDescript, '')»IF(IFNULL(c.sDescript, '') = '', '', CONCAT(c.sDescript, '(', c.sModelCde, ')'))»IFNULL(d.sDescript, '')»a.nSelPrice»a.sStockIDx",
                byCode ? 0 : 1);

        if (poJSON != null) {
            return poModel.openRecord((String) poJSON.get("sStockIDx"));
        } else {
            poJSON = new JSONObject();
            poJSON.put("result", "error");
            poJSON.put("message", "No record loaded.");
            return poJSON;
        }
    }
    
    
    public JSONObject searchRecord(String value, 
                                    boolean byCode, 
                                    String inventoryTypeId) throws SQLException, GuanzonException {
        String lsSQL = MiscUtil.addCondition(getSQ_Browse(), 
                                                "a.sInvTypCd = " + SQLUtil.toSQL(inventoryTypeId));
        
        poJSON = ShowDialogFX.Search(poGRider,
                lsSQL,
                value,
                "Bar Code»Description»Brand»Model»Color»Selling Price»ID",
                "sBarCodex»sDescript»xBrandNme»xModelNme»xColorNme»nSelPrice»sStockIDx",
                "a.sBarCodex»a.sDescript»IFNULL(b.sDescript, '')»IF(IFNULL(c.sDescript, '') = '', '', CONCAT(c.sDescript, '(', c.sModelCde, ')'))»IFNULL(d.sDescript, '')»a.nSelPrice»a.sStockIDx",
                byCode ? 0 : 1);

        if (poJSON != null) {
            return poModel.openRecord((String) poJSON.get("sStockIDx"));
        } else {
            poJSON = new JSONObject();
            poJSON.put("result", "error");
            poJSON.put("message", "No record loaded.");
            return poJSON;
        }
    }
    
    public JSONObject searchRecord(String value, 
                                    boolean byCode, 
                                    String inventoryTypeId,
                                    String categoryIdLevel1) throws SQLException, GuanzonException{
        String lsSQL = MiscUtil.addCondition(getSQ_Browse(), 
                                                "a.sInvTypCd = " + SQLUtil.toSQL(inventoryTypeId) +
                                                    " AND a.sCategCd1 = " + SQLUtil.toSQL(categoryIdLevel1));
        
        poJSON = ShowDialogFX.Search(poGRider,
                lsSQL,
                value,
                "Bar Code»Description»Brand»Model»Color»Selling Price»ID",
                "sBarCodex»sDescript»xBrandNme»xModelNme»xColorNme»nSelPrice»sStockIDx",
                "a.sBarCodex»a.sDescript»IFNULL(b.sDescript, '')»IF(IFNULL(c.sDescript, '') = '', '', CONCAT(c.sDescript, '(', c.sModelCde, ')'))»IFNULL(d.sDescript, '')»a.nSelPrice»a.sStockIDx",
                byCode ? 0 : 1);

        if (poJSON != null) {
            return poModel.openRecord((String) poJSON.get("sStockIDx"));
        } else {
            poJSON = new JSONObject();
            poJSON.put("result", "error");
            poJSON.put("message", "No record loaded.");
            return poJSON;
        }
    }
    
    public JSONObject searchRecord(String value, 
                                    boolean byCode, 
                                    String inventoryTypeId,
                                    String categoryIdLevel1,
                                    String categoryIdLevel2) throws SQLException, GuanzonException{
        String lsSQL = MiscUtil.addCondition(getSQ_Browse(), 
                                                "a.sInvTypCd = " + SQLUtil.toSQL(inventoryTypeId) +
                                                    " AND a.sCategCd1 = " + SQLUtil.toSQL(categoryIdLevel1) +
                                                    " AND a.sCategCd2 = " + SQLUtil.toSQL(categoryIdLevel2));
        
        poJSON = ShowDialogFX.Search(poGRider,
                lsSQL,
                value,
                "Bar Code»Description»Brand»Model»Color»Selling Price»ID",
                "sBarCodex»sDescript»xBrandNme»xModelNme»xColorNme»nSelPrice»sStockIDx",
                "a.sBarCodex»a.sDescript»IFNULL(b.sDescript, '')»IF(IFNULL(c.sDescript, '') = '', '', CONCAT(c.sDescript, '(', c.sModelCde, ')'))»IFNULL(d.sDescript, '')»a.nSelPrice»a.sStockIDx",
                byCode ? 0 : 1);

        if (poJSON != null) {
            return poModel.openRecord((String) poJSON.get("sStockIDx"));
        } else {
            poJSON = new JSONObject();
            poJSON.put("result", "error");
            poJSON.put("message", "No record loaded.");
            return poJSON;
        }
    }
    
    public JSONObject searchRecordAttributes(String value, boolean byCode) throws SQLException, GuanzonException {
        poJSON = ShowDialogFX.Search(poGRider,
                getSQ_Browse(),
                value,
                "Brand»Model»Color»Selling Price»ID",
                "xBrandNme»xModelNme»xColorNme»nSelPrice»sStockIDx",
                "IFNULL(b.sDescript, '')»IF(IFNULL(c.sDescript, '') = '', '', CONCAT(c.sDescript, '(', c.sModelCde, ')'))»IFNULL(d.sDescript, '')»a.nSelPrice»a.sStockIDx",
                byCode ? 0 : 1);

        if (poJSON != null) {
            return poModel.openRecord((String) poJSON.get("sStockIDx"));
        } else {
            poJSON = new JSONObject();
            poJSON.put("result", "error");
            poJSON.put("message", "No record loaded.");
            return poJSON;
        }
    }
    
    public JSONObject searchRecordAttributes(String value, 
                                    boolean byCode, 
                                    String inventoryTypeId) throws SQLException, GuanzonException {
        String lsSQL = MiscUtil.addCondition(getSQ_Browse(), 
                                                "a.sInvTypCd = " + SQLUtil.toSQL(inventoryTypeId));
        
        poJSON = ShowDialogFX.Search(poGRider,
                lsSQL,
                value,
                "Brand»Model»Color»Selling Price»ID",
                "xBrandNme»xModelNme»xColorNme»nSelPrice»sStockIDx",
                "IFNULL(b.sDescript, '')»IF(IFNULL(c.sDescript, '') = '', '', CONCAT(c.sDescript, '(', c.sModelCde, ')'))»IFNULL(d.sDescript, '')»a.nSelPrice»a.sStockIDx",
                byCode ? 0 : 1);

        if (poJSON != null) {
            return poModel.openRecord((String) poJSON.get("sStockIDx"));
        } else {
            poJSON = new JSONObject();
            poJSON.put("result", "error");
            poJSON.put("message", "No record loaded.");
            return poJSON;
        }
    }
    
    public JSONObject searchRecordAttributes(String value, 
                                    boolean byCode, 
                                    String inventoryTypeId,
                                    String categoryIdLevel1) throws SQLException, GuanzonException {
        String lsSQL = MiscUtil.addCondition(getSQ_Browse(), 
                                                "a.sInvTypCd = " + SQLUtil.toSQL(inventoryTypeId) +
                                                    " AND a.sCategCd1 = " + SQLUtil.toSQL(categoryIdLevel1));
        
        poJSON = ShowDialogFX.Search(poGRider,
                lsSQL,
                value,
                "Brand»Model»Color»Selling Price»ID",
                "xBrandNme»xModelNme»xColorNme»nSelPrice»sStockIDx",
                "IFNULL(b.sDescript, '')»IF(IFNULL(c.sDescript, '') = '', '', CONCAT(c.sDescript, '(', c.sModelCde, ')'))»IFNULL(d.sDescript, '')»a.nSelPrice»a.sStockIDx",
                byCode ? 0 : 1);

        if (poJSON != null) {
            return poModel.openRecord((String) poJSON.get("sStockIDx"));
        } else {
            poJSON = new JSONObject();
            poJSON.put("result", "error");
            poJSON.put("message", "No record loaded.");
            return poJSON;
        }
    }
    
    public JSONObject searchRecordAttributes(String value, 
                                    boolean byCode, 
                                    String inventoryTypeId,
                                    String categoryIdLevel1,
                                    String categoryIdLevel2) throws SQLException, GuanzonException {
        String lsSQL = MiscUtil.addCondition(getSQ_Browse(), 
                                                "a.sInvTypCd = " + SQLUtil.toSQL(inventoryTypeId) +
                                                    " AND a.sCategCd1 = " + SQLUtil.toSQL(categoryIdLevel1) +
                                                    " AND a.sCategCd2 = " + SQLUtil.toSQL(categoryIdLevel2));
        
        poJSON = ShowDialogFX.Search(poGRider,
                lsSQL,
                value,
                "Brand»Model»Color»Selling Price»ID",
                "xBrandNme»xModelNme»xColorNme»nSelPrice»sStockIDx",
                "IFNULL(b.sDescript, '')»IF(IFNULL(c.sDescript, '') = '', '', CONCAT(c.sDescript, '(', c.sModelCde, ')'))»IFNULL(d.sDescript, '')»a.nSelPrice»a.sStockIDx",
                byCode ? 0 : 1);

        if (poJSON != null) {
            return poModel.openRecord((String) poJSON.get("sStockIDx"));
        } else {
            poJSON = new JSONObject();
            poJSON.put("result", "error");
            poJSON.put("message", "No record loaded.");
            return poJSON;
        }
    }
    
    public JSONObject searchRecordWithMeasurement(String value, boolean byCode) throws SQLException, GuanzonException {
        poJSON = ShowDialogFX.Search(poGRider,
                getSQ_Browse(),
                value,
                "Bar Code»Description»Brand»Model»Color»Measurement»Selling Price»ID",
                "sBarCodex»sDescript»xBrandNme»xModelNme»xColorNme»xMeasurNm»nSelPrice»sStockIDx",
                "a.sBarCodex»a.sDescript»IFNULL(b.sDescript, '')»IF(IFNULL(c.sDescript, '') = '', '', CONCAT(c.sDescript, '(', c.sModelCde, ')'))»IFNULL(d.sDescript, '')»IFNULL(e.sMeasurNm, '')»a.nSelPrice»a.sStockIDx",
                byCode ? 0 : 1);

        if (poJSON != null) {
            return poModel.openRecord((String) poJSON.get("sStockIDx"));
        } else {
            poJSON = new JSONObject();
            poJSON.put("result", "error");
            poJSON.put("message", "No record loaded.");
            return poJSON;
        }
    }
    
    public JSONObject searchRecordWithMeasurement(String value, 
                                    boolean byCode, 
                                    String inventoryTypeId) throws SQLException, GuanzonException {
        String lsSQL = MiscUtil.addCondition(getSQ_Browse(), 
                                                "a.sInvTypCd = " + SQLUtil.toSQL(inventoryTypeId));
        
        poJSON = ShowDialogFX.Search(poGRider,
                lsSQL,
                value,
                "Bar Code»Description»Brand»Model»Color»Measurement»Selling Price»ID",
                "sBarCodex»sDescript»xBrandNme»xModelNme»xColorNme»xMeasurNm»nSelPrice»sStockIDx",
                "a.sBarCodex»a.sDescript»IFNULL(b.sDescript, '')»IF(IFNULL(c.sDescript, '') = '', '', CONCAT(c.sDescript, '(', c.sModelCde, ')'))»IFNULL(d.sDescript, '')»IFNULL(e.sMeasurNm, '')»a.nSelPrice»a.sStockIDx",
                byCode ? 0 : 1);

        if (poJSON != null) {
            return poModel.openRecord((String) poJSON.get("sStockIDx"));
        } else {
            poJSON = new JSONObject();
            poJSON.put("result", "error");
            poJSON.put("message", "No record loaded.");
            return poJSON;
        }
    }
    
    public JSONObject searchRecordWithMeasurement(String value, 
                                    boolean byCode, 
                                    String inventoryTypeId,
                                    String categoryIdLevel1) throws SQLException, GuanzonException {
        String lsSQL = MiscUtil.addCondition(getSQ_Browse(), 
                                                "a.sInvTypCd = " + SQLUtil.toSQL(inventoryTypeId) +
                                                    " AND a.sCategCd1 = " + SQLUtil.toSQL(categoryIdLevel1));
        
        poJSON = ShowDialogFX.Search(poGRider,
                lsSQL,
                value,
                "Bar Code»Description»Brand»Model»Color»Measurement»Selling Price»ID",
                "sBarCodex»sDescript»xBrandNme»xModelNme»xColorNme»xMeasurNm»nSelPrice»sStockIDx",
                "a.sBarCodex»a.sDescript»IFNULL(b.sDescript, '')»IF(IFNULL(c.sDescript, '') = '', '', CONCAT(c.sDescript, '(', c.sModelCde, ')'))»IFNULL(d.sDescript, '')»IFNULL(e.sMeasurNm, '')»a.nSelPrice»a.sStockIDx",
                byCode ? 0 : 1);

        if (poJSON != null) {
            return poModel.openRecord((String) poJSON.get("sStockIDx"));
        } else {
            poJSON = new JSONObject();
            poJSON.put("result", "error");
            poJSON.put("message", "No record loaded.");
            return poJSON;
        }
    }
    
    public JSONObject searchRecordWithMeasurement(String value, 
                                    boolean byCode, 
                                    String inventoryTypeId,
                                    String categoryIdLevel1,
                                    String categoryIdLevel2) throws SQLException, GuanzonException {
        String lsSQL = MiscUtil.addCondition(getSQ_Browse(), 
                                                "a.sInvTypCd = " + SQLUtil.toSQL(inventoryTypeId) +
                                                    " AND a.sCategCd1 = " + SQLUtil.toSQL(categoryIdLevel1) +
                                                    " AND a.sCategCd2 = " + SQLUtil.toSQL(categoryIdLevel2));
        
        poJSON = ShowDialogFX.Search(poGRider,
                lsSQL,
                value,
                "Bar Code»Description»Brand»Model»Color»Measurement»Selling Price»ID",
                "sBarCodex»sDescript»xBrandNme»xModelNme»xColorNme»xMeasurNm»nSelPrice»sStockIDx",
                "a.sBarCodex»a.sDescript»IFNULL(b.sDescript, '')»IF(IFNULL(c.sDescript, '') = '', '', CONCAT(c.sDescript, '(', c.sModelCde, ')'))»IFNULL(d.sDescript, '')»IFNULL(e.sMeasurNm, '')»a.nSelPrice»a.sStockIDx",
                byCode ? 0 : 1);

        if (poJSON != null) {
            return poModel.openRecord((String) poJSON.get("sStockIDx"));
        } else {
            poJSON = new JSONObject();
            poJSON.put("result", "error");
            poJSON.put("message", "No record loaded.");
            return poJSON;
        }
    }
    
    @Override
    public String getSQ_Browse(){
        String lsSQL;
        String lsRecdStat = "";

        if (psRecdStat.length() > 1) {
            for (int lnCtr = 0; lnCtr <= psRecdStat.length() - 1; lnCtr++) {
                lsRecdStat += ", " + SQLUtil.toSQL(Character.toString(psRecdStat.charAt(lnCtr)));
            }

            lsRecdStat = "a.cRecdStat IN (" + lsRecdStat.substring(2) + ")";
        } else {
            lsRecdStat = "a.cRecdStat = " + SQLUtil.toSQL(psRecdStat);
        }
        
        lsSQL = "SELECT" +
                    "  a.sStockIDx" +
                    ", a.sBarCodex" +
                    ", a.sDescript" +
                    ", a.sAltBarCd" +
                    ", a.nUnitPrce" +
                    ", a.nSelPrice" +
                    ", IFNULL(b.sDescript, '') xBrandNme" +
                    ", IF(IFNULL(c.sDescript, '') = '', '', CONCAT(c.sDescript, '(', c.sModelCde, ')')) xModelNme" +
                    ", IFNULL(d.sDescript, '') xColorNme" +
                    ", IFNULL(e.sMeasurNm, '') xMeasurNm" +
                    ", IFNULL(f.sDescript, '') xCategNm1" +
                    ", IFNULL(g.sDescript, '') xCategNm2" +
                    ", IFNULL(h.sDescript, '') xCategNm3" +
                    ", IFNULL(i.sDescript, '') xCategNm4" +
                    ", IFNULL(j.sDescript, '') xInvTypNm" +
                " FROM Inventory a" +
                    " LEFT JOIN Brand b ON a.sBrandIDx = b.sBrandIDx" +
                    " LEFT JOIN Model c ON a.sModelIDx = c.sModelIDx" +
                    " LEFT JOIN Color d ON a.sColorIDx = d.sColorIDx" +
                    " LEFT JOIN Measure e ON a.sMeasurID = e.sMeasurID" +
                    " LEFT JOIN Category f ON a.sCategCd1 = f.sCategrCd" +
                    " LEFT JOIN Category_Level2 g ON a.sCategCd2 = g.sCategrCd" +
                    " LEFT JOIN Category_Level3 h ON a.sCategCd3 = h.sCategrCd" +
                    " LEFT JOIN Category_Level4 i ON a.sCategCd4 = i.sCategrCd" +
                    " LEFT JOIN Inv_Type j ON a.sInvTypCd = j.sInvTypCd";
        
        
        System.out.println("query natin to = = " + lsSQL );
        
        if (!psRecdStat.isEmpty()) lsSQL = MiscUtil.addCondition(lsSQL, lsRecdStat);
        
        return lsSQL;
    }
}