package sg.diploma.game;

interface ICollidable{
    String GetType();

    float GetPosX();
    float GetPosY();
    float GetRadius();

    void OnHit(ICollidable _other);
}