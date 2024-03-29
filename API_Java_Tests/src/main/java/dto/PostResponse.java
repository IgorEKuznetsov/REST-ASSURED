package dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PostResponse {
    @JsonProperty("id")
    private Integer id;
    @JsonProperty("name")
    private String name;
}
