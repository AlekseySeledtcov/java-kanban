import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class InMemoryTaskManagerTest extends TaskManagerTest {

    @BeforeEach
    void beforeEach() {
        manager = new InMemoryTaskManager();
        super.beforeEach();
    }

    @Test
    void giveTasksOfDifferentTypesAndFindThemById() throws NotFoundException {
        super.giveTasksOfDifferentTypesAndFindThemById();
    }

    @Test
    void checkThatTasksWithTheGivenIdAndTheGeneratedDoNotConflict() {
        super.checkThatTasksWithTheGivenIdAndTheGeneratedDoNotConflict();
    }

    @Test
    void CheckingThatTheTaskHasNotChangedInAllFieldsAfterAddingToTheManager() {
        super.CheckingThatTheTaskHasNotChangedInAllFieldsAfterAddingToTheManager();
    }

    @Test
    void tasksAddedToHistoryManagerRetainThePreviousVersionOfTheTask() throws NotFoundException {
        super.tasksAddedToHistoryManagerRetainThePreviousVersionOfTheTask();
    }

    @Test
    void calculationOfBoundaryValuesAllSubtasksWithStatus() throws NotFoundException {
        super.calculationOfBoundaryValuesAllSubtasksWithStatus();
    }

    @Test
    void checkingForIntersectionOfTimeIntervals() {
        super.checkingForIntersectionOfTimeIntervals();
    }
}