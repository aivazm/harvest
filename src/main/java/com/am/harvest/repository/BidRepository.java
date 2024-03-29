package com.am.harvest.repository;

import com.am.harvest.model.Bid;
import com.am.harvest.model.BidState;
import com.am.harvest.model.Direction;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BidRepository extends CrudRepository<Bid, Long> {
    List<Bid> findAllByStateAndProductAndDirection(BidState state, String product, Direction direction);
}
