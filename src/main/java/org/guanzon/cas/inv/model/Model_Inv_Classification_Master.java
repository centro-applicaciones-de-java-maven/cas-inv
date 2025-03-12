//package org.guanzon.cas.inv.model;
//
//import java.sql.SQLException;
//import java.util.Date;
//import org.guanzon.appdriver.agent.services.Model;
//import org.guanzon.appdriver.base.MiscUtil;
//import org.guanzon.appdriver.constant.EditMode;
//import org.guanzon.appdriver.constant.TransactionStatus;
//import org.guanzon.cas.parameter.model.Model_Branch;
//import org.guanzon.cas.parameter.model.Model_Category;
//import org.guanzon.cas.parameter.model.Model_Category_Level2;
//import org.guanzon.cas.parameter.services.ParamModels;
//import org.json.simple.JSONObject;
//
//public class Model_Inv_Classification_Master extends Model {
//
//    //reference objects
//    Model_Branch poBranch;
//    Model_Category poIndustry;
//    Model_Category_Level2 poCategory;
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
//            poEntity.updateObject("nTotlSale", 0);
//            poEntity.updateObject("cTranStat", TransactionStatus.STATE_OPEN);
//            poEntity.updateObject("dProcessd", "0000-00-00 00:00:00");
//            poEntity.updateObject("dPostedxx", "0000-00-00 00:00:00");
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
//
//            //initialize reference objects
//            ParamModels model = new ParamModels(poGRider);
//            poBranch = model.Branch();
//            poIndustry = model.Category();
//            poCategory = model.Category2();
//            //end - initialize reference objects
//
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
//    public JSONObject setTotalSales(int totalSalesQuantity) {
//        return setValue("nTotlSale", totalSalesQuantity);
//    }
//
//    public int getTotalSales() {
//        return (int) getValue("nTotlSale");
//    }
//
//    public JSONObject setTransactionStatus(String transactionStatus) {
//        return setValue("cTranStat", transactionStatus);
//    }
//
//    public String getTransactionStatus() {
//        return (String) getValue("cTranStat");
//    }
//
//    public JSONObject setProcessorId(String processorId) {
//        return setValue("sProcessd", processorId);
//    }
//
//    public String getProcessorId() {
//        return (String) getValue("sProcessd");
//    }
//
//    public JSONObject setProcessedDate(Date processedDate) {
//        return setValue("dProcessd", processedDate);
//    }
//
//    public Date getProcessedDate() {
//        return (Date) getValue("dProcessd");
//    }
//
//    public JSONObject setPostedId(String postedById) {
//        return setValue("sPostedxx", postedById);
//    }
//
//    public String getPostedId() {
//        return (String) getValue("sPostedxx");
//    }
//
//    public JSONObject setPostingDate(Date postingDate) {
//        return setValue("dPostedxx", postingDate);
//    }
//
//    public Date getPostingDate() {
//        return (Date) getValue("dPostedxx");
//    }
//
//    public JSONObject setModifyingId(String modifyingId) {
//        return setValue("sModified", modifyingId);
//    }
//
//    public String getModifyingId() {
//        return (String) getValue("sModified");
//    }
//
//    public JSONObject setModifiedDate(Date modifiedDate) {
//        return setValue("dModified", modifiedDate);
//    }
//
//    public Date getModifiedDate() {
//        return (Date) getValue("dModified");
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
//    public JSONObject openRecord(String Id1, Object Id2, Object Id3, Object Id4, Object Id5) {
//        JSONObject loJSON = new JSONObject();
//        loJSON.put("result", "error");
//        loJSON.put("message", "This feature is not supported.");
//        return loJSON;
//    }
//
//    //reference object models
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
//    //end - reference object models
//}
