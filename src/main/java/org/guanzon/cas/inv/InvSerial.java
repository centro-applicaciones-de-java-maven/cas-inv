package org.guanzon.cas.inv;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import org.guanzon.appdriver.agent.ShowDialogFX;
import org.guanzon.appdriver.agent.services.Parameter;
import org.guanzon.appdriver.base.GuanzonException;
import org.guanzon.appdriver.base.SQLUtil;
import org.guanzon.appdriver.constant.EditMode;
import org.guanzon.appdriver.constant.Logical;
import org.guanzon.appdriver.constant.UserRight;
import org.guanzon.cas.inv.model.Model_Inv_Serial;
import org.guanzon.cas.inv.model.Model_Inv_Serial_Ledger;
import org.guanzon.cas.inv.model.Model_Inv_Serial_Registration;
import org.guanzon.cas.inv.services.InvModels;
import org.json.simple.JSONObject;

public class InvSerial extends Parameter{
    Model_Inv_Serial poModel;
    Model_Inv_Serial_Registration poRegistration;
    ArrayList<Model_Inv_Serial_Ledger> paLedger;

    @Override
    public void initialize() throws SQLException, GuanzonException{
        psRecdStat = Logical.YES;

        InvModels inv = new InvModels(poGRider);
        poModel = inv.InventorySerial();       
        
        poRegistration = inv.InventorySerialRegistration();   
        
        super.initialize();
    }
    
    public Model_Inv_Serial_Registration SerialRegistration(){
        return poRegistration;
    }
    
    public Model_Inv_Serial_Ledger Ledger(int row){
        if (row > paLedger.size() -1) return null;
        
        return paLedger.get(row);
    }
    
    public int getLedgerCount(){
        return paLedger.size();
    } 
    
    @Override
    public JSONObject isEntryOkay() throws SQLException, GuanzonException{
        poJSON = new JSONObject();
        
//        if (poGRider.getUserLevel() < UserRight.SYSADMIN){
//            poJSON.put("result", "error");
//            poJSON.put("message", "User is not allowed to save record.");
//            return poJSON;
//        } else {
            poJSON = new JSONObject();
            
            if (poModel.getSerialId().isEmpty()){
                poJSON.put("result", "error");
                poJSON.put("message", "Serial ID must not be empty.");
                return poJSON;
            }
            
            if (poModel.getBranchCode().isEmpty()){
                poJSON.put("result", "error");
                poJSON.put("message", "Branch must not be empty.");
                return poJSON;
            }
            
            if (poModel.getSerial01().isEmpty()){
                poJSON.put("result", "error");
                poJSON.put("message", "Serial 1 must not be empty.");
                return poJSON;
            }
            
            if (poModel.getSerial02().isEmpty()){
                poJSON.put("result", "error");
                poJSON.put("message", "Serial 2 must not be empty.");
                return poJSON;
            }
            
            if (poModel.getStockId().isEmpty()){
                poJSON.put("result", "error");
                poJSON.put("message", "Item must not be empty.");
                return poJSON;
            }
            
            poModel.setModifiedDate(poGRider.getServerDate());
            
            //todo:
            //  more validations/use of validators per category
//        }
        
        poJSON.put("result", "success");
        return poJSON;
    }
    
    @Override
    public Model_Inv_Serial getModel() {
        return poModel;
    }
    
    @Override
    public JSONObject searchRecord(String value, boolean byCode) throws SQLException, GuanzonException{
        poJSON = ShowDialogFX.Search(poGRider,
                getSQ_Browse(),
                value,
                "Serial ID»Description»Serial 01»Serial 02",
                "sSerialID»xDescript»sSerial01»sSerial02",
                "a.sSerialID»b.sDescript»a.sSerial01»a.sSerial02",
                byCode ? 0 : 1);

        if (poJSON != null) {
            poJSON =  openRecord((String) poJSON.get("sSerialID"));
            if (!"success".equals((String) poJSON.get("result"))) return poJSON;
            
            poJSON = new JSONObject();
            poJSON.put("result", "success");
            poJSON.put("message", "Record loaded successfully.");
            return poJSON;
        } else {
            poJSON = new JSONObject();
            poJSON.put("result", "error");
            poJSON.put("message", "No record loaded.");
            return poJSON;
        }
    }
    
    @Override
    public JSONObject openRecord(String Id) throws SQLException, GuanzonException {        
        poJSON = super.openRecord(Id);
        
        loadLedger();
                
        poJSON = poRegistration.openRecord(poModel.getSerialId());
        if (!"success".equals((String) poJSON.get("result"))) return poJSON;
        
        poJSON = new JSONObject();
        poJSON.put("result", "success");
        poJSON.put("message", "Record loaded successfully.");
        return poJSON;
    }
    
    @Override
    public JSONObject updateRecord() {
        if (!pbInitRec){
            poJSON = new JSONObject();
            poJSON.put("result", "error");
            poJSON.put("message", "Object is not initialized.");
            return poJSON;
        }
        
        poJSON =  getModel().updateRecord();
        if (!"success".equals((String) poJSON.get("result"))) return poJSON;
        
        poJSON = poRegistration.updateRecord();
        if (!"success".equals((String) poJSON.get("result"))) return poJSON;
        
        if ("success".equals((String) poJSON.get("result"))){
            poEvent = new JSONObject();
            poEvent.put("event", "UPDATE");            
        }
                
        return poJSON;
    }
    
    private void loadLedger() throws SQLException, GuanzonException{        
        String lsSQL = "SELECT" +
                            "  sSerialID" +
                            ", sBranchCd" +
                            ", sSourceCd" +
                            ", sSourceNo" +
                        " FROM Inv_Serial_Ledger" +
                        " WHERE sSerialID = " + SQLUtil.toSQL(poModel.getSerialId());
        
        ResultSet loRS = poGRider.executeQuery(lsSQL);
        
        paLedger = new ArrayList<>();
        while(loRS.next()){
            InvModels inv = new InvModels(poGRider);
            Model_Inv_Serial_Ledger ledger = inv.InventorySerialLedger();
            
            poJSON = ledger.openRecord(loRS.getString("sSerialID"), 
                                        loRS.getString("sBranchCd"), 
                                        loRS.getString("sSourceCd"), 
                                        loRS.getString("sSourceNo"));
            
            if ("success".equals((String) poJSON.get("result"))) paLedger.add(ledger);
        }
    }
    
    @Override
    public String getSQ_Browse(){       
        String lsSQL = "SELECT" +
                            "  a.sSerialID" +
                            ", a.sBranchCd" +
                            ", a.sClientID" +
                            ", a.sSerial01" +
                            ", a.sSerial02" +
                            ", a.nUnitPrce" +
                            ", a.sStockIDx" +
                            ", a.cLocation" +
                            ", a.cSoldStat" +
                            ", a.cUnitType" +
                            ", a.sCompnyID" +
                            ", a.sWarranty" +
                            ", a.dModified" +
                            ", b.sDescript xDescript" +
                        " FROM Inv_Serial a" +
                            ", Inventory b" +
                        " WHERE a.sStockIDx = b.sStockIDx";
        
        return lsSQL;
    }
    
    @Override
    protected JSONObject initFields() throws SQLException, GuanzonException{
        if (!System.getProperty("user.selected.industry").equals("09")){
            poJSON = poRegistration.newRecord();
        
            if (!"success".equals((String) poJSON.get("result"))) return poJSON;
        }
        
        
        poJSON = new JSONObject();
        poJSON.put("result", "success");
        return poJSON;
    }
    
    @Override
    protected JSONObject saveOthers() throws SQLException, GuanzonException{
        if (poRegistration.getEditMode() == EditMode.ADDNEW){
            poRegistration.setSerialId(poModel.getSerialId());
        }
        
        poJSON = poRegistration.saveRecord();
        if (!"success".equals((String) poJSON.get("result"))) return poJSON;
        
        poJSON = new JSONObject();
        poJSON.put("result", "success");
        return poJSON;
    }
}