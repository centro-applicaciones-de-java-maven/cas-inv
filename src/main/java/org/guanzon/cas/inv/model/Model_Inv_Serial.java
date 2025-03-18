package org.guanzon.cas.inv.model;

import java.sql.SQLException;
import java.util.Date;
import org.guanzon.appdriver.agent.services.Model;
import org.guanzon.appdriver.base.GuanzonException;
import org.guanzon.appdriver.base.MiscUtil;
import org.guanzon.appdriver.constant.EditMode;
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

            //assign default values
            poEntity.updateObject("nUnitPrce", 0.00);
            poEntity.updateObject("cLocation", "0");
            poEntity.updateObject("cSoldStat", "0");
            poEntity.updateObject("cUnitType", "1");
            //end - assign default values

             poEntity.insertRow();
            poEntity.moveToCurrentRow();

            poEntity.absolute(1);

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

    public JSONObject setBranchCode(String branchCode) {
        return setValue("sBranchCd", branchCode);
    }

    public String getBranchCode() {
        return (String) getValue("sBranchCd");
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
    
    public Model_Inventory Inventory() throws SQLException, GuanzonException{
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
    
    public Model_Branch Branch() throws SQLException, GuanzonException{
            System.out.println("Branch == " + (String) getValue("sBranchCd"));
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
