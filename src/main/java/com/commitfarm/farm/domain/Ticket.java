package com.commitfarm.farm.domain;


import com.commitfarm.farm.domain.enumClass.Component;
import com.commitfarm.farm.domain.enumClass.Priority;
import com.commitfarm.farm.domain.enumClass.Status;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "Ticket")
@Getter
@Setter
@NoArgsConstructor
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ticketId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "milestoneId")
    private Milestone milestone;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reporterId")
    private Users reporter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "developerId")
    private Users developer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "projectId")
    private Project project;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Enumerated(EnumType.STRING)
    private Priority priority;

    private LocalDateTime createdTime;

    private LocalDateTime modifiedTime;

    @Enumerated(EnumType.STRING)
    private Component component;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String title;

    @OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;
}
