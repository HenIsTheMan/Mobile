package sg.diploma.product.layer;

import android.view.MotionEvent;

public final class LayerTypes{
    public enum LayerType{
        BG(0),
        GameText(1),
        Entity(100),
        UI(9999),
        Amt(4);

        LayerType(final int val){
            this.val = val;
        }

        public int GetVal(){
            return val;
        }

        private final int val;
    };
}