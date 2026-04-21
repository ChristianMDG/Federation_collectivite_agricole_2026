package com.exam.federation.repository;

import com.exam.federation.config.DataSource;
import com.exam.federation.entity.Collectivity;
import com.exam.federation.entity.CollectivityStructure;
import com.exam.federation.entity.CreateCollectivity;
import com.exam.federation.entity.Member;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

@Repository
public class CollectivityRepository {

    private final DataSource dataSource;

    public CollectivityRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Collectivity save(CreateCollectivity request, List<Member> members, CollectivityStructure structure) {

        if (!request.getFederationApproval()) {
            throw new IllegalArgumentException("Collectivity must have federation approval");
        }

        if (structure == null) {
            throw new IllegalArgumentException("Structure is required");
        }

        if (members == null || members.size() < 10) {
            throw new IllegalArgumentException("At least 10 members required");
        }

        String id = UUID.randomUUID().toString();

        try (Connection conn = dataSource.getConnection()) {

            conn.setAutoCommit(false);

            PreparedStatement ps = conn.prepareStatement("""
                INSERT INTO collectivity (id, location, federation_approval,
                    president_id, vice_president_id, treasurer_id, secretary_id)
                VALUES (?, ?, ?, ?, ?, ?, ?)
            """);

            ps.setString(1, id);
            ps.setString(2, request.getLocation());
            ps.setBoolean(3, request.getFederationApproval());
            ps.setString(4, structure.getPresident().getId());
            ps.setString(5, structure.getVicePresident().getId());
            ps.setString(6, structure.getTreasurer().getId());
            ps.setString(7, structure.getSecretary().getId());

            ps.executeUpdate();

            PreparedStatement updateMember = conn.prepareStatement(
                    "UPDATE member SET collectivity_id = ? WHERE id = ?"
            );

            for (Member member : members) {
                updateMember.setString(1, id);
                updateMember.setString(2, member.getId());
                updateMember.addBatch();
            }

            updateMember.executeBatch();

            conn.commit();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        Collectivity collectivity = new Collectivity();
        collectivity.setId(id);
        collectivity.setLocation(request.getLocation());
        collectivity.setStructure(structure);
        collectivity.setMembers(members);

        return collectivity;
    }
}