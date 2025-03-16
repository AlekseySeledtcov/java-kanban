import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class EpicTest {
    Epic epic1 = new Epic("Name Test Epic", "Description Test Epic");
    Epic epic2 = new Epic("Name Test Epic", "Description Test Epic");

    @Test
    void checkThatInstancesOfTheTaskClassAreEqualToEachOther() {
        assertEquals(epic1.hashCode(), epic2.hashCode(), "Объекты не равны друг другу");
    }

    @Test
    void EpicObjectCannotDeAddedToItself() {
        epic1.setId(epic1.hashCode());
        epic1.setSubtaskIdList(epic1.hashCode());
        assertNull(epic1.getSubtaskIdList().get(0), "Epic нельзя добавить в самого себя в виде подзадачи");
    }


}
