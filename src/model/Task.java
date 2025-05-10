package model;

import file.Status;
import file.Type;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Task {
    protected Integer id;
    protected final String name;
    protected final String description;
    protected Status status;
    protected Duration duration;
    protected LocalDateTime startTime;
    protected LocalDateTime endTime;


    public Task(String name, String description, int durationInMinutes) {
        this.id = null;
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

    public String getName() {
        return name;
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

    public boolean intersectionCheck(Task task) {
        return (task.getEndTime().isAfter(this.getStartTime()) && task.getEndTime().isBefore(this.getEndTime()))
                || (task.getStartTime().isAfter(this.startTime) && task.getStartTime().isBefore(this.getEndTime()))
                || (task.getStartTime().isBefore(this.getStartTime()) && task.getEndTime().isAfter(this.getEndTime()))
                || task.getStartTime().equals(this.startTime) || task.getEndTime().equals(this.endTime);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Task task = (Task) o;

        if (!Objects.equals(id, task.id)) return false;
        if (!Objects.equals(name, task.name)) return false;
        if (!Objects.equals(description, task.description)) return false;
        return status == task.status;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return String.format("%d,%S,%s,%S,%s,%s,%d,%s%n", id, Type.TASK, name, status, description,
                startTime.format(formatter), duration.toMinutes(), endTime.format(formatter));
    }

    public static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm");
}
