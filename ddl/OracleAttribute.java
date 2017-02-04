package ddl;

public class OracleAttribute extends AbstractAttribute{

	public String name;
	public String type;
	public boolean notNull;
	public int size;
	public boolean primaryKey;

	public OracleAttribute(String name, String type, int size, boolean notNull, boolean primaryKey) {
		super(name,type,size,notNull,primaryKey);
	}

	public OracleAttribute(OracleAttribute attributeAt) {
		super(attributeAt);
	}
	
	@Override
	public int checkSizeAttributes(){
		if("VARCHAR2".equals(this.type)){
			if(size == 0 || size > 255){
				return -1;
			}else{
				return 1;
			}
		}else if ("NUMBER".equals(this.type)){
			if(size == 0 || size > 38){
				return -2;
			}else{
				return 2;
			}
		}else if ("CHAR".equals(this.type)){
			if(size == 0 || size > 255){
				return -3;
			}else{
				return 3;
			}
	
		}else{
			return 0;
		}
	}
		public String attributeSizeError(int i){
			switch (i){
				case -1 : return "1 <= taille VARCHAR <= 255";
				case -2 : return "1 <= taille NUMBER <= 38";
				case -3 : return "1 <= taille CHAR <= 255";
				default : return "";
			}
		}


}
