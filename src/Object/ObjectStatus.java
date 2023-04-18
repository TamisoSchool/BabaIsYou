package Object;

import java.util.HashMap;

public class ObjectStatus {
    public HashMap<PropertyTypeText, Boolean> status = new HashMap();
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
