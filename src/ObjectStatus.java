public class ObjectStatus {
    public boolean isPushable;
    public boolean isDeath;
    public boolean isFloat;
    public boolean isMelt;
    public boolean isHot;
    public final ObjectType objectType;
    public ObjectStatus(ObjectType objectType, boolean isDeath, boolean isPushable, boolean isFloat){
        this.objectType = objectType;
        this.isDeath = isDeath;
        this.isPushable = isPushable;
        this.isFloat = isFloat;
    }
}
