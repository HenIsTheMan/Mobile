package sg.diploma.product.entity;

public interface IEntityCollidable{
    String GetType();

    void OnHit(IEntityCollidable _other);
}