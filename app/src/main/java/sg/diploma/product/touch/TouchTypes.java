package sg.diploma.product.touch;

import android.view.MotionEvent;

public final class TouchTypes{
	public enum TouchType{
		Down(MotionEvent.ACTION_DOWN),
		Up(MotionEvent.ACTION_UP),
		Amt(2);

		TouchType(final int val){
			this.val = val;
		}

		public int GetVal(){
			return val;
		}

		private final int val;
	}
}