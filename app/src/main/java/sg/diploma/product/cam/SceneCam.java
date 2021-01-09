package sg.diploma.product.cam;

import sg.diploma.product.math.Vector2;

public final class SceneCam{ //Imaginary //Uses the same coord system as canvas
	public SceneCam(){
		pos = new Vector2();
		vel = new Vector2();
	}

	public void Update(final float dt){
		pos.x += vel.x * dt;
		pos.y += vel.y * dt;
	}

	public Vector2 GetPos(){
		return pos;
	}

	public Vector2 GetVel(){
		return vel;
	}

	public void SetPos(final Vector2 pos){
		this.pos = pos;
	}

	public void SetVel(final Vector2 vel){
		this.vel = vel;
	}

	public void SetPosX(final float x){
		pos.x = x;
	}

	public void SetPosY(final float y){
		pos.y = y;
	}

	public void SetVelX(final float x){
		vel.x = x;
	}

	public void SetVelY(final float y){
		vel.y = y;
	}

	private Vector2 pos;
	private Vector2 vel;
}
