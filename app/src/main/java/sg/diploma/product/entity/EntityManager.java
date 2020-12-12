package sg.diploma.product.entity;

import android.graphics.Canvas;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;

public final class EntityManager{ //Singleton
    private EntityManager(){
        entityList = new LinkedList<>();
        entityRemovalList = new ArrayList<>();
    }

    public void Init(SurfaceView _view){
        view = _view;
    }

    public void Update(float _dt){
        for(EntityAbstract currEntity: entityRemovalList){
            entityList.remove(currEntity);
        }
        entityRemovalList.clear();

        final int entityListSize = entityList.size();
        for(int i = 0; i < entityListSize; ++i){
            entityList.get(i).Update(_dt);
        }

        for(int i = 0; i < entityListSize; ++i){
            EntityAbstract currEntity = entityList.get(i);

            if(currEntity instanceof IEntityCollidable){
                IEntityCollidable collidable0 = (IEntityCollidable)currEntity;

                for(int j = i + 1; j < entityListSize; ++j){
                    EntityAbstract otherEntity = entityList.get(j);

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

        for(EntityAbstract currEntity: entityRemovalList){
            entityList.remove(currEntity);
        }
        entityRemovalList.clear();
    }

    public void Render(Canvas _canvas){
        entityList.sort(new Comparator<EntityAbstract>(){ //Determines render order
            @Override
            public int compare(EntityAbstract o1, EntityAbstract o2){
                return o1.attribs.renderLayer.GetVal() - o2.attribs.renderLayer.GetVal();
            }
        });

        final int entityListSize = entityList.size();
        for(int i = 0; i < entityListSize; ++i){
            entityList.get(i).Render(_canvas);
        }
    }

    public void AddEntity(EntityAbstract _newEntity){
        entityList.add(_newEntity);
    }

    public void SendEntityForRemoval(EntityAbstract entity){
        entityRemovalList.add(entity);
    }

    private final LinkedList<EntityAbstract> entityList;
    private final ArrayList<EntityAbstract> entityRemovalList;
    public SurfaceView view;

    public static final EntityManager Instance;

    static{
        Instance = new EntityManager();
    }
}