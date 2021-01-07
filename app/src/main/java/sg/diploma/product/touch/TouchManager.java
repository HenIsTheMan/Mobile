package sg.diploma.product.touch;

public final class TouchManager{ //Singleton
    private TouchManager(){
        xPos = 0.0f;
        yPos = 0.0f;
        motionEventAction = -999;
    }

    public void Update(final float xPos, final float yPos, final int motionEventAction){
        this.xPos = xPos;
        this.yPos = yPos;
        this.motionEventAction = motionEventAction;
    }

    public float GetXPos(){
        return xPos;
    }

    public float GetYPos(){
        return yPos;
    }

    public int GetMotionEventAction(){
        return motionEventAction;
    }

    private float xPos;
    private float yPos;
    private int motionEventAction;

    public static final TouchManager Instance;

    static{
        Instance = new TouchManager();
    }
}

