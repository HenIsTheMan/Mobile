package sg.diploma.product;

// Created by TanSiewLan2020
// Create an interface "StateBase". That is what a state will need.

import android.graphics.Canvas;
import android.view.SurfaceView;

public interface IState{
    String GetName();

    void OnEnter(SurfaceView _view);
    void OnExit();
    void Render(Canvas _canvas);
    void Update(float _dt);
}

// Name to the state
// 4 methods in every state that we have
// Class example: Gamestate
// 1) String GetName() --> return " ........" Statename eg: Level 1

// Main activity classes left mainly are Main menu, splash, gamepage, option page.
//  Level 1, 2, Gameover --> known as state.
//  Main activity can be modify to a state too.
// 2) OnEnter() --> Using the surfaceview e.g: Renderbackground
// 3) Render () --> E.g: Entitymanager, Instance, Canvas
// 4) Update () --> FPS, timer.. dt