package com.azilen.specification;

import lombok.*;

@NoArgsConstructor
@Getter
@Setter
@Builder
@EqualsAndHashCode(of = {"key"})
public class SearchCriteria {

    private String key;
    private String operation;
    private Object value;
    private String suffixOperation;
    private String variableName;

    public SearchCriteria(String key, String operation, Object value) {
        this.key = key;
        this.operation = operation;
        this.value = value;
    }

    public SearchCriteria(String key, String operation, Object value, String suffixOperation) {
        this.key = key;
        this.operation = operation;
        this.value = value;
        this.suffixOperation = suffixOperation;
    }


    public SearchCriteria(String key, String operation, Object value,String suffixOperation, String variableName) {
        this.key = key;
        this.operation = operation;
        this.value = value;
        this.suffixOperation = suffixOperation;
        this.variableName = variableName;
    }

    @Override
    public String toString() {
        return this.key + " " + this.operation + " " + this.value + " " + (this.variableName != null ? this.variableName : "");
    }
}
