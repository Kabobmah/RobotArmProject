import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Cylinder;
import javafx.scene.transform.Rotate;

public class Joint extends RobotPart implements Movable {
    private final Group group = new Group();
    private final Cylinder visual;
    private final Rotate rotateTransform;
    private final double radius;
    private final double height;
    public double getHeight() {
        return height;
    }
    public double getRadius() {
        return radius;
    }
    private static final double MIN_ANGLE = -180.0;
    private static final double MAX_ANGLE = 180.0;


    //adhoc overloading poly
    public Joint(String name, Point3D pos, double rad, double h, Point3D axis, Color color) {
        super(name, pos);
        this.radius = rad; this.height = h;
        this.visual = new Cylinder(rad, h);
        this.visual.setMaterial(new PhongMaterial(color)); // Установка цвета
        this.visual.getTransforms().add(new Rotate(90, Rotate.Z_AXIS));
        this.group.getChildren().add(new Group(visual));
        updatePosition(pos);
        this.rotateTransform = new Rotate(0, axis);
        this.group.getTransforms().add(rotateTransform);
    }


    // overloaded constructor with default color
    public Joint(String name, Point3D pos, double rad, double h, Point3D axis) {
        this(name, pos, rad, h, axis, Color.GRAY); // Вызов основного конструктора
    }


    // overloaded constructor with default size and color
    public Joint(String name, Point3D pos, Point3D axis) {
        this(name, pos, 20.0, 40.0, axis, Color.GRAY);
    }


    @Override
    public void moveDelta(double delta) throws InvalidMovementException {
        double targetAngle = getRotationAngle() + delta;
        setRotationAngle(targetAngle);
    }


    @Override
    public void updatePosition(Point3D newPosition) {
        this.position = newPosition;
        group.setTranslateX(newPosition.getX());
        group.setTranslateY(newPosition.getY());
        group.setTranslateZ(newPosition.getZ());
    }


    public void setRotationAngle(double angle) throws InvalidMovementException {
        if (angle < MIN_ANGLE || angle > MAX_ANGLE) {
            throw new InvalidMovementException("Angle out of bounds for " + name);
        }
        rotateTransform.setAngle(angle);
    }


    @Override
    public double getRotationAngle() {
        return rotateTransform.getAngle();
    }


    public void setMaterial(PhongMaterial material) {
        visual.setMaterial(material);
    }


    public Group getVisualGroup() {
        return group;
    }


    public Cylinder getCylinder() {
        return visual;
    }
}