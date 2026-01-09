/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.guanzon.cas.inv;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.guanzon.appdriver.base.GRiderCAS;
import org.guanzon.appdriver.base.GuanzonException;
import org.guanzon.appdriver.base.SQLUtil;

/* 
 * Use to update the Inv_Supplier table to monitor the latest purchase price of stock per supplier
 */
public class InvSupplierPrice {
    private GRiderCAS poDriver;
    private String psBranchCD; 
    private String psIndstCdx;
    private String psSupplier;
    private String psSourceNo;
    private String psUserIDxx;
    
    private List<DetailEntry> paDetailEntry;
    private boolean pbIsReverse;
    
    
    /**
     * Constructs a new {@code InvSupplierPrice} instance using the provided CAS driver.
     *
     * <p>This constructor initializes the supplier price context based on the current
     * branch, industry, and user information retrieved from the {@code GRiderCAS} driver.
     * It also sets default values for supplier and source number, and prepares an empty
     * list of detail entries for transaction processing.</p>
     *
     * <p>Initialization details:</p>
     * <ul>
     *   <li>{@code poDriver} is set to the provided CAS driver.</li>
     *   <li>{@code psBranchCD} is initialized from {@code poDriver.getBranchCode()}.</li>
     *   <li>{@code psIndstCdx} is initialized from {@code poDriver.getIndustry()}.</li>
     *   <li>{@code psSupplier} and {@code psSourceNo} are set to empty strings by default.</li>
     *   <li>{@code psUserIDxx} is initialized from {@code poDriver.getUserID()}.</li>
     *   <li>{@code paDetailEntry} is initialized as an empty {@code ArrayList}.</li>
     * </ul>
     *
     * @param foGRider the CAS driver instance used for database operations and context retrieval
     */
    public InvSupplierPrice(GRiderCAS foGRider){
        poDriver = foGRider;
        psBranchCD = poDriver.getBranchCode();
        psIndstCdx = poDriver.getIndustry();
        psSupplier = "";
        psSourceNo = "";
        
        psUserIDxx = poDriver.getUserID();
        
        paDetailEntry = new ArrayList<>();
    }

    /**
     * Constructs a new {@code InvSupplierPrice} instance for managing supplier pricing
     * within the Computerized Accounting System (CAS).
     *
     * <p>This constructor initializes the driver, branch code, supplier, and user context.
     * It also sets default values for industry code, source number, and reverse flag,
     * and prepares an empty list of detail entries for transaction processing.</p>
     *
     * @param foGRider   the CAS driver instance used for database operations
     * @param fsBranchCD the branch code where the transaction is associated
     * @param fsSupplier the supplier identifier for which pricing will be managed
     * @param fsUserIDxx the user ID of the person performing the transaction
     */
    public InvSupplierPrice(GRiderCAS foGRider, String fsBranchCD, String fsSupplier, String fsUserIDxx){
        poDriver = foGRider;
        psBranchCD = fsBranchCD;
        psIndstCdx = "";
        psSupplier = fsSupplier;
        psSourceNo = "";

        psUserIDxx = fsUserIDxx;
        pbIsReverse = false;

        paDetailEntry = new ArrayList<>();
    }

    public void initTransaction(String fsSourceNo){
        psSourceNo = fsSourceNo;
    }

    /**
     * Adds a detail entry for a stock item into the current transaction.
     *
     * <p>This method validates the source number, checks if the stock item exists
     * in the inventory/master tables, and then either adds a new detail entry or
     * updates an existing one in the {@code paDetailEntry} list.</p>
     *
     * @param fsIndstCdx   Industry code of the stock item
     * @param fsStockIDx   Stock ID of the item
     * @param fnQuantity   Quantity to add
     * @param fnUnitPrice  Unit price of the item (used if DB price is not required)
     * @throws SQLException       if a database access error occurs
     * @throws GuanzonException   if validation fails or stock item is not found
     */
    public void addDetail(String fsIndstCdx,
                          String fsStockIDx,
                          double fnQuantity,
                          double fnUnitPrice) throws SQLException, GuanzonException {

        // Validate source number
        if (psSourceNo == null || psSourceNo.isEmpty()) {
            throw new GuanzonException(
                GuanzonException.GE_SEQUENCE_EXCEPTION,
                "Invalid Source No detected!"
            );
        }

        // Build SQL query
        String lsSQL = "SELECT DISTINCT "
                        + "  a.sWHouseID, "
                        + "  a.sStockIDx, "
                        + "  a.sBranchCD, "
                        + "  b.sStockIDx AS sStockIDy"
                     + " FROM Inventory b "
                     + " LEFT JOIN Inv_Master a "
                        + "  ON b.sStockIDx = a.sStockIDx"
                        + " AND b.sIndstCdx = a.sIndstCdx"
                        + " AND a.sBranchCd = " + SQLUtil.toSQL(psBranchCD)
                        + " AND a.cConditnx = " + SQLUtil.toSQL("0") + " "
                     + " WHERE b.sStockIDx = " + SQLUtil.toSQL(fsStockIDx)
                       + " AND b.sIndstCdx = " + SQLUtil.toSQL(fsIndstCdx)
                     + " ORDER BY a.sStockIDx DESC";

        ResultSet loRS = poDriver.executeQuery(lsSQL);
        // Check if stock exists
        if (!loRS.next()) {
            throw new GuanzonException(
                GuanzonException.GE_NOTFOUND_EXCEPTION,
                "Stock Item does not exist!"
            );
        }

        if (loRS.getString("sStockIDx") == null) {
            throw new GuanzonException(
                GuanzonException.GE_NOTFOUND_EXCEPTION,
                "Stock Item does not exist in the branch!"
            );
        }

        // Create detail entry (using provided unit price)
        DetailEntry loDetail = new DetailEntry(
            fsIndstCdx,
            fsStockIDx,
            fnQuantity,
            fnUnitPrice
        );

        // Add or update entry in the list
        Optional<DetailEntry> match = paDetailEntry.stream()
            .filter(p -> p.psIndstCdx.equalsIgnoreCase(loDetail.psIndstCdx)
                      && p.psStockIDx.equalsIgnoreCase(loDetail.psStockIDx))
            .findFirst();

        if (match.isPresent()) {
            // Update quantity
            match.get().pnQuantity += loDetail.pnQuantity;
        } else {
            // Add new entry
            paDetailEntry.add(loDetail);
        }
    }
    
    /**
     * Saves the current transaction by updating inventory, master records, and supplier records.
     *
     * <p>This method iterates through all detail entries in {@code paDetailEntry}, validates
     * their existence in the inventory/master tables, and performs the following operations:</p>
     * <ul>
     *   <li>Updates the unit price in {@code Inventory} if it differs from the purchase price.</li>
     *   <li>Recomputes and updates the average cost in {@code Inv_Master}.</li>
     *   <li>Inserts or updates supplier records in {@code Inv_Supplier} with purchase price and average purchase cost.</li>
     * </ul>
     *
     * @throws SQLException       if a database access error occurs
     * @throws GuanzonException   if validation fails or stock item is not found
     */
    public void saveTransaction() throws SQLException, GuanzonException {
        for (DetailEntry loDetail : paDetailEntry) {
            // Build SQL query
            String lsSQL = "SELECT DISTINCT"
                            + "  a.sStockIDx,"
                            + "  a.sBranchCD,"
                            + "  a.nAvgCostx,"
                            + "  a.nQtyOnHnd,"
                            + "  b.sStockIDx AS sStockIDy,"
                            + "  b.nUnitPrce,"
                            + "  c.sStockIDx AS sStockIDz,"
                            + "  c.nUnitPrce AS zUnitPrce,"
                            + "  c.nAvePurcx AS zAvePurcx,"
                            + "  c.sSourceNo"
                         + " FROM Inventory b"
                             + " LEFT JOIN Inv_Master a "
                                 + "  ON b.sStockIDx = a.sStockIDx "
                                 + " AND b.sIndstCdx = a.sIndstCdx "
                             + " LEFT JOIN Inv_Supplier c "
                                 + "  ON b.sStockIDx = c.sStockIDx "
                                 + " AND b.sIndstCdx = c.sIndstCdx "
                                 + " AND c.sSupplier = " + SQLUtil.toSQL(psSupplier) 
                         + " WHERE a.sStockIDx = " + SQLUtil.toSQL(loDetail.psStockIDx)
                           + " AND a.sIndstCdx = " + SQLUtil.toSQL(loDetail.psIndstCdx)
                           + " AND a.sBranchCd = " + SQLUtil.toSQL(psBranchCD)
                           + " AND a.cConditnx = " + SQLUtil.toSQL("0")
                         + " ORDER BY b.sStockIDx DESC";

            System.out.println(lsSQL);

            try (ResultSet loRS = poDriver.executeQuery(lsSQL)) {
                // Trigger error if not existing (particularly in Inv_Master)
                if (!loRS.next()) {
                    throw new GuanzonException(
                        GuanzonException.GE_NOTFOUND_EXCEPTION,
                        "Item supposed to exist in the branch but found none!"
                    );
                }

                // --- Update Inventory ---
                if (loRS.getDouble("nUnitPrce") != loDetail.pnPurPrice && loDetail.pnPurPrice > 0) {
                    lsSQL = "UPDATE Inventory "
                          + "SET nUnitPrce = " + SQLUtil.toSQL(loDetail.pnPurPrice) + " "
                          + "WHERE sStockIDx = " + SQLUtil.toSQL(loDetail.psStockIDx)
                          + " AND sIndstCdx = " + SQLUtil.toSQL(loDetail.psIndstCdx);
                    System.out.println(lsSQL);
                    poDriver.executeQuery(lsSQL, "Inventory", psBranchCD, "", psIndstCdx);
                }

                // --- Update Inv_Master (average cost) ---
                double lnAvgCostx;
                if (loRS.getDouble("nAvgCostx") <= 0) {
                    lnAvgCostx = loDetail.pnPurPrice;
                } else {
                    double lnTotalQty = loDetail.pnQuantity + loRS.getDouble("nQtyOnHnd");
                    double lnAvgCosty = (loRS.getDouble("nAvgCostx") * loRS.getDouble("nQtyOnHnd") / lnTotalQty)
                                      + (loDetail.pnQuantity * loDetail.pnPurPrice / lnTotalQty);
                    lnAvgCostx = BigDecimal.valueOf(lnAvgCosty)
                                 .setScale(4, RoundingMode.HALF_UP)
                                 .doubleValue();
                }

                if (lnAvgCostx != loRS.getDouble("nAvgCostx")) {
                    lsSQL = "UPDATE Inv_Master "
                          + "SET nAvgCostx = " + SQLUtil.toSQL(lnAvgCostx)
                          + ", sModified = " + SQLUtil.toSQL(psUserIDxx)
                          + ", dModified = " + SQLUtil.toSQL(poDriver.getServerDate()) + " "
                          + "WHERE sBranchCd = " + SQLUtil.toSQL(psBranchCD)
                          + " AND sStockIDx = " + SQLUtil.toSQL(loDetail.psStockIDx)
                          + " AND sIndstCdx = " + SQLUtil.toSQL(loDetail.psIndstCdx)
                          + " AND cConditnx = " + SQLUtil.toSQL("0");
                    System.out.println(lsSQL);
                    poDriver.executeQuery(lsSQL, "Inv_Master", psBranchCD, "", psIndstCdx);
                }

                // --- Insert or Update Inv_Supplier ---
                if (loRS.getString("sStockIDz") == null) {
                    lsSQL = "INSERT INTO Inv_Supplier "
                          + "SET sStockIDx = " + SQLUtil.toSQL(loDetail.psStockIDx)
                          + ", sIndstCdx = " + SQLUtil.toSQL(loDetail.psIndstCdx)
                          + ", sSupplier = " + SQLUtil.toSQL(psSupplier)
                          + ", nUnitPrce = " + SQLUtil.toSQL(loDetail.pnPurPrice)
                          + ", nAvePurcx = " + SQLUtil.toSQL(loDetail.pnPurPrice)
                          + ", sSourceNo = " + SQLUtil.toSQL(psSourceNo)
                          + ", cRecdStat = '1'";
                    System.out.println(lsSQL);
                    poDriver.executeQuery(lsSQL, "Inv_Supplier", psBranchCD, "", psIndstCdx);
                } else {
                    double lnAvePurcx;
                    if (loRS.getDouble("zAvePurcx") <= 0) {
                        lnAvePurcx = loDetail.pnPurPrice;
                    } else {
                        lnAvePurcx = (loDetail.pnPurPrice + loRS.getDouble("zAvePurcx")) / 2;
                        lnAvePurcx = BigDecimal.valueOf(lnAvePurcx)
                                    .setScale(4, RoundingMode.HALF_UP)
                                    .doubleValue();
                    }
                    lsSQL = "UPDATE Inv_Supplier "
                          + "SET nUnitPrce = " + SQLUtil.toSQL(loDetail.pnPurPrice)
                          + ", nAvePurcx = " + SQLUtil.toSQL(lnAvePurcx)
                          + ", sSourceNo = " + SQLUtil.toSQL(psSourceNo)
                          + ", cRecdStat = '1' "
                          + "WHERE sStockIDx = " + SQLUtil.toSQL(loDetail.psStockIDx)
                          + " AND sIndstCdx = " + SQLUtil.toSQL(loDetail.psIndstCdx)
                          + " AND sSupplier = " + SQLUtil.toSQL(psSupplier);
                    System.out.println(lsSQL);
                    poDriver.executeQuery(lsSQL, "Inv_Supplier", psBranchCD, "", psIndstCdx);
                }
            }
        }
    }
    private class DetailEntry{
        public String psStockIDx;
        public String psIndstCdx;
        public double pnQuantity;
        public double pnPurPrice;
        
        public DetailEntry(String fsIndstCdx, String fsStockIDx, double fnQuantity, double fnPurPrice){
            this.psIndstCdx = fsIndstCdx;
            this.psStockIDx = fsStockIDx;
            this.pnQuantity = fnQuantity;
            this.pnPurPrice = fnPurPrice;
        }
    }
}
