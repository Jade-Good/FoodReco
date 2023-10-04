package com.ssafy.special.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FitnessDto {
    private List<Bucket> bucket = new ArrayList<>();
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Bucket {
        private String startTimeMillis;
        private String endTimeMillis;
        private List<Dataset> dataset = new ArrayList<>();
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Dataset {
        private String dataSourceId;
        private List<Point> point = new ArrayList<>();
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Point {
        private String startTimeNanos;
        private String originDataSourceId;
        private String endTimeNanos;
        private List<Value> value = new ArrayList<>();
        private String dataTypeName;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Value {
        private List<Integer> mapVal = new ArrayList<>();
        private Integer intVal;
    }

    // Assuming MapVal is an empty object for now, as no properties are given in the example

}