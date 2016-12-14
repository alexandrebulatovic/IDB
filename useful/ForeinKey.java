package useful;

/**
 * Cette classe définit un paramètre de clé étrangère :
 * Cette clé doit être définit par une clé primaire (référence qui est un attribut) et un second attribut correspondant
 * à la clé étrangère
 * 
 * Il est à noté que les méthodes incluses métadatas collectionnement bien plus d'éléments pour les définir
 * Nous avons donc décidé de choisir les plus pertinents
 *
 */
public class ForeinKey {
	

	private String name;
	

	
	private String pkTableUser;
	
	private String pkTableName;

	private String pkColumnName;
	
	
	@Override
	public String toString() {
		return "ForeinKey [name=" + name + ", pkTableUser=" + pkTableUser + ", pkTableName=" + pkTableName
				+ ", pkColumnName=" + pkColumnName + ", fkTableUser=" + fkTableUser + ", fkTableName=" + fkTableName
				+ ", fkColumnName=" + fkColumnName + "]";
	}

	private String fkTableUser;
	
	private String fkTableName;
	
	private String fkColumnName;
	
	
	public ForeinKey(){}
	
	/**
	 * Constructeur
	 * @param name
	 * @param tableTarget
	 * @param fk
	 * @param pkRef
	 * @param fkTableUser
	 * @param userTarget
	 */
	public ForeinKey(String name, String pkTableUser,String pkTableName,String pkColumnName, String fkTableUser, String fkTableName,String fkColumnName){
		this.setName(name);
		
		this.setPkTableUser(pkTableUser);
		this.setPkTableName(pkTableName);
		this.setPkColumnName(pkColumnName);
		
		this.setFkTableUser(fkTableUser);
		this.setFkTableName(fkTableName);
		this.setFkColumnName(fkColumnName);

	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPkTableUser() {
		return pkTableUser;
	}

	public void setPkTableUser(String pkTableUser) {
		this.pkTableUser = pkTableUser;
	}

	public String getPkTableName() {
		return pkTableName;
	}

	public void setPkTableName(String pkTableName) {
		this.pkTableName = pkTableName;
	}

	public String getPkColumnName() {
		return pkColumnName;
	}

	public void setPkColumnName(String pkColumnName) {
		this.pkColumnName = pkColumnName;
	}

	public String getFkTableUser() {
		return fkTableUser;
	}

	public void setFkTableUser(String fkTableUser) {
		this.fkTableUser = fkTableUser;
	}

	public String getFkTableName() {
		return fkTableName;
	}

	public void setFkTableName(String fkTableName) {
		this.fkTableName = fkTableName;
	}

	public String getFkColumnName() {
		return fkColumnName;
	}

	public void setFkColumnName(String fkColumnName) {
		this.fkColumnName = fkColumnName;
	}
	

}
