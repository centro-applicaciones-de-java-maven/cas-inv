
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;
import org.guanzon.appdriver.base.GRiderCAS;
import org.guanzon.appdriver.base.GuanzonException;
import org.guanzon.cas.inv.roq.ClassifySP;
import org.json.simple.JSONObject;

public class testClassify {
    public static void main(String[] args) throws SQLException, GuanzonException {
        String path;
        if (System.getProperty("os.name").toLowerCase().contains("win")) {
            path = "D:/GGC_Maven_Systems";
        } else {
            path = "/srv/GGC_Maven_Systems";
        }
        System.setProperty("sys.default.path.config", path);
        System.setProperty("sys.default.path.metadata", System.getProperty("sys.default.path.config") + "/config/metadata/new/");

        if (!loadProperties()) {
            System.err.println("Unable to load config.");
            System.exit(1);
        } else {
            System.out.println("Config file loaded successfully.");
        }

        GRiderCAS instance = new GRiderCAS("gRider");

        if (!instance.logUser("gRider", "M001000001")) {
            System.err.println(instance.getMessage());
            System.exit(1);
        }
        
        System.out.println("Connected");
        
        //if the application driver branch is not the same as the configuration branch,
        //set the branch and industry variables with application driver values
        if (!instance.getBranchCode().equals(System.getProperty("store.branch.code"))){
            //get store codes
            System.setProperty("store.branch.code", instance.getBranchCode());
            System.setProperty("store.industry.code", instance.getIndustry());
            
            //save as defaults
            System.setProperty("store.default.branch.code", instance.getBranchCode());
            System.setProperty("store.default.industry.code", instance.getIndustry());
        }
        
        System.out.println("DEFAULT STORE CODES");
        System.out.println("BRANCH CODE: " + System.getProperty("store.branch.code"));
        System.out.println("INDUSTRY CODE: " + System.getProperty("store.industry.code"));

        ClassifySP trans = new ClassifySP();
        trans.setGRider(instance);
        trans.setBranch(instance.getBranchCode());
        trans.setCategory("0004");
        trans.setPeriodYear(2025);
        trans.setPeriodMonth(7);
        
        JSONObject loJSON = trans.InitTransaction();
        
        if (!"success".equals((String) loJSON.get("result"))){
            System.err.println((String) loJSON.get("message"));
            System.exit(1);
        }
        
        loJSON = trans.Classify();
        
        if ("success".equals((String) loJSON.get("result"))){
            System.out.println("Classification Done!!!");
        } else {
            System.err.println((String) loJSON.get("message"));
            System.exit(1);
        }
        
        System.exit(0);
    }

    private static boolean loadProperties() {
        try {
            Properties po_props = new Properties();
            po_props.load(new FileInputStream(System.getProperty("sys.default.path.config") + "/config/cas.properties"));

            //get store codes
            System.setProperty("store.branch.code", po_props.getProperty("store.branch.code"));
            System.setProperty("store.industry.code", po_props.getProperty("store.industry.code"));
            System.setProperty("store.inventory.category", po_props.getProperty("store.inventory.category"));
            
            //save as defaults
            System.setProperty("store.default.branch.code", po_props.getProperty("store.branch.code"));
            System.setProperty("store.default.industry.code", po_props.getProperty("store.industry.code"));
            System.setProperty("store.default.inventory.category", po_props.getProperty("store.inventory.category"));           
            
            return true;
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
            return false;
        } catch (IOException ex) {
            ex.printStackTrace();
            return false;
        }
    }
}
