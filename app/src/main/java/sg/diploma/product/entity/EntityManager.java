package sg.diploma.product.entity;

import android.graphics.Canvas;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;

import sg.diploma.product.cam.SceneCam;
import sg.diploma.product.game.GameManager;
import sg.diploma.product.math.DetectCollision;
import sg.diploma.product.math.ResolveCollision;
import sg.diploma.product.math.Vector2;

public final class EntityManager{ //Singleton
    private EntityManager(){
        entityList = new HashMap<>();
        entityRemovalList = new ArrayList<>();
        cam = new SceneCam();
        view = null;
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
            EntityAbstract pauseButton = entityList.get("Special_pauseButton");
            if(pauseButton != null){
                pauseButton.Update(_dt);
            }
            return;
        }

        for(EntityAbstract entity: entityList.values()){
            entity.Update(_dt);
        }

        ArrayList<String> keys = new ArrayList<>(entityList.keySet());
        final int keysSize = keys.size();
        for(int i = 0; i < keysSize; ++i){
            EntityAbstract currEntity = entityList.get(keys.get(i));

            if(Objects.requireNonNull(currEntity).attribs.collidableType != EntityCollidableTypes.EntityCollidableType.None){
                for(int j = i + 1; j < keysSize; ++j){
                    EntityAbstract otherEntity = entityList.get(keys.get(j));

                    if(Objects.requireNonNull(otherEntity).attribs.collidableType != EntityCollidableTypes.EntityCollidableType.None){
                        CheckCollision(currEntity, otherEntity);
                    }
                }
            }
        }

        for(String element: entityRemovalList){
            entityList.remove(element);
        }
        entityRemovalList.clear();

        cam.Update(_dt);
    }

    public void Render(Canvas _canvas){
        //* Determines render order
        final Object[] myArr = entityList.values().toArray();
        final int myArrLen = myArr.length;
        final EntityAbstract[] entityAbstractArr = new EntityAbstract[myArrLen];
        for(int i = 0; i < myArrLen; ++i){
            entityAbstractArr[i] = (EntityAbstract)myArr[i];
        }
        Arrays.sort(entityAbstractArr, (o1, o2)->o1.attribs.renderLayer.GetVal() - o2.attribs.renderLayer.GetVal());
        //*/

        for(EntityAbstract entity: entityAbstractArr){
            entity.Render(_canvas);
        }
    }

    public void LateUpdate(final float dt){
        if(GameManager.Instance.GetIsPaused()){
            EntityAbstract pauseButton = entityList.get("Special_pauseButton");
            if(pauseButton != null){
                pauseButton.LateUpdate(dt);
            }
            return;
        }

        for(EntityAbstract entity: entityList.values()){
            entity.LateUpdate(dt);
        }
    }

    public void SpecialRender(Canvas _canvas){
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

        final Vector2 camPos = cam.GetPos();

        _canvas.translate(-camPos.x, -camPos.y);
        for(int i = 0; i < myArrLen; ++i){
            EntityAbstract entity = entityAbstractArr[i];
            if(!Objects.requireNonNull(entityToKey.get(entity)).startsWith("Special_")){ //Not special XD
                entity.Render(_canvas);
            }
        }

        _canvas.translate(camPos.x, camPos.y);
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
        final boolean isBox0 = entity0.attribs.collidableType == EntityCollidableTypes.EntityCollidableType.Box;
        final boolean isBox1 = entity1.attribs.collidableType == EntityCollidableTypes.EntityCollidableType.Box;

        if(isBox0 && isBox1){
            if(DetectCollision.AABBAABB(entity0, entity1)){
                ResolveCollision.AABBAABB(entity0, entity1);
            }
        } else if(isBox0 ^ isBox1){
            final EntityAbstract circle = entity0.attribs.collidableType == EntityCollidableTypes.EntityCollidableType.Circle
                ? entity0
                : entity1;

            final EntityAbstract box = entity0.attribs.collidableType == EntityCollidableTypes.EntityCollidableType.Box
                ? entity0
                : entity1;

            if(DetectCollision.CircleAABB(circle, box)){
                ResolveCollision.CircleAABB(circle, box);
            }
        } else{
            if(DetectCollision.CircleCircle(entity0, entity1)){
                ResolveCollision.CircleCircle(entity0, entity1);
            }
        }
    }

    public SceneCam GetSceneCam(){
        return cam;
    }

    private final HashMap<String, EntityAbstract> entityList;
    private final ArrayList<String> entityRemovalList;
    public SceneCam cam;
    public SurfaceView view;

    public static final EntityManager Instance;

    static{
        Instance = new EntityManager();
    }
}