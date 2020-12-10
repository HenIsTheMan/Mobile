package sg.diploma.product.state;

import android.graphics.Canvas;
import android.view.SurfaceView;

public interface IState{
    String GetName();

    void OnEnter(SurfaceView _view);
    void OnExit();
    void Render(Canvas _canvas);
    void Update(float _dt);
}