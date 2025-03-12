//package org.guanzon.cas.inv;
//
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.time.LocalDate;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//import org.guanzon.appdriver.base.GRider;
//import org.guanzon.appdriver.base.MiscUtil;
//import org.guanzon.appdriver.base.SQLUtil;
//import org.guanzon.appdriver.base.LogWrapper;
//import org.guanzon.appdriver.constant.EditMode;
//import org.guanzon.cas.inv.model.Model_Inv_Ledger;
//import org.guanzon.cas.inv.model.Model_Inv_Serial;
//import org.guanzon.cas.inv.services.InvControllers;
//import org.guanzon.cas.inv.services.InvModels;
//import org.json.simple.JSONObject;
//
//public class Inv {
//
//    GRider poGRider;
//    String psParent;
//
//    JSONObject poJSON;
//    LogWrapper poLogWrapper;
//
//    Inv_Master poInventoryMaster;
//    InvSerial poSerial;
//    List<Model_Inv_Ledger> poInventoryLedger;
//    List<Model_Inv_Serial> poSerialLedger;
//
//    public Inv(GRider applicationDriver,
//            String parentClass,
//            LogWrapper logWrapper) {
//
//        poGRider = applicationDriver;
//        psParent = parentClass;
//        poLogWrapper = logWrapper;
//
//        poInventoryMaster = new InvControllers(applicationDriver, logWrapper).InventoryMaster();
//        poSerial = new InvControllers(applicationDriver, logWrapper).InventorySerial();
//        poInventoryLedger = new ArrayList<>();
//        poSerialLedger = new ArrayList<>();
//    }
//
//    public Inv_Master InvMaster() {
//        return poInventoryMaster;
//    }
//
//    public Model_Inv_Ledger InvLedger(int row) {
//        return poInventoryLedger.get(row);
//    }
//    public InvSerial InvSerials() {
//        return poSerial;
//    }
//    public Model_Inv_Serial InvSerial(int row) {
//        return poSerialLedger.get(row);
//    }
//
//    
//    public int getInvLedgerCount() {
//        return poInventoryLedger.size();
//    }
//    public int getInvSerialCount() {
//        return poSerialLedger.size();
//    }
//    public JSONObject addInvSerial() {
//        poJSON = new JSONObject();
//
//        if (poSerialLedger.isEmpty()) {
//            poSerialLedger.add(invSerial());
//        } else {
//            if (!poSerialLedger.get(poSerialLedger.size() - 1).getStockId().isEmpty()) {
//                poSerialLedger.add(invSerial());
//            } else {
//                poJSON.put("result", "error");
//                poJSON.put("message", "Unable to add serialLedger.");
//                return poJSON;
//            }
//        }
//
//        poJSON.put("result", "success");
//        return poJSON;
//    }
//    
//    
//
//    public JSONObject addInvLedger() {
//        poJSON = new JSONObject();
//
//        if (poInventoryLedger.isEmpty()) {
//            poInventoryLedger.add(invLedger());
//        } else {
//            if (!poInventoryLedger.get(poInventoryLedger.size() - 1).getStockId().isEmpty()) {
//                poInventoryLedger.add(invLedger());
//            } else {
//                poJSON.put("result", "error");
//                poJSON.put("message", "Unable to add serialLedger.");
//                return poJSON;
//            }
//        }
//
//        poJSON.put("result", "success");
//        return poJSON;
//    }
//    public JSONObject deleteSerialLedger(int row) {
//        poJSON = new JSONObject();
//
//        if (poSerialLedger.isEmpty()) {
//            poJSON.put("result", "error");
//            poJSON.put("result", "Unable to delete ledger. Mobile list is empty.");
//            return poJSON;
//        }
//
//        if (row >= poSerialLedger.size()) {
//            poJSON.put("result", "error");
//            poJSON.put("result", "Unable to delete ledger. Row is more than the mobile list.");
//            return poJSON;
//        }
//
//        if (poSerialLedger.get(row).getEditMode() != EditMode.ADDNEW) {
//            poJSON.put("result", "error");
//            poJSON.put("result", "Unable to delete old mobile. You can deactivate the record instead.");
//            return poJSON;
//        }
//
//        poSerialLedger.remove(row);
//        poJSON.put("result", "success");
//        return poJSON;
//    }
//
//    public JSONObject deleteInvLedger(int row) {
//        poJSON = new JSONObject();
//
//        if (poInventoryLedger.isEmpty()) {
//            poJSON.put("result", "error");
//            poJSON.put("result", "Unable to delete ledger. Mobile list is empty.");
//            return poJSON;
//        }
//
//        if (row >= poInventoryLedger.size()) {
//            poJSON.put("result", "error");
//            poJSON.put("result", "Unable to delete ledger. Row is more than the mobile list.");
//            return poJSON;
//        }
//
//        if (poInventoryLedger.get(row).getEditMode() != EditMode.ADDNEW) {
//            poJSON.put("result", "error");
//            poJSON.put("result", "Unable to delete old mobile. You can deactivate the record instead.");
//            return poJSON;
//        }
//
//        poInventoryLedger.remove(row);
//        poJSON.put("result", "success");
//        return poJSON;
//    }
//
//    public JSONObject New() {
//        poJSON = poInventoryMaster.newRecord();
//        if (!"success".equals((String) poJSON.get("result"))) {
//            return poJSON;
//        }
//
//        poInventoryLedger.clear();
//        poJSON = addInvLedger();
//        if (!"success".equals((String) poJSON.get("result"))) {
//            return poJSON;
//        }
//        poSerialLedger.clear();
//        poJSON = addInvSerial();
//        if (!"success".equals((String) poJSON.get("result"))) {
//            return poJSON;
//        }
//
//        poJSON = new JSONObject();
//        poJSON.put("result", "success");
//        return poJSON;
//    }
//
//    public JSONObject Save() {
//        int lnCtr;
//
//        if (psParent.isEmpty()) {
//            poGRider.beginTrans();
//        }
//
//        //assign modified info
//        poInventoryMaster.getModel().setModifiedDate(poGRider.getServerDate());
//
//        //save client master
//        poJSON = poInventoryMaster.saveRecord();
//
//        if (!"success".equals((String) poJSON.get("result"))) {
//            if (psParent.isEmpty()) {
//                poGRider.rollbackTrans();
//            }
//            return poJSON;
//        }
//
//        //save inv ledger
//        if (!poInventoryLedger.isEmpty()) {
//            for (lnCtr = 0; lnCtr <= poInventoryLedger.size() - 1; lnCtr++) {
//                if ((poInventoryLedger.get(lnCtr).getEditMode() == EditMode.ADDNEW
//                        || poInventoryLedger.get(lnCtr).getEditMode() == EditMode.UPDATE)
//                        && !poInventoryLedger.get(lnCtr).getStockId().isEmpty()) {
//
//                    if (poInventoryLedger.get(lnCtr).getEditMode() == EditMode.ADDNEW) {
//                        poInventoryLedger.get(lnCtr).setStockId(poInventoryMaster.getModel().getStockId());
//                    }
//
//                    poInventoryLedger.get(lnCtr).setModifiedDate(poInventoryMaster.getModel().getModifiedDate());
//
//                    //save
//                    poJSON = poInventoryLedger.get(lnCtr).saveRecord();
//
//                    if (!"success".equals((String) poJSON.get("result"))) {
//                        if (psParent.isEmpty()) {
//                            poGRider.rollbackTrans();
//                        }
//                        return poJSON;
//                    }
//                }
//            }
//        }
//        
//        //save serial ledger
////        if (!poSerial.isEmpty()) {
////            for (lnCtr = 0; lnCtr <= poSerial.size() - 1; lnCtr++) {
////                if ((poSerial.get(lnCtr).getEditMode() == EditMode.ADDNEW
////                        || poSerial.get(lnCtr).getEditMode() == EditMode.UPDATE)
////                        && !poSerial.get(lnCtr).getStockId().isEmpty()) {
////
////                    if (poSerial.get(lnCtr).getEditMode() == EditMode.ADDNEW) {
////                        poSerial.get(lnCtr).setStockId(poInventoryMaster.getModel().getStockId());
////                    }
////
////                    poInventoryLedger.get(lnCtr).setModifiedDate(poInventoryMaster.getModel().getModifiedDate());
////
////                    //save
////                    poJSON = poInventoryLedger.get(lnCtr).saveRecord();
////
////                    if (!"success".equals((String) poJSON.get("result"))) {
////                        if (psParent.isEmpty()) {
////                            poGRider.rollbackTrans();
////                        }
////                        return poJSON;
////                    }
////                }
////            }
////        }
//
//        if (psParent.isEmpty()) {
//            poGRider.commitTrans();
//        }
//
//        poJSON = new JSONObject();
//        poJSON.put("result", "success");
//        return poJSON;
//    }
//
//    private Model_Inv_Ledger invLedger() {
//        return new InvModels(poGRider).InventoryLedger();
//    }
//    
//    private Model_Inv_Serial invSerial() {
//        return new InvModels(poGRider).InventorySerial();
//    }
//
//    private Model_Inv_Ledger inventoryLedger(String serialId,String sBranchCd,String sWHouseID, String sourceCode, String sourceNo) {
//        Model_Inv_Ledger object = new InvModels(poGRider).InventoryLedger();
//
//        JSONObject loJSON = object.openRecord(serialId,sBranchCd,sWHouseID, sourceCode, sourceNo);
//
//        if ("success".equals((String) loJSON.get("result"))) {
//            return object;
//        } else {
//            return new InvModels(poGRider).InventoryLedger();
//        }
//    }
//    
//    private Model_Inv_Serial inventorySerial(String serialId,String sBranchCd) {
//        Model_Inv_Serial object = new InvModels(poGRider).InventorySerial();
//
//        JSONObject loJSON = object.openRecord(serialId,sBranchCd);
//
//        if ("success".equals((String) loJSON.get("result"))) {
//            return object;
//        } else {
//            return new InvModels(poGRider).InventorySerial();
//        }
//    }
//
//    public JSONObject OpenInvLedger(String fsStockID,LocalDate fdDateFrom, LocalDate fdDateThru ) {
//        StringBuilder lsSQL = new StringBuilder("SELECT a.sStockIDx, "
//                + " a.dTransact, "
//                + " c.sBranchNm, "
//                + " a.sSourceCd, "
//                + " a.sSourceNo, "
//                + " a.nQtyInxxx, "
//                + " a.nQtyOutxx, "
//                + " a.nQtyOnHnd, "
//                + " a.nLedgerNo, "
//                + " a.sWHouseID, "
//                + " c.sBranchCd "
//                + " FROM Inv_Ledger a "
//                + " LEFT JOIN Inv_Master b ON a.sStockIDx = b.sStockIDx "
//                + " LEFT JOIN Branch c ON a.sBranchCd = c.sBranchCd ");
//
//        // Use SQLUtil.toSQL for handling the dates
//        String condition = "a.sStockIDx = " + SQLUtil.toSQL(fsStockID)
//                + " AND a.dTransact BETWEEN " + SQLUtil.toSQL(fdDateFrom.toString())
//                + " AND " + SQLUtil.toSQL(fdDateThru.toString());
//        lsSQL.append(MiscUtil.addCondition("", condition));
//        lsSQL.append(" ORDER BY a.nLedgerNo ASC");
//
//        System.out.println("Executing SQL: " + lsSQL.toString());
//
//        ResultSet loRS = poGRider.executeQuery(lsSQL.toString());
//        JSONObject poJSON = new JSONObject();
//
//        try {
//            int lnctr = 0;
//
//            if (MiscUtil.RecordCount(loRS) >= 0) {
//                poInventoryLedger = new ArrayList<>();
//                while (loRS.next()) {
//                    // Print the result set
//
//                    System.out.println("sSerialID: " + loRS.getString("sStockIDx"));
//                    System.out.println("sBranchNme: " + loRS.getString("sBranchNm"));
//                    System.out.println("nLedgerNo: " + loRS.getInt("nLedgerNo"));
//                    System.out.println("dTransact: " + loRS.getDate("dTransact"));
//                    System.out.println("sSourceNo: " + loRS.getString("sSourceNo"));
//                    System.out.println("sSourceCd: " + loRS.getString("sSourceCd"));
//                    System.out.println("cSoldStat: " + loRS.getString("sBranchCd"));
//                    System.out.println("cLocation: " + loRS.getString("sWHouseID"));
//                    System.out.println("------------------------------------------------------------------------------");
//
//                    poInventoryLedger.add(inventoryLedger(loRS.getString("sStockIDx"),loRS.getString("sBranchCd"),loRS.getString("sWHouseID"), loRS.getString("sSourceCd"), loRS.getString("sSourceNo")));
//                    poInventoryLedger.get(poInventoryLedger.size() - 1)
//                            .openRecord(loRS.getString("sStockIDx"), loRS.getString("sBranchCd"),loRS.getString("sWHouseID"), loRS.getString("sSourceCd"), loRS.getString("sSourceNo"));
//                    lnctr++;
//                }
//
//                System.out.println("Records found: " + lnctr);
//                poJSON.put("result", "success");
//                poJSON.put("message", "Record loaded successfully.");
//
//            } else {
//                poInventoryLedger = new ArrayList<>();
//                addInvLedger();
//                poJSON.put("result", "error");
//                poJSON.put("continue", true);
//                poJSON.put("message", "No record found .");
//            }
//            MiscUtil.close(loRS);
//        } catch (SQLException e) {
//            poJSON.put("result", "error");
//            poJSON.put("message", e.getMessage());
//        }
//        return poJSON;
//    }
//
//    public JSONObject OpenInvSerialLedger(String fsStockID, String fsUnitType ) {
//        StringBuilder lsSQL = new StringBuilder("SELECT" +
//               "   a.sSerialID" +
//               " , a.sBranchCd" +
//               " , a.sSerial01" +
//               " , a.sSerial02" +
//               " , a.nUnitPrce" +
//               " , a.sStockIDx" +
//               " , a.cLocation" +
//               " , a.cSoldStat" +
//               " , a.cUnitType" +
//               " , a.sCompnyID" +
//               " , a.sWarranty" +
//               " , a.dModified" +
//               " , b.sBrandIDx" +
//               " , b.sBarCodex AS xBarCodex" + 
//               " , b.sDescript AS xDescript" + 
//               " FROM Inv_Serial a" +
//               " LEFT JOIN Inventory b ON a.sStockIDx = b.sStockIDx");
//
//        // Use SQLUtil.toSQL for handling the dates
//        String condition = "a.sStockIDx = " + SQLUtil.toSQL(fsStockID)
//                + " AND a.cUnitType like " + SQLUtil.toSQL(fsUnitType);
//        lsSQL.append(MiscUtil.addCondition("", condition));
////        lsSQL.append(" ORDER BY a.nLedgerNo ASC");
//
//        System.out.println("Executing SQL: " + lsSQL.toString());
//
//        ResultSet loRS = poGRider.executeQuery(lsSQL.toString());
//        JSONObject poJSON = new JSONObject();
//
//        try {
//            int lnctr = 0;
//
//            if (MiscUtil.RecordCount(loRS) >= 0) {
//                poSerialLedger = new ArrayList<>();
//                while (loRS.next()) {
//                    // Print the result set
//
//                    System.out.println("sSerialID: " + loRS.getString("sSerialID"));
//                    System.out.println("sSerial01: " + loRS.getString("sSerial01"));
//                    System.out.println("sSerial02: " + loRS.getString("sSerial02"));
//                    System.out.println("------------------------------------------------------------------------------");
//
//                    poSerialLedger.add(inventorySerial(loRS.getString("sSerialID"),loRS.getString("sBranchCd")));
//                    poSerialLedger.get(poSerialLedger.size() - 1)
//                            .openRecord(loRS.getString("sSerialID"),loRS.getString("sBranchCd"));
//                    lnctr++;
//                }
//
//                System.out.println("Records found: " + lnctr);
//                poJSON.put("result", "success");
//                poJSON.put("message", "Record loaded successfully.");
//
//            } else {
//                poSerialLedger = new ArrayList<>();
//                addInvLedger();
//                poJSON.put("result", "error");
//                poJSON.put("continue", true);
//                poJSON.put("message", "No record found .");
//            }
//            MiscUtil.close(loRS);
//        } catch (SQLException e) {
//            poJSON.put("result", "error");
//            poJSON.put("message", e.getMessage());
//        }
//        System.out.println("RESULT == " + poJSON);
//        return poJSON;
//    }
//    
//    
//}
