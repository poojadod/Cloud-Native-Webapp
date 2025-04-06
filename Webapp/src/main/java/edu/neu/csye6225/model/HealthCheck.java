package edu.neu.csye6225.model;
import jakarta.persistence.*;
import java.time.Instant;
import java.util.Date;


@Entity
@Table(name = "CSYE6225")
public class HealthCheck {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "check_id")
    private Long checkId;

    @Column(name = "datetime", nullable = false)
    private Date datetime= new Date();


}