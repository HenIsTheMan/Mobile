package sg.diploma.product.entity;

import android.graphics.Canvas;

public abstract class EntityAbstract{
	protected EntityAbstract(){
		attribs = new EntityAttribs();
	}

	public abstract void Update(final float dt);
	public abstract void Render(final Canvas canvas);
	public abstract void SpecialRender(final Canvas canvas);

	public abstract void Collided();

	public EntityAttribs attribs;
}