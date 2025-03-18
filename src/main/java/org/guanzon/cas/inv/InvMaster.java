package org.guanzon.cas.inv;

import java.sql.SQLException;
import org.guanzon.cas.inv.services.InvControllers;
import org.guanzon.appdriver.agent.ShowDialogFX;
import org.guanzon.appdriver.agent.services.Parameter;
import org.guanzon.appdriver.base.GuanzonException;
import org.guanzon.appdriver.base.MiscUtil;
import org.guanzon.appdriver.base.SQLUtil;
import org.guanzon.appdriver.constant.Logical;
import org.guanzon.appdriver.constant.UserRight;
import org.guanzon.cas.inv.model.Model_Inv_Master;
import org.guanzon.cas.parameter.Branch;
import org.guanzon.cas.parameter.InvLocation;
import org.guanzon.cas.parameter.services.ParamControllers;
import org.guanzon.cas.parameter.Warehouse;
import org.json.simple.JSONObject;

public class InvMaster extends Parameter{
    //object model
    Model_Inv_Master poModel;
    
    //reference objects
    ParamControllers poParams;
    InvControllers poInv;
    
    Branch poBranch;
    InvLocation poLocation;
    Inventory poInventory;
    Warehouse poWarehouse;
    //end - reference objects
    
    //optional only
    String psBranchCd;
    public void setBranchCode(String branchCode){
        psBranchCd = branchCode;
    }
    
    //return reference objects
    public Branch Branch(){
        return poBranch;
    }
    
    public InvLocation InvLocation(){
        return poLocation;
    }
    
    public Inventory Inventory(){
        return poInventory;
    }
    
    public Warehouse Warehouse(){
        return poWarehouse;
    }
    //end - return reference objects
    
    @Override
    public void initialize() throws SQLException, GuanzonException{
        psRecdStat = Logical.YES;
        
        poModel = new Model_Inv_Master();
        poModel.setApplicationDriver(poGRider);
        poModel.setXML("Model_Inv_Master");
        poModel.setTableName("Inv_Master");
        poModel.initialize();
        
        psBranchCd = poGRider.getBranchCode();
        
        //initialize reference objects
        poParams = new ParamControllers(poGRider, logwrapr);
        poBranch = poParams.Branch();
        poLocation = poParams.InventoryLocation();
        poWarehouse = poParams.Warehouse();
        
        poInv = new InvControllers(poGRider, logwrapr);
        poInventory = poInv.Inventory();
        //end - initialize reference objects
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
            
            if (poModel.getStockId().isEmpty()){
                poJSON.put("result", "error");
                poJSON.put("message", "Item must not be empty.");
                return poJSON;
            }
            
            if (poModel.getBranchCode().isEmpty()){
                poJSON.put("result", "error");
                poJSON.put("message", "Branch location must not be empty.");
                return poJSON;
            }
            
//            if (poModel.getWarehouseId().isEmpty()){
//                poJSON.put("result", "error");
//                poJSON.put("message", "Warehouse location must not be empty.");
//                return poJSON;
//            }
//            
//            if (poModel.getLocationId().isEmpty()){
//                poJSON.put("result", "error");
//                poJSON.put("message", "Location must not be empty.");
//                return poJSON;
//            }     
            //todo:
            //  more validations/use of validators per category
            
            poModel.setModifyingId(poGRider.Encrypt(poGRider.getUserID()));
            poModel.setModifiedDate(poGRider.getServerDate());
        }
        
        poJSON.put("result", "success");
        return poJSON;
    }
    
    @Override
    public Model_Inv_Master getModel() {
        return poModel;
    }
    
    @Override
    public JSONObject searchRecord(String value, boolean byCode) throws SQLException, GuanzonException {
        String lsSQL = getSQ_Browse();
        
        poJSON = ShowDialogFX.Search(poGRider,
                lsSQL,
                value,
                "Bar Code»Description»Brand»Model»UOM»QOH",
                "sBarCodex»sDescript»xBrandNme»xModelNme»xMeasurNm»xQtyOnHnd",
                "a.sBarCodex»a.sDescript»IFNULL(b.sDescript, '')»IFNULL(c.sDescript, '')»IFNULL(e.sDescript, '')»b.nQtyOnHnd",
                byCode ? 0 : 1);
 
        if (poJSON != null) {
            return poModel.openRecord((String) poJSON.get("sStockIDx"), (String) poJSON.get("xBranchCd"));
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
            lsSQL = MiscUtil.addCondition(lsSQL, "h.sSupplier = " + SQLUtil.toSQL(supplierId));
        }
        
        poJSON = ShowDialogFX.Search(poGRider,
                lsSQL,
                value,
                "Bar Code»Description»Brand»Model»UOM»QOH",
                "sBarCodex»sDescript»xBrandNme»xModelNme»xMeasurNm»xQtyOnHnd",
                "a.sBarCodex»a.sDescript»IFNULL(b.sDescript, '')»IFNULL(c.sDescript, '')»IFNULL(e.sDescript, '')»b.nQtyOnHnd",
                byCode ? 0 : 1);
 
        if (poJSON != null) {
            return poModel.openRecord((String) poJSON.get("sStockIDx"), (String) poJSON.get("xBranchCd"));
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
                "Brand»Model»Variant»Code»Color»QOH",
                "xBrandNme»xModelNme»xVrntName»xModelCde»xColorNme»xQtyOnHnd",
                "IFNULL(b.sDescript, '')»IFNULL(c.sDescript, '')»TRIM(CONCAT(IFNULL(f.sDescript, ''), ' ', IFNULL(f.nYearMdlx, '')))»IFNULL(c.sModelCde, '') xModelCde»IFNULL(d.sDescript, '')»b.nQtyOnHnd",
                byCode ? 0 : 1);

        if (poJSON != null) {
            return poModel.openRecord((String) poJSON.get("sStockIDx"), (String) poJSON.get("xBranchCd"));
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
            lsSQL = MiscUtil.addCondition(lsSQL, "h.sSupplier = " + SQLUtil.toSQL(supplierId));
        }
        
        poJSON = ShowDialogFX.Search(poGRider,
                lsSQL,
                value,
                "Brand»Model»Variant»Code»Color»QOH",
                "xBrandNme»xModelNme»xVrntName»xModelCde»xColorNme»xQtyOnHnd",
                "IFNULL(b.sDescript, '')»IFNULL(c.sDescript, '')»TRIM(CONCAT(IFNULL(f.sDescript, ''), ' ', IFNULL(f.nYearMdlx, '')))»IFNULL(c.sModelCde, '') xModelCde»IFNULL(d.sDescript, '')»b.nQtyOnHnd",
                byCode ? 0 : 1);

        if (poJSON != null) {
            return poModel.openRecord((String) poJSON.get("sStockIDx"), (String) poJSON.get("xBranchCd"));
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
                            ", g.nQtyOnHnd xQtyOnHnd" +
                            ", g.sBranchCd xBranchCd" +
                        " FROM Inventory a" +
                                " LEFT JOIN Brand b ON a.sBrandIDx = b.sBrandIDx" +
                                " LEFT JOIN Model c ON a.sModelIDx = c.sModelIDx" +
                                " LEFT JOIN Color d ON a.sColorIDx = d.sColorIDx" +
                                " LEFT JOIN Measure e ON a.sMeasurID = e.sMeasurID" + 
                                " LEFT JOIN Model_Variant f ON a.sVrntIDxx = f.sVrntIDxx" +
                                " LEFT JOIN Inv_Supplier h ON a.sStockIDx = h.sStockIDx" +
                            ", Inv_Master g" +
                        " WHERE a.sStockIDx = g.sStockIDx" +
                            " AND g.sBranchCd = " + SQLUtil.toSQL(psBranchCd);
        
        return MiscUtil.addCondition(lsSQL, lsCondition);
    }
}
        