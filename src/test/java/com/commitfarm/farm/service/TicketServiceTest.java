package com.commitfarm.farm.service;

import com.commitfarm.farm.domain.*;
import com.commitfarm.farm.domain.enumClass.*;
import com.commitfarm.farm.dto.ticket.request.CreateTicketDto;
import com.commitfarm.farm.dto.ticket.request.UpdateStatusReq;
import com.commitfarm.farm.dto.ticket.response.DetailTicketRes;
import com.commitfarm.farm.dto.ticket.response.StaticsRes;
import com.commitfarm.farm.dto.ticket.response.TicketListRes;
import com.commitfarm.farm.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TicketServiceTest {

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private UsersRepository usersRepository;

    @Mock
    private ProjectRepository projectRepository;

    @InjectMocks
    private TicketService ticketService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateTicket() {
        Long projectId = 1L;
        CreateTicketDto ticketDto = new CreateTicketDto();
        ticketDto.setMilestone(new Milestone());
        ticketDto.setReporter(new Users());
        ticketDto.setPriority(Priority.Major);
        ticketDto.setComponent(Component.UIComponent);
        ticketDto.setDescription("Test description");
        ticketDto.setTitle("Test title");

        Project project = new Project();
        project.setProjectId(projectId);

        Member developer = new Member();
        developer.setUser(new Users());

        when(projectRepository.findByProjectId(projectId)).thenReturn(project);
        when(memberRepository.findAllByProjectAndUserType(project, UserType.Developer)).thenReturn(Collections.singletonList(developer));
        when(ticketRepository.save(any(Ticket.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CreateTicketDto result = ticketService.createTicket(projectId, ticketDto);

        assertNotNull(result);
        assertEquals(Status.New, result.getStatus());
        verify(ticketRepository, times(1)).save(any(Ticket.class));
    }

    @Test
    void testReadTicketList() {
        Long projectId = 1L;
        Long userId = 1L;

        Member member = new Member();
        member.setUserType(UserType.Developer);
        member.setUser(new Users());

        Ticket ticket = new Ticket();
        ticket.setTitle("Test title");
        ticket.setStatus(Status.Assigned);
        ticket.setCreatedTime(LocalDateTime.now());

        when(memberRepository.findByProject_ProjectIdAndUser_UserId(projectId, userId)).thenReturn(Optional.of(member));
        when(ticketRepository.findByProject_ProjectIdAndDeveloper_UserIdAndStatus(projectId, userId, Status.Assigned))
                .thenReturn(Collections.singletonList(ticket));
        when(ticketRepository.findByProject_ProjectIdAndDeveloper_UserIdAndStatus(projectId, userId, Status.Closed))
                .thenReturn(Collections.singletonList(ticket));

        TicketListRes result = ticketService.readTicketList(projectId, userId);

        assertNotNull(result);
        assertEquals(1, result.getAssignedTickets().size());
        assertEquals(1, result.getClosedTickets().size());
    }

    @Test
    void testReadDetailTicket() {
        Long ticketId = 1L;

        Ticket ticket = new Ticket();
        ticket.setDescription("Test description");
        ticket.setStatus(Status.New);
        ticket.setPriority(Priority.Major);
        ticket.setCreatedTime(LocalDateTime.now());
        ticket.setModifiedTime(LocalDateTime.now());
        ticket.setComponent(Component.UIComponent);
        ticket.setDeveloper(new Users());
        ticket.setReporter(new Users());
        ticket.setMilestone(new Milestone());

        when(ticketRepository.findById(ticketId)).thenReturn(Optional.of(ticket));

        DetailTicketRes result = ticketService.readDetailTicket(ticketId);

        assertNotNull(result);
        assertEquals("Test description", result.getDescription());
    }

    @Test
    void testReadTicketStatics() {
        Long projectId = 1L;
        LocalDateTime startOfToday = LocalDate.now().atStartOfDay();
        LocalDateTime endOfToday = startOfToday.plusDays(1);
        YearMonth thisMonth = YearMonth.now();
        LocalDateTime startOfMonth = thisMonth.atDay(1).atStartOfDay();
        LocalDateTime endOfMonth = thisMonth.atEndOfMonth().atTime(23, 59, 59);

        Ticket ticket = new Ticket();
        ticket.setStatus(Status.New);
        ticket.setPriority(Priority.Major);
        ticket.setCreatedTime(LocalDateTime.now());

        List<Ticket> todayTickets = Collections.singletonList(ticket);
        List<Ticket> monthTickets = Collections.singletonList(ticket);

        when(ticketRepository.findByProject_ProjectIdAndCreatedTimeBetween(projectId, startOfToday, endOfToday)).thenReturn(todayTickets);
        when(ticketRepository.findByProject_ProjectIdAndCreatedTimeBetween(projectId, startOfMonth, endOfMonth)).thenReturn(monthTickets);

        StaticsRes result = ticketService.readTicketStatics(projectId);

        assertEquals(1, result.getTodayStatusCount().get(Status.New));
        assertEquals(1, result.getTodayPriorityCount().get(Priority.Major));
        assertEquals(1, result.getMonthStatusCount().get(Status.New));
        assertEquals(1, result.getMonthPriorityCount().get(Priority.Major));

        verify(ticketRepository, times(1)).findByProject_ProjectIdAndCreatedTimeBetween(projectId, startOfToday, endOfToday);
        verify(ticketRepository, times(1)).findByProject_ProjectIdAndCreatedTimeBetween(projectId, startOfMonth, endOfMonth);
    }

    @Test
    void testUpdateTicketStatus() {
        Long projectId = 1L;
        Long ticketId = 1L;
        Long userId = 1L;
        UpdateStatusReq dto = new UpdateStatusReq();
        dto.setStatus(Status.Assigned);

        Member member = new Member();
        member.setUserType(UserType.ProjectLeader);

        Ticket ticket = new Ticket();
        ticket.setStatus(Status.New);

        when(memberRepository.findByProject_ProjectIdAndUser_UserId(projectId, userId)).thenReturn(Optional.of(member));
        when(ticketRepository.findById(ticketId)).thenReturn(Optional.of(ticket));
        when(ticketRepository.save(any(Ticket.class))).thenReturn(ticket);

        String result = ticketService.updateTicketStatus(projectId, ticketId, userId, dto);

        assertEquals("상태 변경 성공", result);
        assertEquals(Status.Assigned, ticket.getStatus());
    }

    @Test
    void testUpdateTicketStatus_Unauthorized() {
        Long projectId = 1L;
        Long ticketId = 1L;
        Long userId = 1L;
        UpdateStatusReq dto = new UpdateStatusReq();
        dto.setStatus(Status.Closed);

        Member member = new Member();
        member.setUserType(UserType.Developer);

        when(memberRepository.findByProject_ProjectIdAndUser_UserId(projectId, userId)).thenReturn(Optional.of(member));

        String result = ticketService.updateTicketStatus(projectId, ticketId, userId, dto);

        assertEquals("권한이 없습니다.", result);
    }

    @Test
    void testDeleteTicket() {
        Long ticketId = 1L;

        doNothing().when(ticketRepository).deleteById(ticketId);

        ticketService.deleteTicket(ticketId);

        verify(ticketRepository, times(1)).deleteById(ticketId);
    }
}
