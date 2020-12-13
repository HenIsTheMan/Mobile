package sg.diploma.product.entity;

import android.graphics.Canvas;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public final class EntityManager{ //Singleton
    private EntityManager(){
        entityList = new HashMap<>();
        entityRemovalList = new ArrayList<>();
    }

    public void Init(SurfaceView _view){
        view = _view;
    }

    public void Update(float _dt){
        for(String element: entityRemovalList){
            entityList.remove(element);
        }
        entityRemovalList.clear();

        for(EntityAbstract entity: entityList.values()){
            entity.Update(_dt);
        }

        ArrayList<String> keys = new ArrayList(entityList.keySet());
        final int keysSize = keys.size();
        for(int i = 0; i < keysSize; ++i){
            EntityAbstract currEntity = entityList.get(keys.get(i));

            if(currEntity instanceof IEntityCollidable){
                IEntityCollidable collidable0 = (IEntityCollidable)currEntity;

                for(int j = i + 1; j < keysSize; ++j){
                    EntityAbstract otherEntity = entityList.get(keys.get(j));

                    if(otherEntity instanceof IEntityCollidable){
                        IEntityCollidable collidable1 = (IEntityCollidable)otherEntity;

                        CheckCollision(collidable0, collidable1);
                    }
                }
            }
        }

        for(String element: entityRemovalList){
            entityList.remove(element);
        }
        entityRemovalList.clear();
    }

    public void Render(Canvas _canvas){
        //Determines render order
        final Object[] myArr = entityList.values().toArray();
        final int myArrLen = myArr.length;
        final EntityAbstract[] entityAbstractArr = new EntityAbstract[myArrLen];
        for(int i = 0; i < myArrLen; ++i){
            entityAbstractArr[i] = (EntityAbstract)myArr[i];
        }
        Arrays.sort(entityAbstractArr, (o1, o2)->o1.attribs.renderLayer.GetVal() - o2.attribs.renderLayer.GetVal());

        for(EntityAbstract entity: entityAbstractArr){
            entity.Render(_canvas);
        }
    }

    public void AddEntity(String key, EntityAbstract _newEntity){
        entityList.put(key, _newEntity);
    }

    public void SendEntityForRemoval(String key){
        entityRemovalList.add(key);
    }

    public void SendAllEntitiesForRemoval(){
        entityRemovalList.addAll(entityList.keySet());
    }

    private void CheckCollision(IEntityCollidable collidable0, IEntityCollidable collidable1){
        //collidable0.OnHit(collidable1);
        //collidable1.OnHit(collidable0);
    }

    private final HashMap<String, EntityAbstract> entityList;
    private final ArrayList<String> entityRemovalList;
    public SurfaceView view;

    public static final EntityManager Instance;

    static{
        Instance = new EntityManager();
    }
}