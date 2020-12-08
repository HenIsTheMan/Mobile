package sg.diploma.game;

import android.view.MotionEvent;

public final class TouchTypes{
	enum TouchType{
		Down(MotionEvent.ACTION_DOWN);

		TouchType(final int val){
			this.val = val;
		}

		public int GetVal(){
			return val;
		}

		private final int val;
	}
}