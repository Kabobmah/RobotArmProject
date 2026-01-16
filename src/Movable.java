    public interface Movable {
        // Interfaces define what an object CAN DO
        void moveDelta(double delta) throws InvalidMovementException;
        double getRotationAngle();
    }