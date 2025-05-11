package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TaskTest {

    private final Task task1 = new Task("Name Test model.Task", "Description Test model.Task");
    private final Task task2 = new Task("Name Test model.Task", "Description Test model.Task");

    @Test
    public void checkThatInstancesOfTheTaskClassAreEqualToEachOther() {
        assertEquals(task1.hashCode(), task2.hashCode(), "Объекты не равны друг другу");
    }
}