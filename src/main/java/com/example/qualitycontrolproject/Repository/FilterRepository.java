package com.example.qualitycontrolproject.Repository;

import com.example.qualitycontrolproject.entities.Filter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FilterRepository extends JpaRepository<Filter, Long> {
 Filter findByName(String name);
 boolean existsByCode(String code);
 Filter findByCode(String code);


 Filter findByQuery(String query);

 // Find all filters containing a keyword in their query
 List<Filter> findByQueryContaining(String keyword);


}
