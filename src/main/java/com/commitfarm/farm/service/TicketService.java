package com.commitfarm.farm.service;

import com.commitfarm.farm.domain.*;
import com.commitfarm.farm.domain.enumClass.*;
import com.commitfarm.farm.dto.ticket.request.CreateTicketDto;
import com.commitfarm.farm.dto.ticket.request.UpdateStatusReq;
import com.commitfarm.farm.dto.ticket.response.DetailTicketRes;
import com.commitfarm.farm.dto.ticket.response.StaticsRes;
import com.commitfarm.farm.dto.ticket.response.TicketListRes;
import com.commitfarm.farm.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TicketService {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private ProjectRepository projectRepository;


    private static final EnumSet<Status> PROJECT_LEADER_STATUSES = EnumSet.of(Status.Assigned, Status.Closed);
    private static final EnumSet<Status> DEVELOPER_STATUSES = EnumSet.of(Status.Resolved);
    private static final EnumSet<Status> TESTER_STATUSES = EnumSet.of(Status.New, Status.Reopened);

    @Transactional
    public CreateTicketDto createTicket(Long projectId, CreateTicketDto ticketDTO) {
        Ticket ticket = new Ticket();

        // automatically assign time
        LocalDateTime now = LocalDateTime.now();

        ticket.setMilestone(ticketDTO.getMilestone());
        ticket.setReporter(ticketDTO.getReporter());
        ticket.setStatus(Status.New); // always set to New when created
        ticket.setPriority(ticketDTO.getPriority());
        ticket.setCreatedTime(now);
        ticket.setModifiedTime(now);
        ticket.setComponent(ticketDTO.getComponent());
        ticket.setDescription(ticketDTO.getDescription());
        ticket.setTitle(ticketDTO.getTitle());

        Project project = projectRepository.findByProjectId(projectId);

        // get developers in the project
        List<Member> developers = memberRepository.findAllByProjectAndUserType(project, UserType.Developer);

        // get the developer with the least number of tickets assigned
        Optional<Users> assignedDeveloper = developers.stream()
                .collect(Collectors.groupingBy(Member::getUser, Collectors.summingInt(dev -> (int) ticketRepository.countByComponentAndDeveloper(ticketDTO.getComponent(), dev.getUser()))))
                .entrySet().stream()
                .sorted((entry1, entry2) -> {
                    int cmp = entry2.getValue().compareTo(entry1.getValue());
                    if (cmp == 0) {
                        // 동일한 티켓 수일 경우, 할당된 티켓 수로 비교
                        long dev1AssignedCount = ticketRepository.countByDeveloperAndStatus(entry1.getKey(), Status.Assigned);
                        long dev2AssignedCount = ticketRepository.countByDeveloperAndStatus(entry2.getKey(), Status.Assigned);
                        cmp = Long.compare(dev1AssignedCount, dev2AssignedCount);
                    }
                    if (cmp == 0) {
                        // 동일한 할당된 티켓 수일 경우, 사용자 ID로 비교
                        cmp = entry1.getKey().getUserId().compareTo(entry2.getKey().getUserId());
                    }
                    return cmp;
                })
                .map(entry -> entry.getKey())
                .findFirst();

        assignedDeveloper.ifPresent(ticket::setDeveloper);

        Ticket savedTicket = ticketRepository.save(ticket);

        CreateTicketDto responseDto = new CreateTicketDto();
        responseDto.setMilestone(savedTicket.getMilestone());
        responseDto.setReporter(savedTicket.getReporter());
        responseDto.setStatus(savedTicket.getStatus());
        responseDto.setPriority(savedTicket.getPriority());
        responseDto.setCreatedTime(savedTicket.getCreatedTime());
        responseDto.setModifiedTime(savedTicket.getModifiedTime());
        responseDto.setComponent(savedTicket.getComponent());
        responseDto.setDescription(savedTicket.getDescription());
        responseDto.setTitle(savedTicket.getTitle());

        return responseDto;
    }
    @Transactional(readOnly = true)
    public TicketListRes readTicketList(Long projectId, Long userId) {
        // 사용자 타입 확인
        Member member = memberRepository.findByProject_ProjectIdAndUser_UserId(projectId, userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자를 찾을 수 없습니다."));

        if (member.getUserType() != UserType.Developer) {
            throw new IllegalArgumentException("해당 사용자는 개발자가 아닙니다.");
        }

        Users user = member.getUser();

        // Assigned 티켓 리스트 조회
        List<TicketListRes.TicketInfo> assignedTickets = ticketRepository.findByProject_ProjectIdAndDeveloper_UserIdAndStatus(projectId, userId, Status.Assigned)
                .stream()
                .map(ticket -> new TicketListRes.TicketInfo(
                        ticket.getTitle(),
                        ticket.getStatus().toString(),
                        ticket.getCreatedTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                ))
                .collect(Collectors.toList());

        // Closed 티켓 리스트 조회
        List<TicketListRes.TicketInfo> closedTickets = ticketRepository.findByProject_ProjectIdAndDeveloper_UserIdAndStatus(projectId, userId, Status.Closed)
                .stream()
                .map(ticket -> new TicketListRes.TicketInfo(
                        ticket.getTitle(),
                        ticket.getStatus().toString(),
                        ticket.getCreatedTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                ))
                .collect(Collectors.toList());

        return new TicketListRes(assignedTickets, closedTickets);
    }


    @Transactional
    public DetailTicketRes readDetailTicket(Long ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new IllegalArgumentException("해당 티켓을 찾을 수 없습니다. ID: " + ticketId));

        List<DetailTicketRes.CommentResponse> comments = ticket.getComments().stream()
                .map(comment -> new DetailTicketRes.CommentResponse(
                        comment.getContent(),
                        comment.getTimeStamp().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                ))
                .collect(Collectors.toList());

        return new DetailTicketRes(
                ticket.getDescription(),
                ticket.getStatus().toString(),
                ticket.getPriority().toString(),
                ticket.getCreatedTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                ticket.getModifiedTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                ticket.getComponent().toString(),
                ticket.getDeveloper().getUsername(),
                ticket.getReporter().getUsername(),
                ticket.getMilestone().getName(),
                comments
        );
    }

    @Transactional(readOnly = true)
    public StaticsRes readTicketStatics(Long projectId) {
        LocalDateTime startOfToday = LocalDate.now().atStartOfDay();
        LocalDateTime endOfToday = startOfToday.plusDays(1);

        YearMonth thisMonth = YearMonth.now();
        LocalDateTime startOfMonth = thisMonth.atDay(1).atStartOfDay();
        LocalDateTime endOfMonth = thisMonth.atEndOfMonth().atTime(23, 59, 59);

        List<Ticket> todayTickets = ticketRepository.findByProject_ProjectIdAndCreatedTimeBetween(projectId, startOfToday, endOfToday);
        List<Ticket> monthTickets = ticketRepository.findByProject_ProjectIdAndCreatedTimeBetween(projectId, startOfMonth, endOfMonth);

        Map<Status, Long> todayStatusCount = todayTickets.stream().collect(Collectors.groupingBy(Ticket::getStatus, Collectors.counting()));
        Map<Priority, Long> todayPriorityCount = todayTickets.stream().collect(Collectors.groupingBy(Ticket::getPriority, Collectors.counting()));

        Map<Status, Long> monthStatusCount = monthTickets.stream().collect(Collectors.groupingBy(Ticket::getStatus, Collectors.counting()));
        Map<Priority, Long> monthPriorityCount = monthTickets.stream().collect(Collectors.groupingBy(Ticket::getPriority, Collectors.counting()));

        return new StaticsRes(todayStatusCount, todayPriorityCount, monthStatusCount, monthPriorityCount);
    }
    @Transactional
    public String updateTicketStatus(Long projectId, Long ticketId, Long userId, UpdateStatusReq dto) {
        Member member = memberRepository.findByProject_ProjectIdAndUser_UserId(projectId, userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자를 찾을 수 없습니다."));

        UserType userType = member.getUserType();
        Status newStatus = dto.getStatus();

        boolean isStatusChangeAllowed = switch (userType) {
            case ProjectLeader -> PROJECT_LEADER_STATUSES.contains(newStatus);
            case Developer -> DEVELOPER_STATUSES.contains(newStatus);
            case Tester -> TESTER_STATUSES.contains(newStatus);
        };

        if (!isStatusChangeAllowed) {
            return "권한이 없습니다.";
        }

        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new IllegalArgumentException("해당 티켓을 찾을 수 없습니다."));

        ticket.setStatus(newStatus);
        ticketRepository.save(ticket);

        return "상태 변경 성공";
    }
    @Transactional
    public void deleteTicket(Long ticketId) {
        ticketRepository.deleteById(ticketId);
    }








}
