import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.Objects;

public class Task {
    protected int id;
    protected final String name;
    protected final String description;
    protected Status status;
    protected Duration duration;
    protected LocalDateTime startTime;
    protected LocalDateTime endTime;

    public Task(String name, String description, int durationInMinutes) {
        this.id = 0;
        this.name = name;
        this.description = description;
        this.status = Status.NEW;
        this.duration = Duration.ofMinutes(durationInMinutes);
        this.startTime = LocalDateTime.now();
        this.endTime = startTime.plus(duration);
    }

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
        this.status = Status.NEW;

    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Status getStatus() {
        return status;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public static class TaskTimeComparator implements Comparator<Task> {
        @Override
        public int compare(Task o1, Task o2) {
            int difference = 0;
            if (o1.startTime.getYear() != o2.startTime.getYear()) {
                difference = o1.startTime.getYear() - o2.startTime.getYear();
            } else if (o1.startTime.getMonthValue() != (o2.startTime.getMonthValue())) {
                difference = o1.startTime.getMonthValue() - o2.startTime.getMonthValue();
            } else if (o1.startTime.getDayOfMonth() != o2.startTime.getDayOfMonth()) {
                difference = o1.startTime.getDayOfMonth() - o2.startTime.getDayOfMonth();
            } else if (o1.startTime.getHour() != o2.startTime.getHour()) {
                difference = o1.startTime.getHour() - o2.startTime.getHour();
            } else if (o1.startTime.getMinute() != o2.startTime.getMinute()) {
                difference = o1.startTime.getHour() - o2.startTime.getHour();
            } else if (o1.startTime.getSecond() != o2.startTime.getSecond()) {
                difference = o1.startTime.getSecond() - o2.startTime.getSecond();
            }
            return difference;
        }
    }

    public boolean intersectionCheck(Task task) {
        return this.getEndTime().isAfter(task.getStartTime());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Task task = (Task) o;

        if (id != task.id) return false;
        if (!Objects.equals(name, task.name)) return false;
        if (!Objects.equals(description, task.description)) return false;
        return status == task.status;
    }

    @Override
    public int hashCode() {
        int result = (name != null ? name.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return String.format("%d,%S,%s,%S,%s,%s,%d,%s%n", id, Type.TASK, name, status, description
                , startTime.format(formatter), duration.toMinutes(), endTime.format(formatter));
    }

    protected static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm");
}
