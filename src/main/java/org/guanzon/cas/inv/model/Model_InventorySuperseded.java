package org.guanzon.cas.inv.model;

import java.sql.SQLException;
import java.util.Date;
import org.guanzon.appdriver.agent.services.Model;
import org.guanzon.appdriver.base.GuanzonException;
import org.guanzon.appdriver.base.MiscUtil;
import org.guanzon.appdriver.constant.EditMode;
import org.guanzon.appdriver.constant.Logical;
import org.guanzon.appdriver.constant.RecordStatus;
import org.guanzon.cas.inv.services.InvModels;
import org.guanzon.cas.parameter.model.Model_Brand;
import org.guanzon.cas.parameter.model.Model_Category;
import org.guanzon.cas.parameter.model.Model_Category_Level2;
import org.guanzon.cas.parameter.model.Model_Category_Level3;
import org.guanzon.cas.parameter.model.Model_Category_Level4;
import org.guanzon.cas.parameter.model.Model_Color;
import org.guanzon.cas.parameter.model.Model_Industry;
import org.guanzon.cas.parameter.model.Model_Inv_Type;
import org.guanzon.cas.parameter.model.Model_Measure;
import org.guanzon.cas.parameter.model.Model_Model;
import org.guanzon.cas.parameter.model.Model_Model_Variant;
import org.guanzon.cas.parameter.services.ParamModels;
import org.json.simple.JSONObject;

public class Model_InventorySuperseded extends Model {

    private Model_Industry poIndustry;
    private Model_Category poCategory;
    private Model_Category_Level2 poCategoryLevel2;
    private Model_Category_Level3 poCategoryLevel3;
    private Model_Category_Level4 poCategoryLevel4;
    private Model_Brand poBrand;
    private Model_Model poModel;
    private Model_Color poColor;
    private Model_Measure poMeasure;
    private Model_Inv_Type poInventoryType;
    private Model_Model_Variant poVariant;

    @Override
    public void initialize() {
        try {
            poEntity = MiscUtil.xml2ResultSet(System.getProperty("sys.default.path.metadata") + XML, getTable());

            poEntity.last();
            poEntity.moveToInsertRow();

            MiscUtil.initRowSet(poEntity);

            //assign default values
            poEntity.updateObject("nUnitPrce", 0.00);
            poEntity.updateObject("nSelPrice", 0.00);
            poEntity.updateObject("nDiscLev1", 0.00);
            poEntity.updateObject("nDiscLev2", 0.00);
            poEntity.updateObject("nDiscLev3", 0.00);
            poEntity.updateObject("nDealrDsc", 0.00);
            poEntity.updateObject("nMinLevel", 0);
            poEntity.updateObject("nMaxLevel", 0);
            poEntity.updateObject("nShlfLife", 0);
            poEntity.updateString("cComboInv", Logical.NO);
            poEntity.updateString("cWthPromo", Logical.NO);
            poEntity.updateString("cSerialze", Logical.NO);
            poEntity.updateString("cUnitType", Logical.NO);
            poEntity.updateString("cInvStatx", RecordStatus.ACTIVE);
            poEntity.updateString("cRecdStat", RecordStatus.ACTIVE);
            //end - assign default values

            poEntity.insertRow();
            poEntity.moveToCurrentRow();

            poEntity.absolute(1);

            ID = poEntity.getMetaData().getColumnLabel(1);

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

    public JSONObject setBarCode(String barCode) {
        return setValue("sBarCodex", barCode);
    }

    public String getBarCode() {
        return (String) getValue("sBarCodex");
    }

    public JSONObject setDescription(String description) {
        return setValue("sDescript", description);
    }

    public String getDescription() {
        return (String) getValue("sDescript");
    }

    public JSONObject setBriefDescription(String briefDescription) {
        return setValue("sBriefDsc", briefDescription);
    }

    public String getBriefDescription() {
        return (String) getValue("sBriefDsc");
    }

    public JSONObject setAlternateBarCode(String alternateBarCode) {
        return setValue("sAltBarCd", alternateBarCode);
    }

    public String getAlternateBarCode() {
        return (String) getValue("sAltBarCd");
    }

    public String getIndustryCode() {
        return (String) getValue("sIndstCdx");
    }

    public String getRecordStatus() {
        return (String) getValue("cRecdStat");
    }

    public boolean isRecordActive() {
        return ((String) getValue("cRecdStat")).equals("1");
    }

    public String getModifyingId() {
        return (String) getValue("sModified");
    }

    public Date getModifiedDate() {
        return (Date) getValue("dModified");
    }

    @Override
    public String getNextCode() {
        return "";
    }

}
