package com.commitfarm.farm.service;


import com.commitfarm.farm.domain.Member;
import com.commitfarm.farm.domain.Project;
import com.commitfarm.farm.domain.Users;

import com.commitfarm.farm.dto.project.CreateProjectReq;
import com.commitfarm.farm.dto.project.ManageUserAccountReq;
import com.commitfarm.farm.dto.project.ProjectListDto;
import com.commitfarm.farm.repository.MemberRepository;
import com.commitfarm.farm.repository.ProjectRepository;
import com.commitfarm.farm.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private UsersRepository usersRepository;


    @Transactional
    public void createProject(CreateProjectReq createProjectReq) throws Exception {
        Project project = new Project();
        project.setName(createProjectReq.getName());
        project.setStartDate(createProjectReq.getStartDate());
        project.setEndDate(createProjectReq.getEndDate());
        project.setDescription(createProjectReq.getDescription());

        projectRepository.save(project);

        for (ManageUserAccountReq manageUserAccountReq : createProjectReq.getManageUserAccounts()) {
            Users user = usersRepository.findByEmail(manageUserAccountReq.getUserEmail())
                    .orElseThrow(() -> new Exception("사용자를 찾을 수 없습니다."));

            Member member = new Member();
            member.setProject(project);
            member.setUser(user);
            member.setUserType(manageUserAccountReq.getUserType()); //추가
            memberRepository.save(member);
        }

    }

    @Transactional
    public void deleteProject(Long projectId) {
        projectRepository.deleteById(projectId);
    }


    @Transactional
    public List<ProjectListDto> readProjectList(Long userId) throws Exception {
        // 사용자가 존재하는지 확인
        if (!usersRepository.existsById(userId)) {
            throw new Exception("사용자를 찾을 수 없습니다.");
        }


        List<Project> projects = projectRepository.findAllByUserId(userId);
        return projects.stream()
                .map(project -> new ProjectListDto(project.getName(), project.getEndDate()))
                .collect(Collectors.toList());
    }

}
