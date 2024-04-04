package com.pwdmgr.repository;

import com.pwdmgr.model.Container;
import com.pwdmgr.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContainerRepository extends JpaRepository<Container,Integer> {
    List<Container> findByUser(User user);
}
