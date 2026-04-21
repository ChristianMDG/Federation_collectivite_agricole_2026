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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
@AllArgsConstructor
public class MemberRepository {
    private final DataSource dataSource;

    // Méthode simple dans MemberRepository
    public Member save(CreateMember request) {
        String insertSql = """
        INSERT INTO member (
            id, firstname, lastname, birthday, gender, address,
            profession, phone_number, email, occupation,
            registration_fee_paid, membership_dues_paid, collectivity_id
        ) VALUES (
            'mem_' || REPLACE(gen_random_uuid()::TEXT, '-', ''),
            ?, ?, ?::DATE, ?::gender_type, ?, ?, ?, ?, ?::member_occupation_type, ?, ?, ?
        )
        RETURNING id, firstname, lastname, birthday, gender, address, 
                  profession, phone_number, email, occupation
    """;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(insertSql)) {

            ps.setString(1, request.getFirstName());
            ps.setString(2, request.getLastName());
            ps.setDate(3, Date.valueOf(request.getBirthDate()));
            ps.setString(4, request.getGender().name());
            ps.setString(5, request.getAddress());
            ps.setString(6, request.getProfession());
            ps.setInt(7, request.getPhoneNumber());
            ps.setString(8, request.getEmail());
            ps.setString(9, request.getOccupation().name());
            ps.setBoolean(10, request.getRegistrationFeePaid());
            ps.setBoolean(11, request.getMembershipDuesPaid());
            ps.setString(12, request.getCollectivityIdentifier());

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Member member = new Member();
                member.setId(rs.getString("id"));
                member.setFirstName(rs.getString("firstname"));
                member.setLastName(rs.getString("lastname"));
                member.setBirthDate(rs.getDate("birthday").toLocalDate());
                member.setGender(Gender.valueOf(rs.getString("gender")));
                member.setAddress(rs.getString("address"));
                member.setProfession(rs.getString("profession"));
                member.setPhoneNumber(rs.getString("phone_number"));
                member.setEmail(rs.getString("email"));
                member.setOccupation(MemberOccupation.valueOf(rs.getString("occupation")));

                return member;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
