package com.web.crawler.crud.repositories;

import com.web.crawler.entities.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
}
