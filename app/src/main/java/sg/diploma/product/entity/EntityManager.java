package sg.diploma.product.entity;

import android.graphics.Canvas;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;

public final class EntityManager{ //Singleton
    private EntityManager(){
        entityList = null;
        entityRemovalList = null;
        view = null;
    }

    public void Init(SurfaceView _view){
        entityList = new LinkedList<>();
        entityRemovalList = new ArrayList<>();
        view = _view;
    }

    public void Update(float _dt){
        for(IEntity currEntity: entityRemovalList){
            entityList.remove(currEntity);
        }
        entityRemovalList.clear();

        final int entityListSize = entityList.size();
        for(int i = 0; i < entityListSize; ++i){
            entityList.get(i).Update(_dt);
        }

        for(int i = 0; i < entityListSize; ++i){
            IEntity currEntity = entityList.get(i);

            if(currEntity instanceof IEntityCollidable){
                IEntityCollidable collidable0 = (IEntityCollidable)currEntity;

                for(int j = i + 1; j < entityListSize; ++j){
                    IEntity otherEntity = entityList.get(j);

                    if(otherEntity instanceof IEntityCollidable){
                        IEntityCollidable collidable1 = (IEntityCollidable)otherEntity;

                        /*if(CheckCollision.CircleCircle(new Vector2(collidable0.GetPosX(), collidable0.GetPosY()), new Vector2(collidable1.GetPosX(), collidable1.GetPosY()), collidable0.GetRadius(), collidable1.GetRadius())){
                            collidable0.OnHit(collidable1);
                            collidable1.OnHit(collidable0);
                        }*/
                    }
                }
            }
        }

        for(IEntity currEntity: entityRemovalList){
            entityList.remove(currEntity);
        }
        entityRemovalList.clear();
    }

    public void Render(Canvas _canvas){
        entityList.sort(new Comparator<IEntity>(){ //Determines render order
            @Override
            public int compare(IEntity o1, IEntity o2){
                return o1.attribs.renderLayer.GetVal() - o2.attribs.renderLayer.GetVal();
            }
        });

        final int entityListSize = entityList.size();
        for(int i = 0; i < entityListSize; ++i){
            entityList.get(i).Render(_canvas);
        }
    }

    public void AddEntity(IEntity _newEntity, EntityTypes.EntityType EntityType){
        entityList.add(_newEntity);
    }

    public void SendEntityForRemoval(IEntity entity){
        entityRemovalList.add(entity);
    }

    private LinkedList<IEntity> entityList;
    private ArrayList<IEntity> entityRemovalList;
    public SurfaceView view;

    public static final EntityManager Instance;

    static{
        Instance = new EntityManager();
    }
}