package org.guanzon.cas.inv;


// InventoryTransaction poTrans = InventoryTransaction(foGRider, fsBranchCD);
// ??poTrans.initTransaction(String fsSourceCD, String fsSourceNo, Date fdTransact);
// poTrans.JobOrder(String fsSourceNo, Date fdTransact)
// ++++ non-serial
// poTrans.addDetail(fsIndstCdx, fsStockIDx, fcConditnx, fnQuantity, fnOrderQty, fnUnitPrice)
// ++++ serial
// poTrans.addSerial(fsSerialID, fnOrderQty, fnUnitPrice);
// poTrans.SaveTransaction();

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.guanzon.appdriver.base.GRiderCAS;
import org.guanzon.appdriver.base.GuanzonException;
import org.guanzon.appdriver.base.SQLUtil;
import org.json.simple.JSONObject;

public class InventoryTransaction {
    private GRiderCAS poDriver;
    private String psBranchCD; 
    private String psIndstCdx;
    private String psUserIDxx;
    private boolean pbIsWhsexx;
    //private boolean pbDeductxx = true; 
    private String psSourceNo = "";
    private String psSourceCD = "";
    private Date pdTranDate;
    
    private List<DetailEntry> paDetailEntry;
    private List<SerialEntry> paSerialEntry;
    
    public InventoryTransaction(GRiderCAS foGRider){
        poDriver = foGRider;
        psBranchCD = poDriver.getBranchCode();
        psIndstCdx = poDriver.getIndustry();
        psUserIDxx = poDriver.getUserID();
        pbIsWhsexx = poDriver.isWarehouse();
        
        paDetailEntry = new ArrayList<>();
        paSerialEntry = new ArrayList<>();
    }

    public InventoryTransaction(GRiderCAS foGRider, String fsBranchCD, boolean fbIsWarehouse, String fsIndstCdx, String fsUserIDxx){
        poDriver = foGRider;
        psBranchCD = fsBranchCD;
        psIndstCdx = fsIndstCdx;
        psUserIDxx = fsUserIDxx;
        pbIsWhsexx = fbIsWarehouse;
        
        paDetailEntry = new ArrayList<>();
        paSerialEntry = new ArrayList<>();
    }

    public void JobOrder(String fsSourceNo, Date fdTransact){
        initTransaction(InvTransCons.BRANCH_JOBORDER, fsSourceNo, fdTransact);
    }

    public void BranchOrder(String fsSourceNo, Date fdTransact){
        initTransaction(InvTransCons.BRANCH_ORDER, fsSourceNo, fdTransact);
    }

    public void BranchOrderCancellation(String fsSourceNo, Date fdTransact){
        initTransaction(InvTransCons.BRANCH_ORDER_BRANCH_CANCELLATION, fsSourceNo, fdTransact);
    }

    public void BranchOrderWHSECancellation(String fsSourceNo, Date fdTransact){
        initTransaction(InvTransCons.BRANCH_ORDER_WAREHOUSE_CANCELLATION, fsSourceNo, fdTransact);
    }
    
    public void BranchOrderWHSEConfirmation(String fsSourceNo, Date fdTransact){
        initTransaction(InvTransCons.BRANCH_ORDER_CONFIRMATION, fsSourceNo, fdTransact);
    }

    public void Delivery(String fsSourceNo, Date fdTransact){
        initTransaction(InvTransCons.BRANCH_TRANSFER, fsSourceNo, fdTransact);
    }

    public void DeliveryAcceptance(String fsSourceNo, Date fdTransact){
        initTransaction(InvTransCons.BRANCH_TRANSFER_ACCEPTANCE, fsSourceNo, fdTransact);
    }

    public void CustomerOrder(String fsSourceNo, Date fdTransact){
        initTransaction(InvTransCons.CUSTOMER_ORDER, fsSourceNo, fdTransact);
    }

    public void CustomerOrderCancellation(String fsSourceNo, Date fdTransact){
        initTransaction(InvTransCons.CUSTOMER_ORDER_CANCELLATION, fsSourceNo, fdTransact);
    }

    public void GCardRedemption(String fsSourceNo, Date fdTransact){
        initTransaction(InvTransCons.GCARD_REDEMPTION, fsSourceNo, fdTransact);
    }

    public void Impound(String fsSourceNo, Date fdTransact){
        initTransaction(InvTransCons.IMPOUND, fsSourceNo, fdTransact);
    }

    public void ImpoundRelease(String fsSourceNo, Date fdTransact){
        initTransaction(InvTransCons.IMPOUND_RELEASE, fsSourceNo, fdTransact);
    }
    
    public void PurchaseOrder(String fsSourceNo, Date fdTransact){
        initTransaction(InvTransCons.PURCHASE_ORDER, fsSourceNo, fdTransact);
    }

    /**
     * Initiate Purchase Order cancellation directly through the Purchase Order Transaction module.
     * + Purchase order should not have a partial cancellation nor partial delivery 
     * @param fsSourceNo
     * @param fdTransact 
     */
    public void PO_Full_Cancellation(String fsSourceNo, Date fdTransact){
        initTransaction(InvTransCons.PURCHASE_ORDER_FULL_CANCELLATION, fsSourceNo, fdTransact);
    }

    /**
     * 
     * @param fsSourceNo
     * @param fdTransact 
     */
    public void PO_ND_Cancellation(String fsSourceNo, Date fdTransact){
        initTransaction(InvTransCons.PURCHASE_ORDER_ND_CANCELLATION, fsSourceNo, fdTransact);
    }

    /**
     * Initiate the cancellation thru the PO Cancellation Module
     * @param fsSourceNo
     * @param fdTransact 
     */
    public void PO_Partial_Cancellation(String fsSourceNo, Date fdTransact){
        initTransaction(InvTransCons.PURCHASE_ORDER_PARTIAL_CANCELLATION, fsSourceNo, fdTransact);
    }
    
    
    public void PurchaseReceiving(String fsSourceNo, Date fdTransact){
        initTransaction(InvTransCons.PURCHASE_RECEIVING, fsSourceNo, fdTransact);
    }

    public void PurchaseReplacement(String fsSourceNo, Date fdTransact){
        initTransaction(InvTransCons.PURCHASE_REPLACEMENT, fsSourceNo, fdTransact);
    }

    public void PurchaseReturn(String fsSourceNo, Date fdTransact){
        initTransaction(InvTransCons.PURCHASE_RETURN, fsSourceNo, fdTransact);
    }

    public void RetailOrder(String fsSourceNo, Date fdTransact){
        initTransaction(InvTransCons.RETAIL_ORDER, fsSourceNo, fdTransact);
    }

    public void RetailOrderCancellation(String fsSourceNo, Date fdTransact){
        initTransaction(InvTransCons.RETAIL_ORDER_CANCELLATION, fsSourceNo, fdTransact);
    }
    
    public void Sales(String fsSourceNo, Date fdTransact){
        initTransaction(InvTransCons.SALES, fsSourceNo, fdTransact);
    }

    public void SalesGiveaway(String fsSourceNo, Date fdTransact){
        initTransaction(InvTransCons.SALES_GIVEAWAY, fsSourceNo, fdTransact);
    }

    public void GiveawayRelease(String fsSourceNo, Date fdTransact){
        initTransaction(InvTransCons.SALES_GIVEAWAY_RELEASE, fsSourceNo, fdTransact);
    }
    
    public void SalesReplacement(String fsSourceNo, Date fdTransact){
        initTransaction(InvTransCons.SALES_REPLACEMENT, fsSourceNo, fdTransact);
    }

    public void SalesReturn(String fsSourceNo, Date fdTransact){
        initTransaction(InvTransCons.SALES_RETURN, fsSourceNo, fdTransact);
    }

    public void WarrantyRelease(String fsSourceNo, Date fdTransact){
        initTransaction(InvTransCons.WARRANTY_RELEASE, fsSourceNo, fdTransact);
    }
    
    public void CreditMemo(String fsSourceNo, Date fdTransact){
        initTransaction(InvTransCons.CREDIT_MEMO, fsSourceNo, fdTransact);
    }
    public void DebitMemo(String fsSourceNo, Date fdTransact){
        initTransaction(InvTransCons.DEBIT_MEMO, fsSourceNo, fdTransact);
    }

    private void initTransaction(String fsSourceCD, String fsSourceNo, Date fdTransact){
        psSourceCD = fsSourceCD;
        psSourceNo = fsSourceNo;
        pdTranDate = fdTransact;
    }
    
    public void addDetail(String fsIndstCdx, String fsStockIDx, String fcConditnx, double fnQuantity, double fnOrderQty, double fnUnitPrice ) throws SQLException, GuanzonException{
        if(psSourceNo.isEmpty()){
            //throw new GuanzonException(GuanzonException.GE_SEQUENCE_EXCEPTION, "Invalid Source No detected!");
            throw new GuanzonException(GuanzonException.GE_SEQUENCE_EXCEPTION);
        }
        
        //load sstockid  
        String lsSQL = "SELECT DISTINCT" + 
                            "  a.sWHouseID" + 
                            ", a.sStockIDx" +
                            ", a.sBranchCD" +
                            ", a.cConditnx" + 
                            ", b.nPurPrice" + 
                            ", b.cSerialze" +
                            ", b.sStockIDx sStockIDy" +
                       " FROM Inventory b" +  
                          " LEFT JOIN Inv_Master a" +
                                " ON b.sStockIDx = a.sStockIDx" +
                               " AND b.sIndstCdx = a.sIndstCdx" + 
                               " AND a.sBranchCd = " + SQLUtil.toSQL(psBranchCD) +
                               " AND a.cConditnx = " + SQLUtil.toSQL(fcConditnx) + 
                               " AND a.nQtyOnHnd > 0" +
                       " WHERE b.sStockIDx = " + SQLUtil.toSQL(fsStockIDx) +
                         " AND b.sIndstCdx = " + SQLUtil.toSQL(fsIndstCdx) +
                       " ORDER  BY a.sStockIDx DESC";        

        ResultSet loRS = poDriver.executeQuery(lsSQL);
        
//        //create scrollable ResultSet
//        Statement stmt = poDriver.getGConnection().getConnection().createStatement(
//            ResultSet.TYPE_SCROLL_INSENSITIVE,
//            ResultSet.CONCUR_READ_ONLY
//        );
//        ResultSet loRS = stmt.executeQuery(lsSQL);
        
        if(!loRS.next()){
            //throw new GuanzonException(GuanzonException.GE_NOTFOUND_EXCEPTION, "Stock Item does not exist!");
            throw new GuanzonException(GuanzonException.GE_HOSTNAME_EXCEPTION);
        }

        if(fnQuantity > 0){
            if(loRS.getString("sStockIDx") == null){
                //If item is not existing and transaction type is not accept delivery, throw an error
                if(!(InvTransCons.BRANCH_TRANSFER_ACCEPTANCE).toUpperCase().contains(psSourceCD.toUpperCase())){
                    //throw new GuanzonException(GuanzonException.GE_NOTFOUND_EXCEPTION, "Please create the inventory for the branch!");
                    throw new GuanzonException(GuanzonException.GE_HOSTNAME_EXCEPTION);
                }
            }
            
            //If among transaction only orders then transfer quantity to orders and set it to 0
            if(InvTransCons.getOrderTrans().toUpperCase().contains(psSourceCD.toUpperCase())){
                fnOrderQty = fnQuantity;
                fnQuantity = 0;
            }
            else{
                //Check if item is serialized
                if(loRS.getString("cSerialze").equals("1")){
                    //If item is serialized and transaction type is not credit/debit memo, throw an error
                    if(!(InvTransCons.CREDIT_MEMO + ":" + InvTransCons.DEBIT_MEMO).toUpperCase().contains(psSourceCD.toUpperCase())){
                        //throw new GuanzonException(GuanzonException.GE_SEQUENCE_EXCEPTION, "Invalid function called! Please use addSerial if inventory has serial.");
                        throw new GuanzonException(GuanzonException.GE_SEQUENCE_EXCEPTION);
                    }
                }
            }
        }

        //Assumed that programmer have exercise due deligence to determine that
        //transaction is valid(example: item has quantity for transactions such as sales, branch transfer) 

        //Create entry
        DetailEntry loDetail;
        if(loRS.getString("sStockIDx") == null){
            loDetail = new DetailEntry(
                      fsIndstCdx
                    , fsStockIDx
                    , "001"
                    , fnQuantity
                    , fnOrderQty
                    , null
                    , fcConditnx
                    , loRS.getDouble("nPurPrice")
                    , fnUnitPrice);
        }
        else{
            loDetail = new DetailEntry(
                      fsIndstCdx
                    , fsStockIDx
                    , loRS.getString("sWHouseID")
                    , fnQuantity
                    , fnOrderQty
                    , null
                    , fcConditnx
                    , loRS.getDouble("nPurPrice")
                    , fnUnitPrice);
        }
        
        //Is this the first entry
        if(paDetailEntry.isEmpty()){
            paDetailEntry.add(loDetail);
        }
        else{
            // Search and update or add
            Optional<DetailEntry> match = paDetailEntry.stream()
                .filter(p -> p.psIndstCdx.equalsIgnoreCase(loDetail.psIndstCdx) 
                          && p.psStockIDx.equalsIgnoreCase(loDetail.psStockIDx)
                          && p.psWHouseID.equalsIgnoreCase(loDetail.psWHouseID)
                          && p.pcConditnx.equalsIgnoreCase(loDetail.pcConditnx))
                .findFirst();

            if (match.isPresent()) {
                // Update the quantity and order
                match.get().pnQuantity += loDetail.pnQuantity;
                match.get().pnOrderQty += loDetail.pnOrderQty;
            } else {
                // Add new product
                paDetailEntry.add(loDetail);
            }
        }
        
        //perform testing of accuracy of data extracted here
        do{
            System.out.println(loRS.getString("sStockIDy") + ":" + loRS.getString("sBranchCD") + ":" + loRS.getString("sStockIDx"));
        }while(loRS.next());
        
    }

    //public void addSerial(String fsIndstCdx, String fsSerialID, boolean fbWithOrdr, String fcSoldStat, String fcLocation, double fnUnitPrice, String fsWHouseID) throws SQLException, GuanzonException{
    public void addSerial(String fsIndstCdx, String fsSerialID, boolean fbWithOrdr, double fnUnitPrice, String fsWHouseID) throws SQLException, GuanzonException{
        String lsSQL;
        if(!fsWHouseID.isEmpty()){
            lsSQL = "SELECT" + 
                        "  IFNULL(a.sWHouseID, " + SQLUtil.toSQL(fsWHouseID) + ") sWHouseID" + 
                        ", b.sStockIDx" +
                        ", c.cConditnx" + 
                        ", b.nPurPrice" + 
                        ", b.cSerialze" +
                        ", c.sSerialID" +
                        ", a.sBranchCd" +
                        ", c.cSoldStat" +
                        ", c.cLocation" +
                   " FROM Inventory b" +  
                      " LEFT JOIN Inv_Master a" +
                            " ON b.sStockIDx = a.sStockIDx" +
                           " AND b.sIndstCdx = a.sIndstCdx" + 
                           " AND a.sBranchCd = " + SQLUtil.toSQL(psBranchCD) +
                           " AND a.sWHouseID = " + SQLUtil.toSQL(fsWHouseID) + 
                      " LEFT JOIN Inv_Serial c" +
                            " ON b.sStockIDx = c.sStockIDx" +
                           " AND b.sIndstCdx = c.sIndstCdx" + 
                   " WHERE c.sSerialID = " + SQLUtil.toSQL(fsSerialID) +
                     " AND c.sIndstCdx = " + SQLUtil.toSQL(fsIndstCdx);
            
        }
        else{
            lsSQL = "SELECT" + 
                        "  IFNULL(a.sWHouseID, '001') sWHouseID" + 
                        ", b.sStockIDx" +
                        ", c.cConditnx" + 
                        ", b.nPurPrice" + 
                        ", b.cSerialze" +
                        ", c.sSerialID" +
                        ", a.sBranchCd" +
                        ", c.cSoldStat" +
                        ", c.cLocation" +
                   " FROM Inventory b" +  
                      " LEFT JOIN Inv_Master a" +
                            " ON b.sStockIDx = a.sStockIDx" +
                           " AND b.sIndstCdx = a.sIndstCdx" + 
                           " AND a.sBranchCd = " + SQLUtil.toSQL(psBranchCD) +
                      " LEFT JOIN Inv_Serial c" +
                            " ON b.sStockIDx = c.sStockIDx" +
                           " AND b.sIndstCdx = c.sIndstCdx" + 
                   " WHERE c.sSerialID = " + SQLUtil.toSQL(fsSerialID) +
                     " AND c.sIndstCdx = " + SQLUtil.toSQL(fsIndstCdx);
        }

        System.out.println(lsSQL);
        ResultSet loRS = poDriver.executeQuery(lsSQL);
        
        if(!loRS.next()){
            //throw new GuanzonException(GuanzonException.GE_NOTFOUND_EXCEPTION, "Serial ID does not exist!");
            throw new GuanzonException(GuanzonException.GE_HOSTNAME_EXCEPTION);
        }

        String lcSoldStat = loRS.getString("cSoldStat");
        String lcLocation = loRS.getString("cLocation");
        String lcConditnx = loRS.getString("cConditnx");
        switch(psSourceCD){
            case InvTransCons.IMPOUND:
                //Warehouse/Customer
                lcConditnx = "1";
                lcLocation = pbIsWhsexx ? "0" : "1";
                break;
            case InvTransCons.IMPOUND_RELEASE:
                //Customer
                lcLocation = "3";
                break;
            case InvTransCons.SALES:
            case InvTransCons.WHOLESALE:
            case InvTransCons.SALES_REPLACEMENT:
            case InvTransCons.WHOLESALE_REPLACEMENT:    
                //Customer
                lcLocation = "3";
                lcSoldStat = "1";
                break;
            case InvTransCons.SALES_RETURN:
            case InvTransCons.WHOLESALE_RETURN:
                //Warehouse/Branch
                lcLocation = pbIsWhsexx ? "0" : "1";
                break;
            case InvTransCons.BRANCH_TRANSFER:
                //On-Transit
                lcLocation = "4";
                break;
            case InvTransCons.BRANCH_TRANSFER_ACCEPTANCE:
                //Warehouse/Branch
                lcLocation = pbIsWhsexx ? "0" : "1";
                break;
            case InvTransCons.PURCHASE_RECEIVING:    
                //Warehouse/Branch
                lcLocation = pbIsWhsexx ? "0" : "1";
                break;
            case InvTransCons.PURCHASE_REPLACEMENT:    
                //Warehouse/Branch
                lcLocation = pbIsWhsexx ? "0" : "1";
                break;
            case InvTransCons.PURCHASE_RETURN:    
                //Supplier
                lcLocation = "2";
                break;
        }
        
        //add entry for the serialif(
        SerialEntry loSerial = 
                new SerialEntry(
                          fsIndstCdx
                        , loRS.getString("sStockIDx")
                        , loRS.getString("sWHouseID")
                        , fsSerialID
                        , fbWithOrdr
                        , lcConditnx
                        , lcSoldStat
                        , lcLocation
                        , fnUnitPrice
                        , fnUnitPrice);
        paSerialEntry.add(loSerial);

        DetailEntry loDetail; 
        loDetail = new DetailEntry(
                  fsIndstCdx
                , loRS.getString("sStockIDx")
                , loRS.getString("sWHouseID")
                , 1
                , fbWithOrdr ? 1 : 0
                , null
                , lcConditnx
                , loRS.getDouble("nPurPrice")
                , fnUnitPrice);

        if(paDetailEntry.isEmpty()){
            paDetailEntry.add(loDetail);
        }
        else{
            // Search and update or add
            Optional<DetailEntry> match = paDetailEntry.stream()
                .filter(p -> p.psIndstCdx.equalsIgnoreCase(loDetail.psIndstCdx) 
                          && p.psStockIDx.equalsIgnoreCase(loDetail.psStockIDx)
                          && p.psWHouseID.equalsIgnoreCase(loDetail.psWHouseID)
                          && p.pcConditnx.equalsIgnoreCase(loDetail.pcConditnx))
                .findFirst();

            if (match.isPresent()) {
                // Update the fields
                match.get().pnQuantity += loDetail.pnQuantity;
                match.get().pnOrderQty += loDetail.pnOrderQty;
            } else {
                // Add new product
                paDetailEntry.add(loDetail);
            }
        }

        //perform testing of accuracy of data extracted here
        do{
            System.out.println(loRS.getString("sSerialID") + ":" + loRS.getString("sBranchCD") + ":" + loRS.getString("sStockIDx"));
        }while(loRS.next());
    }
    
    public void saveTransaction() throws SQLException, GuanzonException{
        for (DetailEntry loDetail : paDetailEntry) {
            String lsSQL = "SELECT *" + 
                          " FROM Inv_Master" +
                          " WHERE sBranchCd = " + SQLUtil.toSQL(psBranchCD) +
                            " AND sStockIDx = " + SQLUtil.toSQL(loDetail.psStockIDx) +
                            " AND sIndstCdx = " + SQLUtil.toSQL(loDetail.psIndstCdx) +
                            " AND cConditnx = " + SQLUtil.toSQL(loDetail.pcConditnx); 
            ResultSet loRS = poDriver.executeQuery(lsSQL);
            
            int lnLedgerNo;
            
            //Create the record if it does not exist
            if(!loRS.next()){
                lsSQL = "INSERT INTO Inv_Master" + 
                       " SET sBranchCd = " + SQLUtil.toSQL(psBranchCD) +
                          ", sStockIDx = " + SQLUtil.toSQL(loDetail.psStockIDx) + 
                          ", sWHouseID = " + SQLUtil.toSQL(loDetail.psWHouseID) +  
                          ", sIndstCdx = " + SQLUtil.toSQL(loDetail.psIndstCdx) +  
                          ", sLocatnID = " + SQLUtil.toSQL("") +  
                          ", sBinNumbr = " + SQLUtil.toSQL("") +  
                          ", nBegQtyxx = " + SQLUtil.toSQL(0) +  
                          ", nQtyOnHnd = " + SQLUtil.toSQL(0) +  
                          ", nLedgerNo = " + SQLUtil.toSQL(0) +  
                          ", nMinLevel = " + SQLUtil.toSQL(0) +  
                          ", nMaxLevel = " + SQLUtil.toSQL(0) +  
                          ", nAvgMonSl = " + SQLUtil.toSQL(0) +  
                          ", nAvgCostx = " + SQLUtil.toSQL(0) +  
                          ", cClassify = " + SQLUtil.toSQL("F") +  
                          ", nBackOrdr = " + SQLUtil.toSQL(0) +  
                          ", nResvOrdr = " + SQLUtil.toSQL(0) +  
                          ", nFloatQty = " + SQLUtil.toSQL(0) +  
                          ", cPrimaryx = " + SQLUtil.toSQL("0") +  
                          ", cConditnx = " + SQLUtil.toSQL(loDetail.pcConditnx) +  
                          ", sPayLoadx = " + SQLUtil.toSQL("") +  
                          ", cRecdStat = " + SQLUtil.toSQL("1") +
                          ", sModified = " + SQLUtil.toSQL("") +
                          ", dModified = " + SQLUtil.toSQL(""); 
                        
                poDriver.executeQuery(lsSQL, "Inv_Master", psBranchCD, "", psIndstCdx);

                lnLedgerNo = 1;
            }
            else{
                lnLedgerNo = loRS.getInt("nLedgerNo") + 1;
            }
                
            //initialize variable to use in determining the type of changes in the stock
            double lnQtyInxxx = 0;
            double lnQtyOutxx = 0;
            double lnQtyIssue = 0;
            double lnQtyOrder = 0;        

            
            //identify the type of changes in the stock(quantity/order)
            if(InvTransCons.getDebitTrans().toUpperCase().contains(psSourceCD.toUpperCase())){
                lnQtyInxxx += loDetail.pnQuantity;
            } 
            
            if(InvTransCons.getCreditTrans().toUpperCase().contains(psSourceCD.toUpperCase())){
                lnQtyOutxx += loDetail.pnQuantity;
            }
            
            if(InvTransCons.getIssOrderDebit().toUpperCase().contains(psSourceCD.toUpperCase())){
                lnQtyIssue += loDetail.pnOrderQty;
            }

            if(InvTransCons.getIssOrderCredit().toUpperCase().contains(psSourceCD.toUpperCase())){
                lnQtyIssue -= loDetail.pnOrderQty;
            }

            if(InvTransCons.getRecvOrderDebit().toUpperCase().contains(psSourceCD.toUpperCase())){
                lnQtyOrder += loDetail.pnOrderQty;
            }

            if(InvTransCons.getRecvOrderCredit().toUpperCase().contains(psSourceCD.toUpperCase())){
                lnQtyOrder -= loDetail.pnOrderQty;
            }

            //Update Inv_Master
            lsSQL = "UPDATE Inv_Master" + 
                   " SET nQtyOnHnd = nQtyOnHnd + " + SQLUtil.toSQL(lnQtyInxxx - lnQtyOutxx) +
                      ", nBackOrdr = nBackOrdr + " + SQLUtil.toSQL(lnQtyOrder) + 
                      ", nResvOrdr = nResvOrdr + " + SQLUtil.toSQL(lnQtyIssue) + 
                      ", nLedgerNo = " + SQLUtil.toSQL(lnLedgerNo) +
                      ", sModified = " + SQLUtil.toSQL(psUserIDxx) +
                      ", dModified = " + SQLUtil.toSQL(poDriver.getServerDate()) +
                   " WHERE sBranchCd = " + SQLUtil.toSQL(psBranchCD) +
                     " AND sStockIDx = " + SQLUtil.toSQL(loDetail.psStockIDx) +
                     " AND sIndstCdx = " + SQLUtil.toSQL(loDetail.psIndstCdx) +
                     " AND cConditnx = " + SQLUtil.toSQL(loDetail.pcConditnx); 
            System.out.println(lsSQL);
            poDriver.executeQuery(lsSQL, "Inv_Master", psBranchCD, "", psIndstCdx);

            lsSQL = "INSERT INTO Inv_Ledger" +
                   " SET sBranchCd = " + SQLUtil.toSQL(psBranchCD) +
                      ", sStockIDx = " + SQLUtil.toSQL(loDetail.psStockIDx) + 
                      ", sWHouseID = " + SQLUtil.toSQL(loDetail.psWHouseID) +  
                      ", sIndstCdx = " + SQLUtil.toSQL(loDetail.psIndstCdx) +  
                      ", nLedgerNo = " + SQLUtil.toSQL(lnLedgerNo) +
                      ", dTransact = " + SQLUtil.toSQL(pdTranDate) +
                      ", sSourceCD = " + SQLUtil.toSQL(psSourceCD) +
                      ", sSourceNo = " + SQLUtil.toSQL(psSourceNo) +
                      ", nQtyInxxx = " + SQLUtil.toSQL(lnQtyInxxx) +
                      ", nQtyOutxx = " + SQLUtil.toSQL(lnQtyOutxx) +
                      ", nQtyOrder = " + SQLUtil.toSQL(lnQtyOrder) +
                      ", nQtyIssue = " + SQLUtil.toSQL(lnQtyIssue) +
                      ", nPurPrice = " + SQLUtil.toSQL(loDetail.pnPurPrice) +
                      ", nUnitPrce = " + SQLUtil.toSQL(loDetail.pnUnitPrce) +
                      ", cConditnx = " + SQLUtil.toSQL(loDetail.pcConditnx) +
                      ", sModified = " + SQLUtil.toSQL(psUserIDxx) +
                      ", dModified = " + SQLUtil.toSQL(poDriver.getServerDate());
            System.out.println(lsSQL);
            poDriver.executeQuery(lsSQL, "Inv_Ledger", psBranchCD, "", psIndstCdx);
        }
        
        for (SerialEntry loSerial : paSerialEntry) {
            String lsSQL = "SELECT *" +
                          " FROM Inv_Serial" +
                          " WHERE sSerialID = " + SQLUtil.toSQL(loSerial.psSerialID) +
                            " AND sIndstCdx = " + SQLUtil.toSQL(loSerial.psIndstCdx);
            ResultSet loRS = poDriver.executeQuery(lsSQL);

            if(!loRS.next()){
                //throw new GuanzonException(GuanzonException.GE_NOTFOUND_EXCEPTION, "Inventory serial does not exist!");
                throw new GuanzonException(GuanzonException.GE_HOSTNAME_EXCEPTION);
            }

            int lnLedgerNo = loRS.getInt("nLedgerNo") + 1;
            
            lsSQL = "UPDATE Inv_Serial" +
                   " SET sBranchCD = " + SQLUtil.toSQL(psBranchCD) +
                      ", sWHouseID = " + SQLUtil.toSQL(loSerial.psWHouseID) +
                      ", nLedgerNo = " + SQLUtil.toSQL(lnLedgerNo) +
                      ", cLocation = " + SQLUtil.toSQL(loSerial.pcLocation) +
                      ", cSoldStat = " + SQLUtil.toSQL(loSerial.pcSoldStat) +
                      ", dModified = " + SQLUtil.toSQL(poDriver.getServerDate()) +
                   " WHERE sSerialID = " + SQLUtil.toSQL(loSerial.psSerialID) +
                     " AND sIndstCdx = " + SQLUtil.toSQL(loSerial.psIndstCdx);
            System.out.println(lsSQL);
            poDriver.executeQuery(lsSQL, "Inv_Serial", psBranchCD, "", psIndstCdx);
            
            lsSQL = "INSERT INTO Inv_Serial_Ledger" +
                   " SET sSerialID = " + SQLUtil.toSQL(loSerial.psSerialID) +
                      ", sBranchCD = " + SQLUtil.toSQL(psBranchCD) +
                      ", sWHouseID = " + SQLUtil.toSQL(loSerial.psWHouseID) +
                      ", sIndstCdx = " + SQLUtil.toSQL(loSerial.psIndstCdx) +
                      ", nLedgerNo = " + SQLUtil.toSQL(lnLedgerNo) +
                      ", dTransact = " + SQLUtil.toSQL(pdTranDate) +
                      ", sSourceCd = " + SQLUtil.toSQL(psSourceCD) +
                      ", sSourceNo = " + SQLUtil.toSQL(psSourceNo) +
                      ", cLocation = " + SQLUtil.toSQL(loSerial.pcLocation) +
                      ", cConditnx = " + SQLUtil.toSQL(loSerial.pcConditnx) +
                      ", cSoldStat = " + SQLUtil.toSQL(loSerial.pcSoldStat) +
                      ", sModified = " + SQLUtil.toSQL(psUserIDxx) +
                      ", dModified = " + SQLUtil.toSQL(poDriver.getServerDate());
            System.out.println(lsSQL);
            poDriver.executeQuery(lsSQL, "Inv_Serial", psBranchCD, "", psIndstCdx);
        }
    }
    
    private class DetailEntry{
        public String psIndstCdx;
        public String psStockIDx;
        public String psWHouseID;
        public double pnQuantity;
        public double pnOrderQty;
        public Date pdExpiryxx;
        public String pcConditnx;
        public double pnPurPrice;
        public double pnUnitPrce;
        
        public DetailEntry(String fsIndstCdx, String fsStockIDx, String fsWHouseID, double fnQuantity, double fnOrderQty, Date fdExpiryxx, String fcConditnx, double fnPurPrice, double fnUnitPrce){
            this.psIndstCdx = fsIndstCdx;
            this.psStockIDx = fsStockIDx;
            this.psWHouseID = fsWHouseID;
            this.pnQuantity = fnQuantity;
            this.pnOrderQty = fnOrderQty;
            this.pdExpiryxx = fdExpiryxx;
            this.pcConditnx = fcConditnx;
            this.pnPurPrice = fnPurPrice;
            this.pnUnitPrce = fnUnitPrce;
        }
    }
    
    private class SerialEntry{
        public String psIndstCdx;
        public String psStockIDx;
        public String psWHouseID;
        public String psSerialID;
        public boolean pbWithOrdr;
        public String pcConditnx;
        public String pcLocation;
        public String pcSoldStat;
        public double pnPurPrice;
        public double pnUnitPrce;

        public SerialEntry(String fsIndstCdx, String fsStockIDx, String fsWHouseID, String fsSerialID, boolean fbWithOrdr, String fcConditnx, String fcSoldStat, String fcLocation, double fnPurPrice, double fnUnitPrce){
            this.psIndstCdx = fsIndstCdx;
            this.psStockIDx = fsStockIDx;
            this.psWHouseID = fsWHouseID;
            this.psSerialID = fsSerialID;
            this.pbWithOrdr = fbWithOrdr;
            this.pcConditnx = fcConditnx;
            this.pcLocation = fcLocation;
            this.pcSoldStat = fcSoldStat;
            this.pnPurPrice = fnPurPrice;
            this.pnUnitPrce = fnUnitPrce;
        }
    }
}
