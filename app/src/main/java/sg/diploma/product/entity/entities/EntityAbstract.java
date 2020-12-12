package sg.diploma.product.entity.entities;

import android.graphics.Canvas;

import sg.diploma.product.entity.EntityAttribs;

public abstract class EntityAbstract{
	protected EntityAbstract(){
		attribs = new EntityAttribs();
	}

	public abstract void Update(float dt);
	public abstract void Render(Canvas canvas);

	public EntityAttribs attribs;
}
