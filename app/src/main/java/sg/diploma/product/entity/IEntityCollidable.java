package sg.diploma.product.entity;

public interface IEntityCollidable{
    String GetType();

    float GetPosX();
    float GetPosY();
    float GetRadius();

    void OnHit(IEntityCollidable _other);
}