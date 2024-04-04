package com.pwdmgr.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Container {
    @Id
    @GeneratedValue
    private Integer id;
    private String title;
    private String websiteUrl;
    @ManyToOne @JsonIgnore
    private User user;
    @OneToMany(mappedBy = "container")
    private List<Account> accounts;
}
