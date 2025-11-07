package org.guanzon.cas.inv.model;

import java.sql.SQLException;
import java.util.Date;
import org.guanzon.appdriver.agent.services.Model;
import org.guanzon.appdriver.base.GuanzonException;
import org.guanzon.appdriver.base.MiscUtil;
import org.guanzon.appdriver.constant.EditMode;
import org.guanzon.appdriver.constant.Logical;
import org.guanzon.cas.inv.services.InvModels;
import org.guanzon.cas.parameter.model.Model_Branch;
import org.guanzon.cas.parameter.services.ParamModels;
import org.json.simple.JSONObject;

public class Model_Inv_Serial extends Model {

    private Model_Inventory poInventory;
    private Model_Branch poBranch;

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
            poEntity.updateNull("sBranchCd");
            poEntity.updateNull("sWHouseID");
            poEntity.updateNull("sClientID");
            poEntity.updateNull("sSerial01");
            poEntity.updateNull("sSerial02");
            poEntity.updateObject("nUnitPrce", 0.00d);
            poEntity.updateNull("sStockIDx");
            poEntity.updateObject("cLocation", Logical.NO);
            poEntity.updateObject("cSoldStat", Logical.NO);
            poEntity.updateObject("cUnitType", Logical.NO);
            poEntity.updateObject("cConditnx", Logical.NO);
            poEntity.updateNull("sCompnyID");
            poEntity.updateNull("sWarranty");
            poEntity.updateNull("sPayloadx");
            //end - assign default values

            ID = "sSerialID";
            ID2 = "sBranchCd";
            //initialize other connections

            InvModels inv = new InvModels(poGRider);
            poInventory = inv.Inventory();

            ParamModels model = new ParamModels(poGRider);
            poBranch = model.Branch();

            pnEditMode = EditMode.UNKNOWN;
        } catch (SQLException e) {
            logwrapr.severe(e.getMessage());
            System.exit(1);
        }
    }

    public JSONObject setSerialId(String serialId) {
        return setValue("sSerialID", serialId);
    }

    public String getSerialId() {
        return (String) getValue("sSerialID");
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

    public JSONObject setClientId(String branchCode) {
        return setValue("sClientID", branchCode);
    }

    public String getClientId() {
        return (String) getValue("sClientID");
    }

    public JSONObject setSerial01(String serialNumber) {
        return setValue("sSerial01", serialNumber);
    }

    public String getSerial01() {
        return (String) getValue("sSerial01");
    }

    public JSONObject setSerial02(String serialNumber) {
        return setValue("sSerial02", serialNumber);
    }

    public String getSerial02() {
        return (String) getValue("sSerial02");
    }

    public JSONObject setUnitPrice(String unitPrice) {
        return setValue("nUnitPrce", unitPrice);
    }

    public double getUnitPrice() {
        return (double) getValue("nUnitPrce");
    }

    public JSONObject setStockId(String stockId) {
        return setValue("sStockIDx", stockId);
    }

    public String getStockId() {
        return (String) getValue("sStockIDx");
    }

    public JSONObject setLedgerNo(String ledgerNo) {
        return setValue("nLedgerNo", ledgerNo);
    }

    public String getLedgerNo() {
        return (String) getValue("nLedgerNo");
    }

    public JSONObject setLocation(String location) {
        return setValue("cLocation", location);
    }

    public String getLocation() {
        return (String) getValue("cLocation");
    }

    public JSONObject setSoldStatus(String soldStatus) {
        return setValue("cSoldStat", soldStatus);
    }

    public String getSoldStatus() {
        return (String) getValue("cSoldStat");
    }
    
     public JSONObject isSold(boolean isSold) {
        return setValue("cSoldStat", isSold ? "1" : "0");
    }

    public boolean isSold() {
        return ((String) getValue("cSoldStat")).equals("1");
    }

    public JSONObject setUnitType(String unitType) {
        return setValue("cUnitType", unitType);
    }

    public String getUnitType() {
        return (String) getValue("cUnitType");
    }

    public JSONObject setCompnyId(String companyId) {
        return setValue("sCompnyID", companyId);
    }

    public String getCompnyId() {
        return (String) getValue("sCompnyID");
    }

    public JSONObject setWarranty(String warrantyNumber) {
        return setValue("sWarranty", warrantyNumber);
    }

    public String getWarranty() {
        return (String) getValue("sWarranty");
    }

    public JSONObject setCondition(String condition) {
        return setValue("cConditnx", condition);
    }

    public String getCondition() {
        return (String) getValue("cConditnx");
    }

    public JSONObject setPayLoad(String payLoad) {
        return setValue("sPayloadx", payLoad);
    }

    public String getPayLoad() {
        return (String) getValue("sPayloadx");
    }

    public JSONObject setModifiedDate(Date modifiedDate) {
        return setValue("dModified", modifiedDate);
    }

    public Date getModifiedDate() {
        return (Date) getValue("dModified");
    }

    @Override
    public String getNextCode() {
        return MiscUtil.getNextCode(getTable(), ID, true, poGRider.getGConnection().getConnection(), poGRider.getBranchCode());
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

    //todo: connection to clients object
}
