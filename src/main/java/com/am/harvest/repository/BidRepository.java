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
    @Modifying
    @Query("FROM Bid b WHERE b.state=:state AND b.product=:product AND b.direction=:direction")
    List<Bid> findByStateAndProductAndDirection(@Param("state") BidState state,
                                                @Param("product")String product,
                                                @Param ("direction")Direction direction);


//    List<Bid> findByStateAndProductAndDirection(BidState state, String product, Direction direction);

//    @Query("from Auction a join a.category c where c.name=:categoryName")
//    public Iterable<Auction> findByCategory(@Param("categoryName") String categoryName);
}
