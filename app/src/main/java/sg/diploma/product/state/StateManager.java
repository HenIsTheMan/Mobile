package sg.diploma.product.state;

import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceView;

import java.util.HashMap;

public final class StateManager{ //Singleton
    private StateManager(){
        currState = null;
        nextState = null;
        view = null;
        stateMap = new HashMap<String, IState>();
    }

    public final void Init(SurfaceView _view){
        view = _view;
    }

    public final void AddState(IState _newState){
        stateMap.put(_newState.GetName(), _newState);
    }

    public final void ChangeState(String _nextState){
        try{
            nextState = stateMap.get(_nextState);
            if(nextState == null){
                throw new Exception("Var 'nextState' is null!");
            }
        } catch(Exception e){
            nextState = currState;
            Log.w(tag, e.getMessage());
        }
    }

    public final void Update(float _dt){
        if(nextState != currState){
            if(currState != null){
                currState.OnExit();
            }
            currState = nextState;
            currState.OnEnter(view);
        }

        if(currState == null){
            Log.w(tag, "Var 'currState' is null!");
            return;
        }

        currState.Update(_dt);
    }

    public final void Render(Canvas _canvas){
        currState.Render(_canvas);
    }

    public String GetCurrentStateName(){
        return currState == null ? "" : currState.GetName();
    }

    public static final StateManager Instance;
    private static final String tag;

    static{
        Instance = new StateManager();
        tag = "StateManager";
    }

    private final HashMap<String, IState> stateMap;
    private IState currState;
    private IState nextState;
    private SurfaceView view;
}