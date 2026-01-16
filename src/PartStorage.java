import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

// T extends RobotPart — это bounded type parameter (ограниченный дженерик)
// Parametric Polymorphism принимает любые объекты RobotPart
public class PartStorage<T extends RobotPart> {

    private final List<T> parts = new ArrayList<>();



    public void addPart(T part) {
        parts.add(part);
        System.out.println("Storage: Added " + part.getName());
    }


    public T getPart(int index) {
        if (index >= 0 && index < parts.size()) {
            return parts.get(index);
        }
        return null;
    }


    // Пример обобщенного метода для выполнения действия над всеми деталями
    public void forEachPart(Consumer<T> action) {
        for (T part : parts) {
            action.accept(part);
        }
    }


    public int count() {
        return parts.size();
    }
}