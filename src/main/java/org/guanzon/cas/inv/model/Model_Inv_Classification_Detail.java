//package org.guanzon.cas.inv.model;
//
//import java.sql.SQLException;
//import org.guanzon.appdriver.agent.services.Model;
//import org.guanzon.appdriver.base.MiscUtil;
//import org.guanzon.appdriver.constant.EditMode;
//import org.guanzon.appdriver.constant.InventoryClassification;
//import org.guanzon.cas.inv.services.InvModels;
//import org.guanzon.cas.parameter.model.Model_Branch;
//import org.guanzon.cas.parameter.model.Model_Category;
//import org.guanzon.cas.parameter.model.Model_Category_Level2;
//import org.guanzon.cas.parameter.services.ParamModels;
//import org.json.simple.JSONObject;
//
//public class Model_Inv_Classification_Detail extends Model {
//
//    //reference objects
//    Model_Branch poBranch;
//    Model_Category poIndustry;
//    Model_Category_Level2 poCategory;
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
//            poEntity.updateObject("nSoldQtyx", 0);
//            poEntity.updateObject("nAbnrmQty", 0);
//            poEntity.updateObject("nTotlSumx", 0);
//            poEntity.updateObject("nTotlSumP", 0.00);
//            poEntity.updateObject("nAveMonSl", 0);
//            poEntity.updateObject("nMaxSales", 0);
//            poEntity.updateObject("nAveShare", 0);
//            poEntity.updateObject("nTotOrder", 0);
//            poEntity.updateObject("nMinLevel", 0);
//            poEntity.updateObject("nMaxLevel", 0);
//            poEntity.updateObject("cClassify", InventoryClassification.NEW_ITEMS);
//            //end - assign default values
//            poEntity.insertRow();
//            poEntity.moveToCurrentRow();
//
//            poEntity.absolute(1);
//
//            ID = "sIndstCdx";
//            ID2 = "sCategrCd";
//            ID3 = "sBranchCd";
//            ID4 = "sPeriodxx";
//            ID5 = "sStockIDx";
//
//            //initialize reference objects
//            ParamModels modelParam = new ParamModels(poGRider);
//            poBranch = modelParam.Branch();
//            poIndustry = modelParam.Category();
//            poCategory = modelParam.Category2();
//
//            InvModels modelInv = new InvModels(poGRider);
//            poInventory = modelInv.Inventory();
//
//            //end - initialize reference objects
//            pnEditMode = EditMode.UNKNOWN;
//        } catch (SQLException e) {
//            logwrapr.severe(e.getMessage());
//            System.exit(1);
//        }
//    }
//
//    public JSONObject setIndustryId(String industryId) {
//        return setValue("sIndstCdx", industryId);
//    }
//
//    public String getIndustryId() {
//        return (String) getValue("sIndstCdx");
//    }
//
//    public JSONObject setCategoryId(String categoryId) {
//        return setValue("sCategrCd", categoryId);
//    }
//
//    public String getCategoryId() {
//        return (String) getValue("sCategrCd");
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
//    public JSONObject setPeriod(String yyyyMM) {
//        return setValue("sPeriodxx", yyyyMM);
//    }
//
//    public String getPeriod() {
//        return (String) getValue("sPeriodxx");
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
//    public JSONObject setSoldQuantity(int soldQuantity) {
//        return setValue("nSoldQtyx", soldQuantity);
//    }
//
//    public int getSoldQuantity() {
//        return (int) getValue("nSoldQtyx");
//    }
//
//    public JSONObject setAbnrmQuantity(int abnrmQuantity) {
//        return setValue("nAbnrmQty", abnrmQuantity);
//    }
//
//    public int getAbnrmQuantity() {
//        return (int) getValue("nAbnrmQty");
//    }
//
//    public JSONObject setTotalSumQuantity(int totalSumQuantity) {
//        return setValue("nTotlSumx", totalSumQuantity);
//    }
//
//    public int getTotalQuantity() {
//        return (int) getValue("nTotlSumx");
//    }
//
//    public JSONObject setTotalPQuantity(double totalPQuantity) {
//        return setValue("nTotlSumP", totalPQuantity);
//    }
//
//    public double getTotalPQuantity() {
//        return (double) getValue("nTotlSumP");
//    }
//
//    public JSONObject setAveMonSales(int aveMonSalesQuantity) {
//        return setValue("nAveMonSl", aveMonSalesQuantity);
//    }
//
//    public int getAveMonSales() {
//        return (int) getValue("nAveMonSl");
//    }
//
//    public JSONObject setMaxSales(int maxSalesQuantity) {
//        return setValue("nMaxSales", maxSalesQuantity);
//    }
//
//    public int getMaxSales() {
//        return (int) getValue("nMaxSales");
//    }
//
//    public JSONObject setAveShare(int aveShareQuantity) {
//        return setValue("nAveShare", aveShareQuantity);
//    }
//
//    public int getAveShare() {
//        return (int) getValue("nAveShare");
//    }
//
//    public JSONObject setTotalOrder(int totalOrderQuantity) {
//        return setValue("nTotOrder", totalOrderQuantity);
//    }
//
//    public int getTotalOrder() {
//        return (int) getValue("nTotOrder");
//    }
//
//    public JSONObject setMinLevel(int minLevel) {
//        return setValue("nMinLevel", minLevel);
//    }
//
//    public int getMinLevel() {
//        return (int) getValue("nMinLevel");
//    }
//
//    public JSONObject setMaxLevel(int maxLevel) {
//        return setValue("nMaxLevel", maxLevel);
//    }
//
//    public int getMaxLevel() {
//        return (int) getValue("nMaxLevel");
//    }
//
//    public JSONObject setClassify(String classify) {
//        return setValue("cClassify", classify);
//    }
//
//    public String getClassify() {
//        return (String) getValue("cClassify");
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
//    public Model_Category Industry() {
//        if (!"".equals((String) getValue("sIndstCdx"))) {
//            if (poIndustry.getEditMode() == EditMode.READY
//                    && poIndustry.getCategoryId().equals((String) getValue("sIndstCdx"))) {
//                return poIndustry;
//            } else {
//                poJSON = poIndustry.openRecord((String) getValue("sIndstCdx"));
//
//                if ("success".equals((String) poJSON.get("result"))) {
//                    return poIndustry;
//                } else {
//                    poIndustry.initialize();
//                    return poIndustry;
//                }
//            }
//        } else {
//            poIndustry.initialize();
//            return poIndustry;
//        }
//    }
//
//    public Model_Category_Level2 Category() {
//        if (!"".equals((String) getValue("sCategrCd"))) {
//            if (poCategory.getEditMode() == EditMode.READY
//                    && poCategory.getCategoryId().equals((String) getValue("sCategrCd"))) {
//                return poCategory;
//            } else {
//                poJSON = poCategory.openRecord((String) getValue("sCategrCd"));
//
//                if ("success".equals((String) poJSON.get("result"))) {
//                    return poCategory;
//                } else {
//                    poCategory.initialize();
//                    return poCategory;
//                }
//            }
//        } else {
//            poCategory.initialize();
//            return poCategory;
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
//}
