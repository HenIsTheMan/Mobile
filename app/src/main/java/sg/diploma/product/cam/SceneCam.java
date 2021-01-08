package sg.diploma.product.cam;

import sg.diploma.product.math.Vector2;

public class SceneCam{
	SceneCam(){
		pos = new Vector2();
		vel = new Vector2();
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

	private Vector2 pos;
	private Vector2 vel;
}
