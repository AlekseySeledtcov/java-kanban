package memory;

import file.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class InMemoryTaskManagerTest extends TaskManagerTest {

    @BeforeEach
    public void beforeEach() {
        manager = new InMemoryTaskManager();
        super.beforeEach();
    }

    @Test
    public void giveTasksOfDifferentTypesAndFindThemById() throws NotFoundException {
        super.giveTasksOfDifferentTypesAndFindThemById();
    }

    @Test
    public void checkThatTasksWithTheGivenIdAndTheGeneratedDoNotConflict() {
        super.checkThatTasksWithTheGivenIdAndTheGeneratedDoNotConflict();
    }

    @Test
    public void checkingThatTheTaskHasNotChangedInAllFieldsAfterAddingToTheManager() {
        super.checkingThatTheTaskHasNotChangedInAllFieldsAfterAddingToTheManager();
    }

    @Test
    public void tasksAddedToHistoryManagerRetainThePreviousVersionOfTheTask() throws NotFoundException {
        super.tasksAddedToHistoryManagerRetainThePreviousVersionOfTheTask();
    }

    @Test
    public void calculationOfBoundaryValuesAllSubtasksWithStatus() throws NotFoundException {
        super.calculationOfBoundaryValuesAllSubtasksWithStatus();
    }

    @Test
    public void checkingForIntersectionOfTimeIntervals() {
        super.checkingForIntersectionOfTimeIntervals();
    }
}