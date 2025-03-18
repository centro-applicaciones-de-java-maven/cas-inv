package org.guanzon.cas.inv.model;

import java.sql.SQLException;
import java.util.Date;
import org.guanzon.appdriver.agent.services.Model;
import org.guanzon.appdriver.base.GuanzonException;
import org.guanzon.appdriver.base.MiscUtil;
import org.guanzon.appdriver.constant.EditMode;
import org.guanzon.cas.parameter.services.ParamModels;
import org.guanzon.appdriver.constant.RecordStatus;
import org.guanzon.cas.inv.services.InvModels;
import org.guanzon.cas.parameter.model.Model_Branch;
import org.json.simple.JSONObject;

public class Model_Inv_Serial_Ledger extends Model {
    private Model_Inv_Serial poInventorySerial;
    private Model_Branch poBranch;

    @Override
    public void initialize() {
        try {
            poEntity = MiscUtil.xml2ResultSet(System.getProperty("sys.default.path.metadata") + XML, getTable());

            poEntity.last();
            poEntity.moveToInsertRow();

            MiscUtil.initRowSet(poEntity);

            //assign default values
            poEntity.updateString("cSoldStat", RecordStatus.INACTIVE);
            //end - assign default values

             poEntity.insertRow();
            poEntity.moveToCurrentRow();

            poEntity.absolute(1);

            ID = "sSerialID";
            ID2 = "sBranchCd";
            ID3 = "sSourceCd";
            ID4 = "sSourceNo";
            
            //initialize other connections
            InvModels inv = new InvModels(poGRider);
            poInventorySerial = inv.InventorySerial();
            
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

    public JSONObject setLedgerNo(int ledgerNo) {
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
    
    public JSONObject setSourceNo(String sourceNo) {
        return setValue("sSourceNo", sourceNo);
    }

    public String getSourceNo() {
        return (String) getValue("sSourceNo");
    }

    public JSONObject setSoldStatus(String soldStatus) {
        return setValue("cSoldStat", soldStatus);
    }

    public String getSoldStatus() {
        return (String) getValue("cSoldStat");
    }

    public JSONObject setLocation(String location) {
        return setValue("cLocation", location);
    }

    public String getLocation() {
        return (String) getValue("cLocation");
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
    
    public Model_Inv_Serial InventorySerial() throws SQLException, GuanzonException{
        if (!"".equals((String) getValue("sSerialID"))) {
            if (poInventorySerial.getEditMode() == EditMode.READY
                    && poInventorySerial.getStockId().equals((String) getValue("sSerialID"))) {
                return poInventorySerial;
            } else {
                poJSON = poInventorySerial.openRecord((String) getValue("sSerialID"));

                if ("success".equals((String) poJSON.get("result"))) {
                    return poInventorySerial;
                } else {
                    poInventorySerial.initialize();
                    return poInventorySerial;
                }
            }
        } else {
            poInventorySerial.initialize();
            return poInventorySerial;
        }
    }
    public Model_Branch Branch() throws SQLException, GuanzonException{
            System.out.println("here is branch code == " + (String) getValue("sBranchCd"));
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
}
