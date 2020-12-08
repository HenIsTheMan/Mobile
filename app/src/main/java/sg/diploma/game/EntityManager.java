package sg.diploma.game;

import android.graphics.Canvas;
import android.view.SurfaceView;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;

import sg.diploma.game.math.CheckCollision;
import sg.diploma.game.math.Vector2;

// Created by TanSiewLan2019

public class EntityManager {

    public final static EntityManager Instance = new EntityManager();
    private LinkedList<IEntity> entityList = new LinkedList<IEntity>();
    private SurfaceView view = null;

    private EntityManager()
    {
    }

    public void Init(SurfaceView _view)
    {
        view = _view;
    }

    public void Update(float _dt)
    {
        LinkedList<IEntity> removalList = new LinkedList<IEntity>();

        // Update all
        for(int i = 0; i < entityList.size(); ++i)
        {
            // Lets check if is init, initialize if not
            if (!entityList.get(i).IsInit())
            {
                entityList.get(i).Init(view);
            }

            entityList.get(i).Update(_dt);

            // Check if need to clean up
            if (entityList.get(i).IsDone()) {
                // Done! Time to add to the removal list
                removalList.add(entityList.get(i));
            }
        }

        for (IEntity currEntity : entityList)
        {
            // Lets check if is init, initialize if not
            if (!currEntity.IsInit())
                currEntity.Init(view);

            currEntity.Update(_dt);

            // Check if need to clean up
            if (currEntity.IsDone()) {
                // Done! Time to add to the removal list
                removalList.add(currEntity);
            }
        }

        // Remove all entities that are done
        for (IEntity currEntity : removalList) {
            entityList.remove(currEntity);
        }
        removalList.clear(); // Clean up of removal list

        // Collision Check
        // If 2 entities collided, what to do?
        for (int i = 0; i < entityList.size(); ++i)
        {
            IEntity currEntity = entityList.get(i);

            if (currEntity instanceof ICollidable)
            {
                ICollidable first = (ICollidable) currEntity;

                for (int j = i+1; j < entityList.size(); ++j)
                {
                    IEntity otherEntity = entityList.get(j);

                    if (otherEntity instanceof ICollidable)
                    {
                        ICollidable second = (ICollidable) otherEntity;

                        if (CheckCollision.CircleCircle(new Vector2(first.GetPosX(), first.GetPosY()), new Vector2(second.GetPosX(), second.GetPosY()), first.GetRadius(), second.GetRadius()))
                        {
                            first.OnHit(second);
                            second.OnHit(first);
                        } // Change this if you are using other methods
                    }
                }
            }

            // Check if need to clean up
            if (currEntity.IsDone()) {
                removalList.add(currEntity);
            }
        }

        // Remove all entities that are done
        for (IEntity currEntity : removalList) {
            entityList.remove(currEntity);
        }
        removalList.clear();
    }

    public void Render(Canvas _canvas)
    {
      
        // Use the new "rendering layer" to sort the render order
        Collections.sort(entityList, new Comparator<IEntity>() {
            @Override
            public int compare(IEntity o1, IEntity o2) {
                return o1.GetRenderLayer() - o2.GetRenderLayer();
            }
        });

        for(int i = 0; i <entityList.size(); ++i)
        {
            entityList.get(i).Render(_canvas);
        }
    }

    public void AddEntity(IEntity _newEntity, IEntity.ENTITY_TYPE entity_type)
    {
        entityList.add(_newEntity);
    }

    public void Clean()
    {
        entityList.clear();
    }
}

// Entity Manager manages the entity
// Comparator: Interface used to order the objects
// 2 methods  compare (obj1, obj 2)
// equal (obj)
//
// Collections: Store & manipulate groups of objects and allows easy
// use of linkedlist, search, sort, deletion

