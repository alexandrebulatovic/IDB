package useful;

public class ForeinKey {
	
	String utilisateur;
	
	/**
	 * La table contenant l'attribut sur laquelle s'applique cette clé étrangère
	 */
	String table;
	
	/**
	 * La pk sur laquelle cette classe pointe
	 * (nom de la colonne/attribut)
	 */
	private String pkRef;
	
	/**
	 * le nom de la colonne/attribut qui est clé étrangère
	 */
	private String fk;
	
	
	
	
	public ForeinKey(){
		
	}
	
	/**
	 * Constructeur 
	 * @param table le nom de la table de la colonne/attribut
	 * @param fk le nom de la fk
	 * @param ref
	 */
	public ForeinKey(String table,String fk, String ref){
		setTable(table);
		setFk(fk);
		//TODO
	}




	public Object getPkRef() {
		return pkRef;
	}

	public void setTable(String table){
		this.table=table;
	}
	
	public String getTable(){
		return table;
	}



	public void setPkRef(String pkRef) {
		this.pkRef = pkRef;
	}




	public String getFk() {
		return fk;
	}




	public void setFk(String fk) {
		this.fk = fk;
	}
}
