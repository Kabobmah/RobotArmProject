    import javafx.scene.Group;
    import javafx.geometry.Point3D;

    public final class RobotArm {
        private final Group visualGroup = new Group();

        // Generic контейнер (Parametric Polymorphism)
        private final PartStorage<Joint> jointStorage = new PartStorage<>();
        private final PartStorage<Link> linkStorage = new PartStorage<>(); // второй склад для дженерика
        private static final double BASE_Y_OFFSET = -15.0;
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
        public void moveJoint(int index, double newAngle) {
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
                System.out.println("Limit reached: " + e.getMessage());
            }
        }


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
            // 1. Начинаем с высоты плеча
            double currentY = BASE_Y_OFFSET;

            // 2. Накопленный угол наклона в плоскости XY (как твои angles[1] + angles[2]...)
            double cumulativePitch = 0;

            // 3. Проходим по всем суставам, начиная с индекса 1 (Red Joint),
            // так как индекс 0 (Base) крутит только влево-вправо и на высоту не влияет.
            for (int i = 1; i < jointStorage.count(); i++) {

                // Берем либо текущий угол, либо потенциальный для проверки
                double angleDeg = (i == movedIndex) ? potentialAngle : jointStorage.getPart(i).getRotationAngle();
                cumulativePitch += Math.toRadians(angleDeg);

                // Берем соответствующий линк (Link 0 идет после Joint 1)
                Link link = linkStorage.getPart(i - 1);

                if (link != null) {
                    // Считаем высоту конца текущего линка
                    currentY -= link.length * Math.sin(cumulativePitch);

                    // Если точка ушла ниже пола (в JavaFX Y > 0 — это вниз)
                    if (currentY > 0) {
                        return true;
                    }
                }
            }

            return false;
        }
    }