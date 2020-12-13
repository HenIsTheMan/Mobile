package sg.diploma.product.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import sg.diploma.product.R;
import sg.diploma.product.entity.EntityConstraint;
import sg.diploma.product.entity.EntityManager;
import sg.diploma.product.entity.entities.EntityPlayerChar;
import sg.diploma.product.entity.entities.EntityTextOnScreen;
import sg.diploma.product.graphics.Color;
import sg.diploma.product.state.IState;
import sg.diploma.product.state.StateManager;
import sg.diploma.product.thread.UpdateThread;
import sg.diploma.product.touch.TouchManager;
import sg.diploma.product.touch.TouchTypes;

public final class MenuScreenActivity extends Activity implements OnClickListener, IState{
    public MenuScreenActivity(){
        isFingerOffScreenBefore = true;
        shldStartMoving = false;

        startButton = null;
        optionsButton = null;
        exitButton = null;

        menuPlayerChar = null;
        textOnScreen = null;

        playIcon = null;
        gearsIcon = null;
        leaveIcon = null;
        myShape = null;

        updateThread = null;

        gameTitleBossText = null;
        gameTitleGirlText = null;

        font = null;
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.menu_screen_layout);

        SurfaceView menuSurfaceView = findViewById(R.id.menuSurfaceView);
        updateThread = new UpdateThread(menuSurfaceView);
        SurfaceHolder surfaceHolder = menuSurfaceView.getHolder();

        if(surfaceHolder != null){
            surfaceHolder.addCallback(new SurfaceHolder.Callback(){
                @Override
                public void surfaceCreated(SurfaceHolder surfaceHolder){
                    if(!updateThread.GetIsRunning()){
                        updateThread.Init();
                    }

                    if(!updateThread.isAlive()){
                        updateThread.start();
                    }
                }

                @Override
                public void surfaceChanged(SurfaceHolder surfaceHolder, int format, int width, int height){
                }

                @Override
                public void surfaceDestroyed(SurfaceHolder surfaceHolder){
                    updateThread.Terminate();
                }
            });
        }

        InitOthers();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        TouchManager.Instance.Update(event.getX(), event.getY(), event.getAction());
        return true;
    }

    @Override
    public void onClick(View v){
        if(v == startButton){
            EntityManager.Instance.SendAllEntitiesForRemoval();
            StateManager.Instance.ChangeState("GameScreen");

            startActivity(new Intent(this, GameScreenActivity.class));
            return;
        }
        if(v == optionsButton){
            EntityManager.Instance.SendAllEntitiesForRemoval();
            StateManager.Instance.ChangeState("OptionsScreen");

            startActivity(new Intent(this, OptionsScreenActivity.class));
            return;
        }
        if(v == exitButton){
            finishAndRemoveTask();
            System.exit(0);
        }
    }

    @Override
    public void onBackPressed(){
        finishAndRemoveTask();
        System.exit(0);
    }

    @Override
    public void Render(Canvas _canvas){
        _canvas.drawColor(0xFF404040);
        EntityManager.Instance.Render(_canvas);
    }

    @Override
    public void OnEnter(SurfaceView _view){
        menuPlayerChar = EntityPlayerChar.Create("menuPlayerChar", R.drawable.player_char);

        final DisplayMetrics displayMetrics = Resources.getSystem().getDisplayMetrics();
        menuPlayerChar.attribs.pos.x = (int)((float)displayMetrics.widthPixels * 0.5f);
        menuPlayerChar.attribs.pos.y = (int)((float)displayMetrics.heightPixels * 3.0f / 4.0f);

        menuPlayerChar.attribs.scale.x = menuPlayerChar.attribs.scale.y = 1.8f;
        menuPlayerChar.SetSpriteAnimXScale(1.8f);
        menuPlayerChar.SetSpriteAnimYScale(1.8f);

        menuPlayerChar.attribs.yMin = new EntityConstraint();
        menuPlayerChar.attribs.yMin.val = ((float)displayMetrics.heightPixels * 0.35f + (float)displayMetrics.widthPixels / 4.0f) * 1.15f;

        textOnScreen = EntityTextOnScreen.Create("menuTextOnScreen", _view.getContext().getAssets(), "fonts/grobold.ttf");
        textOnScreen.attribs.pos.x = 30.0f * 0.5f;
        textOnScreen.attribs.pos.y = (float)displayMetrics.heightPixels - 30.0f;
        textOnScreen.SetColor(new Color(1.0f, 1.0f, 0.0f, 0.5f));
        textOnScreen.SetStrokeWidth(100.0f);
        textOnScreen.SetTextSize(30.0f);
    }

    @Override
    public void OnExit(){
    }

    @Override
    public void Update(float _dt){
        if(textOnScreen != null){
            textOnScreen.SetText("FPS: " + 1.0f / _dt);
        }

        if(TouchManager.Instance.GetMotionEventAction() == TouchTypes.TouchType.Down.GetVal()) {
            if(isFingerOffScreenBefore){
                shldStartMoving = true;
                isFingerOffScreenBefore = false;
            }
        } else if(TouchManager.Instance.GetMotionEventAction() == TouchTypes.TouchType.Up.GetVal()) {
            isFingerOffScreenBefore = true;
        }

        if(shldStartMoving){
            menuPlayerChar.StartMoving(TouchManager.Instance.GetXPos(), TouchManager.Instance.GetYPos());
            shldStartMoving = false;
        }

        EntityManager.Instance.Update(_dt);
    }

    @Override
    public String GetName(){
        return "MenuScreen";
    }

    @Override
    protected void onPause(){
        super.onPause();
    }

    @Override
    protected void onStop(){
        super.onStop();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
    }

    private void InitOthers(){
        myShape = findViewById(R.id.myShape);
        final DisplayMetrics displayMetrics = Resources.getSystem().getDisplayMetrics();
        myShape.getLayoutParams().width = (int)((float)displayMetrics.widthPixels * 1.2f);
        myShape.getLayoutParams().height = (int)((float)displayMetrics.heightPixels * 1.2f);

        Animation myShapeAnim = AnimationUtils.loadAnimation(this, R.anim.my_shape_anim);
        myShapeAnim.setStartOffset(799);
        myShapeAnim.setAnimationListener(new Animation.AnimationListener(){
            @Override
            public void onAnimationStart(Animation anim){
            }
            @Override
            public void onAnimationRepeat(Animation anim){
            }
            @Override
            public void onAnimationEnd(Animation anim) {
                myShape.setVisibility(View.GONE);
            }
        });
        myShape.startAnimation(myShapeAnim);

        final float buttonFactor = (float)displayMetrics.widthPixels / 4.0f / 300.0f;
        final int buttonSize = (int)(300.0f * buttonFactor);

        startButton = findViewById(R.id.startButton);
        startButton.setOnClickListener(this);
        startButton.getLayoutParams().width = buttonSize;
        startButton.getLayoutParams().height = buttonSize;
        startButton.setTranslationX((float)displayMetrics.widthPixels * 0.2f - (float)buttonSize * 0.5f);
        startButton.setTranslationY((float)displayMetrics.heightPixels * 0.35f);

        optionsButton = findViewById(R.id.optionsButton);
        optionsButton.setOnClickListener(this);
        optionsButton.getLayoutParams().width = buttonSize;
        optionsButton.getLayoutParams().height = buttonSize;
        optionsButton.setTranslationX((float)displayMetrics.widthPixels * 0.5f - (float)buttonSize * 0.5f);
        optionsButton.setTranslationY((float)displayMetrics.heightPixels * 0.35f);

        exitButton = findViewById(R.id.exitButton);
        exitButton.setOnClickListener(this);
        exitButton.getLayoutParams().width = buttonSize;
        exitButton.getLayoutParams().height = buttonSize;
        exitButton.setTranslationX((float)displayMetrics.widthPixels * 0.8f - (float)buttonSize * 0.5f);
        exitButton.setTranslationY((float)displayMetrics.heightPixels * 0.35f);

        playIcon = findViewById(R.id.playIcon);
        playIcon.getLayoutParams().width = (int)((float)buttonSize * 0.65f);
        playIcon.getLayoutParams().height = (int)((float)buttonSize * 0.65f);
        playIcon.setTranslationX(startButton.getTranslationX()
                + (startButton.getLayoutParams().width
                - playIcon.getLayoutParams().width) * 0.5f);
        playIcon.setTranslationY(startButton.getTranslationY()
                + (startButton.getLayoutParams().height
                - playIcon.getLayoutParams().height) * 0.5f);

        gearsIcon = findViewById(R.id.gearsIcon);
        gearsIcon.getLayoutParams().width = (int)((float)buttonSize * 0.65f);
        gearsIcon.getLayoutParams().height = (int)((float)buttonSize * 0.65f);
        gearsIcon.setTranslationX(optionsButton.getTranslationX()
                + (optionsButton.getLayoutParams().width
                - gearsIcon.getLayoutParams().width) * 0.5f);
        gearsIcon.setTranslationY(optionsButton.getTranslationY()
                + (optionsButton.getLayoutParams().height
                - gearsIcon.getLayoutParams().height) * 0.5f);

        leaveIcon = findViewById(R.id.leaveIcon);
        leaveIcon.getLayoutParams().width = (int)((float)buttonSize * 0.65f);
        leaveIcon.getLayoutParams().height = (int)((float)buttonSize * 0.65f);
        leaveIcon.setTranslationX(exitButton.getTranslationX()
                + (exitButton.getLayoutParams().width
                - leaveIcon.getLayoutParams().width) * 0.5f);
        leaveIcon.setTranslationY(exitButton.getTranslationY()
                + (exitButton.getLayoutParams().height
                - leaveIcon.getLayoutParams().height) * 0.5f);

        font = Typeface.createFromAsset(getAssets(), "fonts/grobold.ttf");

        gameTitleBossText = findViewById(R.id.gameTitleBossText);
        gameTitleBossText.setTypeface(font);
        gameTitleBossText.setTextSize(TypedValue.COMPLEX_UNIT_SP,
                (float)displayMetrics.widthPixels * 0.2f
                        / displayMetrics.scaledDensity);
        gameTitleBossText.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        gameTitleBossText.setTranslationX((float)displayMetrics.widthPixels * 0.5f
                - (float)gameTitleBossText.getMeasuredWidth() * 0.5f);
        gameTitleBossText.setTranslationY((float)displayMetrics.heightPixels * 0.07f);

        gameTitleGirlText = findViewById(R.id.gameTitleGirlText);
        gameTitleGirlText.setTypeface(font);
        gameTitleGirlText.setTextSize(TypedValue.COMPLEX_UNIT_SP,
                (float)displayMetrics.widthPixels * 0.18f
                        / displayMetrics.scaledDensity);
        gameTitleGirlText.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        gameTitleGirlText.setTranslationX((float)displayMetrics.widthPixels * 0.5f
                - (float)gameTitleGirlText.getMeasuredWidth() * 0.5f);
        gameTitleGirlText.setTranslationY((float)displayMetrics.heightPixels * 0.2f);
    }

    private boolean isFingerOffScreenBefore;
    private boolean shldStartMoving;

    private Button startButton;
    private Button optionsButton;
    private Button exitButton;

    private EntityPlayerChar menuPlayerChar;
    private EntityTextOnScreen textOnScreen;

    private ImageView playIcon;
    private ImageView gearsIcon;
    private ImageView leaveIcon;
    private ImageView myShape;

    private UpdateThread updateThread;

    private TextView gameTitleBossText;
    private TextView gameTitleGirlText;

    private Typeface font;
}