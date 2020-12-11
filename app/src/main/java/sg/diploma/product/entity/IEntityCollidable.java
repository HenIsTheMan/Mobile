package sg.diploma.product.entity;

public interface IEntityCollidable{
    void OnHit(IEntityCollidable _other);

    public EntityCollidableTypes.EntityCollidableType collidableType = EntityCollidableTypes.EntityCollidableType.Amt;
}