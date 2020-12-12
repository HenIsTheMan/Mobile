package sg.diploma.product.entity;

import android.graphics.Canvas;

public abstract class EntityAbstract{
	protected EntityAbstract(){
		attribs = new EntityAttribs();
	}

	public abstract void Update(float dt);
	public abstract void Render(Canvas canvas);

	public EntityAttribs attribs;
}