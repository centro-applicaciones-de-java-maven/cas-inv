//package org.guanzon.cas.inv.model;
//
//import java.sql.SQLException;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Map;
//import org.guanzon.appdriver.agent.services.Model;
//import org.guanzon.appdriver.base.MiscUtil;
//import org.guanzon.appdriver.constant.EditMode;
//import org.guanzon.appdriver.constant.Logical;
//import org.guanzon.cas.parameter.services.ParamModels;
//import org.guanzon.appdriver.constant.RecordStatus;
//import org.guanzon.cas.parameter.model.Model_Branch;
//import org.guanzon.cas.parameter.model.Model_Inv_Location;
//import org.guanzon.cas.parameter.model.Model_Warehouse;
//import org.json.simple.JSONObject;
//
//public class Model_Inv_Serial_Ledger extends Model {
//    private Model_Inv_Serial poInventorySerial;
//    Model_Branch poBranch;
//    Model_Warehouse poWarehouse;
//    Model_Inventory poInventory;
//    Model_Inv_Location poLocation;
//
//    @Override
//    public void initialize() {
//        try {
//            poEntity = MiscUtil.xml2ResultSet(System.getProperty("sys.default.path.metadata") + XML, getTable());
//
//            poEntity.last();
//            poEntity.moveToInsertRow();
//
//            MiscUtil.initRowSet(poEntity);
//
//            //assign default values
//
//            poEntity.updateString("cSoldStat", RecordStatus.INACTIVE);
//            //end - assign default values
//
//             poEntity.insertRow();
//            poEntity.moveToCurrentRow();
//
//            poEntity.absolute(1);
//
//            ID = "sSerialID";
//            ID2 = "sSourceCd";
//            ID3 = "sSourceNo";
//            //initialize other connections
//
//            poInventorySerial = new Model_Inv_Serial();
//            poInventorySerial.setApplicationDriver(poGRider);
//            poInventorySerial.setXML("Model_Inv_Serial");
//            poInventorySerial.setTableName("Inv_Serial");
//            poInventorySerial.initialize();
//            
//            ParamModels model = new ParamModels(poGRider);
//            poBranch = model.Branch();
//            poWarehouse = model.Warehouse();
//            poLocation = model.InventoryLocation();
//            
//            pnEditMode = EditMode.UNKNOWN;
//        } catch (SQLException e) {
//            logwrapr.severe(e.getMessage());
//            System.exit(1);
//        }
//    }
//    public Model_Inv_Serial InventorySerial() {
//        if (!"".equals((String) getValue("sSerialID"))) {
//            if (poInventorySerial.getEditMode() == EditMode.READY
//                    && poInventorySerial.getStockId().equals((String) getValue("sSerialID"))) {
//                return poInventorySerial;
//            } else {
//                poJSON = poInventorySerial.openRecord((String) getValue("sSerialID"));
//
//                if ("success".equals((String) poJSON.get("result"))) {
//                    return poInventorySerial;
//                } else {
//                    poInventorySerial.initialize();
//                    return poInventorySerial;
//                }
//            }
//        } else {
//            poInventorySerial.initialize();
//            return poInventorySerial;
//        }
//    }
//    public Model_Branch Branch() {
//            System.out.println("here is branch code == " + (String) getValue("sBranchCd"));
//        if (!"".equals((String) getValue("sBranchCd"))) {
//            if (poBranch.getEditMode() == EditMode.READY
//                    && poBranch.getBranchCode().equals((String) getValue("sBranchCd"))) {
//                return poBranch;
//            } else {
//                poJSON = poBranch.openRecord((String) getValue("sBranchCd"));
//
//                if ("success".equals((String) poJSON.get("result"))) {
//                    return poBranch;
//                } else {
//                    poBranch.initialize();
//                    return poBranch;
//                }
//            }
//        } else {
//            poBranch.initialize();
//            return poBranch;
//        }
//    }
//
//    public JSONObject setSerialId(String serialId) {
//        return setValue("sSerialID", serialId);
//    }
//
//    public String getSerialId() {
//        return (String) getValue("sSerialID");
//    }
//
//    public JSONObject setBranchCode(String branchCode) {
//        return setValue("sBranchCd", branchCode);
//    }
//
//    public String getBranchCode() {
//        return (String) getValue("sBranchCd");
//    }
//
//    public JSONObject setLedgerNo(int ledgerNo) {
//        return setValue("nLedgerNo", ledgerNo);
//    }
//
//    public int getLedgerNo() {
//        return (int) getValue("nLedgerNo");
//    }
//
//    public JSONObject setTransactionDate(Date transactionDate) {
//        return setValue("dTransact", transactionDate);
//    }
//
//    public Date getTransactionDate() {
//        return (Date) getValue("dTransact");
//    }
//    
//    public JSONObject setSourceCode(String sourceCode) {
//        return setValue("sSourceCd", sourceCode);
//    }
//
//    public String getSourceCode() {
//        return (String) getValue("sSourceCd");
//    }
//    
//    public JSONObject setSourceNo(String sourceNo) {
//        return setValue("sSourceNo", sourceNo);
//    }
//
//    public String getSourceNo() {
//        return (String) getValue("sSourceNo");
//    }
//
//    public JSONObject setSoldStatus(String soldStatus) {
//        return setValue("cSoldStat", soldStatus);
//    }
//
//    public String getSoldStatus() {
//        return (String) getValue("cSoldStat");
//    }
//
//    public JSONObject setLocation(String location) {
//        return setValue("cLocation", location);
//    }
//
//    public String getLocation() {
//        return (String) getValue("cLocation");
//    }
//
//    public JSONObject setModifiedDate(Date modifiedDate) {
//        return setValue("dModified", modifiedDate);
//    }
//
//    public Date getModifiedDate() {
//        return (Date) getValue("dModified");
//    }
//
//    @Override
//    public String getNextCode() {
//        return MiscUtil.getNextCode(getTable(), ID, true, poGRider.getConnection(), poGRider.getBranchCode());
//    }
//}
