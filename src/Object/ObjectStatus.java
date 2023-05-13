package Object;

import java.util.HashMap;
/**
 * Every object type with every status possible to have.
 */
public class ObjectStatus {
    /**
     * Property like melt, hot etc.
     * used for collision with objects.
     */
    private final HashMap<PropertyTypeText, Boolean> status = new HashMap();
    /**
        * status of specific object type
     */
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
    public void clear_all_property(){
        status.replaceAll((k, v) -> false);
    }
    public boolean get_property(PropertyTypeText property){
        return status.get(property);
    }
}
