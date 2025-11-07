package org.guanzon.cas.inv.model;

import java.sql.SQLException;
import java.util.Date;
import org.guanzon.appdriver.agent.services.Model;
import org.guanzon.appdriver.base.GuanzonException;
import org.guanzon.appdriver.base.MiscUtil;
import org.guanzon.appdriver.constant.EditMode;
import org.guanzon.cas.inv.services.InvModels;
import org.guanzon.cas.parameter.model.Model_Branch;
import org.guanzon.cas.parameter.model.Model_Industry;
import org.guanzon.cas.parameter.model.Model_Warehouse;
import org.guanzon.cas.parameter.model.Model_xxxTransactionSource;
import org.guanzon.cas.parameter.services.ParamModels;
import org.json.simple.JSONObject;

public class Model_Inv_Ledger extends Model {

    //reference objects
    Model_Branch poBranch;
    Model_Warehouse poWarehouse;
    Model_Inventory poInventory;
    Model_Industry poIndustry;
    Model_xxxTransactionSource poTransactionSource;

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
            //end - assign default values
            ID = "sStockIDx";
            ID2 = "sBranchCd";
            ID3 = "sWHouseID";
            ID4 = "sSourceCd";
            ID5 = "sSourceNo";

            //initialize reference objects
            ParamModels model = new ParamModels(poGRider);
            poBranch = model.Branch();
            poWarehouse = model.Warehouse();
            poTransactionSource = model.TransactionSource();
            poInventory = new InvModels(poGRider).Inventory();

            this.poIndustry = (new ParamModels(this.poGRider)).Industry();
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

    public JSONObject setLedgerNo(String ledgerNo) {
        return setValue("nLedgerNo", ledgerNo);
    }

    public int getLedgerNo() {
        return (int) getValue("nLedgerNo");
    }

    public JSONObject setTransactionDate(Date transactionDate) {
        return setValue("dTransact", transactionDate);
    }

    public Date getTransactionDate() {
        return (Date) getValue("dTransact");
    }

    public JSONObject setSourceCode(String sourceCode) {
        return setValue("sSourceCd", sourceCode);
    }

    public String getSourceCode() {
        return (String) getValue("sSourceCd");
    }

    public JSONObject setSourceNo(String sourceNumber) {
        return setValue("sSourceNo", sourceNumber);
    }

    public String getSourceNo() {
        return (String) getValue("sSourceNo");
    }

    public JSONObject setQuantityIn(int quantity) {
        return setValue("nQtyInxxx", quantity);
    }

    public Double getQuantityIn() {
        return Double.parseDouble(getValue("nQtyInxxx").toString());
    }

    public JSONObject setQuantityOut(Double quantity) {
        return setValue("nQtyOutxx", quantity);
    }

    public Double getQuantityOut() {
        return Double.parseDouble(getValue("nQtyOutxx").toString());
    }

    public JSONObject setQuantityOrder(Double quantity) {
        return setValue("nQtyOrder", quantity);
    }

    public Double getQuantityOrder() {
        return Double.parseDouble(getValue("nQtyOrder").toString());
    }

    public JSONObject setQuantityIssued(Double quantity) {
        return setValue("nQtyIssue", quantity);
    }

    public Double getQuantityIssued() {
        return Double.parseDouble(getValue("nQtyIssue").toString());
    }

    public JSONObject setPurchasePrice(Double cost) {
        return setValue("nPurPrice", cost);
    }

    public Double getPurchasePrice() {
        return Double.parseDouble(getValue("nPurPrice").toString());
    }

    public JSONObject setUnitPrice(Double unitprice) {
        return setValue("nUnitPrce", unitprice);
    }

    public Double getUnitPrice() {
        return Double.parseDouble(getValue("nUnitPrce").toString());
    }

    public JSONObject setExpirationDate(Date modifiedDate) {
        return setValue("dExpiryxx", modifiedDate);
    }

    public Date getExpirationDate() {
        return (Date) getValue("dExpiryxx");
    }

    public JSONObject setCondition(String condition) {
        return setValue("cConditnx", condition);
    }

    public String getCondition() {
        return (String) getValue("cConditnx");
    }

    public JSONObject setReverse(String reverse) {
        return setValue("cReversex", reverse);
    }

    public String getReverse() {
        return (String) getValue("cReversex");
    }

    public JSONObject isReverse(boolean reverse) {
        return setValue("cReversex", reverse ? "1" : "0");
    }

    public boolean isReverse() {
        return ((String) getValue("cReversex")).equals("1");
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
    public JSONObject openRecord(String id) {
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

    public Model_xxxTransactionSource TransactionSource() throws SQLException, GuanzonException {
        if (!"".equals(getValue("sSourceCd"))) {
            if (this.poTransactionSource.getEditMode() == 1 && this.poTransactionSource
                    .getSourceCode().equals(getValue("sSourceCd"))) {
                return this.poTransactionSource;
            }
            this.poJSON = this.poTransactionSource.openRecord((String) getValue("sSourceCd"));
            if ("success".equals(this.poJSON.get("result"))) {
                return this.poTransactionSource;
            }
            this.poTransactionSource.initialize();
            return this.poTransactionSource;
        }
        this.poTransactionSource.initialize();
        return this.poTransactionSource;
    }
    //end - reference object models
}
