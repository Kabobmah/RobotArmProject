import javafx.geometry.Point3D;

public abstract class RobotPart {


    private String name;
    private Point3D position;// modified from subclass only

    public final Point3D getPosition() {
        return position;
    }
    protected final void setPosition(Point3D position) {
        this.position = position;
    }
    protected final void setName(String name) {
        this.name = name;
    }

    public final String getName() { return name; }


    public RobotPart(String name, Point3D position) {
        this.name = name;
        this.position = position;
    }


    public abstract void updatePosition(Point3D newPosition);




}

