//package org.guanzon.cas.inv;
//
//import org.guanzon.appdriver.agent.ShowDialogFX;
//import org.guanzon.appdriver.agent.services.Parameter;
//import org.guanzon.appdriver.constant.Logical;
//import org.guanzon.appdriver.constant.UserRight;
//import org.guanzon.cas.inv.model.Model_Inv_Ledger;
//import org.guanzon.cas.inv.model.Model_Inv_Serial_Ledger;
//import org.guanzon.cas.parameter.services.ParamControllers;
//import org.json.simple.JSONObject;
//
//public class Inv_Ledger extends Parameter {
//
//    Model_Inv_Ledger poModelInvLedger;
//    ParamControllers poParams;
//
//    @Override
//    public void initialize() {
//        psRecdStat = Logical.YES;
//
//        poModelInvLedger = new Model_Inv_Ledger();
//        poModelInvLedger.setApplicationDriver(poGRider);
//        poModelInvLedger.setXML("Model_Inv_Ledger");
//        poModelInvLedger.setTableName("Inv_Ledger");
//        poModelInvLedger.initialize();
//
//        //initialize reference objects
//        poParams = new ParamControllers(poGRider, logwrapr);
//    }
//
//    @Override
//    public JSONObject isEntryOkay() {
//        poJSON = new JSONObject();
//
//        if (poGRider.getUserLevel() < UserRight.SYSADMIN) {
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
//            //todo:
//            //  more validations/use of validators per category
//        }
//
//        poJSON.put("result", "success");
//        return poJSON;
//    }
//
//    @Override
//    public Model_Inv_Ledger getModel() {
//        return poModelInvLedger;
//    }
//
//    @Override
//    public JSONObject searchRecord(String value, boolean byCode) {
//        poJSON = ShowDialogFX.Search(poGRider,
//                getSQ_Browse(),
//                value,
//                "Serial ID»Serial 01»Serial 02",
//                "sSerialID»sSerial01»sSerial02",
//                "a.sSerialID»xSerial01»xSerial02",
//                byCode ? 0 : 1);
//
//        if (poJSON != null) {
//            return poModelInvLedger.openRecord((String) poJSON.get("sSerialID"));
//        } else {
//            poJSON = new JSONObject();
//            poJSON.put("result", "error");
//            poJSON.put("message", "No record loaded.");
//            return poJSON;
//        }
//    }
//
//    @Override
//    public String getSQ_Browse() {
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
//        lsSQL = "SELECT"
//                + "   a.sStockIDx"
//                + " , a.sBranchCd"
//                + " , a.sWHouseID"
//                + " , a.nLedgerNo"
//                + " , a.dTransact"
//                + " , a.sSourceCd"
//                + " , a.sSourceNo"
//                + " , a.nQtyInxxx"
//                + " , a.nQtyOutxx"
//                + " , a.nQtyOrder"
//                + " , a.nQtyIssue"
//                + " , a.nPurPrice"
//                + " , a.nUnitPrce"
//                + " , a.nQtyOnHnd"
//                + " , a.dExpiryxx"
//                + " , a.sModified"
//                + " , a.dModified"
//                + " , b.sBarCodex xBarCodex"
//                + " , b.sDescript xDescript"
//                + " , c.sWHouseNm xWHouseNm"
//                + " FROM Inv_Ledger a"
//                + "    LEFT JOIN Inventory b ON a.sStockIDx = b.sStockIDx"
//                + "    LEFT JOIN Warehouse c ON a.sWhouseID = c.sWhouseID";
//
//        System.out.println("getSQ_Browse = = " + lsSQL);
//
////        if (!psRecdStat.isEmpty()) lsSQL = MiscUtil.addCondition(lsSQL, lsRecdStat);
//        return lsSQL;
//    }
//}
