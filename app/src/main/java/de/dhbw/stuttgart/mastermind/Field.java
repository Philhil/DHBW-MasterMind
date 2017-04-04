package de.dhbw.stuttgart.mastermind;
/**
 * Created by philipp on 04.04.17.
 */

public class Field
{
    private int _color;

    public Field()
    {
        _color = -1;
    }

    int getColor()
    {
        return _color;
    }

    void setColor(int color)
    {
        _color = color;
    }
}
