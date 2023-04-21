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

    public void attribute_translate(OperatorText operatorText, ObjectType ob2){
        if(this.objectType == ob2)
            return;
        if(operatorText == OperatorText.IS){
            // set ob objects to ob2
        }
    }
    public void attribute_translate(OperatorText operatorText, PropertyTypeText propertyTypeText){
        if(operatorText == OperatorText.IS){
            status.replace(propertyTypeText, true);
        }
    }
    public boolean get_property(PropertyTypeText property){
        return status.get(property);
    }
}
