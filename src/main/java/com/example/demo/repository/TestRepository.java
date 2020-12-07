package com.example.demo.repository;

import com.example.demo.model.TestTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;
import java.util.UUID;
@Repository
public interface TestRepository extends JpaRepository<TestTable, UUID> {
    @Query(value = "select t.field from test_table t", nativeQuery = true)
    List<Integer> getField();
}
