//package org.guanzon.cas.inv.model;
//
//import java.sql.SQLException;
//import java.util.Date;
//import org.guanzon.appdriver.agent.services.Model;
//import org.guanzon.appdriver.base.MiscUtil;
//import org.guanzon.appdriver.constant.EditMode;
//import org.guanzon.cas.inv.services.InvModels;
//import org.guanzon.cas.parameter.model.Model_Branch;
//import org.guanzon.cas.parameter.model.Model_Warehouse;
//import org.guanzon.cas.parameter.services.ParamModels;
//import org.json.simple.JSONObject;
//
//public class Model_Inv_Ledger extends Model {
//
//    //reference objects
//    Model_Branch poBranch;
//    Model_Warehouse poWarehouse;
//    Model_Inventory poInventory;
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
//            //end - assign default values
//            poEntity.insertRow();
//            poEntity.moveToCurrentRow();
//
//            poEntity.absolute(1);
//
//            ID = "sStockIDx";
//            ID2 = "sBranchCd";
//            ID3 = "sWHouseID";
//            ID4 = "sSourceCd";
//            ID5 = "sSourceNo";
//
//            //initialize reference objects
//            ParamModels model = new ParamModels(poGRider);
//            poBranch = model.Branch();
//            poWarehouse = model.Warehouse();
//
//            poInventory = new InvModels(poGRider).Inventory();
//            //end - initialize reference objects
//
//            pnEditMode = EditMode.UNKNOWN;
//        } catch (SQLException e) {
//            logwrapr.severe(e.getMessage());
//            System.exit(1);
//        }
//    }
//
//    public JSONObject setStockId(String stockId) {
//        return setValue("sStockIDx", stockId);
//    }
//
//    public String getStockId() {
//        return (String) getValue("sStockIDx");
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
//    public JSONObject setWarehouseId(String warehouseId) {
//        return setValue("sWHouseID", warehouseId);
//    }
//
//    public String getWarehouseId() {
//        return (String) getValue("sWHouseID");
//    }
//
//    public JSONObject setLedgerNo(String ledgerNo) {
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
//    public JSONObject setSourceNo(String sourceNumber) {
//        return setValue("sSourceNo", sourceNumber);
//    }
//
//    public String getSourceNo() {
//        return (String) getValue("sSourceNo");
//    }
//
//    public JSONObject setQuantityIn(int quantity) {
//        return setValue("nQtyInxxx", quantity);
//    }
//
//    public int getQuantityIn() {
//        return (int) getValue("nQtyInxxx");
//    }
//
//    public JSONObject setQuantityOut(int quantity) {
//        return setValue("nQtyOutxx", quantity);
//    }
//
//    public int getQuantityOut() {
//        return (int) getValue("nQtyOutxx");
//    }
//
//    public JSONObject setQuantityOrder(int quantity) {
//        return setValue("nQtyOrder", quantity);
//    }
//
//    public int getQuantityOrder() {
//        return (int) getValue("nQtyOrder");
//    }
//
//    public JSONObject setQuantityIssued(int quantity) {
//        return setValue("nQtyIssue", quantity);
//    }
//
//    public int getQuantityIssued() {
//        return (int) getValue("nQtyIssue");
//    }
//
//    public JSONObject setCost(double cost) {
//        return setValue("nPurPrice", cost);
//    }
//
//    public int getCost() {
//        return (int) getValue("nPurPrice");
//    }
//
//    public JSONObject setSellingPrice(double sellingPrice) {
//        return setValue("nUnitPrce", sellingPrice);
//    }
//
//    public int getSellingPrice() {
//        return (int) getValue("nUnitPrce");
//    }
//
//    public JSONObject setQuantityOnHand(int quantity) {
//        return setValue("nQtyOnHnd", quantity);
//    }
//
//    public int getQuantityOnHand() {
//        return (int) getValue("nQtyOnHnd");
//    }
//
//    public JSONObject setExpirationDate(Date modifiedDate) {
//        return setValue("dExpiryxx", modifiedDate);
//    }
//
//    public Date getExpirationDate() {
//        return (Date) getValue("dExpiryxx");
//    }
//
//    public JSONObject setRecordStatus(String recordStatus) {
//        return setValue("cRecdStat", recordStatus);
//    }
//
//    public String getRecordStatus() {
//        return (String) getValue("cRecdStat");
//    }
//
//    public JSONObject setModifyingId(String modifyingId) {
//        return setValue("sModified", modifyingId);
//    }
//
//    public String getModifyingId() {
//        return (String) getValue("sModified");
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
//        return "";
//    }
//
//    @Override
//    public JSONObject openRecord(String id) {
//        JSONObject loJSON = new JSONObject();
//        loJSON.put("result", "error");
//        loJSON.put("message", "This feature is not supported.");
//        return loJSON;
//    }
//
//    @Override
//    public JSONObject openRecord(String Id1, Object Id2) {
//        JSONObject loJSON = new JSONObject();
//        loJSON.put("result", "error");
//        loJSON.put("message", "This feature is not supported.");
//        return loJSON;
//    }
//
//    @Override
//    public JSONObject openRecord(String Id1, Object Id2, Object Id3) {
//        JSONObject loJSON = new JSONObject();
//        loJSON.put("result", "error");
//        loJSON.put("message", "This feature is not supported.");
//        return loJSON;
//    }
//
//    @Override
//    public JSONObject openRecord(String Id1, Object Id2, Object Id3, Object Id4) {
//        JSONObject loJSON = new JSONObject();
//        loJSON.put("result", "error");
//        loJSON.put("message", "This feature is not supported.");
//        return loJSON;
//    }
//
//    //reference object models
//    public Model_Branch Branch() {
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
//    public Model_Warehouse Warehouse() {
//        if (!"".equals((String) getValue("sWHouseID"))) {
//            if (poWarehouse.getEditMode() == EditMode.READY
//                    && poWarehouse.getWarehouseId().equals((String) getValue("sWHouseID"))) {
//                return poWarehouse;
//            } else {
//                poJSON = poWarehouse.openRecord((String) getValue("sWHouseID"));
//
//                if ("success".equals((String) poJSON.get("result"))) {
//                    return poWarehouse;
//                } else {
//                    poWarehouse.initialize();
//                    return poWarehouse;
//                }
//            }
//        } else {
//            poWarehouse.initialize();
//            return poWarehouse;
//        }
//    }
//
//    public Model_Inventory Inventory() {
//        if (!"".equals((String) getValue("sStockIDx"))) {
//            if (poInventory.getEditMode() == EditMode.READY
//                    && poInventory.getStockId().equals((String) getValue("sStockIDx"))) {
//                return poInventory;
//            } else {
//                poJSON = poInventory.openRecord((String) getValue("sStockIDx"));
//
//                if ("success".equals((String) poJSON.get("result"))) {
//                    return poInventory;
//                } else {
//                    poInventory.initialize();
//                    return poInventory;
//                }
//            }
//        } else {
//            poInventory.initialize();
//            return poInventory;
//        }
//    }
//    //end - reference object models
//}
