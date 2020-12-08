package sg.diploma.product.game;

import sg.diploma.product.MainGameSceneState;
import sg.diploma.product.MainMenu;
import sg.diploma.product.StateManager;

public final class GameManager{ //Singleton
    private GameManager(){
        isPaused = false;
    }

    public void Init(){
        ///Add all states to state manager
        StateManager.Instance.AddState(new MainMenu());
        StateManager.Instance.AddState(new MainGameSceneState());
    }

    public boolean GetIsPaused(){
        return isPaused;
    }

    public void SetIsPaused(final boolean isPaused){
        this.isPaused = isPaused;
    }

    private boolean isPaused;

    public static final GameManager Instance;

    static{
        Instance = new GameManager();
    }
}