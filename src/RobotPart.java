import javafx.geometry.Point3D;

public abstract class RobotPart {
    protected String name;
    protected Point3D position;// modified from subclass only

    public RobotPart(String name, Point3D position) {
        this.name = name;
        this.position = position;
    }


    public abstract void updatePosition(Point3D newPosition);


    public final String getName() { return name; }

}

