package server;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import model.Task;

import java.io.IOException;
import java.time.LocalDateTime;

public class LocalDateTimeAdapter extends TypeAdapter<LocalDateTime> {
    @Override
    public void write(final JsonWriter jsonWriter, final LocalDateTime localDateTime) throws IOException {
        if (localDateTime == null) {
            jsonWriter.nullValue();
        } else {
            jsonWriter.value(localDateTime.format(Task.formatter));
        }
    }

    @Override
    public LocalDateTime read(final JsonReader jsonReader) throws IOException {
        String dateString = jsonReader.nextString();
        if ("null".equals(dateString)) {
            return null;
        } else {
            return LocalDateTime.parse(dateString, Task.formatter);
        }
    }
}
