import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TaskTest {

    Task task1 = new Task("Name Test Task", "Description Test Task");
    Task task2 = new Task("Name Test Task", "Description Test Task");

    @Test
            void checkThatInstancesOfTheTaskClassAreEqualToEachOther(){
        assertEquals(task1.hashCode(), task2.hashCode(), "Объекты не равны друг другу");
    }
}