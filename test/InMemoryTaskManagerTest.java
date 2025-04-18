import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class InMemoryTaskManagerTest extends TaskManagerTest {

    @BeforeEach
    void beforeEach() {
        manager = new InMemoryTaskManager();
        super.beforeEach();
    }

    @Test
    void giveTasksOfDifferentTypesAndFindThemById() {
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
    void tasksAddedToHistoryManagerRetainThePreviousVersionOfTheTask() {
        super.tasksAddedToHistoryManagerRetainThePreviousVersionOfTheTask();
    }

    @Test
    void calculationOfBoundaryValuesAllSubtasksWithStatus() {
        super.calculationOfBoundaryValuesAllSubtasksWithStatus();
    }

    @Test
    void checkingForIntersectionOfTimeIntervals() {
        super.checkingForIntersectionOfTimeIntervals();
    }
}