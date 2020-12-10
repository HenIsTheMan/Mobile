package sg.diploma.product.entity;

import android.graphics.Canvas;
import android.view.SurfaceView;

import sg.diploma.product.layer.LayerTypes;

public interface IEntity{
    enum EntityType{
        Amt
    }

    void Update(float _dt);
    void Render(Canvas _canvas);

    LayerTypes.LayerType GetRenderLayer();
    void SetRenderLayer(LayerTypes.LayerType _newLayer);

    EntityType GetEntityType();
    void SetEntityType(EntityType type);
}