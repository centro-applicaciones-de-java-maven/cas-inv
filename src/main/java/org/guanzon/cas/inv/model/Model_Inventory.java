package org.guanzon.cas.inv.model;

import java.sql.SQLException;
import java.util.Date;
import org.guanzon.appdriver.impl.Model;
import org.guanzon.appdriver.base.GuanzonException;
import org.guanzon.appdriver.base.MiscUtil;
import org.guanzon.appdriver.constant.EditMode;
import org.guanzon.appdriver.constant.Logical;
import org.guanzon.appdriver.constant.RecordStatus;
import ph.com.guanzongroup.cas.parameter.model.Model_Brand;
import ph.com.guanzongroup.cas.parameter.model.Model_Category;
import ph.com.guanzongroup.cas.parameter.model.Model_Category_Level2;
import ph.com.guanzongroup.cas.parameter.model.Model_Category_Level3;
import ph.com.guanzongroup.cas.parameter.model.Model_Category_Level4;
import ph.com.guanzongroup.cas.parameter.model.Model_Color;
import ph.com.guanzongroup.cas.parameter.model.Model_Industry;
import ph.com.guanzongroup.cas.parameter.model.Model_Inv_Type;
import ph.com.guanzongroup.cas.parameter.model.Model_Measure;
import ph.com.guanzongroup.cas.parameter.model.Model_Model;
import ph.com.guanzongroup.cas.parameter.model.Model_Model_Variant;
import ph.com.guanzongroup.cas.parameter.services.ParamModels;
import org.json.simple.JSONObject;

public class Model_Inventory extends Model {
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

            //initialize other connections
            ParamModels model = new ParamModels(poGRider);
            poIndustry = model.Industry();
            poCategory = model.Category();
            poCategoryLevel2 = model.Category2();
            poCategoryLevel3 = model.Category3();
            poCategoryLevel4 = model.Category4();
            poBrand = model.Brand();
            poModel = model.Model();
            poColor = model.Color();
            poMeasure = model.Measurement();
            poInventoryType = model.InventoryType();
            poVariant = model.ModelVariant();
            
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

    public JSONObject setCategoryFirstLevelId(String cagetoryId) {
        return setValue("sCategCd1", cagetoryId);
    }

    public String getCategoryFirstLevelId() {
        return (String) getValue("sCategCd1");
    }

    public JSONObject setCategoryIdSecondLevel(String cagetoryId) {
        return setValue("sCategCd2", cagetoryId);
    }

    public String getCategoryIdSecondLevel() {
        return (String) getValue("sCategCd2");
    }

    public JSONObject setCategoryIdThirdLevel(String cagetoryId) {
        return setValue("sCategCd3", cagetoryId);
    }

    public String getCategoryIdThirdLevel() {
        return (String) getValue("sCategCd3");
    }

    public JSONObject setCategoryIdFourthLevel(String cagetoryId) {
        return setValue("sCategCd4", cagetoryId);
    }

    public String getCategoryIdFourthLevel() {
        return (String) getValue("sCategCd4");
    }

    public JSONObject setBrandId(String brandId) {
        return setValue("sBrandIDx", brandId);
    }

    public String getBrandId() {
        return (String) getValue("sBrandIDx");
    }

    public JSONObject setModelId(String brandId) {
        return setValue("sModelIDx", brandId);
    }

    public String getModelId() {
        return (String) getValue("sModelIDx");
    }

    public JSONObject setColorId(String brandId) {
        return setValue("sColorIDx", brandId);
    }

    public String getColorId() {
        return (String) getValue("sColorIDx");
    }
    
    public JSONObject setVariantId(String variantId) {
        return setValue("sVrntIDxx", variantId);
    }

    public String getVariantId() {
        return (String) getValue("sVrntIDxx");
    }

    public JSONObject setMeasurementId(String measurementId) {
        return setValue("sMeasurID", measurementId);
    }

    public String getMeasurementId() {
        return (String) getValue("sMeasurID");
    }

    public JSONObject setInventoryTypeId(String inventoryTypeId) {
        return setValue("sInvTypCd", inventoryTypeId);
    }

    public String getInventoryTypeId() {
        return (String) getValue("sInvTypCd");
    }
    
    public JSONObject setIndustryCode(String industryCode) {
        return setValue("sIndstCdx", industryCode);
    }

    public String getIndustryCode() {
        return (String) getValue("sIndstCdx");
    }

    public JSONObject setCost(Number cost) {
        return setValue("nUnitPrce", cost);
    }

    public Number getCost() {
        return (Number) getValue("nUnitPrce");
    }

    public JSONObject setSellingPrice(Number sellingPrice) {
        return setValue("nSelPrice", sellingPrice);
    }

    public Number getSellingPrice() {
        return (Number) getValue("nSelPrice");
    }

    public JSONObject setDiscountRateLevel1(Number discountRate) {
        return setValue("nDiscLev1", discountRate);
    }

    public Number getDiscountRateLevel1() {
        return (Number) getValue("nDiscLev1");
    }

    public JSONObject setDiscountRateLevel2(Number discountRate) {
        return setValue("nDiscLev2", discountRate);
    }

    public Number getDiscountRateLevel2() {
        return (Number) getValue("nDiscLev2");
    }

    public JSONObject setDiscountRateLevel3(Number discountRate) {
        return setValue("nDiscLev3", discountRate);
    }

    public Number getDiscountRateLevel3() {
        return (Number) getValue("nDiscLev3");
    }

    public JSONObject setDealerDiscountRate(Number discountRate) {
        return setValue("nDealrDsc", discountRate);
    }

    public Number getDealerDiscountRate() {
        return (Number) getValue("nDealrDsc");
    }

    public JSONObject setMinimumInventoryLevel(int quantity) {
        return setValue("nMinLevel", quantity);
    }

    public int getMinimumInventoryLevel() {
        return (int) getValue("nMinLevel");
    }

    public JSONObject setMaximumInventoryLevel(int quantity) {
        return setValue("nMaxLevel", quantity);
    }

    public int getMaximumInventoryLevel() {
        return (int) getValue("nMaxLevel");
    }

    public JSONObject isComboInventory(boolean isComboInventory) {
        return setValue("cComboInv", isComboInventory ? "1" : "0");
    }

    public boolean isComboInventory() {
        return ((String) getValue("cComboInv")).equals("1");
    }

    public JSONObject isWithPromo(boolean isWithPromo) {
        return setValue("cWthPromo", isWithPromo ? "1" : "0");
    }

    public boolean isWithPromo() {
        return ((String) getValue("cWthPromo")).equals("1");
    }

    public JSONObject isSerialized(boolean isSerialized) {
        return setValue("cSerialze", isSerialized ? "1" : "0");
    }

    public boolean isSerialized() {
        return ((String) getValue("cSerialze")).equals("1");
    }

    public JSONObject setUnitType(String unitType) {
        return setValue("cUnitType", unitType);
    }

    public String getUnitType() {
        return (String) getValue("cUnitType");
    }

    public JSONObject setInventoryStatus(String inventoryStatus) {
        return setValue("cInvStatx", inventoryStatus);
    }

    public String getInventoryStatus() {
        return (String) getValue("cInvStatx");
    }

    public JSONObject setShelfLife(int days) {
        return setValue("nShlfLife", days);
    }

    public int getShelfLife() {
        return (int) getValue("nShlfLife");
    }

    public JSONObject setSupersededId(String supersededId) {
        return setValue("sSupersed", supersededId);
    }

    public String getSupersededId() {
        return (String) getValue("sSupersed");
    }

    public JSONObject setRecordStatus(String recordStatus) {
        return setValue("cRecdStat", recordStatus);
    }

    public String getRecordStatus() {
        return (String) getValue("cRecdStat");
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
        return MiscUtil.getNextCode(getTable(), ID, true, poGRider.getGConnection().getConnection(), poGRider.getBranchCode());
    }
    
    public Model_Industry Industry() throws SQLException, GuanzonException{
        if (!"".equals((String) getValue("sIndstCdx"))) {
            if (poIndustry.getEditMode() == EditMode.READY
                    && poIndustry.getIndustryId().equals((String) getValue("sIndstCdx"))) {
                return poIndustry;
            } else {
                poJSON = poIndustry.openRecord((String) getValue("sIndstCdx"));

                if ("success".equals((String) poJSON.get("result"))) {
                    return poIndustry;
                } else {
                    poIndustry.initialize();
                    return poIndustry;
                }
            }
        } else {
            poCategory.initialize();
            return poIndustry;
        }
    }
    
    public Model_Category Category() throws SQLException, GuanzonException{
        if (!"".equals((String) getValue("sCategCd1"))) {
            if (poCategory.getEditMode() == EditMode.READY
                    && poCategory.getCategoryId().equals((String) getValue("sCategCd1"))) {
                return poCategory;
            } else {
                poJSON = poCategory.openRecord((String) getValue("sCategCd1"));

                if ("success".equals((String) poJSON.get("result"))) {
                    return poCategory;
                } else {
                    poCategory.initialize();
                    return poCategory;
                }
            }
        } else {
            poCategory.initialize();
            return poCategory;
        }
    }

    public Model_Category_Level2 CategoryLevel2() throws SQLException, GuanzonException{
        if (!"".equals((String) getValue("sCategCd2"))) {
            if (poCategoryLevel2.getEditMode() == EditMode.READY
                    && poCategoryLevel2.getCategoryId().equals((String) getValue("sCategCd2"))) {
                return poCategoryLevel2;
            } else {
                poJSON = poCategoryLevel2.openRecord((String) getValue("sCategCd2"));

                if ("success".equals((String) poJSON.get("result"))) {
                    return poCategoryLevel2;
                } else {
                    poCategoryLevel2.initialize();
                    return poCategoryLevel2;
                }
            }
        } else {
            poCategoryLevel2.initialize();
            return poCategoryLevel2;
        }
    }

    public Model_Category_Level3 CategoryLevel3() throws SQLException, GuanzonException{
        if (!"".equals((String) getValue("sCategCd3"))) {
            if (poCategoryLevel3.getEditMode() == EditMode.READY
                    && poCategoryLevel3.getCategoryId().equals((String) getValue("sCategCd3"))) {
                return poCategoryLevel3;
            } else {
                poJSON = poCategoryLevel3.openRecord((String) getValue("sCategCd3"));

                if ("success".equals((String) poJSON.get("result"))) {
                    return poCategoryLevel3;
                } else {
                    poCategoryLevel3.initialize();
                    return poCategoryLevel3;
                }
            }
        } else {
            poCategoryLevel3.initialize();
            return poCategoryLevel3;
        }
    }

    public Model_Category_Level4 CategoryLevel4() throws SQLException, GuanzonException{
        if (!"".equals((String) getValue("sCategCd4"))) {
            if (poCategoryLevel4.getEditMode() == EditMode.READY
                    && poCategoryLevel4.getCategoryId().equals((String) getValue("sCategCd4"))) {
                return poCategoryLevel4;
            } else {
                poJSON = poCategoryLevel4.openRecord((String) getValue("sCategCd4"));

                if ("success".equals((String) poJSON.get("result"))) {
                    return poCategoryLevel4;
                } else {
                    poCategoryLevel4.initialize();
                    return poCategoryLevel4;
                }
            }
        } else {
            poCategoryLevel4.initialize();
            return poCategoryLevel4;
        }
    }

    public Model_Brand Brand() throws SQLException, GuanzonException{
        if (!"".equals((String) getValue("sBrandIDx"))) {
            if (poBrand.getEditMode() == EditMode.READY
                    && poBrand.getBrandId().equals((String) getValue("sBrandIDx"))) {
                return poBrand;
            } else {
                poJSON = poBrand.openRecord((String) getValue("sBrandIDx"));

                if ("success".equals((String) poJSON.get("result"))) {
                    return poBrand;
                } else {
                    poBrand.initialize();
                    return poBrand;
                }
            }
        } else {
            poBrand.initialize();
            return poBrand;
        }
    }

    public Model_Model Model() throws SQLException, GuanzonException{
        if (!"".equals((String) getValue("sModelIDx"))) {
            if (poModel.getEditMode() == EditMode.READY
                    && poModel.getModelId().equals((String) getValue("sModelIDx"))) {
                return poModel;
            } else {
                poJSON = poModel.openRecord((String) getValue("sModelIDx"));

                if ("success".equals((String) poJSON.get("result"))) {
                    return poModel;
                } else {
                    poModel.initialize();
                    return poModel;
                }
            }
        } else {
            poModel.initialize();
            return poModel;
        }
    }

    public Model_Color Color() throws SQLException, GuanzonException{
        if (!"".equals((String) getValue("sColorIDx"))) {
            if (poColor.getEditMode() == EditMode.READY
                    && poColor.getColorId().equals((String) getValue("sColorIDx"))) {
                return poColor;
            } else {
                poJSON = poColor.openRecord((String) getValue("sColorIDx"));

                if ("success".equals((String) poJSON.get("result"))) {
                    return poColor;
                } else {
                    poColor.initialize();
                    return poColor;
                }
            }
        } else {
            poColor.initialize();
            return poColor;
        }
    }

    public Model_Measure Measure() throws SQLException, GuanzonException{
        if (!"".equals((String) getValue("sMeasurID"))) {
            if (poMeasure.getEditMode() == EditMode.READY
                    && poMeasure.getMeasureId().equals((String) getValue("sMeasurID"))) {
                return poMeasure;
            } else {
                poJSON = poMeasure.openRecord((String) getValue("sMeasurID"));

                if ("success".equals((String) poJSON.get("result"))) {
                    return poMeasure;
                } else {
                    poMeasure.initialize();
                    return poMeasure;
                }
            }
        } else {
            poMeasure.initialize();
            return poMeasure;
        }
    }

    public Model_Inv_Type InventoryType() throws SQLException, GuanzonException{
        if (!"".equals((String) getValue("sInvTypCd"))) {
            if (poInventoryType.getEditMode() == EditMode.READY
                    && poInventoryType.getInventoryTypeId().equals((String) getValue("sInvTypCd"))) {
                return poInventoryType;
            } else {
                poJSON = poInventoryType.openRecord((String) getValue("sInvTypCd"));

                if ("success".equals((String) poJSON.get("result"))) {
                    return poInventoryType;
                } else {
                    poInventoryType.initialize();
                    return poInventoryType;
                }
            }
        } else {
            poInventoryType.initialize();
            return poInventoryType;
        }
    }
    
    public Model_Model_Variant Variant() throws SQLException, GuanzonException{
        if (!"".equals((String) getValue("sVrntIDxx"))) {
            if (poVariant.getEditMode() == EditMode.READY
                    && poVariant.getVariantId().equals((String) getValue("sVrntIDxx"))) {
                return poVariant;
            } else {
                poJSON = poVariant.openRecord((String) getValue("sVrntIDxx"));

                if ("success".equals((String) poJSON.get("result"))) {
                    return poVariant;
                } else {
                    poVariant.initialize();
                    return poVariant;
                }
            }
        } else {
            poVariant.initialize();
            return poVariant;
        }
    }
}
