package com.am.harvest.repository;

import com.am.harvest.model.Deal;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DealRepository extends CrudRepository<Deal, Long> {
}
