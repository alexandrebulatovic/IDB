package business.oracle;

import business.Table;

public class TableOracle extends Table {

	@Override
	protected String toRenameTableSQL(Table tableSource) {
		return "RENAME "+tableSource.getName()+" TO "+this.name;
	}

}
