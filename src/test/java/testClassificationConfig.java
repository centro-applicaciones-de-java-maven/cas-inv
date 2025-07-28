import java.sql.SQLException;
import org.guanzon.appdriver.base.GRiderCAS;
import org.guanzon.appdriver.base.GuanzonException;
import org.guanzon.appdriver.base.MiscUtil;
import org.guanzon.cas.inv.roq.ClassificationConfig;
import org.json.simple.JSONObject;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class testClassificationConfig {
    static GRiderCAS instance;
    static ClassificationConfig record;

    @BeforeClass
    public static void setUpClass() {
        System.setProperty("sys.default.path.metadata", "D:/GGC_Maven_Systems/config/metadata/new/");

        instance = MiscUtil.Connect();
        
        try {
            record = new ClassificationConfig();
            record.setApplicationDriver(instance);
            record.setWithParentClass(false);
            record.initialize();
        } catch (SQLException | GuanzonException e) {
            Assert.fail(e.getMessage());
        }
    }
   
    @Test
    public void testUpdateRecord() {
        try {
            JSONObject loJSON;

            loJSON = record.openRecord("01", "0001");
            if ("error".equals((String) loJSON.get("result"))) {
                Assert.fail((String) loJSON.get("message"));
            }  
            
            System.out.println(record.getModel().getCategoryId());
            System.out.println(record.getModel().getIndustryCode());
            System.out.println(record.getModel().getMaximumQtyD());
            System.out.println(record.getModel().getMaximumQtyF());
            System.out.println(record.getModel().getMaximumStockA());
            System.out.println(record.getModel().getMaximumStockB());
            System.out.println(record.getModel().getMaximumStockC());
            System.out.println(record.getModel().getMaximumStockD());
            System.out.println(record.getModel().getMaximumStockE());
            System.out.println(record.getModel().getMaximumStockF());
            System.out.println(record.getModel().getMinimumStockA());
            System.out.println(record.getModel().getMinimumStockB());
            System.out.println(record.getModel().getMinimumStockC());
            System.out.println(record.getModel().getMinimumStockD());
            System.out.println(record.getModel().getMinimumStockE());
            System.out.println(record.getModel().getMinimumStockF());
            System.out.println(record.getModel().getNoMinMax());
            System.out.println(record.getModel().getNoOfMonths());
            System.out.println(record.getModel().getOrderFrequency());
            System.out.println(record.getModel().getPurchaseLeadTime());
            System.out.println(record.getModel().getSafetyA());
            System.out.println(record.getModel().getSafetyB());
            System.out.println(record.getModel().getSafetyC());
            System.out.println(record.getModel().getSafetyD());
            System.out.println(record.getModel().getStartMinMax());
            System.out.println(record.getModel().getVolumeRateA());
            System.out.println(record.getModel().getVolumeRateB());
            System.out.println(record.getModel().getVolumeRateC());
            System.out.println(record.getModel().getVolumeRateD());
        } catch (SQLException | GuanzonException e) {
            Assert.fail(e.getMessage());
        }
    }
    
    @AfterClass
    public static void tearDownClass() {
        record = null;
        instance = null;
    }
}
