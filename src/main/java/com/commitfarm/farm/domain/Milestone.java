package com.commitfarm.farm.domain;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "Milestone")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Milestone {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer milestoneId;

    @Column(nullable = false)
    private String name;

    private Date startDate;
    private Date dueDate;
    private String comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "projectId")
    private Project project;
}
