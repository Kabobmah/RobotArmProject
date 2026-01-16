import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.transform.Rotate;

public class Link extends RobotPart {
    private final Group group = new Group();
    private final Box visual;
    public final double length;



    public Link(String name, double length) {
        // Links are usually placed at the origin of their parent joint
        super(name, new Point3D(0, 0, 0));
        this.length = length;

        visual = new Box(10, length, 10);
        visual.setMaterial(new PhongMaterial(Color.GRAY));
        visual.getTransforms().add(new Rotate(90, Rotate.X_AXIS));
        visual.setTranslateZ(length / 2.0);

        group.getChildren().add(visual);
    }


    @Override
    public void updatePosition(Point3D newPosition) {
        this.position = newPosition;
        group.setTranslateX(newPosition.getX());
        group.setTranslateY(newPosition.getY());
        group.setTranslateZ(newPosition.getZ());
    }


    public void setMaterial(PhongMaterial material) {
        visual.setMaterial(material);
    }


    public Group getVisualGroup() {
        return group;
    }
}