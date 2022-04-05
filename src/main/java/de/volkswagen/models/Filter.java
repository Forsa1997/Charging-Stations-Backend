package de.volkswagen.models;

import com.fasterxml.jackson.annotation.JsonBackReference;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Table(name = "filters")
public class Filter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    @Size(max = 50)
    private String name;
    @Size(max = 50)
    private int filterKw;
    private String[] filterPlugtype;
    private String[] filterOperator;
    private String[] filterFreeToUse;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    @JoinColumn(name="user_id", nullable=false)
    private User user;

    public Filter() {
    }

    public Filter(String name, int filterKw, String[] filterPlugtype, String[] filterOperator, String[] filterFreeToUse, User user) {
        this.name = name;
        this.filterKw = filterKw;
        this.filterPlugtype = filterPlugtype;
        this.filterOperator = filterOperator;
        this.filterFreeToUse = filterFreeToUse;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String[] getFilterFreeToUse() {
        return filterFreeToUse;
    }

    public void setFilterFreeToUse(String[] filterFreeToUse) {
        this.filterFreeToUse = filterFreeToUse;
    }

    public long getUser() {
        return this.user.getId();
    }

    public void setUser(User user) {
        this.user = user;
    }

}