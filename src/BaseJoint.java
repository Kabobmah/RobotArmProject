import javafx.geometry.Point3D;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.transform.Rotate;

public class BaseJoint extends Joint {
    public BaseJoint(String name, Point3D position, double radius, double height) {
        super(name, position, radius, height, Rotate.Y_AXIS);
        // Additional visual adjustment for the base disk
        this.getCylinder().getTransforms().add(new Rotate(90, Rotate.Z_AXIS));
        this.setMaterial(new PhongMaterial(Color.GOLD));
    }
}