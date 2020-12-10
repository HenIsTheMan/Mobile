package sg.diploma.product.entity;

import android.graphics.Canvas;
import android.view.SurfaceView;

public interface IEntity{
    enum EntityType{
        Amt
    }

    boolean IsDone();
    void SetIsDone(boolean _isDone);

    void Init(SurfaceView _view);
    void Update(float _dt);
    void Render(Canvas _canvas);

    boolean IsInit();

    int GetRenderLayer();
    void SetRenderLayer(int _newLayer);

    EntityType GetEntityType();
}