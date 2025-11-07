package org.guanzon.cas.inv.model;

import java.sql.SQLException;
import java.util.Date;
import org.guanzon.appdriver.agent.services.Model;
import org.guanzon.appdriver.base.GuanzonException;
import org.guanzon.appdriver.base.MiscUtil;
import org.guanzon.appdriver.constant.EditMode;
import org.guanzon.appdriver.constant.InventoryClassification;
import org.guanzon.appdriver.constant.Logical;
import org.guanzon.appdriver.constant.RecordStatus;
import org.guanzon.cas.inv.services.InvModels;
import org.guanzon.cas.parameter.model.Model_Bin;
import org.guanzon.cas.parameter.model.Model_Branch;
import org.guanzon.cas.parameter.model.Model_Industry;
import org.guanzon.cas.parameter.model.Model_Inv_Location;
import org.guanzon.cas.parameter.model.Model_Warehouse;
import org.guanzon.cas.parameter.services.ParamModels;
import org.json.simple.JSONObject;

public class Model_Inv_Master extends Model {

    //reference objects
    Model_Branch poBranch;
    Model_Warehouse poWarehouse;
    Model_Inventory poInventory;
    Model_Inv_Location poLocation;
    Model_Bin poBinLevel;
    Model_Industry poIndustry;

    @Override
    public void initialize() {
        try {
            poEntity = MiscUtil.xml2ResultSet(System.getProperty("sys.default.path.metadata") + XML, getTable());

            poEntity.last();
            poEntity.moveToInsertRow();

            MiscUtil.initRowSet(poEntity);

            poEntity.insertRow();
            poEntity.moveToCurrentRow();

            poEntity.absolute(1);

            //assign default values
            poEntity.updateNull("sWHouseID");
            poEntity.updateNull("sLocatnID");
            poEntity.updateNull("sBinNumbr");
            poEntity.updateNull("dAcquired");
            poEntity.updateNull("dBegInvxx");
            poEntity.updateObject("nBegQtyxx", 0.0d);
            poEntity.updateObject("nQtyOnHnd", 0.0d);
            poEntity.updateObject("nLedgerNo", 0);
            poEntity.updateObject("nMinLevel", 0.0d);
            poEntity.updateObject("nMaxLevel", 0.0d);
            poEntity.updateObject("nAvgMonSl", 0.0d);
            poEntity.updateObject("nAvgCostx", 0.00d);
            poEntity.updateString("cClassify", InventoryClassification.NEW_ITEMS);
            poEntity.updateObject("nBackOrdr", 0.0d);
            poEntity.updateObject("nResvOrdr", 0.0d);
            poEntity.updateObject("nFloatQty", 0.0d);
            poEntity.updateNull("dLastTran");
            poEntity.updateString("cPrimaryx", Logical.NO);
            poEntity.updateString("cConditnx", Logical.NO);
            poEntity.updateNull("sPayLoadx");
            poEntity.updateString("cRecdStat", RecordStatus.ACTIVE);
            poEntity.updateObject("dModified", poGRider.getServerDate());

            //end - assign default values
            ID = "sStockIDx";
            ID2 = "sBranchCd";

            //initialize reference objects
            ParamModels model = new ParamModels(poGRider);
            poBranch = model.Branch();
            poWarehouse = model.Warehouse();
            poLocation = model.InventoryLocation();
            poBinLevel = model.Bin();
            this.poIndustry = (new ParamModels(this.poGRider)).Industry();

            poInventory = new InvModels(poGRider).Inventory();
            //end - initialize reference objects

            pnEditMode = EditMode.UNKNOWN;
        } catch (SQLException e) {
            logwrapr.severe(e.getMessage());
            System.exit(1);
        }
    }

    public JSONObject setStockId(String stockId) {
        return setValue("sStockIDx", stockId);
    }

    public String getStockId() {
        return (String) getValue("sStockIDx");
    }

    public JSONObject setIndustryCode(String industryCode) {
        return setValue("sIndstCdx", industryCode);
    }

    public String getIndustryCode() {
        return (String) getValue("sIndstCdx");
    }

    public JSONObject setBranchCode(String branchCode) {
        return setValue("sBranchCd", branchCode);
    }

    public String getBranchCode() {
        return (String) getValue("sBranchCd");
    }

    public JSONObject setWarehouseId(String warehouseId) {
        return setValue("sWHouseID", warehouseId);
    }

    public String getWarehouseId() {
        return (String) getValue("sWHouseID");
    }

    public JSONObject setLocationId(String briefDescription) {
        return setValue("sLocatnID", briefDescription);
    }

    public String getLocationId() {
        return (String) getValue("sLocatnID");
    }

    public JSONObject setBinId(String binId) {
        return setValue("sBinNumbr", binId);
    }

    public String getBinId() {
        return (String) getValue("sBinNumbr");
    }

    public JSONObject setDateAcquired(Date dateAcquired) {
        return setValue("dAcquired", dateAcquired);
    }

    public Date getDateAcquired() {
        return (Date) getValue("dAcquired");
    }

    public JSONObject setBeginningInventoryDate(Date beginningInventoryDate) {
        return setValue("dBegInvxx", beginningInventoryDate);
    }

    public Date getBeginningInventoryDate() {
        return (Date) getValue("dBegInvxx");
    }

    public JSONObject setBeginningInventoryQuantity(double beginningInventoryQuantity) {
        return setValue("nBegQtyxx", beginningInventoryQuantity);
    }

    public double getBeginningInventoryQuantity() {
        return Double.parseDouble(String.valueOf(getValue("nBegQtyxx")));
    }

    public JSONObject setQuantityOnHand(double quantityOnHand) {
        return setValue("nQtyOnHnd", quantityOnHand);
    }

    public double getQuantityOnHand() {
        return Double.parseDouble(String.valueOf(getValue("nQtyOnHnd")));
    }

    public JSONObject setLedgerCount(int ledgerCount) {
        return setValue("nLedgerNo", ledgerCount);
    }

    public int getLedgerCount() {
        return (int) getValue("nLedgerNo");
    }

    public JSONObject setMinimumLevel(double minimumInventoryLevel) {
        return setValue("nMinLevel", minimumInventoryLevel);
    }

    public double getMinimumLevel() {
        return Double.parseDouble(String.valueOf(getValue("nMinLevel")));
    }

    public JSONObject setMaximumLevel(double maximumInventoryLevel) {
        return setValue("nMaxLevel", maximumInventoryLevel);
    }

    public double getMaximumLevel() {
        return Double.parseDouble(String.valueOf(getValue("nMaxLevel")));
    }

    public JSONObject setAverageMonthlySale(double averageMonthlySale) {
        return setValue("nMaxLevel", averageMonthlySale);
    }

    public double getAverageMonthlySales() {
        return Double.parseDouble(String.valueOf(getValue("nAvgMonSl")));
    }

    public JSONObject setAverageCost(double averageCost) {
        return setValue("nAvgCostx", averageCost);
    }

    public double getAverageCost() {
        return Double.parseDouble(String.valueOf(getValue("nAvgCostx")));
    }

    public JSONObject setInventoryClassification(String inventoryClassification) {
        return setValue("cClassify", inventoryClassification);
    }

    public String getInventoryClassification() {
        return (String) getValue("cClassify");
    }

    public JSONObject setBackOrderQuantity(double backOrderQuantity) {
        return setValue("nBackOrdr", backOrderQuantity);
    }

    public double getBackOrderQuantity() {
        return Double.parseDouble(String.valueOf(getValue("nAvgMonSl")));
    }

    public JSONObject setReserveOrderQuantity(double reserveOrderQuantity) {
        return setValue("nResvOrdr", reserveOrderQuantity);
    }

    public double getReserveOrderQuantity() {
        return Double.parseDouble(String.valueOf(getValue("nResvOrdr")));
    }

    public JSONObject setFloatQuantity(double reserveQuantity) {
        return setValue("nFloatQty", reserveQuantity);
    }

    public double getFloatQuantity() {
        return Double.parseDouble(String.valueOf(getValue("nFloatQty")));
    }

    public JSONObject setLastTransactionDate(Date lastTransactionDate) {
        return setValue("dLastTran", lastTransactionDate);
    }

    public Date getLastTransactionDate() {
        return (Date) getValue("dLastTran");
    }

    public JSONObject setCondition(String condition) {
        return setValue("cConditnx", condition);
    }

    public String getCondition() {
        return (String) getValue("cConditnx");
    }

    public JSONObject setPayload(String payLoad) {
        return setValue("sPayLoadx", payLoad);
    }

    public String getPayload() {
        return (String) getValue("sPayLoadx");
    }

    public JSONObject isPrimary(boolean isPrimary) {
        return setValue("cPrimaryx", isPrimary ? "1" : "0");
    }

    public boolean isPrimary() {
        return ((String) getValue("cPrimaryx")).equals("1");
    }

    public JSONObject setRecordStatus(String recordStatus) {
        return setValue("cRecdStat", recordStatus);
    }

    public String getRecordStatus() {
        return (String) getValue("cRecdStat");
    }

    public JSONObject setModifyingId(String modifyingId) {
        return setValue("sModified", modifyingId);
    }

    public String getModifyingId() {
        return (String) getValue("sModified");
    }

    public JSONObject setModifiedDate(Date modifiedDate) {
        return setValue("dModified", modifiedDate);
    }

    public Date getModifiedDate() {
        return (Date) getValue("dModified");
    }

    @Override
    public String getNextCode() {
        return "";
    }

    @Override
    public JSONObject openRecord(String Id1) {
        JSONObject loJSON = new JSONObject();
        loJSON.put("result", "error");
        loJSON.put("message", "This feature is not supported.");
        return loJSON;
    }

    @Override
    public JSONObject openRecord(String Id1, Object Id2, Object Id3) {
        JSONObject loJSON = new JSONObject();
        loJSON.put("result", "error");
        loJSON.put("message", "This feature is not supported.");
        return loJSON;
    }

    @Override
    public JSONObject openRecord(String Id1, Object Id2, Object Id3, Object Id4) {
        JSONObject loJSON = new JSONObject();
        loJSON.put("result", "error");
        loJSON.put("message", "This feature is not supported.");
        return loJSON;
    }

    @Override
    public JSONObject openRecord(String Id1, Object Id2, Object Id3, Object Id4, Object Id5) {
        JSONObject loJSON = new JSONObject();
        loJSON.put("result", "error");
        loJSON.put("message", "This feature is not supported.");
        return loJSON;
    }

    //reference object models
    public Model_Branch Branch() throws SQLException, GuanzonException {
        if (!"".equals((String) getValue("sBranchCd"))) {
            if (poBranch.getEditMode() == EditMode.READY
                    && poBranch.getBranchCode().equals((String) getValue("sBranchCd"))) {
                return poBranch;
            } else {
                poJSON = poBranch.openRecord((String) getValue("sBranchCd"));

                if ("success".equals((String) poJSON.get("result"))) {
                    return poBranch;
                } else {
                    poBranch.initialize();
                    return poBranch;
                }
            }
        } else {
            poBranch.initialize();
            return poBranch;
        }
    }

    public Model_Warehouse Warehouse() throws SQLException, GuanzonException {
        if (!"".equals((String) getValue("sWHouseID"))) {
            if (poWarehouse.getEditMode() == EditMode.READY
                    && poWarehouse.getWarehouseId().equals((String) getValue("sWHouseID"))) {
                return poWarehouse;
            } else {
                poJSON = poWarehouse.openRecord((String) getValue("sWHouseID"));

                if ("success".equals((String) poJSON.get("result"))) {
                    return poWarehouse;
                } else {
                    poWarehouse.initialize();
                    return poWarehouse;
                }
            }
        } else {
            poWarehouse.initialize();
            return poWarehouse;
        }
    }

    public Model_Inv_Location Location() throws SQLException, GuanzonException {
        if (!"".equals((String) getValue("sLocatnID"))) {
            if (poLocation.getEditMode() == EditMode.READY
                    && poLocation.getLocationId().equals((String) getValue("sLocatnID"))) {
                return poLocation;
            } else {
                poJSON = poLocation.openRecord((String) getValue("sLocatnID"));

                if ("success".equals((String) poJSON.get("result"))) {
                    return poLocation;
                } else {
                    poLocation.initialize();
                    return poLocation;
                }
            }
        } else {
            poLocation.initialize();
            return poLocation;
        }
    }

    public Model_Bin BinLevel() throws SQLException, GuanzonException {
        if (!"".equals((String) getValue("sBinNumbr"))) {
            if (poBinLevel.getEditMode() == EditMode.READY
                    && poBinLevel.getBinId().equals((String) getValue("sBinNumbr"))) {
                return poBinLevel;
            } else {
                poJSON = poBinLevel.openRecord((String) getValue("sBinNumbr"));

                if ("success".equals((String) poJSON.get("result"))) {
                    return poBinLevel;
                } else {
                    poBinLevel.initialize();
                    return poBinLevel;
                }
            }
        } else {
            poBinLevel.initialize();
            return poBinLevel;
        }
    }

    public Model_Inventory Inventory() throws SQLException, GuanzonException {
        if (!"".equals((String) getValue("sStockIDx"))) {
            if (poInventory.getEditMode() == EditMode.READY
                    && poInventory.getStockId().equals((String) getValue("sStockIDx"))) {
                return poInventory;
            } else {
                poJSON = poInventory.openRecord((String) getValue("sStockIDx"));

                if ("success".equals((String) poJSON.get("result"))) {
                    return poInventory;
                } else {
                    poInventory.initialize();
                    return poInventory;
                }
            }
        } else {
            poInventory.initialize();
            return poInventory;
        }
    }

    public Model_Industry Industry() throws SQLException, GuanzonException {
        if (!"".equals(getValue("sIndstCdx"))) {
            if (this.poIndustry.getEditMode() == 1 && this.poIndustry
                    .getIndustryId().equals(getValue("sIndstCdx"))) {
                return this.poIndustry;
            }
            this.poJSON = this.poIndustry.openRecord((String) getValue("sIndstCdx"));
            if ("success".equals(this.poJSON.get("result"))) {
                return this.poIndustry;
            }
            this.poIndustry.initialize();
            return this.poIndustry;
        }
        this.poIndustry.initialize();
        return this.poIndustry;
    }
    //end - reference object models
}
