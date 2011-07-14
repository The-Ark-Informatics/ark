package au.org.theark.core.util;

import java.io.Serializable;

public class ArkSheetMetaData implements Serializable {
   /**
	 * 
	 */
	private static final long	serialVersionUID	= -1565166236265658458L;
	private int cols = 0;
   private int rows = 0;
   
   public int getCols() {
       return cols;
   }
   
   public void setCols(int cols) {
       this.cols = cols;
   }
   
   public int getRows() {
       return rows;
   }
   
   public void setRows(int rows) {
       this.rows = rows;
   }
}
