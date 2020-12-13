package sg.diploma.product.entity.entities;

import android.graphics.Canvas;

import sg.diploma.product.entity.EntityAbstract;
import sg.diploma.product.entity.EntityCollidableTypes;
import sg.diploma.product.entity.EntityManager;
import sg.diploma.product.entity.EntityRenderLayers;
import sg.diploma.product.entity.EntityTypes;
import sg.diploma.product.graphics.ResourceManager;
import sg.diploma.product.graphics.SpriteAnim;

public final class EntityGamePlayerChar extends EntityAbstract{
	private EntityGamePlayerChar(final int bitmapID){
		super();
		attribs.renderLayer = EntityRenderLayers.EntityRenderLayer.Normal;
		attribs.type = EntityTypes.EntityType.GamePlayerChar;
		attribs.collidableType = EntityCollidableTypes.EntityCollidableType.Box;

		spriteAnim = new SpriteAnim(
			ResourceManager.Instance.GetBitmap(bitmapID),
			4,
			9,
			10
		);

		if((int)Math.random() % 2 == 1){
			spriteAnim.SetFrames(3 * 9 + 1, 3 * 9 + 9);
			attribs.facing = 1;
		} else{
			spriteAnim.SetFrames(9 + 1, 9 + 9);
			attribs.facing = -1;
		}

		attribs.accel.y += 300.0f; //Gravitational accel
	}

	@Override
	public void Update(final float dt){
		attribs.vel.x += attribs.accel.x * dt;
		attribs.vel.y += attribs.accel.y * dt;
		attribs.pos.x += attribs.vel.x * dt;
		attribs.pos.y += attribs.vel.y * dt;

		if(attribs.xMin != null){
			attribs.pos.x = Math.max(attribs.xMin.val, attribs.pos.x);
		}
		if(attribs.xMax != null){
			attribs.pos.x = Math.min(attribs.xMax.val, attribs.pos.x);
		}

		attribs.pos.y += attribs.dir.y * attribs.spd * dt;
		if(attribs.yMin != null){
			attribs.pos.y = Math.max(attribs.yMin.val, attribs.pos.y);
		}
		if(attribs.yMax != null){
			attribs.pos.y = Math.min(attribs.yMax.val, attribs.pos.y);
		}

		spriteAnim.Update(dt);
	}

	@Override
	public void Render(final Canvas canvas){ //Render with img centered
		spriteAnim.Render(canvas, (int)attribs.pos.x, (int)attribs.pos.y);
	}

	public static EntityGamePlayerChar Create(final String key, final int bitmapID){
		EntityGamePlayerChar result = new EntityGamePlayerChar(bitmapID);
		EntityManager.Instance.AddEntity(key, result);
		return result;
	}

	public void SetSpriteAnimXScale(final float xScale){
		spriteAnim.SetXScale(xScale);
	}

	public void SetSpriteAnimYScale(final float yScale){
		spriteAnim.SetYScale(yScale);
	}

	private final SpriteAnim spriteAnim;
}