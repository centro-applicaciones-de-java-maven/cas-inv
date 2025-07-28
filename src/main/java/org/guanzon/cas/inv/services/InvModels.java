package org.guanzon.cas.inv.services;

import org.guanzon.appdriver.base.GRiderCAS;
import org.guanzon.cas.inv.model.Model_Classification_Config;
import org.guanzon.cas.inv.model.Model_Inv_Master;
import org.guanzon.cas.inv.model.Model_Inv_Serial;
import org.guanzon.cas.inv.model.Model_Inv_Serial_Ledger;
import org.guanzon.cas.inv.model.Model_Inv_Serial_Registration;
import org.guanzon.cas.inv.model.Model_Inventory;

public class InvModels {

    public InvModels(GRiderCAS applicationDriver) {
        poGRider = applicationDriver;
    }

    public Model_Inventory Inventory() {
        if (poGRider == null) {
            System.err.println("InvModels.Inventory: Application driver is not set.");
            return null;
        }

        if (poInventory == null) {
            poInventory = new Model_Inventory();
            poInventory.setApplicationDriver(poGRider);
            poInventory.setXML("Model_Inventory");
            poInventory.setTableName("Inventory");
            poInventory.initialize();
        }

        return poInventory;
    }

    public Model_Inv_Master InventoryMaster() {
        if (poGRider == null) {
            System.err.println("InvModels.InventoryMaster: Application driver is not set.");
            return null;
        }

        if (poInvMaster == null) {
            poInvMaster = new Model_Inv_Master();
            poInvMaster.setApplicationDriver(poGRider);
            poInvMaster.setXML("Model_Inv_Master");
            poInvMaster.setTableName("Inv_Master");
            poInvMaster.initialize();
        }

        return poInvMaster;
    }
    
    public Model_Inv_Serial InventorySerial() {
        if (poGRider == null) {
            System.err.println("InvModels.InventorySerial: Application driver is not set.");
            return null;
        }

        if (poInvSerial == null) {
            poInvSerial = new Model_Inv_Serial();
            poInvSerial.setApplicationDriver(poGRider);
            poInvSerial.setXML("Model_Inv_Serial");
            poInvSerial.setTableName("Inv_Serial");
            poInvSerial.initialize();
        }

        return poInvSerial;
    }

//    public Model_Inv_Ledger InventoryLedger() {
//        if (poGRider == null) {
//            System.err.println("InvModels.InventoryLedger: Application driver is not set.");
//            return null;
//        }
//
//        if (poInvLedger == null) {
//            poInvLedger = new Model_Inv_Ledger();
//            poInvLedger.setApplicationDriver(poGRider);
//            poInvLedger.setXML("Model_Inv_Ledger");
//            poInvLedger.setTableName("Inv_Ledger");
//            poInvLedger.initialize();
//        }
//
//        return poInvLedger;
//    }
//   
//    
    public Model_Inv_Serial_Ledger InventorySerialLedger() {
        if (poGRider == null) {
            System.err.println("InvModels.InventorySerialLedger: Application driver is not set.");
            return null;
        }

        if (poInvSerialLeger == null) {
            poInvSerialLeger = new Model_Inv_Serial_Ledger();
            poInvSerialLeger.setApplicationDriver(poGRider);
            poInvSerialLeger.setXML("Model_Inv_Serial_Ledger");
            poInvSerialLeger.setTableName("Inv_Serial_Ledger");
            poInvSerialLeger.initialize();
        }

        return poInvSerialLeger;
    }
    
    public Model_Inv_Serial_Registration InventorySerialRegistration() {
        if (poGRider == null) {
            System.err.println("InvModels.InventorySerialRegistration: Application driver is not set.");
            return null;
        }

        if (poInvSerialReg == null) {
            poInvSerialReg = new Model_Inv_Serial_Registration();
            poInvSerialReg.setApplicationDriver(poGRider);
            poInvSerialReg.setXML("Model_Inv_Serial_Registration");
            poInvSerialReg.setTableName("Inv_Serial_Registration");
            poInvSerialReg.initialize();
        }

        return poInvSerialReg;
    }
    
    public Model_Classification_Config ClassificationConfig() {
        if (poGRider == null) {
            System.err.println("InvModels.ClassificationConfig: Application driver is not set.");
            return null;
        }

        if (poClassificationConfig == null) {
            poClassificationConfig = new Model_Classification_Config();
            poClassificationConfig.setApplicationDriver(poGRider);
            poClassificationConfig.setXML("Model_xxxClassificationConfig");
            poClassificationConfig.setTableName("xxxClassificationConfig");
            poClassificationConfig.initialize();
        }

        return poClassificationConfig;
    }
//
//    public Model_Inv_Classification_Detail InventoryClassificationDetail() {
//        if (poGRider == null) {
//            System.err.println("InvModels.InventoryClassificationDetail: Application driver is not set.");
//            return null;
//        }
//
//        if (poInvClassLedger == null) {
//            poInvClassLedger = new Model_Inv_Classification_Master();
//            poInvClassLedger.setApplicationDriver(poGRider);
//            poInvClassLedger.setXML("Model_Inv_Classification_Detail");
//            poInvClassLedger.setTableName("Inv_Classification_Detail");
//            poInvClassLedger.initialize();
//        }
//
//        return poInvClassDetail;
//    }
//
//    public Model_Inv_Classification_Master InventoryClassificationMaster() {
//        if (poGRider == null) {
//            System.err.println("InvModels.InventoryClassificationMaster: Application driver is not set.");
//            return null;
//        }
//
//        if (poInvClassLedger == null) {
//            poInvClassLedger = new Model_Inv_Classification_Master();
//            poInvClassLedger.setApplicationDriver(poGRider);
//            poInvClassLedger.setXML("Model_Inv_Classification_Master");
//            poInvClassLedger.setTableName("Inv_Classification_Master");
//            poInvClassLedger.initialize();
//        }
//
//        return poInvClassMaster;
//    }
    
    private final GRiderCAS poGRider;

    private Model_Inventory poInventory;
    private Model_Inv_Master poInvMaster;
//    private Model_Inv_Ledger poInvLedger;
//    private Model_Inv_Classification_Master poInvClassLedger;
    private Model_Inv_Serial poInvSerial;
    private Model_Inv_Serial_Ledger poInvSerialLeger;
    private Model_Inv_Serial_Registration poInvSerialReg;
    private Model_Classification_Config poClassificationConfig;
//    private Model_Inv_Classification_Detail poInvClassDetail;
//    private Model_Inv_Classification_Master poInvClassMaster;
}
