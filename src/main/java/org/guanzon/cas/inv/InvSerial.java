//package org.guanzon.cas.inv;
//
//import org.guanzon.appdriver.agent.ShowDialogFX;
//import org.guanzon.appdriver.agent.services.Parameter;
//import org.guanzon.appdriver.constant.Logical;
//import org.guanzon.appdriver.constant.UserRight;
//import org.guanzon.cas.inv.model.Model_Inv_Serial;
//import org.guanzon.cas.parameter.services.ParamControllers;
//import org.json.simple.JSONObject;
//
//public class InvSerial extends Parameter{
//    Model_Inv_Serial poModelSerial;
//    ParamControllers poParams;
//    
//    String psBranchCd;
//    public void setBranchCode(String branchCode){
//        psBranchCd = branchCode;
//    }
//    @Override
//    public void initialize() {
//        psRecdStat = Logical.YES;
//        
//        poModelSerial = new Model_Inv_Serial();
//        poModelSerial.setApplicationDriver(poGRider);
//        poModelSerial.setXML("Model_Inv_Serial");
//        poModelSerial.setTableName("Inv_Serial");
//        poModelSerial.initialize();
//        
//        psBranchCd = poGRider.getBranchCode();
//        
//        //initialize reference objects
//        poParams = new ParamControllers(poGRider, logwrapr);
//        
//    }
//    
//    @Override
//    public JSONObject isEntryOkay() {
//        poJSON = new JSONObject();
//        
//        if (poGRider.getUserLevel() < UserRight.SYSADMIN){
//            poJSON.put("result", "error");
//            poJSON.put("message", "User is not allowed to save record.");
//            return poJSON;
//        } else {
//            poJSON = new JSONObject();
//            
////            if (poModelSerial.gets().isEmpty()){
////                poJSON.put("result", "error");
////                poJSON.put("message", "Item bar code must not be empty.");
////                return poJSON;
////            }
////            
////            if (poModelSerial.getDescription().isEmpty()){
////                poJSON.put("result", "error");
////                poJSON.put("message", "Item description must not be empty.");
////                return poJSON;
////            }
//            
//            //todo:
//            //  more validations/use of validators per category
//        }
//        
//        poJSON.put("result", "success");
//        return poJSON;
//    }
//    
//    @Override
//    public Model_Inv_Serial getModel() {
//        return poModelSerial;
//    }
//    
//    @Override
//    public JSONObject searchRecord(String value, boolean byCode) {
//        poJSON = ShowDialogFX.Search(poGRider,
//                getSQ_Browse(),
//                value,
//                "Serial ID»Serial 01»Serial 02",
//                "sSerialID»sSerial01»sSerial02",
//                "a.sSerialID»a.sSerial01»a.sSerial02",
//                byCode ? 0 : 1);
//
//        if (poJSON != null) {
//            return poModelSerial.openRecord((String) poJSON.get("sSerialID"));
//        } else {
//            poJSON = new JSONObject();
//            poJSON.put("result", "error");
//            poJSON.put("message", "No record loaded.");
//            return poJSON;
//        }
//    }
//    
//    public JSONObject searchRecordStockID(String value, boolean byCode) {
//        poJSON = ShowDialogFX.Search(poGRider,
//                getSQ_Browse(),
//                value,
//                "StockID ID»Serial ID»Serial 01»Serial 02",
//                "sStockIDx»sSerialID»sSerial01»sSerial02",
//                "a.sStockIDx»a.sSerialID»a.sSerial01»a.sSerial02",
//                byCode ? 0 : 1);
//
//        if (poJSON != null) {
//            return poModelSerial.openRecord((String) poJSON.get("sStockIDx"));
//        } else {
//            poJSON = new JSONObject();
//            poJSON.put("result", "error");
//            poJSON.put("message", "No record loaded.");
//            return poJSON;
//        }
//    }
//    
//    
//    @Override
//    public String getSQ_Browse(){
//        String lsSQL;
//        String lsRecdStat = "";
//
////        if (psRecdStat.length() > 1) {
////            for (int lnCtr = 0; lnCtr <= psRecdStat.length() - 1; lnCtr++) {
////                lsRecdStat += ", " + SQLUtil.toSQL(Character.toString(psRecdStat.charAt(lnCtr)));
////            }
////
////            lsRecdStat = "a.cRecdStat IN (" + lsRecdStat.substring(2) + ")";
////        } else {
////            lsRecdStat = "a.cRecdStat = " + SQLUtil.toSQL(psRecdStat);
////        }
//        
//         lsSQL = "SELECT" +
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
//               " LEFT JOIN Inventory b ON a.sStockIDx = b.sStockIDx";
//
//        
//        
//        System.out.println("query natin to = = " + lsSQL );
//        
////        if (!psRecdStat.isEmpty()) lsSQL = MiscUtil.addCondition(lsSQL, lsRecdStat);
//        
//        return lsSQL;
//    }
//}