package org.guanzon.cas.inv.model;

import java.sql.SQLException;
import java.util.Date;
import org.guanzon.appdriver.agent.services.Model;
import org.guanzon.appdriver.base.MiscUtil;
import org.guanzon.appdriver.constant.EditMode;
import org.json.simple.JSONObject;

public class Model_Classification_Config extends Model{         
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

            ID = "sIndstCdx";
            ID2 = "sCategID1";
           
            pnEditMode = EditMode.UNKNOWN;
        } catch (SQLException e) {
            logwrapr.severe(e.getMessage());
            System.exit(1);
        }
    }
    
    public JSONObject setIndustryCode(String indutryCode){
        return setValue("sIndstCdx", indutryCode);
    }
    
    public String getIndustryCode(){
        return (String) getValue("sIndstCdx");
    }
    
    public JSONObject setCategoryId(String categoryId){
        return setValue("sCategID1", categoryId);
    }
    
    public String getCategoryId(){
        return (String) getValue("sCategID1");
    }
    
    public JSONObject setOrderFrequency(double value){
        return setValue("nOrdrFreq", value);
    }
    
    public double getOrderFrequency(){
        return Double.parseDouble(String.valueOf(getValue("nOrdrFreq")));
    }
    
    public JSONObject setSafetyA(double value){
        return setValue("nSafetyCA", value);
    }
    
    public double getSafetyA(){
        return Double.parseDouble(String.valueOf(getValue("nSafetyCA")));
    }
    
    public JSONObject setSafetyB(double value){
        return setValue("nSafetyCB", value);
    }
    
    public double getSafetyB(){
        return Double.parseDouble(String.valueOf(getValue("nSafetyCB")));
    }
    
    public JSONObject setSafetyC(double value){
        return setValue("nSafetyCC", value);
    }
    
    public double getSafetyC(){
        return Double.parseDouble(String.valueOf(getValue("nSafetyCC")));
    }
    
    public JSONObject setSafetyD(double value){
        return setValue("nSafetyCD", value);
    }
    
    public double getSafetyD(){
        return Double.parseDouble(String.valueOf(getValue("nSafetyCD")));
    }
    
    public JSONObject setVolumeRateA(double value){
        return setValue("nVolRateA", value);
    }
    
    public double getVolumeRateA(){
        return Double.parseDouble(String.valueOf(getValue("nVolRateA")));
    }
    
    public JSONObject setVolumeRateB(double value){
        return setValue("nVolRateB", value);
    }
    
    public double getVolumeRateB(){
        return Double.parseDouble(String.valueOf(getValue("nVolRateB")));
    }
    
    public JSONObject setVolumeRateC(double value){
        return setValue("nVolRateC", value);
    }
    
    public double getVolumeRateC(){
        return Double.parseDouble(String.valueOf(getValue("nVolRateC")));
    }
    
    public JSONObject setVolumeRateD(double value){
        return setValue("nVolRateD", value);
    }
    
    public double getVolumeRateD(){
        return Double.parseDouble(String.valueOf(getValue("nVolRateD")));
    }
    
    public JSONObject setMinimumStockA(double value){
        return setValue("nMinStcCA", value);
    }
    
    public double getMinimumStockA(){
        return Double.parseDouble(String.valueOf(getValue("nMinStcCA")));
    }
    
    public JSONObject setMaximumStockA(double value){
        return setValue("nMaxStcCA", value);
    }
    
    public double getMaximumStockA(){
        return Double.parseDouble(String.valueOf(getValue("nMaxStcCA")));
    }
    
    public JSONObject setMinimumStockB(double value){
        return setValue("nMinStcCB", value);
    }
    
    public double getMinimumStockB(){
        return Double.parseDouble(String.valueOf(getValue("nMinStcCB")));
    }
    
    public JSONObject setMaximumStockB(double value){
        return setValue("nMaxStcCB", value);
    }
    
    public double getMaximumStockB(){
        return Double.parseDouble(String.valueOf(getValue("nMaxStcCB")));
    }
    
    public JSONObject setMinimumStockC(double value){
        return setValue("nMinStcCC", value);
    }
    
    public double getMinimumStockC(){
        return Double.parseDouble(String.valueOf(getValue("nMinStcCC")));
    }
    
    public JSONObject setMaximumStockC(double value){
        return setValue("nMaxStcCC", value);
    }
    
    public double getMaximumStockC(){
        return Double.parseDouble(String.valueOf(getValue("nMaxStcCC")));
    }
    
    public JSONObject setMinimumStockD(double value){
        return setValue("nMinStcCD", value);
    }
    
    public double getMinimumStockD(){
        return Double.parseDouble(String.valueOf(getValue("nMinStcCD")));
    }
    
    public JSONObject setMaximumStockD(double value){
        return setValue("nMaxStcCD", value);
    }
    
    public double getMaximumStockD(){
        return Double.parseDouble(String.valueOf(getValue("nMaxStcCD")));
    }
    
    public JSONObject setMaximumQtyD(double value){
        return setValue("nMaxQtyCD", value);
    }
    
    public double getMaximumQtyD(){
        return Double.parseDouble(String.valueOf(getValue("nMaxQtyCD")));
    }
    
    public JSONObject setMinimumStockE(double value){
        return setValue("nMinStcCE", value);
    }
    
    public double getMinimumStockE(){
        return Double.parseDouble(String.valueOf(getValue("nMinStcCE")));
    }
    
    public JSONObject setMaximumStockE(double value){
        return setValue("nMaxStcCE", value);
    }
    
    public double getMaximumStockE(){
        return Double.parseDouble(String.valueOf(getValue("nMaxStcCE")));
    }
    
    public JSONObject setMinimumStockF(double value){
        return setValue("nMinStcCF", value);
    }
    
    public double getMinimumStockF(){
        return Double.parseDouble(String.valueOf(getValue("nMinStcCF")));
    }
    
    public JSONObject setMaximumStockF(double value){
        return setValue("nMaxStcCF", value);
    }
    
    public double getMaximumStockF(){
        return Double.parseDouble(String.valueOf(getValue("nMaxStcCF")));
    }
    
    public JSONObject setMaximumQtyF(double value){
        return setValue("nMaxQtyCF", value);
    }
    
    public double getMaximumQtyF(){
        return Double.parseDouble(String.valueOf(getValue("nMaxQtyCF")));
    }
    
    public JSONObject setPurchaseLeadTime(double value){
        return setValue("nPurcLdTm", value);
    }
    
    public double getPurchaseLeadTime(){
        return Double.parseDouble(String.valueOf(getValue("nPurcLdTm")));
    }
    
    public JSONObject setNoOfMonths(int value){
        return setValue("nNoMonths", value);
    }
    
    public int getNoOfMonths(){
        return Integer.parseInt(String.valueOf(getValue("nNoMonths")));
    }
    
    public JSONObject setNoMinMax(int value){
        return setValue("nNoMinMax", value);
    }
    
    public int getNoMinMax(){
        return Integer.parseInt(String.valueOf(getValue("nNoMinMax")));
    }
    
    public JSONObject setStartMinMax(int value){
        return setValue("nStrtMnMx", value);
    }
    
    public int getStartMinMax(){
        return Integer.parseInt(String.valueOf(getValue("nNoMinMax")));
    }
    
    public JSONObject setModifyingId(String modifiedBy) {
        return setValue("sModified", modifiedBy);
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
}