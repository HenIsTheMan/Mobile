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
        stateMap = null;
    }

    public void Init(SurfaceView _view){
        view = _view;
        stateMap = new HashMap<String, IState>();
    }

    public void AddState(IState _newState){
        stateMap.put(_newState.GetName(), _newState);
    }

    public void ChangeState(String _nextState){
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

    public void Update(float _dt){
        if(nextState != currState){
            currState.OnExit();
            currState = nextState;
            currState.OnEnter(view);
        }

        if(currState == null){
            Log.w(tag, "Var 'currState' is null!");
            return;
        }

        currState.Update(_dt);
    }

    public void Render(Canvas _canvas){
        currState.Render(_canvas);
    }

    public String GetCurrentState(){
        if(currState == null){
            Log.w(tag, "Var 'currState' is null!");
            return "";
        }
        return currState.GetName();
    }

    public void Start(String _newCurrent){
        if(currState != null || nextState != null){ //Ensure this func is only called once
            return;
        }

        currState = stateMap.get(_newCurrent);
        if(currState != null){
            currState.OnEnter(view);
            nextState = currState;
        }
    }

    public static final StateManager Instance;
    private static final String tag;

    static{
        Instance = new StateManager();
        tag = "StateManager";
    }

    private HashMap<String, IState> stateMap;
    private IState currState;
    private IState nextState;
    private SurfaceView view;
}