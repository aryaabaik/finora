package com.finora.finora.Repository;

import com.finora.finora.Model.Template;
import com.finora.finora.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TemplateRepository extends JpaRepository<Template, Long> {

    List<Template> findByUser(User user);

}