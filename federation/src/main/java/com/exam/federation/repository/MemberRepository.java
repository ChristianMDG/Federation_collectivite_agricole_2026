package com.exam.federation.repository;

import com.exam.federation.config.DataSource;
import com.exam.federation.dto.CreateMember;
import com.exam.federation.dto.MemberResponse;
import com.exam.federation.entity.Enums.Gender;
import com.exam.federation.entity.Enums.MemberOccupation;
import com.exam.federation.entity.Member;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
@AllArgsConstructor
public class MemberRepository {

    private final DataSource dataSource;

    public MemberResponse save(CreateMember request) {

        String sql = """
            INSERT INTO member (
                id, firstname, lastname, birthday, gender, address,
                profession, phone_number, email, occupation,
                registration_fee_paid, membership_dues_paid, collectivity_id
            )
            VALUES (
                'mem_' || nextval('member_id_seq'),
                ?, ?, ?::DATE, ?::gender_type, ?, ?, ?, ?, ?::member_occupation_type, ?, ?, ?
            )
            RETURNING id, firstname, lastname, birthday, gender,
                      address, profession, phone_number, email, occupation
        """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, request.getFirstName());
            stmt.setString(2, request.getLastName());
            stmt.setDate(3, Date.valueOf(request.getBirthDate()));
            stmt.setString(4, request.getGender().name());
            stmt.setString(5, request.getAddress());
            stmt.setString(6, request.getProfession());
            stmt.setInt(7, request.getPhoneNumber());
            stmt.setString(8, request.getEmail());
            stmt.setString(9, request.getOccupation().name());
            stmt.setBoolean(10, request.getRegistrationFeePaid());
            stmt.setBoolean(11, request.getMembershipDuesPaid());
            stmt.setString(12, request.getCollectivityIdentifier());

            ResultSet rs = stmt.executeQuery();

            if (!rs.next()) {
                return null;
            }

            MemberResponse member = new MemberResponse();
            member.setId(rs.getString("id"));
            member.setFirstName(rs.getString("firstname"));
            member.setLastName(rs.getString("lastname"));
            member.setBirthDate(rs.getDate("birthday").toLocalDate());
            member.setGender(Gender.valueOf(rs.getString("gender")));
            member.setAddress(rs.getString("address"));
            member.setProfession(rs.getString("profession"));
            member.setPhoneNumber(rs.getInt("phone_number"));
            member.setEmail(rs.getString("email"));
            member.setOccupation(MemberOccupation.valueOf(rs.getString("occupation")));

            if (request.getReferees() != null && !request.getReferees().isEmpty()) {
                addReferees(member.getId(), request.getReferees());
                member.setReferees(findRefereesByMemberId(member.getId()));
            } else {
                member.setReferees(new ArrayList<>());
            }

            return member;

        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la création du membre", e);
        }
    }

    public List<MemberResponse> saveAll(List<CreateMember> requests) {
        List<MemberResponse> responses = new ArrayList<>();

        for (CreateMember request : requests) {

            if (Boolean.FALSE.equals(request.getRegistrationFeePaid())) {
                throw new RuntimeException("Registration fee not paid");
            }

            if (Boolean.FALSE.equals(request.getMembershipDuesPaid())) {
                throw new RuntimeException("Membership dues not paid");
            }

            if (request.getReferees() != null) {
                for (String refereeId : request.getReferees()) {
                    if (!existsById(refereeId)) {
                        throw new RuntimeException("Referee not found: " + refereeId);
                    }
                }
            }

            responses.add(save(request));
        }

        return responses;
    }

    public boolean existsById(String id) {

        String sql = "SELECT COUNT(id) FROM member WHERE id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

            return false;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void addReferees(String memberId, List<String> refereeIds) {

        String sql = "INSERT INTO member_referees (member_id, referee_id) VALUES (?, ?)";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            for (String refereeId : refereeIds) {
                ps.setString(1, memberId);
                ps.setString(2, refereeId);
                ps.addBatch();
            }

            ps.executeBatch();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private List<MemberResponse> findRefereesByMemberId(String memberId) {

        String sql = """
            SELECT r.id, r.firstname, r.lastname, r.birthday, r.gender,
                   r.address, r.profession, r.phone_number, r.email, r.occupation
            FROM member_referees mr
            JOIN member r ON mr.referee_id = r.id
            WHERE mr.member_id = ?
        """;

        List<MemberResponse> referees = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, memberId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                MemberResponse referee = new MemberResponse();
                referee.setId(rs.getString("id"));
                referee.setFirstName(rs.getString("firstname"));
                referee.setLastName(rs.getString("lastname"));
                referee.setBirthDate(rs.getDate("birthday").toLocalDate());
                referee.setGender(Gender.valueOf(rs.getString("gender")));
                referee.setAddress(rs.getString("address"));
                referee.setProfession(rs.getString("profession"));
                referee.setPhoneNumber(rs.getInt("phone_number"));
                referee.setEmail(rs.getString("email"));
                referee.setOccupation(MemberOccupation.valueOf(rs.getString("occupation")));
                referee.setReferees(new ArrayList<>());

                referees.add(referee);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return referees;
    }

    public MemberResponse findById(String id) {
        String sql = """
        SELECT id, firstname, lastname, birthday, gender, 
               address, profession, phone_number, email, occupation
        FROM member WHERE id = ?
    """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                MemberResponse member = new MemberResponse();
                member.setId(rs.getString("id"));
                member.setFirstName(rs.getString("firstname"));
                member.setLastName(rs.getString("lastname"));
                member.setBirthDate(rs.getDate("birthday").toLocalDate());
                member.setGender(Gender.valueOf(rs.getString("gender")));
                member.setAddress(rs.getString("address"));
                member.setProfession(rs.getString("profession"));
                member.setPhoneNumber(rs.getInt("phone_number"));
                member.setEmail(rs.getString("email"));
                member.setOccupation(MemberOccupation.valueOf(rs.getString("occupation")));
                member.setReferees(new ArrayList<>());
                return member;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}