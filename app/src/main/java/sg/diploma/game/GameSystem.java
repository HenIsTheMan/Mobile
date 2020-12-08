package sg.diploma.game;

import android.view.SurfaceView;

class GameSystem { //Singleton
    public final static GameSystem Instance = new GameSystem();

    private boolean isPaused = false;

    private GameSystem(){
    }

    public void Update(float _deltaTime){
    }

    public void Init(SurfaceView _view){
        // We will add all of our states into the state manager here!
        StateManager.Instance.AddState(new MainMenu());
        StateManager.Instance.AddState(new MainGameSceneState());
    }

    public void SetIsPaused(boolean _newIsPaused){
        isPaused = _newIsPaused;
    }

    public boolean GetIsPaused(){
        return isPaused;
    }
}
