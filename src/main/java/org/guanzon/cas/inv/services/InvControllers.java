package org.guanzon.cas.inv.services;

import org.guanzon.appdriver.base.GRiderCAS;
import org.guanzon.appdriver.base.LogWrapper;
import org.guanzon.cas.inv.Inv_Master;
import org.guanzon.cas.inv.Inventory;

public class InvControllers {
    public InvControllers(GRiderCAS applicationDriver, LogWrapper logWrapper) {
        poGRider = applicationDriver;
        poLogWrapper = logWrapper;
    }

    public Inventory Inventory() {
        if (poGRider == null) {
            poLogWrapper.severe("InvControllers.Inventory: Application driver is not set.");
            return null;
        }

        if (poInventory != null) {
            return poInventory;
        }

        poInventory = new Inventory();
        poInventory.setApplicationDriver(poGRider);
        poInventory.setWithParentClass(false);
        poInventory.setLogWrapper(poLogWrapper);
        poInventory.initialize();
        poInventory.newRecord();
        return poInventory;
    }

    public Inv_Master InventoryMaster() {
        if (poGRider == null) {
            poLogWrapper.severe("InvControllers.InventoryMaster: Application driver is not set.");
            return null;
        }

        if (poInvMaster != null) {
            return poInvMaster;
        }

        poInvMaster = new Inv_Master();
        poInvMaster.setApplicationDriver(poGRider);
        poInvMaster.setWithParentClass(false);
        poInvMaster.setLogWrapper(poLogWrapper);
        poInvMaster.initialize();
        poInvMaster.newRecord();
        return poInvMaster;
    }
    
//    public InvSerial InventorySerial() {
//        if (poGRider == null) {
//            poLogWrapper.severe("InvControllers.InventoryMaster: Application driver is not set.");
//            return null;
//        }
//
//        if (poInventorySerial != null) {
//            return poInventorySerial;
//        }
//
//        poInventorySerial = new InvSerial();
//        poInventorySerial.setApplicationDriver(poGRider);
//        poInventorySerial.setWithParentClass(true);
//        poInventorySerial.setLogWrapper(poLogWrapper);
//        poInventorySerial.initialize();
//        poInventorySerial.newRecord();
//        return poInventorySerial;
//    }
    
    

    private final GRiderCAS poGRider;
    private final LogWrapper poLogWrapper;

    private Inventory poInventory;
    private Inv_Master poInvMaster;
//    private InvSerial poInventorySerial;
}
