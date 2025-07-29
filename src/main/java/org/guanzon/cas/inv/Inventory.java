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
import org.guanzon.cas.inv.services.InvModels;
import org.json.simple.JSONObject;

public class Inventory extends Parameter{
    Model_Inventory poModel;
    
    @Override
    public void initialize() {
        psRecdStat = Logical.YES;
        
        InvModels inv = new InvModels(poGRider);
        poModel = inv.Inventory();
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
        String lsSQL = getSQ_Browse();
        
        poJSON = ShowDialogFX.Search(poGRider,
                lsSQL,
                value,
                "Bar Code»Description»Brand»Model»UOM",
                "sBarCodex»sDescript»xBrandNme»xModelNme»xMeasurNm",
                "a.sBarCodex»a.sDescript»IFNULL(b.sDescript, '')»IFNULL(c.sDescript, '')»IFNULL(e.sDescript, '')",
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
    
    public JSONObject searchRecord(String value, boolean byCode, String supplierId) throws SQLException, GuanzonException {
        String lsSQL = getSQ_Browse();
        
        if (supplierId != null){
            lsSQL = MiscUtil.addCondition(lsSQL, "g.sSupplier = " + SQLUtil.toSQL(supplierId));
        }
        
        poJSON = ShowDialogFX.Search(poGRider,
                lsSQL,
                value,
                "Bar Code»Description»Brand»Model»UOM",
                "sBarCodex»sDescript»xBrandNme»xModelNme»xMeasurNm",
                "a.sBarCodex»a.sDescript»IFNULL(b.sDescript, '')»IFNULL(c.sDescript, '')»IFNULL(e.sDescript, '')",
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
    
    public JSONObject searchRecord(String value, boolean byCode, String supplierId, String brandId) throws SQLException, GuanzonException {
        String lsSQL = getSQ_Browse();
        
        if (supplierId != null){
            lsSQL = MiscUtil.addCondition(lsSQL, "g.sSupplier = " + SQLUtil.toSQL(supplierId));
        }
        
        if (brandId != null){
            lsSQL = MiscUtil.addCondition(lsSQL, "a.sBrandIDx = " + SQLUtil.toSQL(brandId));
        }
        
        poJSON = ShowDialogFX.Search(poGRider,
                lsSQL,
                value,
                "Bar Code»Description»Brand»Model»UOM",
                "sBarCodex»sDescript»xBrandNme»xModelNme»xMeasurNm",
                "a.sBarCodex»a.sDescript»IFNULL(b.sDescript, '')»IFNULL(c.sDescript, '')»IFNULL(e.sDescript, '')",
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
    
    public JSONObject searchRecord(String value, boolean byCode, String supplierId, String brandId, String industryId) throws SQLException, GuanzonException {
        String lsSQL = getSQ_Browse();
        
        if (supplierId != null){
            lsSQL = MiscUtil.addCondition(lsSQL, "g.sSupplier = " + SQLUtil.toSQL(supplierId));
        }
        
        if (brandId != null){
            lsSQL = MiscUtil.addCondition(lsSQL, "a.sBrandIDx = " + SQLUtil.toSQL(brandId));
        }
        
        if (industryId != null){
            lsSQL = MiscUtil.addCondition(lsSQL, "a.sIndstCdx = " + SQLUtil.toSQL(industryId));
        }
        
        poJSON = ShowDialogFX.Search(poGRider,
                lsSQL,
                value,
                "Bar Code»Description»Brand»Model»UOM",
                "sBarCodex»sDescript»xBrandNme»xModelNme»xMeasurNm",
                "a.sBarCodex»a.sDescript»IFNULL(b.sDescript, '')»IFNULL(c.sDescript, '')»IFNULL(e.sDescript, '')",
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
    
    public JSONObject searchRecord(String value, boolean byCode, String supplierId, String brandId, String industryId, String categoryId) throws SQLException, GuanzonException {
        String lsSQL = getSQ_Browse();
        
        if (supplierId != null){
            lsSQL = MiscUtil.addCondition(lsSQL, "g.sSupplier = " + SQLUtil.toSQL(supplierId));
        }
        
        if (brandId != null){
            lsSQL = MiscUtil.addCondition(lsSQL, "a.sBrandIDx = " + SQLUtil.toSQL(brandId));
        }
        
        if (industryId != null){
            lsSQL = MiscUtil.addCondition(lsSQL, "a.sIndstCdx = " + SQLUtil.toSQL(industryId));
        }
        
        if (categoryId != null){
            lsSQL = MiscUtil.addCondition(lsSQL, "a.sCategCd1 = " + SQLUtil.toSQL(categoryId));
        }   
        
        poJSON = ShowDialogFX.Search(poGRider,
                lsSQL,
                value,
                "Bar Code»Description»Brand»Model»UOM",
                "sBarCodex»sDescript»xBrandNme»xModelNme»xMeasurNm",
                "a.sBarCodex»a.sDescript»IFNULL(b.sDescript, '')»IFNULL(c.sDescript, '')»IFNULL(e.sDescript, '')",
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
    
    public JSONObject searchRecordOfVariants(String value, boolean byCode) throws SQLException, GuanzonException {
        String lsSQL = getSQ_Browse();
        
        poJSON = ShowDialogFX.Search(poGRider,
                lsSQL,
                value,
                "Brand»Model»Variant»Code»Color",
                "xBrandNme»xModelNme»xVrntName»xModelCde»xColorNme",
                "IFNULL(b.sDescript, '')»IFNULL(c.sDescript, '')»TRIM(CONCAT(IFNULL(f.sDescript, ''), ' ', IFNULL(f.nYearMdlx, '')))»IFNULL(c.sModelCde, '') xModelCde»IFNULL(d.sDescript, '')",
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
    
    public JSONObject searchRecordOfVariants(String value, boolean byCode, String supplierId) throws SQLException, GuanzonException {
        String lsSQL = getSQ_Browse();
        
        if (supplierId != null){
            lsSQL = MiscUtil.addCondition(lsSQL, "g.sSupplier = " + SQLUtil.toSQL(supplierId));
        }
        
        poJSON = ShowDialogFX.Search(poGRider,
                lsSQL,
                value,
                "Brand»Model»Variant»Code»Color",
                "xBrandNme»xModelNme»xVrntName»xModelCde»xColorNme",
                "IFNULL(b.sDescript, '')»IFNULL(c.sDescript, '')»TRIM(CONCAT(IFNULL(f.sDescript, ''), ' ', IFNULL(f.nYearMdlx, '')))»IFNULL(c.sModelCde, '') xModelCde»IFNULL(d.sDescript, '')",
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
    
    public JSONObject searchRecordOfVariants(String value, boolean byCode, String supplierId, String brandId, String industryId, String categoryId) throws SQLException, GuanzonException {
        String lsSQL = getSQ_Browse();
        
        if (supplierId != null){
            lsSQL = MiscUtil.addCondition(lsSQL, "g.sSupplier = " + SQLUtil.toSQL(supplierId));
        }
        
        if (brandId != null){
            lsSQL = MiscUtil.addCondition(lsSQL, "a.sBrandIDx = " + SQLUtil.toSQL(brandId));
        }
        
        if (industryId != null){
            lsSQL = MiscUtil.addCondition(lsSQL, "a.sIndstCdx = " + SQLUtil.toSQL(industryId));
        }
        
        if (categoryId != null){
            lsSQL = MiscUtil.addCondition(lsSQL, "a.sCategCd1 = " + SQLUtil.toSQL(categoryId));
        }
        
        poJSON = ShowDialogFX.Search(poGRider,
                lsSQL,
                value,
                "Brand»Model»Variant»Code»Color",
                "xBrandNme»xModelNme»xVrntName»xModelCde»xColorNme",
                "IFNULL(b.sDescript, '')»IFNULL(c.sDescript, '')»TRIM(CONCAT(IFNULL(f.sDescript, ''), ' ', IFNULL(f.nYearMdlx, '')))»IFNULL(c.sModelCde, '') xModelCde»IFNULL(d.sDescript, '')",
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
    
    public JSONObject searchRecord(String categoryId, String brandId, String modelId, String variantId, String colorId) throws SQLException, GuanzonException {
        String lsSQL = getSQ_Browse();
        
        if (categoryId != null){
            lsSQL = MiscUtil.addCondition(lsSQL, "a.sCategCd1 = " + SQLUtil.toSQL(categoryId));
        }
        
        if (brandId != null){
            lsSQL = MiscUtil.addCondition(lsSQL, "a.sBrandIDx = " + SQLUtil.toSQL(brandId));
        }
        
        if (modelId != null){
            lsSQL = MiscUtil.addCondition(lsSQL, "a.sModelIDx = " + SQLUtil.toSQL(modelId));
        }
        
        if (variantId != null){
            lsSQL = MiscUtil.addCondition(lsSQL, "a.sVrntIDxx = " + SQLUtil.toSQL(variantId));
        }
        
        if (colorId != null){
            lsSQL = MiscUtil.addCondition(lsSQL, "a.sColorIDx = " + SQLUtil.toSQL(colorId));
        }
        
        lsSQL += " GROUP BY a.sStockIDx";
        
        poJSON = ShowDialogFX.Search(poGRider,
                lsSQL,
                "",
                "Stock ID»Brand»Model»Variant»Code»Color",
                "sStockIDx»xBrandNme»xModelNme»xVrntName»xModelCde»xColorNme",
                "a.sStockIDx»IFNULL(b.sDescript, '')»IFNULL(c.sDescript, '')»TRIM(CONCAT(IFNULL(f.sDescript, ''), ' ', IFNULL(f.nYearMdlx, '')))»IFNULL(c.sModelCde, '') xModelCde»IFNULL(d.sDescript, '')",
                0);

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
        String lsCondition = "";

        if (psRecdStat.length() > 1) {
            for (int lnCtr = 0; lnCtr <= psRecdStat.length() - 1; lnCtr++) {
                lsCondition += ", " + SQLUtil.toSQL(Character.toString(psRecdStat.charAt(lnCtr)));
            }

            lsCondition = "a.cRecdStat IN (" + lsCondition.substring(2) + ")";
        } else {
            lsCondition = "a.cRecdStat = " + SQLUtil.toSQL(psRecdStat);
        }
        
        String lsSQL = "SELECT" +
                            "  a.sStockIDx" +
                            ", a.sBarCodex" +
                            ", a.sDescript" +
                            ", a.sBriefDsc" +
                            ", a.sAltBarCd" +
                            ", a.sCategCd1" +
                            ", a.sCategCd2" +
                            ", a.sCategCd3" +
                            ", a.sCategCd4" +
                            ", a.sBrandIDx" +
                            ", a.sModelIDx" +
                            ", a.sColorIDx" +
                            ", a.sVrntIDxx" +
                            ", a.sMeasurID" +
                            ", a.sInvTypCd" +
                            ", a.sIndstCdx" +
                            ", a.nUnitPrce" +	
                            ", a.nSelPrice" +	
                            ", a.nDiscLev1" +	
                            ", a.nDiscLev2" +	
                            ", a.nDiscLev3" +	
                            ", a.nDealrDsc" +	
                            ", a.nMinLevel" +
                            ", a.nMaxLevel" +
                            ", a.cComboInv" +
                            ", a.cWthPromo" +
                            ", a.cSerialze" +
                            ", a.cUnitType" +
                            ", a.cInvStatx" +
                            ", a.nShlfLife" +
                            ", a.sSupersed" +
                            ", a.cRecdStat" +
                            ", a.sModified" +
                            ", a.dModified" +
                            ", IFNULL(b.sDescript, '') xBrandNme" +
                            ", IFNULL(c.sDescript, '') xModelNme" +
                            ", IFNULL(d.sDescript, '') xColorNme" +
                            ", IFNULL(e.sDescript, '') xMeasurNm" +
                            ", TRIM(CONCAT(IFNULL(f.sDescript, ''), ' ', IFNULL(f.nYearMdlx, ''))) xVrntName" +
                            ", IFNULL(c.sModelCde, '') xModelCde" +
                        " FROM Inventory a" +
                            " LEFT JOIN Brand b ON a.sBrandIDx = b.sBrandIDx" +
                            " LEFT JOIN Model c ON a.sModelIDx = c.sModelIDx" +
                            " LEFT JOIN Color d ON a.sColorIDx = d.sColorIDx" +
                            " LEFT JOIN Measure e ON a.sMeasurID = e.sMeasurID" + 
                            " LEFT JOIN Model_Variant f ON a.sVrntIDxx = f.sVrntIDxx" + 
                            " LEFT JOIN Inv_Supplier g ON a.sStockIDx = g.sStockIDx";
        
        return MiscUtil.addCondition(lsSQL, lsCondition);
    }
}