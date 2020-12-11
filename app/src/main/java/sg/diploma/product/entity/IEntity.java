package sg.diploma.product.entity;

import android.graphics.Canvas;

public interface IEntity{
    void Update(float dt);
    void Render(Canvas canvas);

    EntityAttribs attribs = new EntityAttribs();
}