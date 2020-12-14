package sg.diploma.product.entity;

import android.graphics.Canvas;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;

import sg.diploma.product.device.DeviceManager;
import sg.diploma.product.entity.entities.EntityGamePlayerChar;
import sg.diploma.product.game.GameManager;
import sg.diploma.product.math.CollisionDataBoxBoxAABB;
import sg.diploma.product.math.DetectCollision;
import sg.diploma.product.math.ResolveCollision;

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

        if(GameManager.Instance.GetIsPaused()){
            Objects.requireNonNull(entityList.get("Special_pauseButton")).Update(_dt);
            return;
        }

        for(EntityAbstract entity: entityList.values()){
            entity.attribs.prevPos = entity.attribs.boxColliderPos;
            entity.Update(_dt);
        }

        ArrayList<String> keys = new ArrayList(entityList.keySet());
        final int keysSize = keys.size();
        for(int i = 0; i < keysSize; ++i){
            EntityAbstract currEntity = entityList.get(keys.get(i));

            assert currEntity != null;
            if(currEntity.attribs.collidableType != EntityCollidableTypes.EntityCollidableType.None){
                for(int j = i + 1; j < keysSize; ++j){
                    EntityAbstract otherEntity = entityList.get(keys.get(j));

                    assert otherEntity != null;
                    if(otherEntity.attribs.collidableType != EntityCollidableTypes.EntityCollidableType.None){
                        CheckCollision(currEntity, otherEntity);
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

    public void LateUpdate(final float dt){
        if(GameManager.Instance.GetIsPaused()){
            Objects.requireNonNull(entityList.get("Special_pauseButton")).LateUpdate(dt);
            return;
        }

        for(EntityAbstract entity: entityList.values()){
            entity.LateUpdate(dt);
        }
    }

    public void SpecialRender(Canvas _canvas, String playerCharKey){
        if(!entityList.containsKey(playerCharKey)){
            return;
        }

        final Object[] keys = entityList.keySet().toArray();
        final Object[] myArr = entityList.values().toArray();

        final int myArrLen = myArr.length;
        final EntityAbstract[] entityAbstractArr = new EntityAbstract[myArrLen];
        final HashMap<EntityAbstract, String> entityToKey = new HashMap<>();
        for(int i = 0; i < myArrLen; ++i){
            EntityAbstract entity = (EntityAbstract)myArr[i];
            entityToKey.put(entity, (String)keys[i]);
            entityAbstractArr[i] = entity;
        }
        Arrays.sort(entityAbstractArr, (o1, o2)->o1.attribs.renderLayer.GetVal() - o2.attribs.renderLayer.GetVal());

        final EntityGamePlayerChar playerChar = (EntityGamePlayerChar)entityList.get(playerCharKey);
        assert playerChar != null;

        _canvas.translate(0.0f, DeviceManager.screenHeightF * 0.75f - playerChar.attribs.pos.y);

        for(int i = 0; i < myArrLen; ++i){
            EntityAbstract entity = entityAbstractArr[i];
            if(!Objects.requireNonNull(entityToKey.get(entity)).startsWith("Special_")){ //Not special XD
                entity.Render(_canvas);
            }
        }

        _canvas.translate(0.0f, -DeviceManager.screenHeightF * 0.75f + playerChar.attribs.pos.y);

        for(int i = 0; i < myArrLen; ++i){
            EntityAbstract entity = entityAbstractArr[i];
            if(Objects.requireNonNull(entityToKey.get(entity)).startsWith("Special_")){ //✨ Special ✨
                entity.SpecialRender(_canvas);
            }
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

    private void CheckCollision(EntityAbstract entity0, EntityAbstract entity1){
        if(entity0.attribs.collidableType == entity1.attribs.collidableType){
            switch(entity0.attribs.collidableType){
                case Box:
                    CollisionDataBoxBoxAABB collisionData0 = new CollisionDataBoxBoxAABB();
                    CollisionDataBoxBoxAABB collisionData1 = new CollisionDataBoxBoxAABB();
                    if(DetectCollision.BoxBoxAABB(entity0, entity1, collisionData0, collisionData1)){
                        ResolveCollision.BoxBoxAABB(entity0, entity1, collisionData0, collisionData1);
                        ResolveCollision.BoxBoxAABB(entity1, entity0, collisionData1, collisionData0);
                    }
                    break;
                case Circle:
                    break;
            }
        }
    }

    private final HashMap<String, EntityAbstract> entityList;
    private final ArrayList<String> entityRemovalList;
    public SurfaceView view;

    public static final EntityManager Instance;

    static{
        Instance = new EntityManager();
    }
}