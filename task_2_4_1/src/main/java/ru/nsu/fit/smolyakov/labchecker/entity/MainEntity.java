package ru.nsu.fit.smolyakov.labchecker.entity;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.*;
import ru.nsu.fit.smolyakov.labchecker.entity.course.Course;
import ru.nsu.fit.smolyakov.labchecker.entity.group.Group;

import java.io.File;
import java.io.IOException;

@NonNull
@Value
@Builder(builderClassName = "Builder")
@RequiredArgsConstructor
@JsonDeserialize(builder = MainEntity.Builder.class)
public class MainEntity { // TODO rename
    static ObjectMapper objectMapper;
    static {
        objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .setVisibility(
                PropertyAccessor.FIELD,
                JsonAutoDetect.Visibility.ANY
            )
            .disable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE);
    }

    @Getter
    Course course;
    @Getter
    Group group;

    public void serialize(String filename) throws IOException {
        objectMapper.writeValue(new File(filename), this);

    }

    public static MainEntity deserialize(String filename) throws IOException {
        return objectMapper.readValue(new File(filename), MainEntity.class);
    }
}
