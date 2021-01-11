package sg.diploma.product.object_pooling.obj_pools;

import java.util.ArrayList;

import sg.diploma.product.entity.entities.EntityParticle;
import sg.diploma.product.object_pooling.ObjPool;

public final class ParticlePool extends ObjPool<EntityParticle>{
	public ParticlePool(){
		super();
	}

	public ArrayList<EntityParticle> RetrieveActiveParticles(){
		return RetrieveActiveObjs();
	}
}