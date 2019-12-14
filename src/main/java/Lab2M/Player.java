package Lab2M;

import java.awt.*;

public class Player {
    public String name;
    public Point point = new Point(1,1);

    @Override
    public String toString()
    {
        return name + "," + point.x + "," + point.y;
    }
}
