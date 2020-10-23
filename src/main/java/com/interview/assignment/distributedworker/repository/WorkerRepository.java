package com.interview.assignment.distributedworker.repository;

import com.interview.assignment.distributedworker.model.WorkerModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkerRepository extends JpaRepository<WorkerModel,Long> {

    @Query(value = "SELECT get_processing()", nativeQuery = true)
    int get_processing();


}