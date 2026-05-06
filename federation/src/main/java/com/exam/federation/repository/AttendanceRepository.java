package com.exam.federation.repository;

import com.exam.federation.config.DataSource;
import com.exam.federation.dto.ActivityMemberAttendance;
import com.exam.federation.dto.CreateActivityMemberAttendance;
import com.exam.federation.dto.MemberDescription;
import com.exam.federation.entity.Enums.AttendanceStatus;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class AttendanceRepository {

    private DataSource dataSource;
    private MemberRepository memberRepository;

    public AttendanceRepository(DataSource dataSource, MemberRepository memberRepository) {
        this.dataSource = dataSource;
        this.memberRepository = memberRepository;
    }

    public List<ActivityMemberAttendance> saveAll(String activityId, List<CreateActivityMemberAttendance> requests) {
        List<ActivityMemberAttendance> responses = new ArrayList<>();

        String checkSql = "SELECT attendance_status FROM member_attendance WHERE activity_id = ? AND member_id = ?";
        String insertSql = """
            INSERT INTO member_attendance (id, activity_id, member_id, attendance_status)
            VALUES ('att_' || nextval('attendance_id_seq'), ?, ?, ?)
            RETURNING id
        """;

        try (Connection conn = dataSource.getConnection()) {
            for (CreateActivityMemberAttendance request : requests) {
                String memberId = request.getMemberIdentifier();
                AttendanceStatus status = request.getAttendanceStatus();

                if (memberRepository.existsById(memberId) == false) {
                    throw new IllegalArgumentException("Member not found: " + memberId);
                }

                try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
                    checkStmt.setString(1, activityId);
                    checkStmt.setString(2, memberId);
                    ResultSet rs = checkStmt.executeQuery();
                    if (rs.next()) {
                        String existingStatus = rs.getString("attendance_status");
                        throw new IllegalStateException(
                                "Attendance for member " + memberId + " already set to " + existingStatus + " and cannot be modified"
                        );
                    }
                }

                try (PreparedStatement ps = conn.prepareStatement(insertSql)) {
                    ps.setString(1, activityId);
                    ps.setString(2, memberId);
                    ps.setString(3, status.name());

                    ResultSet rs = ps.executeQuery();
                    if (rs.next()) {
                        ActivityMemberAttendance attendance = new ActivityMemberAttendance();
                        attendance.setId(rs.getString("id"));
                        attendance.setAttendanceStatus(status);

                        MemberDescription memberDesc = getMemberDescription(memberId);
                        attendance.setMemberDescription(memberDesc);

                        responses.add(attendance);
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error saving attendance: " + e.getMessage(), e);
        }
        return responses;
    }

    private MemberDescription getMemberDescription(String memberId) {
        Object member = memberRepository.findById(memberId);
        if (member == null) {
            return null;
        }

        MemberDescription desc = new MemberDescription();
        desc.setId(((com.exam.federation.dto.MemberResponse) member).getId());
        desc.setFirstName(((com.exam.federation.dto.MemberResponse) member).getFirstName());
        desc.setLastName(((com.exam.federation.dto.MemberResponse) member).getLastName());
        desc.setEmail(((com.exam.federation.dto.MemberResponse) member).getEmail());
        desc.setOccupation(((com.exam.federation.dto.MemberResponse) member).getOccupation().name());
        return desc;
    }
}