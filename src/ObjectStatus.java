import java.util.AbstractMap;
import java.util.AbstractMap.SimpleEntry;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class ObjectStatus {
    public HashMap<PropertyTypeText, boolean> status = new HashMap();
    public final ObjectType objectType;
    public ObjectStatus(ObjectType objectType){
        this.objectType = objectType;
        PropertyTypeText[] enumConstants = (PropertyTypeText.class).getEnumConstants();
        for (PropertyTypeText enumConstant : enumConstants) {
            status.put(enumConstant, false);
        }
    }
    public void set_property(PropertyTypeText property, boolean newVal){
        if(property == null)
            return;
        status.replace(property, newVal);
    }

    public boolean get_property(PropertyTypeText property){
        return status.get(property);
    }
}
