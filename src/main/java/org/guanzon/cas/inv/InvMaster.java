package org.guanzon.cas.inv;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.guanzon.appdriver.agent.ShowDialogFX;
import org.guanzon.appdriver.agent.services.Model;
import org.guanzon.appdriver.agent.services.Parameter;
import org.guanzon.appdriver.base.GuanzonException;
import org.guanzon.appdriver.base.MiscUtil;
import org.guanzon.appdriver.base.SQLUtil;
import org.guanzon.appdriver.constant.RecordStatus;
import org.guanzon.appdriver.constant.UserRight;
import org.guanzon.cas.inv.model.Model_Inv_Ledger;
import org.guanzon.cas.inv.model.Model_Inv_Master;
import org.guanzon.cas.inv.model.Model_Inv_Serial;
import org.guanzon.cas.inv.model.Model_Inventory;
import org.guanzon.cas.inv.services.InvModels;
import org.guanzon.cas.parameter.model.Model_Bin;
import org.guanzon.cas.parameter.model.Model_Inv_Location;
import org.guanzon.cas.parameter.services.ParamModels;
import org.json.simple.JSONObject;

public class InvMaster extends Parameter {

    //object model
    Model_Inv_Master poModel;
    private List<Model> paRecordSerial;
    private List<Model> paRecordLedger;

    private String psIndustryCode = "";
    private String psCategoryCode = "";
    private String psApprovalUser = "";

    public void setIndustryID(String industryId) {
        psIndustryCode = industryId;
    }

    String psBranchCd;

    public void setBranchCode(String branchCode) {
        psBranchCd = branchCode;
    }

    public void setCategory(String categoryid) {
        psCategoryCode = categoryid;
    }

    @Override
    public void initialize() throws SQLException, GuanzonException {

        poModel = new InvModels(poGRider).InventoryMaster();
        paRecordSerial = new ArrayList<Model>();
        paRecordLedger = new ArrayList<Model>();
        super.initialize();
    }

    @Override
    public JSONObject isEntryOkay() throws SQLException {
        poJSON = new JSONObject();

//        if (poGRider.getUserLevel() < UserRight.SYSADMIN) {
//            poJSON.put("result", "error");
//            poJSON.put("message", "User is not allowed to save record.");
//            return poJSON;
//        } else {
        poJSON = new JSONObject();

        if (poModel.getStockId().isEmpty()) {
            poJSON.put("result", "error");
            poJSON.put("message", "Item must not be empty.");
            return poJSON;
        }

        if (poModel.getBranchCode().isEmpty()) {
            poJSON.put("result", "error");
            poJSON.put("message", "Branch location must not be empty.");
            return poJSON;
        }

        if (poModel.getLocationId() == null || poModel.getLocationId().isEmpty()) {
            poJSON.put("result", "error");
            poJSON.put("message", "Location must not be empty.");
            return poJSON;
        }
        //todo:
        //  more validations/use of validators per category
        poModel.setModifyingId(poGRider.Encrypt(poGRider.getUserID()));
        poModel.setModifiedDate(poGRider.getServerDate());
//            poModel.setIndustryCode(psIndustryCode);
//        }

        poJSON.put("result", "success");
        return poJSON;
    }

    @SuppressWarnings("unchecked")
    public List<Model_Inv_Ledger> getLedgerList() {
        return (List<Model_Inv_Ledger>) (List<?>) paRecordLedger;
    }

    @SuppressWarnings("unchecked")
    public List<Model_Inv_Serial> getSerialList() {
        return (List<Model_Inv_Serial>) (List<?>) paRecordSerial;
    }

    @Override
    public Model_Inv_Master getModel() {
        return poModel;
    }

    @Override
    public JSONObject searchRecord(String value, boolean byCode) throws SQLException, GuanzonException {
        String lsSQL = getSQ_Browse();

        poJSON = ShowDialogFX.Search(poGRider,
                lsSQL,
                value,
                "Barcode»Description»Brand»Model»UOM»QOH",
                "sBarCodex»sDescript»xBrandNme»xModelNme»xMeasurNm»xQtyOnHnd",
                "a.sBarCodex»a.sDescript»IFNULL(b.sDescript, '')»IFNULL(c.sDescript, '')»IFNULL(e.sDescript, '')»b.nQtyOnHnd",
                byCode ? 0 : 1);

        if (poJSON != null) {
            return poModel.openRecord((String) poJSON.get("sStockIDx"), (String) poJSON.get("xBranchCd"));
        } else {
            poJSON = new JSONObject();
            poJSON.put("result", "error");
            poJSON.put("message", "No record loaded.");
            return poJSON;
        }
    }

    public JSONObject searchRecord(String value, boolean byCode, String supplierId) throws SQLException, GuanzonException {
        String lsSQL = getSQ_Browse();

        if (supplierId != null) {
            lsSQL = MiscUtil.addCondition(lsSQL, "h.sSupplier = " + SQLUtil.toSQL(supplierId));
        }

        poJSON = ShowDialogFX.Search(poGRider,
                lsSQL,
                value,
                "Barcode»Description»Brand»Model»UOM»QOH",
                "sBarCodex»sDescript»xBrandNme»xModelNme»xMeasurNm»xQtyOnHnd",
                "a.sBarCodex»a.sDescript»IFNULL(b.sDescript, '')»IFNULL(c.sDescript, '')»IFNULL(e.sDescript, '')»b.nQtyOnHnd",
                byCode ? 0 : 1);

        if (poJSON != null) {
            return poModel.openRecord((String) poJSON.get("sStockIDx"), (String) poJSON.get("xBranchCd"));
        } else {
            poJSON = new JSONObject();
            poJSON.put("result", "error");
            poJSON.put("message", "No record loaded.");
            return poJSON;
        }
    }

    public JSONObject searchRecord(String value, boolean byCode, String supplierId, String industryCode) throws SQLException, GuanzonException {
        String lsSQL = getSQ_Browse();

        if (supplierId != null) {
            lsSQL = MiscUtil.addCondition(lsSQL, "h.sSupplier = " + SQLUtil.toSQL(supplierId));
        }

        if (industryCode != null) {
            lsSQL = MiscUtil.addCondition(lsSQL, "a.sIndstCdx = " + SQLUtil.toSQL(industryCode));
        }

        poJSON = ShowDialogFX.Search(poGRider,
                lsSQL,
                value,
                "Barcode»Description»Brand»Model»UOM»QOH",
                "sBarCodex»sDescript»xBrandNme»xModelNme»xMeasurNm»xQtyOnHnd",
                "a.sBarCodex»a.sDescript»IFNULL(b.sDescript, '')»IFNULL(c.sDescript, '')»IFNULL(e.sDescript, '')»b.nQtyOnHnd",
                byCode ? 0 : 1);

        if (poJSON != null) {
            return poModel.openRecord((String) poJSON.get("sStockIDx"), (String) poJSON.get("xBranchCd"));
        } else {
            poJSON = new JSONObject();
            poJSON.put("result", "error");
            poJSON.put("message", "No record loaded.");
            return poJSON;
        }
    }

    public JSONObject searchRecord(String value, boolean byCode, String supplierId, String industryCode, String brandId) throws SQLException, GuanzonException {
        String lsSQL = getSQ_Browse();

        if (supplierId != null) {
            lsSQL = MiscUtil.addCondition(lsSQL, "h.sSupplier = " + SQLUtil.toSQL(supplierId));
        }

        if (industryCode != null) {
            lsSQL = MiscUtil.addCondition(lsSQL, "a.sIndstCdx = " + SQLUtil.toSQL(industryCode));
        }

        if (brandId != null) {
            lsSQL = MiscUtil.addCondition(lsSQL, "a.sBrandIDx = " + SQLUtil.toSQL(brandId));
        }

        poJSON = ShowDialogFX.Search(poGRider,
                lsSQL,
                value,
                "Barcode»Description»Brand»Model»UOM»QOH",
                "sBarCodex»sDescript»xBrandNme»xModelNme»xMeasurNm»xQtyOnHnd",
                "a.sBarCodex»a.sDescript»IFNULL(b.sDescript, '')»IFNULL(c.sDescript, '')»IFNULL(e.sDescript, '')»b.nQtyOnHnd",
                byCode ? 0 : 1);

        if (poJSON != null) {
            return poModel.openRecord((String) poJSON.get("sStockIDx"), (String) poJSON.get("xBranchCd"));
        } else {
            poJSON = new JSONObject();
            poJSON.put("result", "error");
            poJSON.put("message", "No record loaded.");
            return poJSON;
        }
    }

    public JSONObject searchRecordOfVariants(String value, boolean byCode) throws SQLException, GuanzonException {
        String lsSQL = getSQ_Browse();

        poJSON = ShowDialogFX.Search(poGRider,
                lsSQL,
                value,
                "Brand»Model»Variant»Code»Color»QOH",
                "xBrandNme»xModelNme»xVrntName»xModelCde»xColorNme»xQtyOnHnd",
                "IFNULL(b.sDescript, '')»IFNULL(c.sDescript, '')»TRIM(CONCAT(IFNULL(f.sDescript, ''), ' ', IFNULL(f.nYearMdlx, '')))»IFNULL(c.sModelCde, '') xModelCde»IFNULL(d.sDescript, '')»b.nQtyOnHnd",
                byCode ? 0 : 1);

        if (poJSON != null) {
            return poModel.openRecord((String) poJSON.get("sStockIDx"), (String) poJSON.get("xBranchCd"));
        } else {
            poJSON = new JSONObject();
            poJSON.put("result", "error");
            poJSON.put("message", "No record loaded.");
            return poJSON;
        }
    }

    public JSONObject searchRecordOfVariants(String value, boolean byCode, String supplierId) throws SQLException, GuanzonException {
        String lsSQL = getSQ_Browse();

        if (supplierId != null) {
            lsSQL = MiscUtil.addCondition(lsSQL, "h.sSupplier = " + SQLUtil.toSQL(supplierId));
        }

        poJSON = ShowDialogFX.Search(poGRider,
                lsSQL,
                value,
                "Brand»Model»Variant»Code»Color»QOH",
                "xBrandNme»xModelNme»xVrntName»xModelCde»xColorNme»xQtyOnHnd",
                "IFNULL(b.sDescript, '')»IFNULL(c.sDescript, '')»TRIM(CONCAT(IFNULL(f.sDescript, ''), ' ', IFNULL(f.nYearMdlx, '')))»IFNULL(c.sModelCde, '') xModelCde»IFNULL(d.sDescript, '')»b.nQtyOnHnd",
                byCode ? 0 : 1);

        if (poJSON != null) {
            return poModel.openRecord((String) poJSON.get("sStockIDx"), (String) poJSON.get("xBranchCd"));
        } else {
            poJSON = new JSONObject();
            poJSON.put("result", "error");
            poJSON.put("message", "No record loaded.");
            return poJSON;
        }
    }

    @Override
    public String getSQ_Browse() {
        String lsCondition = "";

        if (psRecdStat.length() > 1) {
            for (int lnCtr = 0; lnCtr <= psRecdStat.length() - 1; lnCtr++) {
                lsCondition += ", " + SQLUtil.toSQL(Character.toString(psRecdStat.charAt(lnCtr)));
            }

            lsCondition = "a.cRecdStat IN (" + lsCondition.substring(2) + ")";
        } else {
            lsCondition = "a.cRecdStat = " + SQLUtil.toSQL(psRecdStat);
        }

        String lsSQL = "SELECT"
                + "  a.sStockIDx"
                + ", a.sBarCodex"
                + ", a.sDescript"
                + ", a.sBriefDsc"
                + ", a.sAltBarCd"
                + ", a.sCategCd1"
                + ", a.sCategCd2"
                + ", a.sCategCd3"
                + ", a.sCategCd4"
                + ", a.sBrandIDx"
                + ", a.sModelIDx"
                + ", a.sColorIDx"
                + ", a.sVrntIDxx"
                + ", a.sMeasurID"
                + ", a.sInvTypCd"
                + ", a.sIndstCdx"
                + ", a.nUnitPrce"
                + ", a.nSelPrice"
                + ", a.nDiscLev1"
                + ", a.nDiscLev2"
                + ", a.nDiscLev3"
                + ", a.nDealrDsc"
                + ", a.nMinLevel"
                + ", a.nMaxLevel"
                + ", a.cComboInv"
                + ", a.cWthPromo"
                + ", a.cSerialze"
                + ", a.cUnitType"
                + ", a.cInvStatx"
                + ", a.nShlfLife"
                + ", a.sSupersed"
                + ", a.cRecdStat"
                + ", a.sModified"
                + ", a.dModified"
                + ", IFNULL(b.sDescript, '') xBrandNme"
                + ", IFNULL(c.sDescript, '') xModelNme"
                + ", IFNULL(d.sDescript, '') xColorNme"
                + ", IFNULL(e.sDescript, '') xMeasurNm"
                + ", TRIM(CONCAT(IFNULL(f.sDescript, ''), ' ', IFNULL(f.nYearMdlx, ''))) xVrntName"
                + ", IFNULL(c.sModelCde, '') xModelCde"
                + ", g.nQtyOnHnd xQtyOnHnd"
                + ", g.sBranchCd xBranchCd"
                + " FROM Inventory a"
                + " LEFT JOIN Brand b ON a.sBrandIDx = b.sBrandIDx"
                + " LEFT JOIN Model c ON a.sModelIDx = c.sModelIDx"
                + " LEFT JOIN Color d ON a.sColorIDx = d.sColorIDx"
                + " LEFT JOIN Measure e ON a.sMeasurID = e.sMeasurID"
                + " LEFT JOIN Model_Variant f ON a.sVrntIDxx = f.sVrntIDxx"
                + " LEFT JOIN Inv_Supplier h ON a.sStockIDx = h.sStockIDx"
                + ", Inv_Master g"
                + " WHERE a.sStockIDx = g.sStockIDx";

        if (psBranchCd != null) {
            if (!psBranchCd.isEmpty()) {
                lsSQL = lsSQL + " AND g.sBranchCd = " + SQLUtil.toSQL(psBranchCd);
            }
        }
        return MiscUtil.addCondition(lsSQL, lsCondition);
    }

    public JSONObject searchLocation(String value, boolean byExact, boolean byCode) throws SQLException, GuanzonException {
        Model_Inv_Location loBrowse = new ParamModels(poGRider).InventoryLocation();
        loBrowse.initialize();
        String lsSQL = "SELECT * FROM " + loBrowse.getTable() + " a"
                + " LEFT JOIN Warehouse b ON a.sWHouseID = b.sWHouseID"
                + " LEFT JOIN Section c ON a.sSectnIDx = c.sSectnIDx"
                + " WHERE a.cRecdStat = " + SQLUtil.toSQL(RecordStatus.ACTIVE)
                + " AND b.cRecdStat = " + SQLUtil.toSQL(RecordStatus.ACTIVE)
                + " AND c.cRecdStat = " + SQLUtil.toSQL(RecordStatus.ACTIVE);

        System.out.println("Search Record Query : " + lsSQL);
        poJSON = new JSONObject();
        poJSON = ShowDialogFX.Search(poGRider,
                lsSQL,
                value,
                "ID»Location»Warehouse»Section",
                "a.sLocatnID»a.sDescript»b.sWHouseNm»c.sSectnNme",
                "a.sLocatnID»a.sDescript»b.sWHouseNm»c.sSectnNme",
                byExact ? (byCode ? 0 : 1) : (byCode ? 2 : 3));

        if (poJSON != null) {
            poJSON = loBrowse.openRecord((String) this.poJSON.get("sLocatnID"));
            System.out.println("result " + (String) poJSON.get("result"));

            if ("success".equals((String) poJSON.get("result"))) {

                this.poJSON = new JSONObject();
                this.poJSON.put("result", "success");
                getModel().setLocationId(loBrowse.getLocationId());
                getModel().setWarehouseId(loBrowse.getWarehouseId());
                return poJSON;
            }

        }
        this.poJSON = new JSONObject();
        this.poJSON.put("result", "error");
        this.poJSON.put("message", "No record loaded.");
        return this.poJSON;

    }

    public JSONObject searchBinLevel(String value, boolean byCode) throws SQLException, GuanzonException {
        Model_Bin loBrowse = new ParamModels(poGRider).Bin();
        loBrowse.initialize();
        String lsSQL = "SELECT * FROM " + loBrowse.getTable() + " WHERE cRecdStat = " + SQLUtil.toSQL(RecordStatus.ACTIVE);

        System.out.println("Search Record Query : " + lsSQL);
        poJSON = new JSONObject();
        poJSON = ShowDialogFX.Search(poGRider,
                lsSQL,
                value,
                "ID»Name",
                "sBinIDxxx»sBinNamex",
                "sBinIDxxx»sBinNamex",
                byCode ? 0 : 1);

        if (poJSON != null) {
            poJSON = loBrowse.openRecord((String) this.poJSON.get("sBinIDxxx"));
            System.out.println("result " + (String) poJSON.get("result"));

            if ("success".equals((String) poJSON.get("result"))) {

                this.poJSON = new JSONObject();
                this.poJSON.put("result", "success");
                getModel().setBinId(loBrowse.getBinId());
                return poJSON;
            }

        }
        this.poJSON = new JSONObject();
        this.poJSON.put("result", "error");
        this.poJSON.put("message", "No record loaded.");
        return this.poJSON;

    }

    public JSONObject searchRecordInventoryMaster(String value, boolean byCode) throws SQLException, GuanzonException {
        String lsSQL = getSQ_BrowseInventory();

        if (psIndustryCode != null) {
            if (!psIndustryCode.isEmpty()) {
                lsSQL = MiscUtil.addCondition(lsSQL, "a.sIndstCdx = " + SQLUtil.toSQL(psIndustryCode));
            }
        }

        if (psCategoryCode != null) {
            if (!psCategoryCode.isEmpty()) {
                lsSQL = MiscUtil.addCondition(lsSQL, "a.sCategCd1 = " + SQLUtil.toSQL(psCategoryCode));
            }
        }
        JSONObject loJSON = new JSONObject();
        System.out.println("Search Inventory Master Record Query : " + lsSQL);
        poJSON = ShowDialogFX.Search(poGRider,
                lsSQL,
                value,
                "Barcode»Description»Brand»Model»UOM",
                "sBarCodex»sDescript»xBrandNme»xModelNme»xMeasurNm",
                "a.sBarCodex»a.sDescript»IFNULL(b.sDescript, '')»IFNULL(c.sDescript, '')»IFNULL(e.sDescript, '')",
                byCode ? 0 : 1);

        if (poJSON != null) {
            String stockID = (String) poJSON.get("sStockIDx");
            loJSON = poModel.openRecord(stockID, poGRider.getBranchCode());
            if (!"success".equals(loJSON.get("result"))) {
                loJSON = new JSONObject();
                loJSON = newRecord();
                getModel().setStockId(stockID);
                getModel().setBranchCode(poGRider.getBranchCode());
                getModel().setIndustryCode(psIndustryCode);
            }
            return loJSON;
        } else {
            poJSON = new JSONObject();
            poJSON.put("result", "error");
            poJSON.put("message", "No record loaded.");
            return poJSON;
        }
    }

    public String getSQ_BrowseInventory() {
        String lsCondition = "";

        if (psRecdStat.length() > 1) {
            for (int lnCtr = 0; lnCtr <= psRecdStat.length() - 1; lnCtr++) {
                lsCondition += ", " + SQLUtil.toSQL(Character.toString(psRecdStat.charAt(lnCtr)));
            }

            lsCondition = "a.cRecdStat IN (" + lsCondition.substring(2) + ")";
        } else {
            lsCondition = "a.cRecdStat = " + SQLUtil.toSQL(psRecdStat);
        }

        String lsSQL = "SELECT"
                + "  a.sStockIDx"
                + ", a.sBarCodex"
                + ", a.sDescript"
                + ", a.sBriefDsc"
                + ", a.sAltBarCd"
                + ", a.sCategCd1"
                + ", a.sCategCd2"
                + ", a.sCategCd3"
                + ", a.sCategCd4"
                + ", a.sBrandIDx"
                + ", a.sModelIDx"
                + ", a.sColorIDx"
                + ", a.sVrntIDxx"
                + ", a.sMeasurID"
                + ", a.sInvTypCd"
                + ", a.sIndstCdx"
                + ", a.nUnitPrce"
                + ", a.nSelPrice"
                + ", a.nDiscLev1"
                + ", a.nDiscLev2"
                + ", a.nDiscLev3"
                + ", a.nDealrDsc"
                + ", a.nMinLevel"
                + ", a.nMaxLevel"
                + ", a.cComboInv"
                + ", a.cWthPromo"
                + ", a.cSerialze"
                + ", a.cUnitType"
                + ", a.cInvStatx"
                + ", a.nShlfLife"
                + ", a.sSupersed"
                + ", a.cRecdStat"
                + ", a.sModified"
                + ", a.dModified"
                + ", IFNULL(b.sDescript, '') xBrandNme"
                + ", IFNULL(c.sDescript, '') xModelNme"
                + ", IFNULL(d.sDescript, '') xColorNme"
                + ", IFNULL(e.sDescript, '') xMeasurNm"
                + ", TRIM(CONCAT(IFNULL(f.sDescript, ''), ' ', IFNULL(f.nYearMdlx, ''))) xVrntName"
                + ", IFNULL(c.sModelCde, '') xModelCde"
                + " FROM Inventory a"
                + " LEFT JOIN Brand b ON a.sBrandIDx = b.sBrandIDx"
                + " LEFT JOIN Model c ON a.sModelIDx = c.sModelIDx"
                + " LEFT JOIN Color d ON a.sColorIDx = d.sColorIDx"
                + " LEFT JOIN Measure e ON a.sMeasurID = e.sMeasurID"
                + " LEFT JOIN Model_Variant f ON a.sVrntIDxx = f.sVrntIDxx"
                + " LEFT JOIN Inv_Supplier h ON a.sStockIDx = h.sStockIDx";

        return MiscUtil.addCondition(lsSQL, lsCondition);
    }

    public JSONObject loadLedgerList(String dateFrom, String dateThru)
            throws SQLException, GuanzonException, CloneNotSupportedException {

        if (getModel().getStockId() == null || getModel().getStockId().isEmpty()) {
            poJSON.put("result", "error");
            poJSON.put("message", "No Inventory loaded.");
        }
        paRecordLedger.clear();
        String lsSQL = "SELECT"
                + "  a.sStockIDx"
                + ", a.sIndstCdx"
                + ", a.sBranchCd"
                + ", a.sWHouseID"
                + ", a.nLedgerNo"
                + ", a.dTransact"
                + ", a.sSourceCd"
                + ", a.sSourceNo"
                + ", a.nQtyInxxx"
                + ", a.nQtyOutxx"
                + ", a.nQtyOrder"
                + ", a.nQtyIssue"
                + ", a.nPurPrice"
                + ", a.nUnitPrce"
                + ", a.dExpiryxx"
                + ", a.cConditnx"
                + ", a.cReversex"
                + " FROM Inv_Ledger a ";

        if (!psIndustryCode.isEmpty()) {
            lsSQL = MiscUtil.addCondition(lsSQL, "a.sIndstCdx = " + SQLUtil.toSQL(psIndustryCode));
        }

        lsSQL = MiscUtil.addCondition(lsSQL, "a.sBranchCd = " + SQLUtil.toSQL(poGRider.getBranchCode()));
        if (!dateFrom.isEmpty() && !dateThru.isEmpty()) {
            lsSQL = MiscUtil.addCondition(lsSQL, " a.dTransact BETWEEN " + SQLUtil.toSQL(dateFrom)
                    + " AND " + SQLUtil.toSQL(dateThru));
        }

        lsSQL = MiscUtil.addCondition(lsSQL, "a.sStockIDx = " + SQLUtil.toSQL(getModel().getStockId()));
        ResultSet loRS = poGRider.executeQuery(lsSQL);
        System.out.println("Load Record list query is " + lsSQL);

        if (MiscUtil.RecordCount(loRS)
                <= 0) {
            poJSON.put("result", "error");
            poJSON.put("message", "No record found.");
            return poJSON;
        }

        while (loRS.next()) {
            String stockId = loRS.getString("sStockIDx");

            Model_Inv_Ledger loInventoryLedger = new InvModels(poGRider).InventoryLedger();

            poJSON = loInventoryLedger.openRecord(stockId, poGRider.getBranchCode());

            if ("success".equals((String) poJSON.get("result"))) {
                paRecordLedger.add((Model) loInventoryLedger);
            } else {
                return poJSON;
            }
        }

        poJSON = new JSONObject();
        poJSON.put("result", "success");
        return poJSON;
    }

    public JSONObject loadSerialList(String unitType)
            throws SQLException, GuanzonException, CloneNotSupportedException {

        paRecordSerial.clear();
        String lsSQL = "SELECT"
                + "  a.sSerialID"
                + ", a.sIndstCdx"
                + ", a.sBranchCd"
                + ", a.sWHouseID"
                + ", a.sClientID"
                + ", a.sSerial01"
                + ", a.sSerial02"
                + ", a.nUnitPrce"
                + ", a.sStockIDx"
                + ", a.nLedgerNo"
                + ", a.cLocation"
                + ", a.cSoldStat"
                + ", a.cUnitType"
                + ", a.sCompnyID"
                + ", a.sWarranty"
                + ", a.cConditnx"
                + ", a.sPayloadx"
                + " FROM Inv_Serial a";

        if (!psIndustryCode.isEmpty()) {
            lsSQL = MiscUtil.addCondition(lsSQL, "a.sIndstCdx = " + SQLUtil.toSQL(psIndustryCode));
        }

        if (!unitType.isEmpty()) {
            lsSQL = MiscUtil.addCondition(lsSQL, "a.cUnitType = " + SQLUtil.toSQL(unitType));
        }
        lsSQL = MiscUtil.addCondition(lsSQL, "a.sBranchCd = " + SQLUtil.toSQL(poGRider.getBranchCode()
                + " AND cLocation IN ('0','1')"));

        lsSQL = MiscUtil.addCondition(lsSQL, "a.sStockIDx = " + SQLUtil.toSQL(getModel().getStockId()));
        ResultSet loRS = poGRider.executeQuery(lsSQL);
        System.out.println("Load Transaction list query is " + lsSQL);

        if (MiscUtil.RecordCount(loRS) <= 0) {
            poJSON.put("result", "error");
            poJSON.put("message", "No record found.");
            return poJSON;
        }

        Set<String> processedTrans = new HashSet<>();

        while (loRS.next()) {
            String serialID = loRS.getString("sSerialID");

            // Skip if we already processed this stock number
            if (processedTrans.contains(serialID)) {
                continue;
            }

            Model_Inv_Serial loInventorySerial = new InvModels(poGRider).InventorySerial();

            poJSON = loInventorySerial.openRecord(serialID, poGRider.getBranchCode());

            if ("success".equals((String) poJSON.get("result"))) {
                paRecordSerial.add((Model) loInventorySerial);

                // Mark this transaction as processed
                processedTrans.add(serialID);
            } else {
                return poJSON;
            }
        }

        poJSON = new JSONObject();
        poJSON.put("result", "success");
        return poJSON;
    }
}
