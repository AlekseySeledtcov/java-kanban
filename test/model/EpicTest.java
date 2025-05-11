package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class EpicTest {
    private final Epic epic1 = new Epic("Name Test model.Epic", "Description Test model.Epic");
    private final Epic epic2 = new Epic("Name Test model.Epic", "Description Test model.Epic");

    @Test
    public void checkThatInstancesOfTheTaskClassAreEqualToEachOther() {
        assertEquals(epic1.hashCode(), epic2.hashCode(), "Объекты не равны друг другу");
    }

    @Test
    public void epicObjectCannotDeAddedToItself() {
        epic1.setId(10);
        epic1.setSubtaskIdList(epic1.getId());
        assertNull(epic1.getSubtaskIdList().get(0), "model.Epic нельзя добавить в самого себя в виде подзадачи");
    }
}
