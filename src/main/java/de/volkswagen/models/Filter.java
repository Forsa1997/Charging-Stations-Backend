package de.volkswagen.models;

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

    public Filter() {
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    private String[] filterFreeToUse;
    @ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    private User user;
}