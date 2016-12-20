package useful;
import java.awt.event.*;

public class FieldsKeyAdapter extends KeyAdapter{
	private String type;
	
	public FieldsKeyAdapter(String type){
		super();
		this.type=type;
		
		
	}

	@Override
	public void keyTyped(KeyEvent evt) {
		if(this.type.equals("int")){
		char c = evt.getKeyChar();
		if (c >= '0' && c <= '9') {
		} else {
			evt.consume();
		}
	}else if(this.type.equals("String")){
			char c = evt.getKeyChar();
			if ((c >= 'A' && c <= 'Z')||(c >= 'a' && c <= 'z')||(c == '_')||(c >= '0' && c <= '9')) {
			} else {
				evt.consume();
			}
		}
	}
		
	}

