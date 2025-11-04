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
import org.guanzon.appdriver.constant.Logical;
import org.guanzon.appdriver.constant.RecordStatus;
import org.guanzon.appdriver.constant.UserRight;
import org.guanzon.cas.inv.model.Model_Inventory;
import org.guanzon.cas.inv.services.InvModels;
import org.guanzon.cas.parameter.model.Model_Brand;
import org.guanzon.cas.parameter.model.Model_Category;
import org.guanzon.cas.parameter.model.Model_Category_Level2;
import org.guanzon.cas.parameter.model.Model_Category_Level3;
import org.guanzon.cas.parameter.model.Model_Category_Level4;
import org.guanzon.cas.parameter.model.Model_Color;
import org.guanzon.cas.parameter.model.Model_Inv_Type;
import org.guanzon.cas.parameter.model.Model_Measure;
import org.guanzon.cas.parameter.model.Model_Model;
import org.guanzon.cas.parameter.model.Model_Model_Variant;
import org.guanzon.cas.parameter.services.ParamModels;
import org.json.simple.JSONObject;

public class Inventory extends Parameter {

    Model_Inventory poModel;
    private List<Model> paRecord;

    private String psIndustryCode = "";
    private String psCategoryCode = "";
    private String psApprovalUser = "";

    @Override
    public void initialize() throws SQLException, GuanzonException {
        psRecdStat = Logical.YES;

        InvModels inv = new InvModels(poGRider);
        poModel = inv.Inventory();

        paRecord = new ArrayList<Model>();
        super.initialize();
    }

    public void setIndustryID(String industryId) {
        psIndustryCode = industryId;
    }

    public void setCategory(String categoryid) {
        psCategoryCode = categoryid;
    }

    @Override
    public JSONObject isEntryOkay() throws SQLException {
        poJSON = new JSONObject();

        if (poGRider.getUserLevel() < UserRight.SYSADMIN) {
            poJSON.put("result", "error");
            poJSON.put("message", "User is not allowed to save record.");
            return poJSON;
        } else {
            poJSON = new JSONObject();

            if (poModel.getBarCode().isEmpty()) {
                poJSON.put("result", "error");
                poJSON.put("message", "Item barcode must not be empty.");
                return poJSON;
            }

            if (poModel.getDescription().isEmpty()) {
                poJSON.put("result", "error");
                poJSON.put("message", "Item description must not be empty.");
                return poJSON;
            }

            if (poModel.getCategoryFirstLevelId() == null || poModel.getCategoryFirstLevelId().isEmpty()) {
                poJSON.put("result", "error");
                poJSON.put("message", "Item category must not be empty.");
                return poJSON;
            }

            if (poModel.getCategoryIdSecondLevel() == null || poModel.getCategoryIdSecondLevel().isEmpty()) {
                poJSON.put("result", "error");
                poJSON.put("message", "Item category 2  must not be empty.");
                return poJSON;
            }

            if (poModel.getInventoryTypeId() == null || poModel.getInventoryTypeId().isEmpty()) {
                poJSON.put("result", "error");
                poJSON.put("message", "Item category 2  must not be empty.");
                return poJSON;
            }

            //todo:
            //  more validations/use of validators per category
            poModel.setModifyingId(poGRider.Encrypt(poGRider.getUserID()));
            poModel.setModifiedDate(poGRider.getServerDate());
            poModel.setIndustryCode(psIndustryCode);
        }

        poJSON.put("result", "success");
        return poJSON;
    }

    @SuppressWarnings("unchecked")
    public List<Model_Inventory> getSubItemList() {
        return (List<Model_Inventory>) (List<?>) paRecord;
    }

    //to refactor in sub inventory
    @SuppressWarnings("unchecked")
    public Model_Inventory getOther(int entryNo) {
        if (getModel().getStockId().isEmpty() || entryNo <= 0) {
            return null;
        }

        //autoadd detail if empty
        Model_Inventory lastDetail = (Model_Inventory) paRecord.get(paRecord.size() - 1);
        String stockID = lastDetail.getStockId();
        if (stockID != null && !stockID.trim().isEmpty()) {
            Model_Inventory newDetail = new InvModels(poGRider).Inventory();
            newDetail.newRecord();
            newDetail.setStockId(getModel().getStockId());
//            newDetail.setEntryNo(paRecord.size() + 1);
            paRecord.add(newDetail);
        }

        Model_Inventory loDetail;

        //find the detail record
        for (int lnCtr = 0; lnCtr <= paRecord.size() - 1; lnCtr++) {
            loDetail = (Model_Inventory) paRecord.get(lnCtr);

//            if (loDetail.getEntryNo() == entryNo) {
//                return loDetail;
//            }
        }

        loDetail = new InvModels(poGRider).Inventory();
        loDetail.newRecord();
        loDetail.setStockId(getModel().getStockId());
//        loDetail.setEntryNo(entryNo);
        paRecord.add(loDetail);

        return loDetail;
    }

    @Override
    public Model_Inventory getModel() {
        return poModel;
    }

    @Override
    public JSONObject searchRecord(String value, boolean byCode) throws SQLException, GuanzonException {
        String lsSQL = getSQ_Browse();
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

        System.out.println("Search Record Query : " + lsSQL);
        poJSON = ShowDialogFX.Search(poGRider,
                lsSQL,
                value,
                "Barcode»Description»Brand»Model»UOM",
                "sBarCodex»sDescript»xBrandNme»xModelNme»xMeasurNm",
                "a.sBarCodex»a.sDescript»IFNULL(b.sDescript, '')»IFNULL(c.sDescript, '')»IFNULL(e.sDescript, '')",
                byCode ? 0 : 1);

        if (poJSON != null) {
            return poModel.openRecord((String) poJSON.get("sStockIDx"));
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
            lsSQL = MiscUtil.addCondition(lsSQL, "g.sSupplier = " + SQLUtil.toSQL(supplierId));
        }

        poJSON = ShowDialogFX.Search(poGRider,
                lsSQL,
                value,
                "Barcode»Description»Brand»Model»UOM",
                "sBarCodex»sDescript»xBrandNme»xModelNme»xMeasurNm",
                "a.sBarCodex»a.sDescript»IFNULL(b.sDescript, '')»IFNULL(c.sDescript, '')»IFNULL(e.sDescript, '')",
                byCode ? 0 : 1);

        if (poJSON != null) {
            return poModel.openRecord((String) poJSON.get("sStockIDx"));
        } else {
            poJSON = new JSONObject();
            poJSON.put("result", "error");
            poJSON.put("message", "No record loaded.");
            return poJSON;
        }
    }

    public JSONObject searchRecord(String value, boolean byCode, String supplierId, String brandId) throws SQLException, GuanzonException {
        String lsSQL = getSQ_Browse();

        if (supplierId != null) {
            lsSQL = MiscUtil.addCondition(lsSQL, "g.sSupplier = " + SQLUtil.toSQL(supplierId));
        }

        if (brandId != null) {
            lsSQL = MiscUtil.addCondition(lsSQL, "a.sBrandIDx = " + SQLUtil.toSQL(brandId));
        }

        poJSON = ShowDialogFX.Search(poGRider,
                lsSQL,
                value,
                "Barcode»Description»Brand»Model»UOM",
                "sBarCodex»sDescript»xBrandNme»xModelNme»xMeasurNm",
                "a.sBarCodex»a.sDescript»IFNULL(b.sDescript, '')»IFNULL(c.sDescript, '')»IFNULL(e.sDescript, '')",
                byCode ? 0 : 1);

        if (poJSON != null) {
            return poModel.openRecord((String) poJSON.get("sStockIDx"));
        } else {
            poJSON = new JSONObject();
            poJSON.put("result", "error");
            poJSON.put("message", "No record loaded.");
            return poJSON;
        }
    }

    public JSONObject searchRecord(String value, boolean byCode, String supplierId, String brandId, String industryId) throws SQLException, GuanzonException {
        String lsSQL = getSQ_Browse();

        if (supplierId != null) {
            lsSQL = MiscUtil.addCondition(lsSQL, "g.sSupplier = " + SQLUtil.toSQL(supplierId));
        }

        if (brandId != null) {
            lsSQL = MiscUtil.addCondition(lsSQL, "a.sBrandIDx = " + SQLUtil.toSQL(brandId));
        }

        if (industryId != null) {
            lsSQL = MiscUtil.addCondition(lsSQL, "a.sIndstCdx = " + SQLUtil.toSQL(industryId));
        }

        poJSON = ShowDialogFX.Search(poGRider,
                lsSQL,
                value,
                "Barcode»Description»Brand»Model»UOM",
                "sBarCodex»sDescript»xBrandNme»xModelNme»xMeasurNm",
                "a.sBarCodex»a.sDescript»IFNULL(b.sDescript, '')»IFNULL(c.sDescript, '')»IFNULL(e.sDescript, '')",
                byCode ? 0 : 1);

        if (poJSON != null) {
            return poModel.openRecord((String) poJSON.get("sStockIDx"));
        } else {
            poJSON = new JSONObject();
            poJSON.put("result", "error");
            poJSON.put("message", "No record loaded.");
            return poJSON;
        }
    }

    public JSONObject searchRecord(String value, boolean byCode, String supplierId, String brandId, String industryId, String categoryId) throws SQLException, GuanzonException {
        String lsSQL = getSQ_Browse();

        if (supplierId != null) {
            lsSQL = MiscUtil.addCondition(lsSQL, "g.sSupplier = " + SQLUtil.toSQL(supplierId));
        }

        if (brandId != null) {
            lsSQL = MiscUtil.addCondition(lsSQL, "a.sBrandIDx = " + SQLUtil.toSQL(brandId));
        }

        if (industryId != null) {
            lsSQL = MiscUtil.addCondition(lsSQL, "a.sIndstCdx = " + SQLUtil.toSQL(industryId));
        }

        if (categoryId != null) {
            lsSQL = MiscUtil.addCondition(lsSQL, "a.sCategCd1 = " + SQLUtil.toSQL(categoryId));
        }

        poJSON = ShowDialogFX.Search(poGRider,
                lsSQL,
                value,
                "Barcode»Description»Brand»Model»UOM",
                "sBarCodex»sDescript»xBrandNme»xModelNme»xMeasurNm",
                "a.sBarCodex»a.sDescript»IFNULL(b.sDescript, '')»IFNULL(c.sDescript, '')»IFNULL(e.sDescript, '')",
                byCode ? 0 : 1);

        if (poJSON != null) {
            return poModel.openRecord((String) poJSON.get("sStockIDx"));
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
                "Brand»Model»Variant»Code»Color",
                "xBrandNme»xModelNme»xVrntName»xModelCde»xColorNme",
                "IFNULL(b.sDescript, '')»IFNULL(c.sDescript, '')»TRIM(CONCAT(IFNULL(f.sDescript, ''), ' ', IFNULL(f.nYearMdlx, '')))»IFNULL(c.sModelCde, '') xModelCde»IFNULL(d.sDescript, '')",
                byCode ? 0 : 1);

        if (poJSON != null) {
            return poModel.openRecord((String) poJSON.get("sStockIDx"));
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
            lsSQL = MiscUtil.addCondition(lsSQL, "g.sSupplier = " + SQLUtil.toSQL(supplierId));
        }

        poJSON = ShowDialogFX.Search(poGRider,
                lsSQL,
                value,
                "Brand»Model»Variant»Code»Color",
                "xBrandNme»xModelNme»xVrntName»xModelCde»xColorNme",
                "IFNULL(b.sDescript, '')»IFNULL(c.sDescript, '')»TRIM(CONCAT(IFNULL(f.sDescript, ''), ' ', IFNULL(f.nYearMdlx, '')))»IFNULL(c.sModelCde, '') xModelCde»IFNULL(d.sDescript, '')",
                byCode ? 0 : 1);

        if (poJSON != null) {
            return poModel.openRecord((String) poJSON.get("sStockIDx"));
        } else {
            poJSON = new JSONObject();
            poJSON.put("result", "error");
            poJSON.put("message", "No record loaded.");
            return poJSON;
        }
    }

    public JSONObject searchRecordOfVariants(String value, boolean byCode, String supplierId, String brandId, String industryId, String categoryId) throws SQLException, GuanzonException {
        String lsSQL = getSQ_Browse();

        if (supplierId != null) {
            lsSQL = MiscUtil.addCondition(lsSQL, "g.sSupplier = " + SQLUtil.toSQL(supplierId));
        }

        if (brandId != null) {
            lsSQL = MiscUtil.addCondition(lsSQL, "a.sBrandIDx = " + SQLUtil.toSQL(brandId));
        }

        if (industryId != null) {
            lsSQL = MiscUtil.addCondition(lsSQL, "a.sIndstCdx = " + SQLUtil.toSQL(industryId));
        }

        if (categoryId != null) {
            lsSQL = MiscUtil.addCondition(lsSQL, "a.sCategCd1 = " + SQLUtil.toSQL(categoryId));
        }

        poJSON = ShowDialogFX.Search(poGRider,
                lsSQL,
                value,
                "Brand»Model»Variant»Code»Color",
                "xBrandNme»xModelNme»xVrntName»xModelCde»xColorNme",
                "IFNULL(b.sDescript, '')»IFNULL(c.sDescript, '')»TRIM(CONCAT(IFNULL(f.sDescript, ''), ' ', IFNULL(f.nYearMdlx, '')))»IFNULL(c.sModelCde, '') xModelCde»IFNULL(d.sDescript, '')",
                byCode ? 0 : 1);

        if (poJSON != null) {
            return poModel.openRecord((String) poJSON.get("sStockIDx"));
        } else {
            poJSON = new JSONObject();
            poJSON.put("result", "error");
            poJSON.put("message", "No record loaded.");
            return poJSON;
        }
    }

    public JSONObject searchRecord(String categoryId, String brandId, String modelId, String variantId, String colorId) throws SQLException, GuanzonException {
        String lsSQL = getSQ_Browse();

        if (categoryId != null) {
            lsSQL = MiscUtil.addCondition(lsSQL, "a.sCategCd1 = " + SQLUtil.toSQL(categoryId));
        }

        if (brandId != null) {
            lsSQL = MiscUtil.addCondition(lsSQL, "a.sBrandIDx = " + SQLUtil.toSQL(brandId));
        }

        if (modelId != null) {
            lsSQL = MiscUtil.addCondition(lsSQL, "a.sModelIDx = " + SQLUtil.toSQL(modelId));
        }

        if (variantId != null) {
            lsSQL = MiscUtil.addCondition(lsSQL, "a.sVrntIDxx = " + SQLUtil.toSQL(variantId));
        }

        if (colorId != null) {
            lsSQL = MiscUtil.addCondition(lsSQL, "a.sColorIDx = " + SQLUtil.toSQL(colorId));
        }

        lsSQL += " GROUP BY a.sStockIDx";

        poJSON = ShowDialogFX.Search(poGRider,
                lsSQL,
                "",
                "Stock ID»Brand»Model»Variant»Code»Color",
                "sStockIDx»xBrandNme»xModelNme»xVrntName»xModelCde»xColorNme",
                "a.sStockIDx»IFNULL(b.sDescript, '')»IFNULL(c.sDescript, '')»TRIM(CONCAT(IFNULL(f.sDescript, ''), ' ', IFNULL(f.nYearMdlx, '')))»IFNULL(c.sModelCde, '') xModelCde»IFNULL(d.sDescript, '')",
                0);

        if (poJSON != null) {
            return poModel.openRecord((String) poJSON.get("sStockIDx"));
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
                + " FROM Inventory a"
                + " LEFT JOIN Brand b ON a.sBrandIDx = b.sBrandIDx"
                + " LEFT JOIN Model c ON a.sModelIDx = c.sModelIDx"
                + " LEFT JOIN Color d ON a.sColorIDx = d.sColorIDx"
                + " LEFT JOIN Measure e ON a.sMeasurID = e.sMeasurID"
                + " LEFT JOIN Model_Variant f ON a.sVrntIDxx = f.sVrntIDxx"
                + " LEFT JOIN Inv_Supplier g ON a.sStockIDx = g.sStockIDx";

        return MiscUtil.addCondition(lsSQL, lsCondition);
    }

    //refactor me if sub item will be implemented
    public JSONObject loadSubItemList()
            throws SQLException, GuanzonException, CloneNotSupportedException {

        paRecord.clear();
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
                + " LEFT JOIN Inv_Supplier g ON a.sStockIDx = g.sStockIDx";

        if (!psIndustryCode.isEmpty()) {
            lsSQL = MiscUtil.addCondition(lsSQL, "a.sIndstCdx = " + SQLUtil.toSQL(psIndustryCode));
        }

        lsSQL = MiscUtil.addCondition(lsSQL, "a.sStockIDx = " + SQLUtil.toSQL(getModel().getStockId()));
        ResultSet loRS = poGRider.executeQuery(lsSQL);
        System.out.println("Load Transaction list query is " + lsSQL);

        if (MiscUtil.RecordCount(loRS)
                <= 0) {
            poJSON.put("result", "error");
            poJSON.put("message", "No record found.");
            return poJSON;
        }
        Set<String> processedTrans = new HashSet<>();

        while (loRS.next()) {
            String stockId = loRS.getString("sStockIDx");

            // Skip if we already processed this stock number
            if (processedTrans.contains(stockId)) {
                continue;
            }

            Model_Inventory loInventory = new InvModels(poGRider).Inventory();

            poJSON = loInventory.openRecord(stockId);

            if ("success".equals((String) poJSON.get("result"))) {
                paRecord.add((Model) loInventory);

                // Mark this transaction as processed
                processedTrans.add(stockId);
            } else {
                return poJSON;
            }
        }

        poJSON = new JSONObject();
        poJSON.put("result", "success");
        return poJSON;
    }

    public JSONObject searchCategory(String value, boolean byCode) throws SQLException, GuanzonException {
        Model_Category loBrowse = new ParamModels(poGRider).Category();
        loBrowse.initialize();
        String lsSQL = "SELECT * FROM " + loBrowse.getTable() + " WHERE cRecdStat = " + SQLUtil.toSQL(RecordStatus.ACTIVE);

        if (!psIndustryCode.isEmpty()) {
            lsSQL = MiscUtil.addCondition(lsSQL, "sIndstCdx = " + SQLUtil.toSQL(psIndustryCode));
        }

        System.out.println("Search Record Query : " + lsSQL);
        poJSON = new JSONObject();
        poJSON = ShowDialogFX.Search(poGRider,
                lsSQL,
                value,
                "Code»Description»Desc Code",
                "sCategrCd»sDescript»sDescCode",
                "sCategrCd»sDescript»sDescCode",
                byCode ? 0 : 1);

        if (poJSON != null) {
            poJSON = loBrowse.openRecord((String) this.poJSON.get("sCategrCd"));
            System.out.println("result " + (String) poJSON.get("result"));

            if ("success".equals((String) poJSON.get("result"))) {

                this.poJSON = new JSONObject();
                this.poJSON.put("result", "success");
                getModel().setCategoryFirstLevelId(loBrowse.getCategoryId());
                return poJSON;
            }

        }
        this.poJSON = new JSONObject();
        this.poJSON.put("result", "error");
        this.poJSON.put("message", "No record loaded.");
        return this.poJSON;

    }

    public JSONObject searchCategory2(String value, boolean byCode) throws SQLException, GuanzonException {
        Model_Category_Level2 loBrowse = new ParamModels(poGRider).Category2();
        loBrowse.initialize();
        String lsSQL = "SELECT * FROM " + loBrowse.getTable() + " WHERE cRecdStat = " + SQLUtil.toSQL(RecordStatus.ACTIVE);

        if (!psIndustryCode.isEmpty()) {
            lsSQL = MiscUtil.addCondition(lsSQL, "sIndstCdx = " + SQLUtil.toSQL(psIndustryCode));
        }

        System.out.println("Search Record Query : " + lsSQL);
        poJSON = new JSONObject();
        poJSON = ShowDialogFX.Search(poGRider,
                lsSQL,
                value,
                "Code»Description",
                "sCategrCd»sDescript",
                "sCategrCd»sDescript",
                byCode ? 0 : 1);

        if (poJSON != null) {
            poJSON = loBrowse.openRecord((String) this.poJSON.get("sCategrCd"));
            System.out.println("result " + (String) poJSON.get("result"));

            if ("success".equals((String) poJSON.get("result"))) {

                this.poJSON = new JSONObject();
                this.poJSON.put("result", "success");
                getModel().setCategoryIdSecondLevel(loBrowse.getCategoryId());
                return poJSON;
            }

        }
        this.poJSON = new JSONObject();
        this.poJSON.put("result", "error");
        this.poJSON.put("message", "No record loaded.");
        return this.poJSON;

    }

    public JSONObject searchCategory3(String value, boolean byCode) throws SQLException, GuanzonException {
        Model_Category_Level3 loBrowse = new ParamModels(poGRider).Category3();
        loBrowse.initialize();
        String lsSQL = "SELECT * FROM " + loBrowse.getTable() + " WHERE cRecdStat = " + SQLUtil.toSQL(RecordStatus.ACTIVE);

        if (!psIndustryCode.isEmpty()) {
            lsSQL = MiscUtil.addCondition(lsSQL, "sIndstCdx = " + SQLUtil.toSQL(psIndustryCode));
        }

        System.out.println("Search Record Query : " + lsSQL);
        poJSON = new JSONObject();
        poJSON = ShowDialogFX.Search(poGRider,
                lsSQL,
                value,
                "Code»Description",
                "sCategrCd»sDescript",
                "sCategrCd»sDescript",
                byCode ? 0 : 1);

        if (poJSON != null) {
            poJSON = loBrowse.openRecord((String) this.poJSON.get("sCategrCd"));
            System.out.println("result " + (String) poJSON.get("result"));

            if ("success".equals((String) poJSON.get("result"))) {

                this.poJSON = new JSONObject();
                this.poJSON.put("result", "success");
                getModel().setCategoryIdThirdLevel(loBrowse.getCategoryId());
                return poJSON;
            }

        }
        this.poJSON = new JSONObject();
        this.poJSON.put("result", "error");
        this.poJSON.put("message", "No record loaded.");
        return this.poJSON;

    }

    public JSONObject searchCategory4(String value, boolean byCode) throws SQLException, GuanzonException {
        Model_Category_Level4 loBrowse = new ParamModels(poGRider).Category4();
        loBrowse.initialize();
        String lsSQL = "SELECT * FROM " + loBrowse.getTable() + " WHERE cRecdStat = " + SQLUtil.toSQL(RecordStatus.ACTIVE);

        if (!psIndustryCode.isEmpty()) {
            lsSQL = MiscUtil.addCondition(lsSQL, "sIndstCdx = " + SQLUtil.toSQL(psIndustryCode));
        }

        System.out.println("Search Record Query : " + lsSQL);
        poJSON = new JSONObject();
        poJSON = ShowDialogFX.Search(poGRider,
                lsSQL,
                value,
                "Code»Description",
                "sCategrCd»sDescript",
                "sCategrCd»sDescript",
                byCode ? 0 : 1);

        if (poJSON != null) {
            poJSON = loBrowse.openRecord((String) this.poJSON.get("sCategrCd"));
            System.out.println("result " + (String) poJSON.get("result"));

            if ("success".equals((String) poJSON.get("result"))) {

                this.poJSON = new JSONObject();
                this.poJSON.put("result", "success");
                getModel().setCategoryIdFourthLevel(loBrowse.getCategoryId());
                return poJSON;
            }

        }
        this.poJSON = new JSONObject();
        this.poJSON.put("result", "error");
        this.poJSON.put("message", "No record loaded.");
        return this.poJSON;

    }

    public JSONObject searchInvType(String value, boolean byCode) throws SQLException, GuanzonException {
        Model_Inv_Type loBrowse = new ParamModels(poGRider).InventoryType();
        loBrowse.initialize();
        String lsSQL = "SELECT * FROM " + loBrowse.getTable() + " WHERE cRecdStat = " + SQLUtil.toSQL(RecordStatus.ACTIVE);

//        if (!psIndustryCode.isEmpty()) {
//            lsSQL = MiscUtil.addCondition(lsSQL, "sIndstCdx = " + SQLUtil.toSQL(psIndustryCode));
//        }
        System.out.println("Search Record Query : " + lsSQL);
        poJSON = new JSONObject();
        poJSON = ShowDialogFX.Search(poGRider,
                lsSQL,
                value,
                "Code»Description",
                "sInvTypCd»sDescript",
                "sInvTypCd»sDescript",
                byCode ? 0 : 1);

        if (poJSON != null) {
            poJSON = loBrowse.openRecord((String) this.poJSON.get("sInvTypCd"));
            System.out.println("result " + (String) poJSON.get("result"));

            if ("success".equals((String) poJSON.get("result"))) {

                this.poJSON = new JSONObject();
                this.poJSON.put("result", "success");
                getModel().setInventoryTypeId(loBrowse.getInventoryTypeId());
                return poJSON;
            }

        }
        this.poJSON = new JSONObject();
        this.poJSON.put("result", "error");
        this.poJSON.put("message", "No record loaded.");
        return this.poJSON;

    }

    public JSONObject searchBrand(String value, boolean byCode) throws SQLException, GuanzonException {
        Model_Brand loBrowse = new ParamModels(poGRider).Brand();
        loBrowse.initialize();
        String lsSQL = "SELECT * FROM " + loBrowse.getTable() + " WHERE cRecdStat = " + SQLUtil.toSQL(RecordStatus.ACTIVE);

        if (!psIndustryCode.isEmpty()) {
            lsSQL = MiscUtil.addCondition(lsSQL, "sIndstCdx = " + SQLUtil.toSQL(psIndustryCode));
        }
        System.out.println("Search Record Query : " + lsSQL);

        poJSON = new JSONObject();
        poJSON = ShowDialogFX.Search(poGRider,
                lsSQL,
                value,
                "Code»Description",
                "sBrandIDx»sDescript",
                "sBrandIDx»sDescript",
                byCode ? 0 : 1);

        if (poJSON != null) {
            poJSON = loBrowse.openRecord((String) this.poJSON.get("sBrandIDx"));
            System.out.println("result " + (String) poJSON.get("result"));

            if ("success".equals((String) poJSON.get("result"))) {

                this.poJSON = new JSONObject();
                this.poJSON.put("result", "success");
                getModel().setBrandId(loBrowse.getBrandId());
                return poJSON;
            }

        }
        this.poJSON = new JSONObject();
        this.poJSON.put("result", "error");
        this.poJSON.put("message", "No record loaded.");
        return this.poJSON;

    }

    public JSONObject searchModel(String value, boolean byCode) throws SQLException, GuanzonException {
        Model_Model loBrowse = new ParamModels(poGRider).Model();
        loBrowse.initialize();
        String lsSQL = "SELECT * FROM " + loBrowse.getTable() + " WHERE cRecdStat = " + SQLUtil.toSQL(RecordStatus.ACTIVE);

        if (!psIndustryCode.isEmpty()) {
            lsSQL = MiscUtil.addCondition(lsSQL, "sIndstCdx = " + SQLUtil.toSQL(psIndustryCode));
        }

        System.out.println("Search Record Query : " + lsSQL);
        poJSON = new JSONObject();
        poJSON = ShowDialogFX.Search(poGRider,
                lsSQL,
                value,
                "ID»Description»Model Code",
                "sModelIDx»sDescript»sModelCde",
                "sModelIDx»sDescript»sModelCde",
                byCode ? 0 : 1);

        if (poJSON != null) {
            poJSON = loBrowse.openRecord((String) this.poJSON.get("sModelIDx"));
            System.out.println("result " + (String) poJSON.get("result"));

            if ("success".equals((String) poJSON.get("result"))) {

                this.poJSON = new JSONObject();
                this.poJSON.put("result", "success");
                getModel().setModelId(loBrowse.getModelId());
                getModel().setBrandId(loBrowse.getBrandId());
                return poJSON;
            }

        }
        this.poJSON = new JSONObject();
        this.poJSON.put("result", "error");
        this.poJSON.put("message", "No record loaded.");
        return this.poJSON;

    }

    public JSONObject searchColor(String value, boolean byCode) throws SQLException, GuanzonException {
        Model_Color loBrowse = new ParamModels(poGRider).Color();
        loBrowse.initialize();
        String lsSQL = "SELECT * FROM " + loBrowse.getTable() + " WHERE cRecdStat = " + SQLUtil.toSQL(RecordStatus.ACTIVE);

//        if (!psIndustryCode.isEmpty()) {
//            lsSQL = MiscUtil.addCondition(lsSQL, "sIndstCdx = " + SQLUtil.toSQL(psIndustryCode));
//        }
        System.out.println("Search Record Query : " + lsSQL);
        poJSON = new JSONObject();
        poJSON = ShowDialogFX.Search(poGRider,
                lsSQL,
                value,
                "ID»Description»Model Code",
                "sColorIDx»sDescript»sColorCde",
                "sColorIDx»sDescript»sColorCde",
                byCode ? 0 : 1);

        if (poJSON != null) {
            poJSON = loBrowse.openRecord((String) this.poJSON.get("sColorIDx"));
            System.out.println("result " + (String) poJSON.get("result"));

            if ("success".equals((String) poJSON.get("result"))) {

                this.poJSON = new JSONObject();
                this.poJSON.put("result", "success");
                getModel().setColorId(loBrowse.getColorId());
                return poJSON;
            }

        }
        this.poJSON = new JSONObject();
        this.poJSON.put("result", "error");
        this.poJSON.put("message", "No record loaded.");
        return this.poJSON;

    }

    public JSONObject searchMeasure(String value, boolean byCode) throws SQLException, GuanzonException {
        Model_Measure loBrowse = new ParamModels(poGRider).Measurement();
        loBrowse.initialize();
        String lsSQL = "SELECT * FROM " + loBrowse.getTable() + " WHERE cRecdStat = " + SQLUtil.toSQL(RecordStatus.ACTIVE);

//        if (!psIndustryCode.isEmpty()) {
//            lsSQL = MiscUtil.addCondition(lsSQL, "sIndstCdx = " + SQLUtil.toSQL(psIndustryCode));
//        }
        System.out.println("Search Record Query : " + lsSQL);
        poJSON = new JSONObject();
        poJSON = ShowDialogFX.Search(poGRider,
                lsSQL,
                value,
                "ID»Description",
                "sMeasurID»sDescript",
                "sMeasurID»sDescript",
                byCode ? 0 : 1);

        if (poJSON != null) {
            poJSON = loBrowse.openRecord((String) this.poJSON.get("sMeasurID"));
            System.out.println("result " + (String) poJSON.get("result"));

            if ("success".equals((String) poJSON.get("result"))) {

                this.poJSON = new JSONObject();
                this.poJSON.put("result", "success");
                getModel().setMeasurementId(loBrowse.getMeasureId());
                return poJSON;
            }

        }
        this.poJSON = new JSONObject();
        this.poJSON.put("result", "error");
        this.poJSON.put("message", "No record loaded.");
        return this.poJSON;

    }

    public JSONObject searchVariant(String value, boolean byCode) throws SQLException, GuanzonException {
        Model_Model_Variant loBrowse = new ParamModels(poGRider).ModelVariant();
        loBrowse.initialize();
        String lsSQL = "SELECT * FROM " + loBrowse.getTable() + " WHERE cRecdStat = " + SQLUtil.toSQL(RecordStatus.ACTIVE);

//        if (!psIndustryCode.isEmpty()) {
//            lsSQL = MiscUtil.addCondition(lsSQL, "sIndstCdx = " + SQLUtil.toSQL(psIndustryCode));
//        }
        System.out.println("Search Record Query : " + lsSQL);
        poJSON = new JSONObject();
        poJSON = ShowDialogFX.Search(poGRider,
                lsSQL,
                value,
                "ID»Description",
                "sVrntIDxx»sDescript",
                "sVrntIDxx»sDescript",
                byCode ? 0 : 1);

        if (poJSON != null) {
            poJSON = loBrowse.openRecord((String) this.poJSON.get("sVrntIDxx"));
            System.out.println("result " + (String) poJSON.get("result"));

            if ("success".equals((String) poJSON.get("result"))) {

                this.poJSON = new JSONObject();
                this.poJSON.put("result", "success");
                getModel().setVariantId(loBrowse.getVariantId());
                return poJSON;
            }

        }
        this.poJSON = new JSONObject();
        this.poJSON.put("result", "error");
        this.poJSON.put("message", "No record loaded.");
        return this.poJSON;

    }

    public JSONObject searchSuperseded(String value, boolean byCode) throws SQLException, GuanzonException {
        Model_Inventory loBrowse = new InvModels(poGRider).Inventory();
        loBrowse.initialize();
        String lsSQL = getSQ_Browse();
        if (!psIndustryCode.isEmpty()) {
            lsSQL = MiscUtil.addCondition(lsSQL, "a.sIndstCdx = " + SQLUtil.toSQL(psIndustryCode));
        }

        System.out.println("Search Record Query : " + lsSQL);
        poJSON = new JSONObject();
        poJSON = ShowDialogFX.Search(poGRider,
                lsSQL,
                value,
                "Barcode»Description»Brand»Model»UOM",
                "sBarCodex»sDescript»xBrandNme»xModelNme»xMeasurNm",
                "a.sBarCodex»a.sDescript»IFNULL(b.sDescript, '')»IFNULL(c.sDescript, '')»IFNULL(e.sDescript, '')",
                byCode ? 0 : 1);

        if (poJSON != null) {
            poJSON = loBrowse.openRecord((String) this.poJSON.get("sVrntIDxx"));
            System.out.println("result " + (String) poJSON.get("result"));

            if ("success".equals((String) poJSON.get("result"))) {

                this.poJSON = new JSONObject();
                this.poJSON.put("result", "success");
                getModel().setSupersededId(loBrowse.getStockId());
                return poJSON;
            }

        }
        this.poJSON = new JSONObject();
        this.poJSON.put("result", "error");
        this.poJSON.put("message", "No record loaded.");
        return this.poJSON;

    }

    public JSONObject searchOther(int row, String value, boolean byCode) throws SQLException, GuanzonException {
        Model_Inventory loBrowse = new InvModels(poGRider).Inventory();
        loBrowse.initialize();
        String lsSQL = getSQ_Browse();
        if (!psIndustryCode.isEmpty()) {
            lsSQL = MiscUtil.addCondition(lsSQL, "a.sIndstCdx = " + SQLUtil.toSQL(psIndustryCode));
        }

        if (psCategoryCode != null) {
            if (!psCategoryCode.isEmpty()) {
                lsSQL = MiscUtil.addCondition(lsSQL, "a.sCategCd1 = " + SQLUtil.toSQL(psCategoryCode));
            }
        }

        System.out.println("Search Record Query : " + lsSQL);
        poJSON = new JSONObject();
        poJSON = ShowDialogFX.Search(poGRider,
                lsSQL,
                value,
                "Barcode»Description»Brand»Model»UOM",
                "sBarCodex»sDescript»xBrandNme»xModelNme»xMeasurNm",
                "a.sBarCodex»a.sDescript»IFNULL(b.sDescript, '')»IFNULL(c.sDescript, '')»IFNULL(e.sDescript, '')",
                byCode ? 0 : 1);

        if (poJSON != null) {
            poJSON = loBrowse.openRecord((String) this.poJSON.get("sStockIDx"));
            System.out.println("result " + (String) poJSON.get("result"));

            if ("success".equals((String) poJSON.get("result"))) {
                for (int lnExisting = 0; lnExisting <= paRecord.size() - 1; lnExisting++) {
                    Model_Inventory loExisting = (Model_Inventory) paRecord.get(lnExisting);
                    if (loExisting.getStockId() != null) {
                        if (loExisting.getStockId().equals(loBrowse.getStockId())) {
                            poJSON = new JSONObject();
                            poJSON.put("result", "error");
                            poJSON.put("message", "Selected stock is already exist!");
                            return poJSON;
                        }
                    }
                }

                this.poJSON = new JSONObject();
                this.poJSON.put("result", "success");
                getOther(row).setStockId(loBrowse.getStockId());
                return poJSON;
            }

        }
        this.poJSON = new JSONObject();
        this.poJSON.put("result", "error");
        this.poJSON.put("message", "No record loaded.");
        return this.poJSON;

    }

}
