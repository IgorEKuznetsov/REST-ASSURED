package dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Response {
    @JsonProperty("aisles")
    private List<Aisle> aisles = new ArrayList<Aisle>();

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Aisle {
        @JsonProperty("aisle")
        private String aisle;
        @JsonProperty("items")
        private List<Item> items = new ArrayList<Item>();

    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Item {
        @JsonProperty("id")
        private Integer id;
        @JsonProperty("name")
        private String name;

    }
}

