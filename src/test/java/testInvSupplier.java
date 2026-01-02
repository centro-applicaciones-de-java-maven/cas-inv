/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.guanzon.appdriver.base.GRiderCAS;
import org.guanzon.appdriver.base.GuanzonException;
import org.guanzon.cas.inv.InvSupplierPrice;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Administrator
 */
public class testInvSupplier {
    static GRiderCAS instance;
    static InvSupplierPrice poTrans;
    
    public testInvSupplier() {
    }
    
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
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
     @Test
     public void test() throws SQLException, GuanzonException {
        System.out.println("testPORcv_MC()");
        String lsSQL;
        lsSQL = "SELECT * FROM Inv_Master" + 
               " WHERE sBranchCD = 'M095'" + 
                 " AND sStockIDx IN ('M00124110')" + 
                 " AND sIndstCdx = '02'";

        ResultSet loRSOldInvMas01 = instance.executeQuery(lsSQL);
        loRSOldInvMas01.next();
        System.out.println("++++++++ OLD ++++++++");
        System.out.println("TABLE: Inv_Master");
        System.out.println("Stock ID: " + loRSOldInvMas01.getString("sStockIDx"));
        System.out.println("Avg Cost: " + loRSOldInvMas01.getDouble("nAvgCostx"));
        System.out.println("QTY-Hand: " + loRSOldInvMas01.getDouble("nQtyOnHnd"));

        instance.beginTrans("CREATE", "Create Purchase Order", "", "M0012500001");
        poTrans = new InvSupplierPrice(instance, "M095", "02", "M001250002");
        poTrans.initTransaction("M0952500004");
        poTrans.addDetail("02", "M00124110", 5, 20);
        poTrans.addDetail("02", "M00124112", 6, 30);
        poTrans.saveTransaction();

        lsSQL = "SELECT * FROM Inv_Master" + 
               " WHERE sBranchCD = 'M095'" + 
                 " AND sStockIDx IN ('M00124110')" + 
                 " AND sIndstCdx = '02'";
                 

        ResultSet loRSNewInvMas01 = instance.executeQuery(lsSQL);
        loRSNewInvMas01.next();
        System.out.println("++++++++ NEW ++++++++");
        System.out.println("TABLE: Inv_Master");
        System.out.println("Stock ID: " + loRSNewInvMas01.getString("sStockIDx"));
        System.out.println("Avg Cost: " + loRSNewInvMas01.getDouble("nAvgCostx"));
        System.out.println("QTY-Hand: " + loRSNewInvMas01.getDouble("nQtyOnHnd"));

        //Di dapat gumalaw ang quantity on hand
        assertEquals(loRSOldInvMas01.getDouble("nQtyOnHnd"), loRSNewInvMas01.getDouble("nQtyOnHnd"), 4);

        double lnAvgCostx;
        if (loRSOldInvMas01.getDouble("nAvgCostx") <= 0) {
            //Please the poTrans.addDetail above for this particular stock
            lnAvgCostx = 20;
        } else {
            double lnTotalQty = 5 + loRSOldInvMas01.getDouble("nQtyOnHnd");
            double lnAvgCosty = (loRSOldInvMas01.getDouble("nAvgCostx") * loRSOldInvMas01.getDouble("nQtyOnHnd") / lnTotalQty)
                              + (5 * 20 / lnTotalQty);
            lnAvgCostx = BigDecimal.valueOf(lnAvgCosty)
                         .setScale(4, RoundingMode.HALF_UP)
                         .doubleValue();
        }
        assertEquals(loRSNewInvMas01.getDouble("nAvgCostx"), lnAvgCostx, 4);
        
        lsSQL = "SELECT * FROM Inv_Supplier" + 
               " WHERE sStockIDx IN ('M00124110')" + 
                 " AND sIndstCdx = '02'";

        ResultSet loRSSuppInvMas01 = instance.executeQuery(lsSQL);
        loRSSuppInvMas01.next();
        System.out.println("++++++++ OLD ++++++++");
        System.out.println("TABLE: Inv_Supplier");
        System.out.println("Stock ID: " + loRSSuppInvMas01.getString("sStockIDx"));
        System.out.println("Supplier: " + loRSSuppInvMas01.getString("sSupplier"));
        System.out.println("Industry: " + loRSSuppInvMas01.getString("sIndstCdx"));
        System.out.println("Pur Prce: " + loRSSuppInvMas01.getDouble("nUnitPrce"));
        System.out.println("Avg Prce: " + loRSSuppInvMas01.getDouble("nAvePurcx"));
        System.out.println("Source  : " + loRSSuppInvMas01.getString("sSourceNo"));
        
        
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

        //Create Inv_Supplier
        lsSQL = "CREATE TABLE `Inv_Supplier` (" +
                "  `sStockIDx` varchar(12) NOT NULL," +
                "  `sIndstCdx` varchar(2) NOT NULL," +
                "  `sSupplier` varchar(12) NOT NULL," +
                "  `nUnitPrce` decimal(13,4) DEFAULT NULL," +
                "  `nAvePurcx` decimal(13,4) DEFAULT NULL," +
                "  `sSourceNo` varchar(12) DEFAULT NULL," +
                "  `cRecdStat` char(1) DEFAULT '1'," +
                "  `dTimeStmp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP," +
                "  PRIMARY KEY (`sStockIDx`,`sIndstCdx`,`sSupplier`))";
        instance.executeUpdate(lsSQL);
    }    

    public static void populateInventoryTable() throws SQLException{
        String lsSQL; 
        //populate Inventory from MC_Inventory;
        lsSQL = "INSERT INTO Inventory SET sStockIDx = 'M00124110', sBrandIDx = '', sModelIDx = 'M00124070', sColorIDx = 'M001220', sIndstCdx = '02', nUnitPrce = 0, nPurPrice = 4, cSerialze = '1', cInvStatx = '1', cRecdStat = '1', sModified = '', dModified = null";
        instance.executeUpdate(lsSQL);
        lsSQL = "INSERT INTO Inventory SET sStockIDx = 'M00124112', sBrandIDx = '', sModelIDx = 'M00124070', sColorIDx = 'M001281', sIndstCdx = '02', nUnitPrce = 0, nPurPrice = 3, cSerialze = '1', cInvStatx = '1', cRecdStat = '1', sModified = '', dModified = null";
        instance.executeUpdate(lsSQL);
        lsSQL = "INSERT INTO Inventory SET sStockIDx = 'M00125035', sBrandIDx = '', sModelIDx = 'M00125026', sColorIDx = 'M001287', sIndstCdx = '02', nUnitPrce = 0, nPurPrice = 2, cSerialze = '1', cInvStatx = '1', cRecdStat = '1', sModified = '', dModified = null";
        instance.executeUpdate(lsSQL);
        lsSQL = "INSERT INTO Inventory SET sStockIDx = 'M00125036', sBrandIDx = '', sModelIDx = 'M00125027', sColorIDx = 'M0W2008', sIndstCdx = '02', nUnitPrce = 0, nPurPrice = 1, cSerialze = '1', cInvStatx = '1', cRecdStat = '1', sModified = '', dModified = null";
        instance.executeUpdate(lsSQL);
        lsSQL = "INSERT INTO Inventory SET sStockIDx = 'M00125055', sBrandIDx = '', sModelIDx = 'M00125045', sColorIDx = 'M001301', sIndstCdx = '02', nUnitPrce = 0, nPurPrice = 0, cSerialze = '1', cInvStatx = '1', cRecdStat = '1', sModified = '', dModified = null";
        instance.executeUpdate(lsSQL);
        
        //populate Inv_Master from MC_Inventory
        lsSQL = "INSERT INTO Inv_Master SET sStockIDx = 'M00124110', sBranchCd = 'M095', sWHouseID = '001', sIndstCdx = '02', sLocatnID = '', nBegQtyxx = 0, nQtyOnHnd = 2, nAvgCostx = 5, nLedgerNo = 1, nBackOrdr = 4, nResvOrdr = 3, nFloatQty = 0, cPrimaryx = '1', cConditnx = '0', sPayLoadx = '', cRecdStat = '1', sModified = '', dModified = null";
        instance.executeUpdate(lsSQL);
        lsSQL = "INSERT INTO Inv_Master SET sStockIDx = 'M00124112', sBranchCd = 'M095', sWHouseID = '001', sIndstCdx = '02', sLocatnID = '', nBegQtyxx = 0, nQtyOnHnd = 1, nAvgCostx = 6, nLedgerNo = 2, nBackOrdr = 3, nResvOrdr = 2, nFloatQty = 0, cPrimaryx = '1', cConditnx = '0', sPayLoadx = '', cRecdStat = '1', sModified = '', dModified = null";
        instance.executeUpdate(lsSQL);
        lsSQL = "INSERT INTO Inv_Master SET sStockIDx = 'M00125035', sBranchCd = 'M095', sWHouseID = '001', sIndstCdx = '02', sLocatnID = '', nBegQtyxx = 0, nQtyOnHnd = 1, nAvgCostx = 7, nLedgerNo = 3, nBackOrdr = 2, nResvOrdr = 1, nFloatQty = 0, cPrimaryx = '1', cConditnx = '0', sPayLoadx = '', cRecdStat = '1', sModified = '', dModified = null";
        instance.executeUpdate(lsSQL);
        lsSQL = "INSERT INTO Inv_Master SET sStockIDx = 'M00125036', sBranchCd = 'M095', sWHouseID = '001', sIndstCdx = '02', sLocatnID = '', nBegQtyxx = 0, nQtyOnHnd = 1, nAvgCostx = 8, nLedgerNo = 4, nBackOrdr = 1, nResvOrdr = 0, nFloatQty = 0, cPrimaryx = '1', cConditnx = '0', sPayLoadx = '', cRecdStat = '1', sModified = '', dModified = null";
        instance.executeUpdate(lsSQL);
        lsSQL = "INSERT INTO Inv_Master SET sStockIDx = 'M00125055', sBranchCd = 'M095', sWHouseID = '001', sIndstCdx = '02', sLocatnID = '', nBegQtyxx = 0, nQtyOnHnd = 3, nAvgCostx = 0, nLedgerNo = 5, nBackOrdr = 0, nResvOrdr = 1, nFloatQty = 0, cPrimaryx = '1', cConditnx = '0', sPayLoadx = '', cRecdStat = '1', sModified = '', dModified = null";
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
    }
}
