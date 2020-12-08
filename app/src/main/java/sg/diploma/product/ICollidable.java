package sg.diploma.product;

// Created by TanSiewLan2020

public interface ICollidable{
    String GetType();

    float GetPosX();
    float GetPosY();
    float GetRadius();

    void OnHit(ICollidable _other);
}