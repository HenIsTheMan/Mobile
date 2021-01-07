package sg.diploma.product.game;

public final class GameManager{ //Singleton
    private GameManager(){
        isPaused = false;
    }

    public final boolean GetIsPaused(){
        return isPaused;
    }

    public final void SetIsPaused(final boolean isPaused){
        this.isPaused = isPaused;
    }

    private boolean isPaused;

    public static final GameManager Instance;

    static{
        Instance = new GameManager();
    }
}