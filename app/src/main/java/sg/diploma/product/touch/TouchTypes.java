package sg.diploma.product.touch;

import android.view.MotionEvent;

public final class TouchTypes{
	public enum TouchType{
		Down(MotionEvent.ACTION_DOWN),
		Amt(1);

		TouchType(final int val){
			this.val = val;
		}

		public int GetVal(){
			return val;
		}

		private final int val;
	}
}