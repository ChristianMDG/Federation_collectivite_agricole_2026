package com.exam.federation.repository;

import com.exam.federation.config.DataSource;
import com.exam.federation.entity.Member;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
@AllArgsConstructor
public class MemberRepository {
    private final DataSource dataSource;

    public List<Member> createMembers(List<Member> members) {
        List<Member> createdMembers = new ArrayList<>();
    String createMemberQuery = """
            
            """;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(createMemberQuery)) {

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return createdMembers;
    }
}
