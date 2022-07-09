package ro.fasttrackit.hotelapi.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import ro.fasttrackit.hotelapi.model.Room;
import ro.fasttrackit.hotelapi.model.RoomFilter;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class RoomDAO {

    private final MongoTemplate mongo;

    public Page<Room> findBy(RoomFilter filter, Pageable pageable){
        Criteria criteria = new Criteria();

        Optional.ofNullable(filter.id())
                .ifPresent(id -> criteria.and("id").is(id));
        Optional.ofNullable(filter.number())
                .ifPresent(number -> criteria.and("number").is(number));
        Optional.ofNullable(filter.floor())
                .ifPresent(floor -> criteria.and("floor").gte(floor));
        Optional.ofNullable(filter.hotelName())
                .ifPresent(name -> criteria.and("hotelName").is(name));
        Optional.ofNullable(filter.tv())
                .ifPresent(tv -> criteria.and("roomFacilities.tv").ne(!tv));
        Optional.ofNullable(filter.doubleBed())
                .ifPresent(doubleBed -> criteria.and("roomFacilities.doubleBed").ne(!doubleBed));

        Query query = Query.query(criteria).with(pageable);
        List<Room> content = mongo.find(query, Room.class);
        return PageableExecutionUtils.getPage(content, pageable,
                () -> mongo.count(Query.query(criteria), Room.class));
    }
}
