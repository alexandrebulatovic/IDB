package ddl;

public interface I_Attribute 
{

	public abstract String getName();

	public abstract String getType();

	public abstract int getSize();

	public abstract boolean isNotNull();

	public abstract boolean isPk();

	public abstract int checkSizeAttributes();

	public abstract void setName(String name);

	public abstract void setType(String type);

	public abstract void setNotNull(boolean notNull);

	public abstract void setSize(int size);

	public abstract void setPk(boolean pk);

	public abstract String attributeSizeError(int i);
}
