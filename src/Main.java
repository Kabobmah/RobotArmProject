/*import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import java.util.Objects;
*/
import javafx.application.Application;
import javafx.geometry.Point3D;
import javafx.scene.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;


public class Main extends Application {

    private static final int WIDTH = 1000;
    private static final int HEIGHT = 750;
    private static final double FLOOR_Y_POS = 5.0;

    // Link Lengths (Constants for Kinematics)
    private static final double L1 = 120.0; // Blue link
    private static final double L2 = 100.0; // Yellow link
    private static final double L3 = 60.0;  // Cyan link

    private PerspectiveCamera camera; //we declare variable camera of type Perspective camera
    private final Group root = new Group(); // final means the root variable forever reserved for this object
    private RobotArm robotArm;



    @Override // shows to java that we are overriding the start method of application which is empty by default
    public void start(Stage primaryStage) { // “JavaFX will call this method, give you the main window (primaryStage),and you must build and show your UI here.”
        String javaVersion = System.getProperty("java.version");// just for me to check the version of java
        String javafxVersion = System.getProperty("javafx.version");

        System.out.println("Java version: " + javaVersion);
        System.out.println("JavaFX version: " + javafxVersion); //show versions  so i know versions

        setupCamera();
        setupScene();
        setupRobotArm();
        setupCoordinateAxes(); // calling methods of setuping everything through java fx



        Scene scene = new Scene(root, WIDTH, HEIGHT, true, SceneAntialiasing.BALANCED); // using construct to make e new scene

        scene.setFill(Color.LIGHTSKYBLUE);
        scene.setCamera(camera);
        scene.setOnKeyPressed(this::handleKeyPress); // java fx methods

        primaryStage.setTitle("3D Robot Arm Simulation"); //name of window
        primaryStage.setScene(scene);
        primaryStage.show();

        /*startAnimation();*/
    }//росто ебаная настройка сценф


    private void setupCoordinateAxes() { // this is for orientation , useful only to me

        Point3D offset = new Point3D(-200, -100, 0); // move arrows from center

        Group coordinateSystemGroup = new Group(); //group groups all 3d things i make in coordinateSystemGroup

        // Sizes:
        double axisLength = 80;
        double axisRadius = 1.5;
        double arrowheadRadius = 5;//variables

        // X-axis (Red)
        Cylinder xAxisBody = new Cylinder(axisRadius, axisLength);//calling construct with param
        xAxisBody.setMaterial(new PhongMaterial(Color.RED)); //creating a new instance of the PhongMaterial class with the diffuse color set to red.
        xAxisBody.getTransforms().add(new Rotate(90, Rotate.Z_AXIS));
        xAxisBody.setTranslateX(axisLength / 2); //from (-40 to 40) to (0 to 80) bc new objects are centered
        Sphere xArrowHead = new Sphere(arrowheadRadius);
        xArrowHead.setMaterial(new PhongMaterial(Color.RED));
        xArrowHead.setTranslateX(axisLength);
        Group xAxis = new Group(xAxisBody, xArrowHead);
        coordinateSystemGroup.getChildren().add(xAxis);

        // Y-axis (Yellow)
        Cylinder yAxisBody = new Cylinder(axisRadius, axisLength);
        yAxisBody.setMaterial(new PhongMaterial(Color.YELLOW));
        yAxisBody.setTranslateY(axisLength / 2);
        Sphere yArrowHead = new Sphere(arrowheadRadius);
        yArrowHead.setMaterial(new PhongMaterial(Color.YELLOW));
        yArrowHead.setTranslateY(axisLength);
        Group yAxis = new Group(yAxisBody, yArrowHead);
        coordinateSystemGroup.getChildren().add(yAxis);

        // Z-axis (Blue)
        Cylinder zAxisBody = new Cylinder(axisRadius, axisLength);
        zAxisBody.setMaterial(new PhongMaterial(Color.BLUE));
        zAxisBody.getTransforms().add(new Rotate(90, Rotate.X_AXIS));
        zAxisBody.setTranslateZ(axisLength / 2);
        Sphere zArrowHead = new Sphere(arrowheadRadius);
        zArrowHead.setMaterial(new PhongMaterial(Color.BLUE));
        zArrowHead.setTranslateZ(axisLength);
        Group zAxis = new Group(zAxisBody, zArrowHead);
        coordinateSystemGroup.getChildren().add(zAxis);

        // Apply the offset to the entire coordinate system group
        coordinateSystemGroup.setTranslateX(offset.getX());
        coordinateSystemGroup.setTranslateY(offset.getY());
        coordinateSystemGroup.setTranslateZ(offset.getZ());

        root.getChildren().add(coordinateSystemGroup);// adding our arrows into main scene by adding them inside the root group
    }


    /* private void startAnimation() {
        Timeline timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
        KeyFrame keyFrame = new KeyFrame(Duration.millis(33), e -> {
            double time = System.currentTimeMillis() / 1000.0; // time in seconds

            double baseAngle = Math.sin(time * 0.5) * 170;
            double joint1Angle = Math.cos(time * 0.8) * 90;
            double joint2Angle = Math.sin(time * 1.2) * 45;
            double joint3Angle = Math.cos(time) * 60;

            robotArm.moveJoint(0, baseAngle);
            robotArm.moveJoint(1, joint1Angle);
            robotArm.moveJoint(2, joint2Angle);
            robotArm.moveJoint(3, joint3Angle);
        });
        timeline.getKeyFrames().add(keyFrame);
        timeline.play();

        Timeline stopTimeline = new Timeline(new KeyFrame(Duration.seconds(5), e -> timeline.stop()));
        stopTimeline.play();
    }//просто ебанная анимация */


    private void setupCamera() {
        camera = new PerspectiveCamera(true);
        camera.setNearClip(0.1);
        camera.setFarClip(10000.0);
        camera.setTranslateZ(-1200);
        camera.setTranslateY(-300);

        camera.getTransforms().add(new Rotate(-25, Rotate.X_AXIS));
        root.getChildren().add(camera);
    }//просто ебаная камера


    private void setupScene() {

        Box floor = new Box(1500, 10, 1500);
        floor.setTranslateY(5); // This will now be 5 units *up* from the world origin
        floor.setMaterial(new PhongMaterial(Color.DARKSEAGREEN));
        root.getChildren().add(floor);

        AmbientLight ambientLight = new AmbientLight(Color.rgb(80, 80, 80));
        PointLight mainLight = new PointLight(Color.WHITE);
        mainLight.setTranslateY(-500);
        mainLight.setTranslateZ(-800);
        root.getChildren().addAll(ambientLight, mainLight);
    }//просто ебаная сцена


    private void setupRobotArm() {
        robotArm = new RobotArm();
        //created object of class RobotArm

        // Yellow base joint at 0 0 0
        //инклюзивный полиморфизм  можно ставить BaseJoint или Joint и будет работать идиноково так как BaseJoint ребенок JOint
        /*Base*/
        Joint base1 = new BaseJoint("gold", new Point3D(0, 0, 0), 90, 30);
        robotArm.addJoint(base1);
        robotArm.getVisualGroup().getChildren().add(base1.getVisualGroup());

        Joint redJoint = new Joint("red", new Point3D(0, base1.getHeight() / -2.0, 0), 25, 50, Rotate.X_AXIS);
        redJoint.setMaterial(new PhongMaterial(Color.RED));
        robotArm.addJoint(redJoint);
        base1.getVisualGroup().getChildren().add(redJoint.getVisualGroup());


        RobotPart link1 = new Link("blue link1", L1); //we can put robotarm her instead of Link ink link1 = new Link("blue link1", L1);  inclusion polymorphism
        ((Link) link1).setMaterial(new PhongMaterial(Color.BLUE));
        robotArm.connect(redJoint, link1);

        Joint greenJoint = new Joint("green Joint2", new Point3D(0, 0, ((Link) link1).length), 20, 40, Rotate.X_AXIS);
        greenJoint.setMaterial(new PhongMaterial(Color.GREEN));
        robotArm.addJoint(greenJoint);
        robotArm.connect(link1, greenJoint);

        Link link2 = new Link("yellow Link2", L2);
        link2.setMaterial(new PhongMaterial(Color.YELLOW));
        robotArm.connect(greenJoint, link2);

        Joint purpleJoint = new Joint("purple joint3", new Point3D(0, 0, link2.length), 18, 35, Rotate.X_AXIS);
        purpleJoint.setMaterial(new PhongMaterial(Color.PURPLE));
        robotArm.addJoint(purpleJoint);
        robotArm.connect(link2, purpleJoint);

        Link link3 = new Link("cyan Link3", L3);
        link3.setMaterial(new PhongMaterial(Color.CYAN));
        robotArm.connect(purpleJoint, link3);

        root.getChildren().add(robotArm.getVisualGroup());


    }


    //просто ебаное управление
    private void handleKeyPress(KeyEvent event) {
        final double angleStep = 2.0;
        final double cameraMoveStep = 30.0;
        calculateAndPrintJointPositions();
        switch (event.getCode()) {
            case LEFT: camera.setTranslateX(camera.getTranslateX() - cameraMoveStep); break;
            case RIGHT: camera.setTranslateX(camera.getTranslateX() + cameraMoveStep); break;
            case SHIFT: camera.setTranslateY(camera.getTranslateY() - cameraMoveStep); break;
            case CONTROL: camera.setTranslateY(camera.getTranslateY() + cameraMoveStep); break;
            case UP: camera.setTranslateZ(camera.getTranslateZ() + cameraMoveStep); break;
            case DOWN: camera.setTranslateZ(camera.getTranslateZ() - cameraMoveStep); break;
            case W: camera.getTransforms().add(new Rotate(-2, Rotate.X_AXIS)); break;
            case S: camera.getTransforms().add(new Rotate(2, Rotate.X_AXIS)); break;
            case A: camera.getTransforms().add(new Rotate(-2, Rotate.Y_AXIS)); break;
            case D: camera.getTransforms().add(new Rotate(2, Rotate.Y_AXIS)); break;

            // Manual joint controls using delta movements
            case R: robotArm.moveJointDelta(0, -angleStep); break;
            case F: robotArm.moveJointDelta(0, angleStep); break;
            case T: robotArm.moveJointDelta(1, -angleStep); break;
            case G: robotArm.moveJointDelta(1, angleStep); break;
            case Y: robotArm.moveJointDelta(2, -angleStep); break;
            case H: robotArm.moveJointDelta(2, angleStep); break;
            case U: robotArm.moveJointDelta(3, -angleStep); break;
            case J: robotArm.moveJointDelta(3, angleStep); break;
            default: break;
        }

    }

    //-------------------kinematics--------------------------
    // 1. Метод без параметров (использует перегрузку) adhoc overloading poly for extensibility if needed we can turn true and output more info
    private final void calculateAndPrintJointPositions() {
        calculateAndPrintJointPositions(false);
    }


    // 2. Метод с параметром (основная логика)
    private void calculateAndPrintJointPositions(boolean verbose) {
        System.out.println("--- Current Joint Coordinates ---");

        // 1. Получаем углы напрямую из суставов (Encapsulation) bc getJoint is a method
        double theta0 = Math.toRadians(robotArm.getJoint(0).getRotationAngle()); // База (Y-axis)
        double theta1 = Math.toRadians(robotArm.getJoint(1).getRotationAngle()); // Red (X-axis)
        double theta2 = Math.toRadians(robotArm.getJoint(2).getRotationAngle()); // Green (X-axis)
        double theta3 = Math.toRadians(robotArm.getJoint(3).getRotationAngle()); // Purple (X-axis)

        // Joint 1: Red Joint (всегда на -15 по Y относительно базы)
        Point3D p1 = new Point3D(0, -15, 0);
        System.out.printf("Joint 1 (Red):   [x: %7.2f, y: %7.2f, z: %7.2f]\n", p1.getX(), p1.getY(), p1.getZ());

        // Joint 2: Green Joint (Конец Blue Link)
        double x2 = L1 * Math.sin(theta0) * Math.cos(theta1);
        double y2 = p1.getY() - (L1 * Math.sin(theta1));
        double z2 = L1 * Math.cos(theta0) * Math.cos(theta1);
        System.out.printf("Joint 2 (Green): [x: %7.2f, y: %7.2f, z: %7.2f]\n", x2, y2, z2);

        // Joint 3: Purple Joint (Конец Yellow Link)
        double cum2 = theta1 + theta2;
        double x3 = x2 + L2 * Math.sin(theta0) * Math.cos(cum2);
        double y3 = y2 - (L2 * Math.sin(cum2));
        double z3 = z2 + L2 * Math.cos(theta0) * Math.cos(cum2);
        System.out.printf("Joint 3 (Purple):[x: %7.2f, y: %7.2f, z: %7.2f]\n", x3, y3, z3);

        // End Effector (Конец Cyan Link)
        double cum3 = theta1 + theta2 + theta3;
        double xEnd = x3 + L3 * Math.sin(theta0) * Math.cos(cum3);
        double yEnd = y3 - (L3 * Math.sin(cum3));
        double zEnd = z3 + L3 * Math.cos(theta0) * Math.cos(cum3);
        System.out.printf("End Effector:    [x: %7.2f, y: %7.2f, z: %7.2f]\n", xEnd, yEnd, zEnd);

        if (verbose) System.out.println("Full info mode");
    }
    //--------------------------------------------------------
}