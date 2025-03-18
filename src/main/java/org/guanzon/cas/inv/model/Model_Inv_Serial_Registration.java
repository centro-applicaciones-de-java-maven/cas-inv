package org.guanzon.cas.inv.model;

import java.sql.SQLException;
import java.util.Date;
import org.guanzon.appdriver.agent.services.Model;
import org.guanzon.appdriver.base.GuanzonException;
import org.guanzon.appdriver.base.MiscUtil;
import org.guanzon.appdriver.constant.EditMode;
import org.guanzon.cas.inv.services.InvModels;
import org.json.simple.JSONObject;

public class Model_Inv_Serial_Registration extends Model {
    private Model_Inv_Serial poSerial;

    @Override
    public void initialize() {
        try {
            poEntity = MiscUtil.xml2ResultSet(System.getProperty("sys.default.path.metadata") + XML, getTable());

            poEntity.last();
            poEntity.moveToInsertRow();

            MiscUtil.initRowSet(poEntity);

            //assign default values
            poEntity.updateObject("dRegister", "1900-01-01");
            poEntity.updateObject("cScannedx", "0");
            poEntity.updateObject("nYearModl", "1900");
            //end - assign default values

             poEntity.insertRow();
            poEntity.moveToCurrentRow();

            poEntity.absolute(1);

            ID = "sSerialID";
            
            //initialize other connections
            InvModels inv = new InvModels(poGRider);
            poSerial = inv.InventorySerial();
            
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

    public JSONObject setFileNumber(String fileNumber) {
        return setValue("sFileNoxx", fileNumber);
    }

    public String getFileNumber() {
        return (String) getValue("sFileNoxx");
    }
    
    public JSONObject setRegistrationORNo(String registrationORNo) {
        return setValue("sRegORNox", registrationORNo);
    }

    public String getRegistrationORNo() {
        return (String) getValue("sRegORNox");
    }

    public JSONObject setCRENumber(String creNumber) {
        return setValue("sCRENoxxx", creNumber);
    }

    public String getCRENumber() {
        return (String) getValue("sCRENoxxx");
    }

    public JSONObject setCRNumber(String crNumber) {
        return setValue("sCRNoxxxx", crNumber);
    }

    public String getCRNumber() {
        return (String) getValue("sCRNoxxxx");
    }

    public JSONObject setPlateNoP(String plateNo) {
        return setValue("sPlateNoP", plateNo);
    }

    public String getPlateNoP() {
        return (String) getValue("sPlateNoP");
    }

    public JSONObject setPlateNoH(String plateNo) {
        return setValue("sPlateNoH", plateNo);
    }

    public String getPlateNoH() {
        return (String) getValue("sPlateNoH");
    }

    public JSONObject setStickerNo(String stickerNo) {
        return setValue("sStickrNo", stickerNo);
    }

    public String getStickerNo() {
        return (String) getValue("sStickrNo");
    }
    
    public JSONObject setYearModel(String yearModel) {
        return setValue("nYearModl", yearModel);
    }

    public int getYearModel() {
        return (int) getValue("nYearModl");
    }

    public JSONObject setRegistrationDate(Date registrationDate) {
        return setValue("dRegister", registrationDate);
    }

    public Date getRegistrationDate() {
        return (Date) getValue("dRegister");
    }
    
    public JSONObject setCRLocation(String crLocation) {
        return setValue("sLocatnCR", crLocation);
    }

    public String getCRLocation() {
        return (String) getValue("sLocatnCR");
    }

    public JSONObject setScanned(boolean isScanned) {
        return setValue("cScannedx", isScanned ? "1" : "0");
    }

    public boolean getScanned() {
        return "1".equals((String) getValue("cScannedx")) ;
    }

    public JSONObject setPNPClearanceNo(String pnpClearanceno) {
        return setValue("sPNPClrNo", pnpClearanceno);
    }

    public String getPNPClearanceNo() {
        return (String) getValue("sPNPClrNo");
    }
    
    public JSONObject setCSRNumber(String csrNumber) {
        return setValue("sCSRValNo", csrNumber);
    }

    public String getCSRNumber() {
        return (String) getValue("sCSRValNo");
    }
    
    public JSONObject setRegistrationApplicationNo(String regAppNumber) {
        return setValue("sRegAppNo", regAppNumber);
    }

    public String getRegistrationApplicationNo() {
        return (String) getValue("sRegAppNo");
    }

    @Override
    public String getNextCode() {
        return "";
    }
    
    public Model_Inv_Serial InvSerial() throws SQLException, GuanzonException{
        if (!"".equals((String) getValue("sSerialID"))) {
            if (poSerial.getEditMode() == EditMode.READY
                    && poSerial.getStockId().equals((String) getValue("sSerialID"))) {
                return poSerial;
            } else {
                poJSON = poSerial.openRecord((String) getValue("sSerialID"));

                if ("success".equals((String) poJSON.get("result"))) {
                    return poSerial;
                } else {
                    poSerial.initialize();
                    return poSerial;
                }
            }
        } else {
            poSerial.initialize();
            return poSerial;
        }
    }
    //todo: connection to clients object
}
