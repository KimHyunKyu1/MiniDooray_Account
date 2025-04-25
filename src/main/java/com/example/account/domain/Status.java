package com.example.account.domain;

import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.IOException;

@Entity
@NoArgsConstructor
public class Status {

    @Setter
    @Id
    private String status;

    public Status(String registered) {
        this.status = registered;
    }

    @JsonValue
    public String getStatus() {
        return status;
    }
}
class StatusSerializer extends StdSerializer<Status> {
    public StatusSerializer() {
        this(null);
    }

    public StatusSerializer(Class<Status> t) {
        super(t);
    }

    @Override
    public void serialize(Status status, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeString(status.getStatus());
    }
}
