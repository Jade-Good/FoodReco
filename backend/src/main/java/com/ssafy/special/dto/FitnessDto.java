package com.ssafy.special.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class FitnessDto {
    private List<Bucket> bucket;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Bucket {
        private String startTimeMillis;
        private String endTimeMillis;
        private List<Dataset> dataset;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Dataset {
        private String dataSourceId;
        private List<Point> point;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Point {
        private String startTimeNanos;
        private String originDataSourceId;
        private String endTimeNanos;
        private List<Value> value;
        private String dataTypeName;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Value {
        private List<Integer> mapVal;
        private Integer intVal;
    }

    // Assuming MapVal is an empty object for now, as no properties are given in the example

}