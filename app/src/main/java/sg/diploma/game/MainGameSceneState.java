package sg.diploma.game;

import android.graphics.Canvas;
import android.view.SurfaceView;

import sg.diploma.game.touch.TouchManager;
import sg.diploma.game.touch.TouchTypes;

// Created by TanSiewLan2020

public class MainGameSceneState implements IState{
    private float timer = 0.0f;

    @Override
    public String GetName() {
        return "MainGame";
    }

    @Override
    public void OnEnter(SurfaceView _view)
    {
        //RenderBackground.Create();
        // Example to include another Renderview for Pause Button
    }

    @Override
    public void OnExit() {
        EntityManager.Instance.Clean();
        GamePage.Instance.finish();
    }

    @Override
    public void Render(Canvas _canvas)
    {
        EntityManager.Instance.Render(_canvas);

    }

    @Override
    public void Update(float _dt) {
        EntityManager.Instance.Update(_dt);

        if (TouchManager.Instance.GetMotionEventAction() == TouchTypes.TouchType.Down.GetVal()) {
            StateManager.Instance.ChangeState("MainMenu"); //Go back to MainMenu
        }
    }
}



