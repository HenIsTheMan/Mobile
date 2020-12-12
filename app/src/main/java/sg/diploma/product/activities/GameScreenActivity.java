package sg.diploma.product.activities;

import android.app.Activity;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.SurfaceView;

import sg.diploma.product.entity.EntityManager;
import sg.diploma.product.entity.entities.EntityBG;
import sg.diploma.product.game.GameView;
import sg.diploma.product.state.IState;
import sg.diploma.product.state.StateManager;
import sg.diploma.product.touch.TouchManager;
import sg.diploma.product.touch.TouchTypes;

public final class GameScreenActivity extends Activity implements IState{
    public GameScreenActivity(){
        entityBG = null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Instance = this;
        setContentView(new GameView(this));
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        TouchManager.Instance.Update(event.getX(), event.getY(), event.getAction());
        return true;
    }

    @Override
    public String GetName(){
        return "GameScreen";
    }

    @Override
    public void OnEnter(SurfaceView _view){
        entityBG = EntityBG.Create();
    }

    @Override
    public void OnExit(){
        Instance.finish();
    }

    @Override
    public void Render(Canvas _canvas){
        EntityManager.Instance.Render(_canvas);
    }

    @Override
    public void Update(float _dt) {
        EntityManager.Instance.Update(_dt);

        if(TouchManager.Instance.GetMotionEventAction() == TouchTypes.TouchType.Down.GetVal()) {
            EntityManager.Instance.ClearAllEntities();
            StateManager.Instance.ChangeState("MenuScreen");
        }
    }

    private EntityBG entityBG;

    public static GameScreenActivity Instance;

    static{
        Instance = null;
    }
}