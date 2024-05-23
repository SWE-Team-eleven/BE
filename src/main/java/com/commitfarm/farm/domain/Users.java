package com.commitfarm.farm.domain;

import com.commitfarm.farm.domain.enumClass.UserType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "Users")
@Getter
@Setter
@NoArgsConstructor
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private String username;

    private String password;

    private String email;



    private boolean isAdmin;

    @OneToMany(mappedBy = "user")
    private List<Member> members;

    @OneToMany(mappedBy = "reporter")
    private List<Ticket> reportedTickets;

    @OneToMany(mappedBy = "developer")
    private List<Ticket> assignedTickets;

    @OneToMany(mappedBy = "user")
    private List<Comment> comments;

}