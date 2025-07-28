import java.sql.SQLException;
import javax.sql.rowset.CachedRowSet;
import org.guanzon.appdriver.base.GRiderCAS;
import org.guanzon.appdriver.base.MiscUtil;
import org.guanzon.cas.inv.roq.ROQFactory;
import org.guanzon.cas.inv.roq.iROQ;
import org.json.simple.JSONObject;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class testGetROQ {
    static GRiderCAS instance;
    static iROQ trans;
    
    static JSONObject poJSON;

    @BeforeClass
    public static void setUpClass() {
        System.setProperty("sys.default.path.metadata", "D:/GGC_Maven_Systems/config/metadata/new/");

        instance = MiscUtil.Connect();
        
        trans = ROQFactory.make(instance, "0004");
        
        poJSON = trans.InitTransaction();
        
        if (!"success".equals((String) poJSON.get("result"))){
            System.err.println((String) poJSON.get("message"));
            Assert.fail();
        }
    }

    @Test
    public void testNewTransaction() {        
        try {
           poJSON = trans.LoadRecommendedOrder();
           
           if (!"success".equals((String) poJSON.get("result"))){
                System.err.println((String) poJSON.get("message"));
                Assert.fail();
           }
           
            CachedRowSet loROQ =  trans.getRecommendations();
            
            loROQ.last();
            System.out.println("TOTAL WITH ROQ ITEMS: " + loROQ.getRow());
            
            loROQ.beforeFirst();
            
            while(loROQ.next()){
                String lsStockIDx = loROQ.getString("sStockIDx");
                String lsModelNme = loROQ.getString("sModelNme");
                double lnBackOrder = loROQ.getDouble("nBackOrdr");
                double lnOnTransit = loROQ.getDouble("nOnTranst");
                double lnReservedx = loROQ.getDouble("nResvOrdr");
                double lnQOH = loROQ.getDouble("nQtyOnHnd");
                double lnROQ = loROQ.getDouble("nRecOrder");
                
                System.out.println("Stock Id: "  +  lsStockIDx + ", Model: " + lsModelNme + ", BO: " + lnBackOrder + ", On Transit: " + lnOnTransit + ", Reserved Order: " + lnReservedx + ", QOH: " + lnQOH + ", ROQ: " + lnROQ);
            }
        } catch (SQLException e) {
            System.err.println(MiscUtil.getException(e));
            Assert.fail();
        }
    }   
    
    @AfterClass
    public static void tearDownClass() {
        trans = null;
        instance = null;
    }
}
