package com.interview.assignment.distributedworker.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "workertable")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class WorkerModel {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    Long id;

    @Column(name="url")
    String url;

    @Column(name = "status")
    String status;

    @Column(name = "http_code")
    String http_code;


}
