    import javafx.scene.Group;

    public final class RobotArm {
        private final Group visualGroup = new Group();

        // Generic контейнер (Parametric Polymorphism)
        private final PartStorage<Joint> jointStorage = new PartStorage<>();
        private final PartStorage<Link> linkStorage = new PartStorage<>(); // второй склад для дженерика
        private static final double BASE_Y_OFFSET = -15.0;
        private static final double MIN_JOINT_ANGLE = -170.0;
        private static final double MAX_JOINT_ANGLE = 170.0;

        public void addJoint  (Joint joint) {
            jointStorage.addPart(joint);
        }
        public void addLink     (Link link)    {
            linkStorage.addPart(link);
        }

        public Group getVisualGroup() {
            return visualGroup;
        }


        public Joint getJoint(int index) {
            return jointStorage.getPart(index);
        }
        public Link   getLink(int index) {
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
                if (newAngle < MIN_JOINT_ANGLE || newAngle > MAX_JOINT_ANGLE) {
                    throw new InvalidMovementException("Angle out of bounds");
                }

                if (wouldHitFloor(index, newAngle)) {
                    throw new InvalidMovementException("Safety: Floor hit prevented" );
                }

                joint.setRotationAngle(newAngle);

            } catch (InvalidMovementException e) {
                System.out.println("Movement blocked: " + e.getMessage());
            }
        }

        public void moveJointDelta(int index, double deltaAngle) {
            Joint joint = jointStorage.getPart(index);
            if (joint != null) {
                moveJoint(index, joint.getRotationAngle() + deltaAngle);
            }
        }


        private boolean wouldHitFloor(int movedIndex, double potentialAngle) {
            double currentY = BASE_Y_OFFSET;

            double cumulativePitch = 0;

            for (int i = 1; i < jointStorage.count(); i++) {

                double angleDeg = (i == movedIndex) ? potentialAngle : jointStorage.getPart(i).getRotationAngle();
                cumulativePitch += Math.toRadians(angleDeg);

                Link link = linkStorage.getPart(i - 1);

                if (link != null) {
                    currentY -= link.length * Math.sin(cumulativePitch);

                    if (currentY > 0) {
                        return true;
                    }
                }
            }

            return false;
        }
    }