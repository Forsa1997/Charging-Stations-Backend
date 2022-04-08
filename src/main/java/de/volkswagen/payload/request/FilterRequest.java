package de.volkswagen.payload.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class FilterRequest {
    @NotBlank
    @Size(max = 50)
    private String name;
    @NotBlank
    private long userId;
    @Size(max = 50)
    private int filterKw;
    private String[] filterPlugtype;
    private String[] filterOperator;
    private String filterFreeToUse;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getFilterKw() {
        return filterKw;
    }

    public void setFilterKw(int filterKw) {
        this.filterKw = filterKw;
    }

    public String[] getFilterPlugtype() {
        return filterPlugtype;
    }

    public void setFilterPlugtype(String[] filterPlugtype) {
        this.filterPlugtype = filterPlugtype;
    }

    public String[] getFilterOperator() {
        return filterOperator;
    }

    public void setFilterOperator(String[] filterOperator) {
        this.filterOperator = filterOperator;
    }

    public String getFilterFreeToUse() {
        return filterFreeToUse;
    }

    public void setFilterFreeToUse(String filterFreeToUse) {
        this.filterFreeToUse = filterFreeToUse;
    }
}
