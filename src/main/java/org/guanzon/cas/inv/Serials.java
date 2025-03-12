//package org.guanzon.cas.inv;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.util.ArrayList;
//import java.util.List;
//import org.guanzon.appdriver.base.GRider;
//import org.guanzon.appdriver.base.MiscUtil;
//import org.guanzon.appdriver.base.SQLUtil;
//import org.guanzon.appdriver.base.LogWrapper;
//import org.guanzon.appdriver.constant.EditMode;
//import org.guanzon.cas.inv.model.Model_Inv_Serial_Ledger;
//import org.guanzon.cas.inv.services.InvControllers;
//import org.guanzon.cas.inv.services.InvModels;
//import org.json.simple.JSONObject;
//
//public class Serials {    
//    GRider poGRider;
//    String psParent;
//
//    
//    JSONObject poJSON;
//    LogWrapper poLogWrapper;
//    
//    InvSerial poInventorySerial;
//    InvSerialLedger poInventorySerialx;
//    List<Model_Inv_Serial_Ledger> poInventorySerialLedger;
//    
//    public Serials(GRider applicationDriver,
//                    String parentClass,
//                    LogWrapper logWrapper){
//        
//        poGRider = applicationDriver;
//        psParent = parentClass;
//        poLogWrapper = logWrapper;
//        
//        poInventorySerial = new InvControllers(applicationDriver, logWrapper).InventorySerial();
//        poInventorySerialLedger = new ArrayList<>();
//    }
//        
//    public InvSerial Serial(){
//        return poInventorySerial;
//    }
//
//    
//    public Model_Inv_Serial_Ledger SerialLedger(int row){
//        return poInventorySerialLedger.get(row);
//    }
//    
//    
//    public int getSerialLedgerCount(){
//        return poInventorySerialLedger.size();
//    }    
//    
//    
//    public JSONObject addSerialLedger(){
//        poJSON = new JSONObject();
//        
//        if (poInventorySerialLedger.isEmpty()){
//            poInventorySerialLedger.add(serialLedger());            
//        } else {
//            if (!poInventorySerialLedger.get(poInventorySerialLedger.size()-1).getSerialId().isEmpty()){
//                poInventorySerialLedger.add(serialLedger());
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
//    public JSONObject deleteSerialLedger(int row){
//        poJSON = new JSONObject();
//        
//        if (poInventorySerialLedger.isEmpty()){
//            poJSON.put("result", "error");
//            poJSON.put("result", "Unable to delete ledger. Mobile list is empty.");
//            return poJSON;
//        }
//        
//        if (row >= poInventorySerialLedger.size()){
//            poJSON.put("result", "error");
//            poJSON.put("result", "Unable to delete ledger. Row is more than the mobile list.");
//            return poJSON;
//        }
//        
//        if (poInventorySerialLedger.get(row).getEditMode() != EditMode.ADDNEW){
//            poJSON.put("result", "error");
//            poJSON.put("result", "Unable to delete old mobile. You can deactivate the record instead.");
//            return poJSON;
//        }
//        
//        poInventorySerialLedger.remove(row);
//        poJSON.put("result", "success");
//        return poJSON;
//    }
//    
//    public JSONObject New(){
//        poJSON = poInventorySerial.newRecord();
//        if (!"success".equals((String) poJSON.get("result"))) return poJSON;
//        
//        poInventorySerialLedger.clear();
//        poJSON = addSerialLedger();
//        if (!"success".equals((String) poJSON.get("result"))) return poJSON;
//        
//        poJSON = new JSONObject();
//        poJSON.put("result", "success");
//        return poJSON;
//    }
//    
//    public JSONObject Save(){
//        int lnCtr;
//        
//        if (psParent.isEmpty()) 
//            poGRider.beginTrans();
//        
//        //assign modified info
//        poInventorySerial.getModel().setModifiedDate(poGRider.getServerDate());
//        
//        //save client master
//        poJSON = poInventorySerial.saveRecord();
//        
//        if (!"success".equals((String) poJSON.get("result"))){
//            if (psParent.isEmpty()) poGRider.rollbackTrans();
//            return poJSON;
//        }
//        
//        //save mobile
//        if (!poInventorySerialLedger.isEmpty()){            
//            for(lnCtr = 0; lnCtr <= poInventorySerialLedger.size()-1; lnCtr++){
//                if ((poInventorySerialLedger.get(lnCtr).getEditMode() == EditMode.ADDNEW ||
//                        poInventorySerialLedger.get(lnCtr).getEditMode() == EditMode.UPDATE) &&
//                            !poInventorySerialLedger.get(lnCtr).getSerialId().isEmpty()){
//
//                    if (poInventorySerialLedger.get(lnCtr).getEditMode() == EditMode.ADDNEW){
//                        poInventorySerialLedger.get(lnCtr).setSerialId(poInventorySerial.getModel().getSerialId());
//                    }
//                    
//                    poInventorySerialLedger.get(lnCtr).setModifiedDate(poInventorySerial.getModel().getModifiedDate());
//                    
//                    //save
//                    poJSON = poInventorySerialLedger.get(lnCtr).saveRecord();
//
//                    if (!"success".equals((String) poJSON.get("result"))){
//                        if (psParent.isEmpty()) poGRider.rollbackTrans();
//                        return poJSON;
//                    }
//                }
//            }
//        }
//        
//        if (psParent.isEmpty()) 
//            poGRider.commitTrans();
//        
//        poJSON = new JSONObject();
//        poJSON.put("result", "success");
//        return poJSON;        
//    }
//    
//    private Model_Inv_Serial_Ledger serialLedger(){
//        return new InvModels(poGRider).InventorySerialLedger();
//    }
//
//    private Model_Inv_Serial_Ledger serialLedger(String serialId, String sourceCode, String sourceNo){
//        Model_Inv_Serial_Ledger object = new InvModels(poGRider).InventorySerialLedger();
//        
//        JSONObject loJSON = object.openRecord(serialId, sourceCode, sourceNo);
//        
//        if ("success".equals((String) loJSON.get("result"))){
//            return object;
//        } else {
//            return new InvModels(poGRider).InventorySerialLedger();
//        }        
//    }
//    
//    public JSONObject OpenSerialLedger(String fsValue) {
//    StringBuilder lsSQL = new StringBuilder("SELECT " +
//                "  a.sSerialID, " +
//                "  a.sBranchCd, " +
//                "  a.nLedgerNo, " +
//                "  a.dTransact, " +
//                "  a.sSourceCd, " +
//                "  a.sSourceNo, " +
//                "  a.cSoldStat, " +
//                "  a.cLocation, " +
//                "  b.sBranchNm " +
//                "FROM Inv_Serial_Ledger a " + 
//                " LEFT JOIN Branch b on b.sBranchCd = a.sBranchCd");
//
//    // Add condition to the query
//    lsSQL.append(MiscUtil.addCondition("", "sSerialID = " + SQLUtil.toSQL(fsValue)));
//    lsSQL.append(" ORDER BY nLedgerNo");
//
//    System.out.println("Executing SQL: " + lsSQL.toString());
//
//    ResultSet loRS = poGRider.executeQuery(lsSQL.toString());
//    poJSON = new JSONObject();
//
//    try {
//        int lnctr = 0;
//
//        if (MiscUtil.RecordCount(loRS) >= 0) {
//            poInventorySerialLedger = new ArrayList<>();
//            while (loRS.next()) {
//                // Print the result set
//                
//                System.out.println("sSerialID: " + loRS.getString("sSerialID"));
//                System.out.println("sBranchNme: " + loRS.getString("sBranchNm"));
//                System.out.println("nLedgerNo: " + loRS.getInt("nLedgerNo"));
//                System.out.println("dTransact: " + loRS.getDate("dTransact"));
//                System.out.println("sSourceNo: " + loRS.getString("sSourceNo"));
//                System.out.println("sSourceCd: " + loRS.getString("sSourceCd"));
//                System.out.println("cSoldStat: " + loRS.getString("cSoldStat"));
//                System.out.println("cLocation: " + loRS.getString("cLocation"));
//                System.out.println("------------------------------------------------------------------------------" );
//
//                poInventorySerialLedger.add(serialLedger(loRS.getString("sSerialID"), loRS.getString("sSourceCd"), loRS.getString("sSourceNo")));
//                poInventorySerialLedger.get(poInventorySerialLedger.size() - 1)
//                        .openRecord(loRS.getString("sSerialID"), loRS.getString("sSourceCd"), loRS.getString("sSourceNo"));
//                lnctr++;
//            }
//
//            System.out.println("Records found: " + lnctr);
//            poJSON.put("result", "success");
//            poJSON.put("message", "Record loaded successfully.");
//            
//            
//        }else{
//                poInventorySerialLedger = new ArrayList<>();
//                addSerialLedger();
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
//    
//    
//}
