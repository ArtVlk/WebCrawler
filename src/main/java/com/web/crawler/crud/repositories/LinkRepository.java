package com.web.crawler.crud.repositories;

import com.web.crawler.entities.Link;
import org.springframework.data.repository.CrudRepository;

public interface LinkRepository extends CrudRepository<Link, Long> {
}
