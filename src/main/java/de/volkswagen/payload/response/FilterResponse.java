package de.volkswagen.payload.response;


import de.volkswagen.models.Filter;

import java.util.Set;

public class FilterResponse{
    private Set<Filter> filters;

    public FilterResponse(Set<Filter> filters){
        this.filters = filters;
    }

    public Set<Filter> getFilters() {
        return filters;
    }

    public void setFilters(Set<Filter> filters) {
        this.filters = filters;
    }
}

