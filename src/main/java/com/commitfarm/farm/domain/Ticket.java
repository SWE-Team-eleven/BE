package com.commitfarm.farm.domain;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "Ticket")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer ticketId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "milestoneId")
    private Milestone milestone;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reporterId")
    private Users reporter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "developerId")
    private Users developer;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @Enumerated(EnumType.STRING)
    private Priority priority;

    private Date createdTime;

    @Column(nullable = false, columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    private Date modifiedTime;

    @Enumerated(EnumType.STRING)
    private Component component;

    public enum Status {
        New, Assigned, Closed, Reopened
    }

    public enum Priority {
        Blocker, Critical, Major, Minor, Trivial
    }

    public enum Component {
        UIComponent, DataAcessComponent, SecurityComponent
    }
}
