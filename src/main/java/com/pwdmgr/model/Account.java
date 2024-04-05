package com.pwdmgr.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Account {
    @Id
    @GeneratedValue
    private Integer id;

    private String email;
    private String password;
    @Transient
    private Integer accountId;
    @Transient
    private Integer containerId;
    @ManyToOne @JsonIgnore
    private Container container;
}
