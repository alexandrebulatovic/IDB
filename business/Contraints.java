package business;

public abstract class Contraints {
	

	
	/**
	 * Mot clé définissant la contrainte 
	 * exemple : PRIMARY KEY
	 */
	protected String keyWord;
	
	/**
	 * Une contrainte est appliqué à un attribut
	 */
	protected Attribute attribute;
	
	/**
	 * C'est le nom de la contrainte contenue dans la bdd 
	 * comme pk_machin_truc
	 */
	protected String name;
	
	/**
	 *
	 * @return String nom
	 */
	protected String getName(){
		return this.name;
	}
	
	protected void setName(String name){
		this.name = name;
	}
	

	/**
	 * Retourne un type CHECK(machin IS NOT NULL)
	 * @return String CHECK(machin IS NOT NULL)
	 */
	protected abstract String getNameSQL();
	
	
	protected void setAttribute(Attribute att){
		this.attribute = att;
	}
	
	
	protected Attribute getAttribute(){
		return this.attribute;
	}

	/**
	 * @return the keyWord
	 */
	public String getKeyWord() {
		return keyWord;
	}

	/**
	 * @param keyWord the keyWord to set
	 */
	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}
	
	
	
	
}
