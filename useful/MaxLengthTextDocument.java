package useful;

//import javax.print.attribute.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;


@SuppressWarnings("serial")
public class MaxLengthTextDocument
extends PlainDocument{
	//Store maximum characters permitted
		private int maxChars;

		public void insertString(int offs, String str, javax.swing.text.AttributeSet a)
				throws BadLocationException {
			if(str != null && (getLength() + str.length() < maxChars)){
				super.insertString(offs, str, a);
			}
		}
			public void setMaxChars(int i){
				this.maxChars=i+1;
					
			}


}
