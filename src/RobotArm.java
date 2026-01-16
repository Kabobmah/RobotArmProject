    import javafx.scene.Group;
    import javafx.geometry.Point3D;

    public final class RobotArm {
        private final Group visualGroup = new Group();

        // Generic контейнер (Parametric Polymorphism)
        private final PartStorage<Joint> jointStorage = new PartStorage<>();
        private final PartStorage<Link> linkStorage = new PartStorage<>(); // второй склад для дженерика

        private static final double MIN_JOINT_ANGLE = -170.0;
        private static final double MAX_JOINT_ANGLE = 170.0;

        public void addJoint(Joint joint) {
            jointStorage.addPart(joint);}
        public void addLink(Link link) {
            linkStorage.addPart(link);
        }

        public Group getVisualGroup() {
            return visualGroup;
        }


        public Joint getJoint(int index) {
            return jointStorage.getPart(index);
        }
        public Link getLink(int index) {
            return linkStorage.getPart(index);
        }

        //Method Overloading + Coercion Polymorphism)
        //-------------------------------------------
        public void connect(Joint joint, Link link) {
            joint.getVisualGroup().getChildren().add(link.getVisualGroup());
        }


        public void connect(Joint joint, RobotPart part) {
            if (part instanceof Link) {
                Link link = (Link) part; // Downcasting (Coercion)
                joint.getVisualGroup().getChildren().add(link.getVisualGroup());
            }
        }


        public void connect(RobotPart part, Joint joint) {
            if (part instanceof Link) {
                Link link = (Link) part; // Downcasting
                link.getVisualGroup().getChildren().add(joint.getVisualGroup());
            }
        }

        //-------------------------------------------
        //for animation
        /*public void moveJoint(int index, double newAngle) {
            Joint joint = jointStorage.getPart(index);
            if (joint == null) return;

            try {
                if (!wouldHitFloor(index, newAngle)) {
                    joint.setRotationAngle(newAngle);
                } else {
                    throw new InvalidMovementException("Safety: Floor hit prevented.");
                }
            } catch (InvalidMovementException e) {
                // Обработка исключения (Exception Handling)
                System.err.println("Limit reached: " + e.getMessage());
            }
        }*/


        public void moveJointDelta(int index, double deltaAngle) {
            Joint joint = jointStorage.getPart(index);
            if (joint == null) return;

            double currentAngle = joint.getRotationAngle();
            double targetAngle = currentAngle + deltaAngle;

            try {
                if (targetAngle < MIN_JOINT_ANGLE || targetAngle > MAX_JOINT_ANGLE) {
                    throw new InvalidMovementException("Angle out of bounds: " + targetAngle);
                }

                if (wouldHitFloor(index, targetAngle)) {
                    throw new InvalidMovementException("Movement blocked: Floor hit!");
                }

                joint.setRotationAngle(targetAngle);
            } catch (InvalidMovementException e) {
                System.out.println(e.getMessage());
            }
        }


        private boolean wouldHitFloor(int movedIndex, double potentialAngle) {
            // Длины звеньев (L1=120, L2=100, L3=60)
            double l1 = 120.0;
            double l2 = 100.0;
            double l3 = 60.0;

            // Массив для хранения углов (текущие + тот, который хотим изменить)
            double[] angles = new double[4];
            for (int i = 0; i < 4; i++) {
                angles[i] = Math.toRadians(jointStorage.getPart(i).getRotationAngle());
            }
            angles[movedIndex] = Math.toRadians(potentialAngle);

            // Расчет высоты Y (в JavaFX -Y — это вверх)
            double y1 = -15.0; // Стартовая высота над полом
            double y2 = y1 - (l1 * Math.sin(angles[1]));
            double y3 = y2 - (l2 * Math.sin(angles[1] + angles[2]));
            double yEnd = y3 - (l3 * Math.sin(angles[1] + angles[2] + angles[3]));

            // Если любая из точек Y > 0, значит она коснулась пола (ушла вниз)
            return (y2 > 0 || y3 > 0 || yEnd > 0);
        }
    }