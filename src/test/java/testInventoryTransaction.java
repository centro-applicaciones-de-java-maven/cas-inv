/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */

import java.sql.ResultSet;
import java.sql.SQLException;
import org.guanzon.appdriver.base.GRiderCAS;
import org.guanzon.appdriver.base.GuanzonException;
import org.guanzon.appdriver.base.SQLUtil;
import org.guanzon.cas.inv.InvTransCons;
import org.guanzon.cas.inv.InventoryTransaction;
import org.json.simple.JSONObject;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Administrator
 */
public class testInventoryTransaction {
    static GRiderCAS instance;
    static InventoryTransaction poTrans;
    
//    public testInventoryTransaction() {
//    }
    
    @BeforeClass
    public static void setUpClass() throws SQLException, GuanzonException {
        System.out.println("setUpClass()");
        instance = new GRiderCAS("test");
        
        createCoreTable();
        createInventoryTable();
        
        populateInventoryTable();
    }
    
    @AfterClass
    public static void tearDownClass() {
        System.out.println("tearDownClass()");
        poTrans = null;
        instance = null;
    }
    
    @Test
    public void testPO_SP() throws SQLException, GuanzonException{
        System.out.println("testPO_SP()");

        String lsSQL;
        ResultSet loRS;

        //What is the previous value
        lsSQL = "SELECT * FROM Inv_Master" + 
               " WHERE sBranchCD = 'M001'" + 
                 " AND sStockIDx IN ('M00115049321')";
        
        loRS = instance.executeQuery(lsSQL);
        
        instance.beginTrans("CREATE", "Create Purchase Order", InvTransCons.PURCHASE_ORDER, "M0012500002");
        poTrans = new InventoryTransaction(instance, "M001", false, "02", "M001250002");
        poTrans.PurchaseOrder("M0012500002", SQLUtil.toDate("2025-07-22", SQLUtil.FORMAT_SHORT_DATE), false);
        poTrans.addDetail("02", "M00115049321", "0", 0, 5, 0);
        poTrans.saveTransaction();
        instance.commitTrans();

//        JSONObject result = poTrans.setMaster("dTransact", "2026-07-04");
//        assertFalse(((String)result.get("result")).equalsIgnoreCase("error"));
//        assert
        
        
        //What is the new value
        lsSQL = "SELECT * FROM Inv_Master" + 
               " WHERE sBranchCD = 'M001'" + 
                 " AND sStockIDx IN ('M00115049321')";
        ResultSet loRSNew = instance.executeQuery(lsSQL);

        loRS.next();
        loRSNew.next();

        //text expected result
        assertEquals(loRS.getInt("nQtyOnHnd"), loRSNew.getInt("nQtyOnHnd"));
        assertEquals(loRS.getInt("nBackOrdr") + 5, loRSNew.getInt("nBackOrdr"));
        assertEquals(loRS.getInt("nResvOrdr"), loRSNew.getInt("nResvOrdr"));
        assertEquals(loRS.getString("sStockIDx"), "M00115049321");
        assertEquals(loRSNew.getString("sStockIDx"), "M00115049321");
        assertEquals(loRS.getInt("nLedgerNo") + 1, loRSNew.getInt("nLedgerNo"));
    }
    
    @Test
    public void testPO_MC() throws SQLException, GuanzonException{
        System.out.println("testPO_MC()");
        String lsSQL;
        ResultSet loRS;

        lsSQL = "SELECT * FROM Inv_Master" + 
               " WHERE sBranchCD = 'M001'" + 
                 " AND sStockIDx IN ('M00124112')";
        
        loRS = instance.executeQuery(lsSQL);

        instance.beginTrans("CREATE", "Create Purchase Order", InvTransCons.PURCHASE_ORDER, "M0012500001");
        poTrans = new InventoryTransaction(instance, "M001", false, "02", "M001250002");
        poTrans.PurchaseOrder("M0012500001", SQLUtil.toDate("2025-07-22", SQLUtil.FORMAT_SHORT_DATE), false);
        poTrans.addDetail("02", "M00124112", "0", 0, 1, 0);
        poTrans.saveTransaction();
        instance.commitTrans();
        
        lsSQL = "SELECT * FROM Inv_Master" + 
               " WHERE sBranchCD = 'M001'" + 
                 " AND sStockIDx IN ('M00124112')";
        ResultSet loRSNew = instance.executeQuery(lsSQL);

        loRS.next();
        loRSNew.next();

        assertEquals(loRS.getInt("nQtyOnHnd"), loRSNew.getInt("nQtyOnHnd"));
        assertEquals(loRS.getInt("nBackOrdr") + 1, loRSNew.getInt("nBackOrdr"));
        assertEquals(loRS.getInt("nResvOrdr"), loRSNew.getInt("nResvOrdr"));
        assertEquals(loRS.getString("sStockIDx"), "M00124112");
        assertEquals(loRSNew.getString("sStockIDx"), "M00124112");
        assertEquals(loRS.getInt("nLedgerNo") + 1, loRSNew.getInt("nLedgerNo"));
    }
    
    @Test
    public void testPORcv_SP() throws SQLException, GuanzonException{
        System.out.println("testPORcv_SP()");

        String lsSQL;
        lsSQL = "SELECT * FROM Inv_Master" + 
               " WHERE sBranchCD = 'M001'" + 
                 " AND sStockIDx IN ('M0W323000395')";
        
        ResultSet loRS = instance.executeQuery(lsSQL);
        
        instance.beginTrans("CREATE", "Create Purchase Order Receiving", InvTransCons.PURCHASE_RECEIVING, "M0012500002");
        poTrans = new InventoryTransaction(instance, "M001", false, "02", "M001250002");
        poTrans.PurchaseReceiving("M0012500002", SQLUtil.toDate("2025-07-23", SQLUtil.FORMAT_SHORT_DATE), false);
        poTrans.addDetail("02", "M0W323000395", "0", 5, 2, 0);
        poTrans.saveTransaction();
        instance.commitTrans();
        
        lsSQL = "SELECT * FROM Inv_Master" + 
               " WHERE sBranchCD = 'M001'" + 
                 " AND sStockIDx IN ('M0W323000395')";
        
        ResultSet loRSNew = instance.executeQuery(lsSQL);

        loRS.next();
        loRSNew.next();

        assertEquals(loRS.getInt("nQtyOnHnd") + 5, loRSNew.getInt("nQtyOnHnd"));
        assertEquals(loRS.getInt("nBackOrdr") - 2, loRSNew.getInt("nBackOrdr"));
        assertEquals(loRS.getInt("nResvOrdr"), loRSNew.getInt("nResvOrdr"));
        assertEquals(loRS.getString("sStockIDx"), "M0W323000395");
        assertEquals(loRSNew.getString("sStockIDx"), "M0W323000395");
        assertEquals(loRS.getInt("nLedgerNo") + 1, loRSNew.getInt("nLedgerNo"));
    }
    
    @Test
    public void testPORcv_MC() throws SQLException, GuanzonException{
        System.out.println("testPORcv_MC()");
        String lsSQL;
        lsSQL = "SELECT * FROM Inv_Master" + 
               " WHERE sBranchCD = 'M001'" + 
                 " AND sStockIDx IN ('M00125055')";

        ResultSet loRS = instance.executeQuery(lsSQL);
        
        instance.beginTrans("CREATE", "Create Purchase Order", InvTransCons.PURCHASE_ORDER, "M0012500001");
        poTrans = new InventoryTransaction(instance, "M001", false, "02", "M001250002");
        poTrans.PurchaseReceiving("M0012500001", SQLUtil.toDate("2025-07-23", SQLUtil.FORMAT_SHORT_DATE), false);
        poTrans.addSerial("02", "M09525000285", true, 0, "001");
        poTrans.saveTransaction();
        instance.commitTrans();
        
        lsSQL = "SELECT * FROM Inv_Master" + 
               " WHERE sBranchCD = 'M001'" + 
                 " AND sStockIDx IN ('M00125055')";
        
        ResultSet loRSNew = instance.executeQuery(lsSQL);

        loRS.next();
        loRSNew.next();

        assertEquals(loRS.getInt("nQtyOnHnd") + 1, loRSNew.getInt("nQtyOnHnd"));
        assertEquals(loRS.getInt("nBackOrdr")- 1, loRSNew.getInt("nBackOrdr"));
        assertEquals(loRS.getInt("nResvOrdr"), loRSNew.getInt("nResvOrdr"));
        assertEquals(loRS.getString("sStockIDx"), "M00125055");
        assertEquals(loRSNew.getString("sStockIDx"), "M00125055");
        assertEquals(loRS.getInt("nLedgerNo") + 1, loRSNew.getInt("nLedgerNo"));
    }

    @Test
    public void testDeliveryAcceptance_MC() throws SQLException, GuanzonException{
        System.out.println("testDeliverAcceptance_MC()");
        String lsSQL;
        ResultSet loRS;

        lsSQL = "SELECT * FROM Inv_Master" + 
               " WHERE sBranchCD = 'M001'" + 
                 " AND sStockIDx IN ('M00125036')";
        
        loRS = instance.executeQuery(lsSQL);

        instance.beginTrans("POSTING", "Accept Branch Transfer", InvTransCons.BRANCH_TRANSFER_ACCEPTANCE, "M0012500002");
        poTrans = new InventoryTransaction(instance, "M001", false, "02", "M001250002");
        poTrans.DeliveryAcceptance("M0012500001", SQLUtil.toDate("2025-07-22", SQLUtil.FORMAT_SHORT_DATE), false);
        poTrans.addSerial("02", "M09525000287", true, 0, "001");
        //poTrans.addDetail("02", "M00124112", "0", 1, 1, 0);
        poTrans.saveTransaction();
        instance.commitTrans();
        
        lsSQL = "SELECT * FROM Inv_Master" + 
               " WHERE sBranchCD = 'M001'" + 
                 " AND sStockIDx IN ('M00125036')";
        ResultSet loRSNew = instance.executeQuery(lsSQL);

        loRS.next();
        loRSNew.next();

        assertEquals(loRS.getInt("nQtyOnHnd") + 1, loRSNew.getInt("nQtyOnHnd"));
        assertEquals(loRS.getInt("nBackOrdr") - 1, loRSNew.getInt("nBackOrdr"));
        assertEquals(loRS.getInt("nResvOrdr"), loRSNew.getInt("nResvOrdr"));
        assertEquals(loRS.getString("sStockIDx"), "M00125036");
        assertEquals(loRSNew.getString("sStockIDx"), "M00125036");
        assertEquals(loRS.getInt("nLedgerNo") + 1, loRSNew.getInt("nLedgerNo"));
    }
    
    public static void createCoreTable() throws SQLException{
        String lsSQL;
        
        //Create xxxAuditLogMaster
        lsSQL = "CREATE TABLE `xxxAuditLogMaster` (" +
            "  `sTransNox` char(25) NOT NULL," +
            "  `sComptrNm` char(32) DEFAULT NULL," +
            "  `sSourceCD` char(4) DEFAULT NULL," +
            "  `sSourceNo` char(20) DEFAULT NULL," +
            "  `sEventNme` char(32) DEFAULT NULL," +
            "  `sRemarksx` varchar(256) DEFAULT NULL," +
            "  `sModified` char(32) NOT NULL," +
            "  `dModified` datetime DEFAULT NULL," +
            "  `dTimeStmp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP," +
            "  PRIMARY KEY (`sTransNox`))";
        instance.executeUpdate(lsSQL);
        
        //Create xxxAuditLogDetail
        lsSQL = "CREATE TABLE `xxxauditlogdetail` (" +
            "  `sTransNox` char(20) NOT NULL," +
            "  `sBranchCd` char(4) NOT NULL," +
            "  `sSourceNo` char(25) NOT NULL," +
            "  `sQryTypex` char(16) DEFAULT NULL," +
            "  `cIsJsonxx` char(1) DEFAULT NULL," +
            "  `sPayloadx` varchar(3072) DEFAULT NULL," +
            "  `sFilterxx` varchar(512) DEFAULT NULL," +
            "  `sTableNme` char(64) DEFAULT NULL," +
            "  `sDestinat` char(4) DEFAULT NULL," +
            "  `sDivision` char(4) DEFAULT NULL," +
            "  `sModified` char(32) DEFAULT NULL," +
            "  `dModified` datetime DEFAULT NULL," +
            "  `dTimeStmp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP," +
            "  PRIMARY KEY (`sTransNox`))";
        instance.executeUpdate(lsSQL);
    }
    
    public static void createInventoryTable() throws SQLException{
        String lsSQL;
        //Create Inventory
        lsSQL = "CREATE TABLE `inventory` (" +
                "  `sStockIDx` char(12) NOT NULL," +
                "  `sBarCodex` char(25) DEFAULT NULL," +
                "  `sDescript` char(64) DEFAULT NULL," +
                "  `sBriefDsc` char(20) DEFAULT NULL," +
                "  `sAltBarCd` char(25) DEFAULT NULL," +
                "  `sCategCd1` char(4) DEFAULT NULL," +
                "  `sCategCd2` char(4) DEFAULT NULL," +
                "  `sCategCd3` char(4) DEFAULT NULL," +
                "  `sCategCd4` char(4) DEFAULT NULL," +
                "  `sBrandIDx` varchar(7) DEFAULT NULL," +
                "  `sModelIDx` char(9) DEFAULT NULL," +
                "  `sColorIDx` char(7) DEFAULT NULL," +
                "  `sVrntIDxx` char(5) DEFAULT NULL," +
                "  `sMeasurID` char(5) DEFAULT NULL," +
                "  `sInvTypCd` char(4) DEFAULT NULL," +
                "  `sIndstCdx` char(2) NOT NULL," +
                "  `nPurPrice` decimal(11,2) DEFAULT NULL," +
                "  `nUnitPrce` decimal(13,4) DEFAULT NULL," +
                "  `nDiscLev1` decimal(8,2) DEFAULT NULL," +
                "  `nDiscLev2` decimal(8,2) DEFAULT NULL," +
                "  `nDiscLev3` decimal(8,2) DEFAULT NULL," +
                "  `nDealrDsc` decimal(8,2) DEFAULT NULL," +
                "  `nMinLevel` smallint(6) DEFAULT NULL," +
                "  `nMaxLevel` smallint(6) DEFAULT NULL," +
                "  `cComboInv` char(1) DEFAULT '0'," +
                "  `cWthPromo` char(1) DEFAULT '0'," +
                "  `cSerialze` char(1) DEFAULT '0'," +
                "  `cUnitType` char(1) DEFAULT '0'," +
                "  `cInvStatx` char(1) DEFAULT '0'," +
                "  `nShlfLife` smallint(7) DEFAULT NULL," +
                "  `sSupersed` varchar(12) DEFAULT NULL," +
                "  `sPayLoadx` varchar(512) DEFAULT NULL," +
                "  `cRecdStat` char(1) DEFAULT '1'," +
                "  `sModified` char(32) DEFAULT NULL," +
                "  `dModified` datetime DEFAULT NULL," +
                "  `dTimeStmp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP," +
                "  PRIMARY KEY (`sStockIDx`, `sIndstCdx`))";
        instance.executeUpdate(lsSQL);

        //Create Inv_Master
        lsSQL = "CREATE TABLE `inv_master` (" +
                "  `sStockIDx` char(12) NOT NULL," +
                "  `sBranchCd` char(4) NOT NULL," +
                "  `sWHouseID` char(3) NOT NULL," +
                "  `sIndstCdx` char(2) NOT NULL," +
                "  `sLocatnID` char(3) DEFAULT NULL," +
                "  `sBinNumbr` char(7) DEFAULT NULL," +
                "  `dAcquired` date DEFAULT NULL," +
                "  `dBegInvxx` date DEFAULT NULL," +
                "  `nBegQtyxx` decimal(8,2) DEFAULT NULL," +
                "  `nQtyOnHnd` decimal(8,2) DEFAULT NULL," +
                "  `nLedgerNo` smallint(6) DEFAULT NULL," +
                "  `nMinLevel` decimal(8,2) DEFAULT NULL," +
                "  `nMaxLevel` decimal(8,2) DEFAULT NULL," +
                "  `nAvgMonSl` decimal(8,2) DEFAULT NULL," +
                "  `nAvgCostx` decimal(12,4) DEFAULT NULL," +
                "  `cClassify` char(1) DEFAULT 'F'," +
                "  `nBackOrdr` decimal(8,2) DEFAULT NULL," +
                "  `nResvOrdr` decimal(8,2) DEFAULT NULL," +
                "  `nFloatQty` decimal(8,2) DEFAULT NULL," +
                "  `dLastTran` date DEFAULT NULL," +
                "  `cPrimaryx` char(1) DEFAULT NULL," +
                "  `cConditnx` char(1) NOT NULL," +
                "  `sPayLoadx` varchar(512) DEFAULT NULL," +
                "  `cRecdStat` char(1) DEFAULT NULL," +
                "  `sModified` char(32) DEFAULT NULL," +
                "  `dModified` datetime DEFAULT NULL," +
                "  `dTimeStmp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP," +
                "  PRIMARY KEY (`sBranchCd`,`sStockIDx`, `sWHouseID`, `sIndstCdx`, `cConditnx` ))";
        instance.executeUpdate(lsSQL);

        //Create Inv_Ledger
        lsSQL = "CREATE TABLE `inv_ledger` (" +
                "  `sStockIDx` varchar(12) NOT NULL," +
                "  `sBranchCd` varchar(4) NOT NULL," +
                "  `sWHouseID` char(3) NOT NULL," +
                "  `sIndstCdx` char(2) NOT NULL," +
                "  `nLedgerNo` int(11) DEFAULT NULL," +
                "  `dTransact` date DEFAULT NULL," +
                "  `sSourceCd` varchar(4) NOT NULL," +
                "  `sSourceNo` varchar(12) NOT NULL," +
                "  `nQtyInxxx` decimal(8,2) DEFAULT NULL," +
                "  `nQtyOutxx` decimal(8,2) DEFAULT NULL," +
                "  `nQtyOrder` decimal(8,2) DEFAULT NULL," +
                "  `nQtyIssue` decimal(8,2) DEFAULT NULL," +
                "  `nPurPrice` decimal(12,4) DEFAULT NULL," +
                "  `nUnitPrce` decimal(12,4) DEFAULT NULL," +
                "  `dExpiryxx` date DEFAULT NULL," +
                "  `cConditnx` char(1) NOT NULL," +
                "  `sModified` varchar(12) DEFAULT NULL," +
                "  `dModified` datetime DEFAULT NULL," +
                "  `dTimeStmp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP," +
                "  PRIMARY KEY (`sStockIDx`,`sBranchCd`,`sWHouseID`, `cConditnx`, `sSourceCd`,`sSourceNo`))";
        instance.executeUpdate(lsSQL);
        
        //Create Inv_Serial
        lsSQL = "CREATE TABLE `inv_serial` (" +
                "  `sSerialID` char(12) NOT NULL," +
                "  `sIndstCdx` char(2) NOT NULL," +
                "  `sBranchCd` char(4) NOT NULL," +
                "  `sWHouseID` char(3) NOT NULL," +
                "  `sClientID` char(12) DEFAULT NULL," +
                "  `sSerial01` char(20) DEFAULT NULL," +
                "  `sSerial02` char(20) DEFAULT NULL," +
                "  `nUnitPrce` decimal(10,2) DEFAULT NULL," +
                "  `sStockIDx` char(12) DEFAULT NULL," +
                "  `nLedgerNo` smallint(6) DEFAULT NULL," +
                "  `cLocation` char(1) DEFAULT NULL," +
                "  `cSoldStat` char(1) DEFAULT NULL," +
                "  `cUnitType` char(1) DEFAULT NULL," +
                "  `sCompnyID` char(4) DEFAULT NULL," +
                "  `sWarranty` char(12) DEFAULT NULL," +
                "  `sPayLoadx` varchar(512) DEFAULT NULL," +
                "  `cConditnx` char(1) NOT NULL," +
                "  `sModified` varchar(12) DEFAULT NULL," +
                "  `dModified` datetime DEFAULT NULL," +
                "  `dTimeStmp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP," +
                "  PRIMARY KEY (`sSerialID`,`sIndstCdx`,`cConditnx`))";
        instance.executeUpdate(lsSQL);
        
        //create Inv_Serial_Ledger
        lsSQL = "CREATE TABLE `inv_serial_ledger` (" +
                "  `sSerialID` char(12) NOT NULL," +
                "  `sIndstCdx` char(2) NOT NULL," +
                "  `sBranchCd` char(4) NOT NULL," +
                "  `sWHouseID` char(3) NOT NULL," +
                "  `nLedgerNo` smallint(6) DEFAULT NULL," +
                "  `dTransact` date DEFAULT NULL," +
                "  `sSourceCd` char(4) NOT NULL," +
                "  `sSourceNo` char(12) NOT NULL," +
                "  `cConditnx` char(1) NOT NULL," +
                "  `cSoldStat` char(1) DEFAULT '0'," +
                "  `cLocation` char(1) DEFAULT NULL," +
                "  `sModified` varchar(12) DEFAULT NULL," +
                "  `dModified` datetime DEFAULT NULL," +
                "  `dTimeStmp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP," +
                "  PRIMARY KEY (`sSerialID`,`sBranchCd`,`sSourceCd`,`sSourceNo`))";
        instance.executeUpdate(lsSQL);

    }
    
    public static void populateInventoryTable() throws SQLException{
        String lsSQL; 
        //populate Inventory from MC_Inventory;
        lsSQL = "INSERT INTO Inventory SET sStockIDx = 'M00124110', sBrandIDx = '', sModelIDx = 'M00124070', sColorIDx = 'M001220', sIndstCdx = '02', nUnitPrce = 0, nPurPrice = 0, cSerialze = '1', cInvStatx = '1', cRecdStat = '1', sModified = '', dModified = null";
        instance.executeUpdate(lsSQL);
        lsSQL = "INSERT INTO Inventory SET sStockIDx = 'M00124112', sBrandIDx = '', sModelIDx = 'M00124070', sColorIDx = 'M001281', sIndstCdx = '02', nUnitPrce = 0, nPurPrice = 0, cSerialze = '1', cInvStatx = '1', cRecdStat = '1', sModified = '', dModified = null";
        instance.executeUpdate(lsSQL);
        lsSQL = "INSERT INTO Inventory SET sStockIDx = 'M00125035', sBrandIDx = '', sModelIDx = 'M00125026', sColorIDx = 'M001287', sIndstCdx = '02', nUnitPrce = 0, nPurPrice = 0, cSerialze = '1', cInvStatx = '1', cRecdStat = '1', sModified = '', dModified = null";
        instance.executeUpdate(lsSQL);
        lsSQL = "INSERT INTO Inventory SET sStockIDx = 'M00125036', sBrandIDx = '', sModelIDx = 'M00125027', sColorIDx = 'M0W2008', sIndstCdx = '02', nUnitPrce = 0, nPurPrice = 0, cSerialze = '1', cInvStatx = '1', cRecdStat = '1', sModified = '', dModified = null";
        instance.executeUpdate(lsSQL);
        lsSQL = "INSERT INTO Inventory SET sStockIDx = 'M00125055', sBrandIDx = '', sModelIDx = 'M00125045', sColorIDx = 'M001301', sIndstCdx = '02', nUnitPrce = 0, nPurPrice = 0, cSerialze = '1', cInvStatx = '1', cRecdStat = '1', sModified = '', dModified = null";
        instance.executeUpdate(lsSQL);
        
        //populate Inv_Master from MC_Inventory
        lsSQL = "INSERT INTO Inv_Master SET sStockIDx = 'M00124110', sBranchCd = 'M095', sWHouseID = '001', sIndstCdx = '02', sLocatnID = '', nBegQtyxx = 0, nQtyOnHnd = 2, nLedgerNo = 1, nBackOrdr = 4, nResvOrdr = 3, nFloatQty = 0, cPrimaryx = '1', cConditnx = '0', sPayLoadx = '', cRecdStat = '1', sModified = '', dModified = null";
        instance.executeUpdate(lsSQL);
        lsSQL = "INSERT INTO Inv_Master SET sStockIDx = 'M00124112', sBranchCd = 'M095', sWHouseID = '001', sIndstCdx = '02', sLocatnID = '', nBegQtyxx = 0, nQtyOnHnd = 1, nLedgerNo = 2, nBackOrdr = 3, nResvOrdr = 2, nFloatQty = 0, cPrimaryx = '1', cConditnx = '0', sPayLoadx = '', cRecdStat = '1', sModified = '', dModified = null";
        instance.executeUpdate(lsSQL);
        lsSQL = "INSERT INTO Inv_Master SET sStockIDx = 'M00125035', sBranchCd = 'M095', sWHouseID = '001', sIndstCdx = '02', sLocatnID = '', nBegQtyxx = 0, nQtyOnHnd = 1, nLedgerNo = 3, nBackOrdr = 2, nResvOrdr = 1, nFloatQty = 0, cPrimaryx = '1', cConditnx = '0', sPayLoadx = '', cRecdStat = '1', sModified = '', dModified = null";
        instance.executeUpdate(lsSQL);
        lsSQL = "INSERT INTO Inv_Master SET sStockIDx = 'M00125036', sBranchCd = 'M095', sWHouseID = '001', sIndstCdx = '02', sLocatnID = '', nBegQtyxx = 0, nQtyOnHnd = 1, nLedgerNo = 4, nBackOrdr = 1, nResvOrdr = 0, nFloatQty = 0, cPrimaryx = '1', cConditnx = '0', sPayLoadx = '', cRecdStat = '1', sModified = '', dModified = null";
        instance.executeUpdate(lsSQL);
        lsSQL = "INSERT INTO Inv_Master SET sStockIDx = 'M00125055', sBranchCd = 'M095', sWHouseID = '001', sIndstCdx = '02', sLocatnID = '', nBegQtyxx = 0, nQtyOnHnd = 3, nLedgerNo = 5, nBackOrdr = 0, nResvOrdr = 1, nFloatQty = 0, cPrimaryx = '1', cConditnx = '0', sPayLoadx = '', cRecdStat = '1', sModified = '', dModified = null";
        instance.executeUpdate(lsSQL);

        lsSQL = "INSERT INTO Inv_Master SET sStockIDx = 'M00124110', sBranchCd = 'M001', sWHouseID = '001', sIndstCdx = '02', sLocatnID = '', nBegQtyxx = 0, nQtyOnHnd = 3, nLedgerNo = 5, nBackOrdr = 0, nResvOrdr = 1, nFloatQty = 0, cPrimaryx = '1', cConditnx = '0', sPayLoadx = '', cRecdStat = '1', sModified = '', dModified = null";
        instance.executeUpdate(lsSQL);
        lsSQL = "INSERT INTO Inv_Master SET sStockIDx = 'M00124112', sBranchCd = 'M001', sWHouseID = '001', sIndstCdx = '02', sLocatnID = '', nBegQtyxx = 0, nQtyOnHnd = 2, nLedgerNo = 4, nBackOrdr = 1, nResvOrdr = 2, nFloatQty = 0, cPrimaryx = '1', cConditnx = '0', sPayLoadx = '', cRecdStat = '1', sModified = '', dModified = null";
        instance.executeUpdate(lsSQL);
        lsSQL = "INSERT INTO Inv_Master SET sStockIDx = 'M00125035', sBranchCd = 'M001', sWHouseID = '001', sIndstCdx = '02', sLocatnID = '', nBegQtyxx = 0, nQtyOnHnd = 2, nLedgerNo = 3, nBackOrdr = 2, nResvOrdr = 3, nFloatQty = 0, cPrimaryx = '1', cConditnx = '0', sPayLoadx = '', cRecdStat = '1', sModified = '', dModified = null";
        instance.executeUpdate(lsSQL);
        lsSQL = "INSERT INTO Inv_Master SET sStockIDx = 'M00125036', sBranchCd = 'M001', sWHouseID = '001', sIndstCdx = '02', sLocatnID = '', nBegQtyxx = 0, nQtyOnHnd = 1, nLedgerNo = 2, nBackOrdr = 3, nResvOrdr = 4, nFloatQty = 0, cPrimaryx = '1', cConditnx = '0', sPayLoadx = '', cRecdStat = '1', sModified = '', dModified = null";
        instance.executeUpdate(lsSQL);
        lsSQL = "INSERT INTO Inv_Master SET sStockIDx = 'M00125055', sBranchCd = 'M001', sWHouseID = '001', sIndstCdx = '02', sLocatnID = '', nBegQtyxx = 0, nQtyOnHnd = 4, nLedgerNo = 1, nBackOrdr = 4, nResvOrdr = 5, nFloatQty = 0, cPrimaryx = '1', cConditnx = '0', sPayLoadx = '', cRecdStat = '1', sModified = '', dModified = null";
        instance.executeUpdate(lsSQL);
        
        //populate Inv_Master from MC_Serial
        lsSQL = "INSERT INTO Inv_Serial SET sSerialID = 'M09525000285', sIndstCdx = '02', sBranchCd = 'M001', sWHouseID = '001', sClientID = '', sSerial01 = 'G3S7E-0108359', sSerial02 = 'MH3RG5620S0012020', nUnitPrce = 0, sStockIDx = 'M00125055', cLocation = '1', cSoldStat = '0', cUnitType = '', sCompnyID = '', sWarranty = '', sPayloadx = '', cConditnx = '0', nLedgerNo = 0, dModified = null"; 
        instance.executeUpdate(lsSQL);
        lsSQL = "INSERT INTO Inv_Serial SET sSerialID = 'M09525000286', sIndstCdx = '02', sBranchCd = 'M001', sWHouseID = '001', sClientID = '', sSerial01 = 'E31XE-0104402', sSerial02 = 'MH3SEJ940S0040997', nUnitPrce = 0, sStockIDx = 'M00125035', cLocation = '1', cSoldStat = '0', cUnitType = '', sCompnyID = '', sWarranty = '', sPayloadx = '', cConditnx = '0', nLedgerNo = 0, dModified = null"; 
        instance.executeUpdate(lsSQL);
        lsSQL = "INSERT INTO Inv_Serial SET sSerialID = 'M09525000287', sIndstCdx = '02', sBranchCd = 'M001', sWHouseID = '001', sClientID = '', sSerial01 = 'E31XE-0104363', sSerial02 = 'MH3SEJ940S0040959', nUnitPrce = 0, sStockIDx = 'M00125036', cLocation = '1', cSoldStat = '0', cUnitType = '', sCompnyID = '', sWarranty = '', sPayloadx = '', cConditnx = '0', nLedgerNo = 0, dModified = null"; 
        instance.executeUpdate(lsSQL);
        lsSQL = "INSERT INTO Inv_Serial SET sSerialID = 'M09525000288', sIndstCdx = '02', sBranchCd = 'M001', sWHouseID = '001', sClientID = '', sSerial01 = 'E33WE-0485448', sSerial02 = 'MH3SEJ750S0020127', nUnitPrce = 0, sStockIDx = 'M00124110', cLocation = '1', cSoldStat = '0', cUnitType = '', sCompnyID = '', sWarranty = '', sPayloadx = '', cConditnx = '0', nLedgerNo = 0, dModified = null"; 
        instance.executeUpdate(lsSQL);
        lsSQL = "INSERT INTO Inv_Serial SET sSerialID = 'M09525000289', sIndstCdx = '02', sBranchCd = 'M001', sWHouseID = '001', sClientID = '', sSerial01 = 'E33WE-0505231', sSerial02 = 'MH3SEJ750S0024509', nUnitPrce = 0, sStockIDx = 'M00124112', cLocation = '1', cSoldStat = '0', cUnitType = '', sCompnyID = '', sWarranty = '', sPayloadx = '', cConditnx = '0', nLedgerNo = 0, dModified = null"; 
        instance.executeUpdate(lsSQL);
        lsSQL = "INSERT INTO Inv_Serial SET sSerialID = 'M09525000290', sIndstCdx = '02', sBranchCd = 'M001', sWHouseID = '001', sClientID = '', sSerial01 = 'E33WE-0505244', sSerial02 = 'MH3SEJ750S0024522', nUnitPrce = 0, sStockIDx = 'M00124112', cLocation = '1', cSoldStat = '0', cUnitType = '', sCompnyID = '', sWarranty = '', sPayloadx = '', cConditnx = '0', nLedgerNo = 0, dModified = null"; 
        instance.executeUpdate(lsSQL);
        lsSQL = "INSERT INTO Inv_Serial SET sSerialID = 'M09525000291', sIndstCdx = '02', sBranchCd = 'M001', sWHouseID = '001', sClientID = '', sSerial01 = 'E33WE-0506513', sSerial02 = 'MH3SEJ750S0024792', nUnitPrce = 0, sStockIDx = 'M00124112', cLocation = '1', cSoldStat = '0', cUnitType = '', sCompnyID = '', sWarranty = '', sPayloadx = '', cConditnx = '0', nLedgerNo = 0, dModified = null"; 
        instance.executeUpdate(lsSQL);
        lsSQL = "INSERT INTO Inv_Serial SET sSerialID = 'M09525000292', sIndstCdx = '02', sBranchCd = 'M001', sWHouseID = '001', sClientID = '', sSerial01 = 'G3V5E-0348797', sSerial02 = 'PA0SGA510S0028638', nUnitPrce = 0, sStockIDx = 'M00125019', cLocation = '1', cSoldStat = '0', cUnitType = '', sCompnyID = '', sWarranty = '', sPayloadx = '', cConditnx = '0', nLedgerNo = 0, dModified = null"; 
        instance.executeUpdate(lsSQL);
        lsSQL = "INSERT INTO Inv_Serial SET sSerialID = 'M09525000293', sIndstCdx = '02', sBranchCd = 'M001', sWHouseID = '001', sClientID = '', sSerial01 = 'G3V5E-0348803', sSerial02 = 'PA0SGA510S0028645', nUnitPrce = 0, sStockIDx = 'M00125019', cLocation = '1', cSoldStat = '0', cUnitType = '', sCompnyID = '', sWarranty = '', sPayloadx = '', cConditnx = '0', nLedgerNo = 0, dModified = null"; 
        instance.executeUpdate(lsSQL);
        lsSQL = "INSERT INTO Inv_Serial SET sSerialID = 'M09525000294', sIndstCdx = '02', sBranchCd = 'M001', sWHouseID = '001', sClientID = '', sSerial01 = 'G3V5E-0348807', sSerial02 = 'PA0SGA510S0028649', nUnitPrce = 0, sStockIDx = 'M00125019', cLocation = '1', cSoldStat = '0', cUnitType = '', sCompnyID = '', sWarranty = '', sPayloadx = '', cConditnx = '0', nLedgerNo = 0, dModified = null"; 
        instance.executeUpdate(lsSQL);
        lsSQL = "INSERT INTO Inv_Serial SET sSerialID = 'M09525000295', sIndstCdx = '02', sBranchCd = 'M001', sWHouseID = '001', sClientID = '', sSerial01 = 'G3V5E-0424173', sSerial02 = 'PA0SGA510S0039056', nUnitPrce = 0, sStockIDx = 'M00125020', cLocation = '1', cSoldStat = '0', cUnitType = '', sCompnyID = '', sWarranty = '', sPayloadx = '', cConditnx = '0', nLedgerNo = 0, dModified = null"; 
        instance.executeUpdate(lsSQL);
        lsSQL = "INSERT INTO Inv_Serial SET sSerialID = 'M09525000296', sIndstCdx = '02', sBranchCd = 'M001', sWHouseID = '001', sClientID = '', sSerial01 = 'G3V5E-0424195', sSerial02 = 'PA0SGA510S0039080', nUnitPrce = 0, sStockIDx = 'M00125020', cLocation = '1', cSoldStat = '0', cUnitType = '', sCompnyID = '', sWarranty = '', sPayloadx = '', cConditnx = '0', nLedgerNo = 0, dModified = null"; 
        instance.executeUpdate(lsSQL);
        lsSQL = "INSERT INTO Inv_Serial SET sSerialID = 'M09525000297', sIndstCdx = '02', sBranchCd = 'M001', sWHouseID = '001', sClientID = '', sSerial01 = 'G3V5E-0424199', sSerial02 = 'PA0SGA510S0039084', nUnitPrce = 0, sStockIDx = 'M00125020', cLocation = '1', cSoldStat = '0', cUnitType = '', sCompnyID = '', sWarranty = '', sPayloadx = '', cConditnx = '0', nLedgerNo = 0, dModified = null"; 
        instance.executeUpdate(lsSQL);
        
        //populate Inventory from Spareparts
        lsSQL = "INSERT INTO Inventory SET sStockIDx = 'M00115049321', sBarCodex = '2BL-E1181-00-00', sDescript = 'GASKET, CYLINDER HEAD 1', sBrandIDx = 'M0W1003', sModelIDx = 'M0W316024', sColorIDx = 'M001220', sIndstCdx = '02', nUnitPrce = 90 , nPurPrice = 63    , cSerialze = '0', cInvStatx = '1', cRecdStat = '1', sModified = '', dModified = null";
        instance.executeUpdate(lsSQL);
        lsSQL = "INSERT INTO Inventory SET sStockIDx = 'M00116019390', sBarCodex = '2DP-E5407-00-00', sDescript = 'ELEMENT 1              ', sBrandIDx = 'M0W1003', sModelIDx = 'M0W315037', sColorIDx = 'M001220', sIndstCdx = '02', nUnitPrce = 110, nPurPrice = 77    , cSerialze = '0', cInvStatx = '1', cRecdStat = '1', sModified = '', dModified = null";
        instance.executeUpdate(lsSQL);
        lsSQL = "INSERT INTO Inventory SET sStockIDx = 'M00117001959', sBarCodex = '2PH-E1351-10-00', sDescript = 'GASKET, CYLINDER (2SX2)', sBrandIDx = 'M0W1003', sModelIDx = 'M00116031', sColorIDx = 'M001220', sIndstCdx = '02', nUnitPrce = 45 , nPurPrice = 31.5  , cSerialze = '0', cInvStatx = '1', cRecdStat = '1', sModified = '', dModified = null";
        instance.executeUpdate(lsSQL);
        lsSQL = "INSERT INTO Inventory SET sStockIDx = 'M0W323000395', sBarCodex = '2PH-F8385-01-00', sDescript = 'COVER, UNDER 1         ', sBrandIDx = 'M0W1003', sModelIDx = 'M00124070', sColorIDx = 'M001220', sIndstCdx = '02', nUnitPrce = 280, nPurPrice = 195.98, cSerialze = '0', cInvStatx = '1', cRecdStat = '1', sModified = '', dModified = null";
        instance.executeUpdate(lsSQL);
        

        //populate Inv_Master from SP_Inventory
        lsSQL = "INSERT INTO Inv_Master SET sStockIDx = 'M00115049321', sBranchCd = 'M001', sWHouseID = '001', sIndstCdx = '02', sLocatnID = '', nBegQtyxx = 0, nQtyOnHnd = 1, nLedgerNo = 5, nBackOrdr = 1, nResvOrdr = 4, nFloatQty = 0, cPrimaryx = '1', cConditnx = '0', sPayLoadx = '', cRecdStat = '1', sModified = '', dModified = null";
        instance.executeUpdate(lsSQL);
        lsSQL = "INSERT INTO Inv_Master SET sStockIDx = 'M00116019390', sBranchCd = 'M001', sWHouseID = '001', sIndstCdx = '02', sLocatnID = '', nBegQtyxx = 0, nQtyOnHnd = 2, nLedgerNo = 4, nBackOrdr = 2, nResvOrdr = 3, nFloatQty = 0, cPrimaryx = '1', cConditnx = '0', sPayLoadx = '', cRecdStat = '1', sModified = '', dModified = null";
        instance.executeUpdate(lsSQL);
        lsSQL = "INSERT INTO Inv_Master SET sStockIDx = 'M00117001959', sBranchCd = 'M001', sWHouseID = '001', sIndstCdx = '02', sLocatnID = '', nBegQtyxx = 0, nQtyOnHnd = 3, nLedgerNo = 3, nBackOrdr = 3, nResvOrdr = 2, nFloatQty = 0, cPrimaryx = '1', cConditnx = '0', sPayLoadx = '', cRecdStat = '1', sModified = '', dModified = null";
        instance.executeUpdate(lsSQL);
        lsSQL = "INSERT INTO Inv_Master SET sStockIDx = 'M0W323000395', sBranchCd = 'M001', sWHouseID = '001', sIndstCdx = '02', sLocatnID = '', nBegQtyxx = 0, nQtyOnHnd = 4, nLedgerNo = 2, nBackOrdr = 4, nResvOrdr = 1, nFloatQty = 0, cPrimaryx = '1', cConditnx = '0', sPayLoadx = '', cRecdStat = '1', sModified = '', dModified = null";
        instance.executeUpdate(lsSQL);
    }
  
    
}
