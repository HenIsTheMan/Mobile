package sg.diploma.product;

import android.graphics.Canvas;
import android.view.SurfaceView;

// Created by TanSiewLan2020

public interface IEntity
{
 	 //used for entities such as background
    enum ENTITY_TYPE{
        //ENT_PLAYER,
        //ENT_SMURF,
        //ENT_PAUSE,
        //ENT_TEXT,
        //ENT_NEXT,
        ENT_DEFAULT,
    }

    boolean IsDone();
    void SetIsDone(boolean _isDone);

    void Init(SurfaceView _view);
    void Update(float _dt);
    void Render(Canvas _canvas);

    boolean IsInit();

    int GetRenderLayer();
    void SetRenderLayer(int _newLayer);

	 ENTITY_TYPE GetEntityType();
}

//Example: Background
//Background === Surfaceview

//Render Layer ---> LayerConstants class
//  Contains each background or images that appears on screen.
//  Player, tiles, enemies are entity
//  But then are also layer itself
//
//  rendering of which entity
//
//  Entities are stored in a linked list
//