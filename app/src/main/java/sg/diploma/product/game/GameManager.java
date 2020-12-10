package sg.diploma.product.game;

import sg.diploma.product.activities.GameScreenActivity;
import sg.diploma.product.activities.MenuScreenActivity;
import sg.diploma.product.state.StateManager;

public final class GameManager{ //Singleton
    private GameManager(){
        isPaused = false;
    }

    public void Init(){
        ///Add all states to state manager
        StateManager.Instance.AddState(new MenuScreenActivity());
        StateManager.Instance.AddState(new GameScreenActivity());
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