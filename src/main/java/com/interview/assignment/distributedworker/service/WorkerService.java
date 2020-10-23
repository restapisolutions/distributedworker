package com.interview.assignment.distributedworker.service;

import com.interview.assignment.distributedworker.model.WorkerModel;
import com.interview.assignment.distributedworker.repository.WorkerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WorkerService{
    @Autowired
    WorkerRepository repository;

    public WorkerModel findById(Long id){
        return repository.findById(id).get();
    }

    public int getJobID(){
        return repository.get_processing();
    }

    public void save(WorkerModel model) { repository.save(model); }
}