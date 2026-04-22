package com.exam.federation.repository;

import com.exam.federation.config.DataSource;
import com.exam.federation.dto.*;
import com.exam.federation.entity.AssignIdentificationRequest;
import com.exam.federation.entity.CreateCollectivityStructure;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class CollectivityRepository {

    private final DataSource dataSource;
    private final MemberRepository memberRepository;


    public CollectivityResponse save(CreateCollectivityRequest request) {
        String sql = """
            INSERT INTO collectivity (
                id, location, president_id, vice_president_id, treasurer_id, secretary_id
            ) VALUES (
                'col_' || REPLACE(gen_random_uuid()::TEXT, '-', ''),
                ?, ?, ?, ?, ?
            )
            RETURNING id, location
        """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            CreateCollectivityStructure structure = request.getStructure();

            ps.setString(1, request.getLocation());
            ps.setString(2, structure.getPresident());
            ps.setString(3, structure.getVicePresident());
            ps.setString(4, structure.getTreasurer());
            ps.setString(5, structure.getSecretary());

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String id = rs.getString("id");
                String location = rs.getString("location");


                List<MemberResponse> members = getMembersByIds(request.getMembers());

                CollectivityStructureResponse structureResponse = new CollectivityStructureResponse();
                structureResponse.setPresident(memberRepository.findById(structure.getPresident()));
                structureResponse.setVicePresident(memberRepository.findById(structure.getVicePresident()));
                structureResponse.setTreasurer(memberRepository.findById(structure.getTreasurer()));
                structureResponse.setSecretary(memberRepository.findById(structure.getSecretary()));

                CollectivityResponse response = new CollectivityResponse();
                response.setId(id);
                response.setNumber(null);
                response.setName(null);
                response.setLocation(location);
                response.setStructure(structureResponse);
                response.setMembers(members);

                insertCollectivityMembers(id, request.getMembers());

                return response;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    private void insertCollectivityMembers(String collectivityId, List<String> memberIds) {
        String sql = "INSERT INTO collectivity_members (collectivity_id, member_id) VALUES (?, ?)";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            for (String memberId : memberIds) {
                ps.setString(1, collectivityId);
                ps.setString(2, memberId);
                ps.addBatch();
            }
            ps.executeBatch();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public CollectivityResponse findById(String id) {
        String sql = """
            SELECT id, number, name, location, 
                   president_id, vice_president_id, treasurer_id, secretary_id
            FROM collectivity 
            WHERE id = ?
        """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String presidentId = rs.getString("president_id");
                String vicePresidentId = rs.getString("vice_president_id");
                String treasurerId = rs.getString("treasurer_id");
                String secretaryId = rs.getString("secretary_id");

                List<String> memberIds = getMemberIdsByCollectivityId(id);
                List<MemberResponse> members = getMembersByIds(memberIds);

                CollectivityStructureResponse structure = new CollectivityStructureResponse();
                structure.setPresident(memberRepository.findById(presidentId));
                structure.setVicePresident(memberRepository.findById(vicePresidentId));
                structure.setTreasurer(memberRepository.findById(treasurerId));
                structure.setSecretary(memberRepository.findById(secretaryId));

                CollectivityResponse response = new CollectivityResponse();
                response.setId(rs.getString("id"));
                response.setNumber(rs.getString("number"));
                response.setName(rs.getString("name"));
                response.setLocation(rs.getString("location"));
                response.setStructure(structure);
                response.setMembers(members);

                return response;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }


    public boolean existsByNumber(String number) {
        String sql = "SELECT COUNT(number) FROM collectivity WHERE number = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, number);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }


    public boolean existsByName(String name) {
        String sql = "SELECT COUNT(*) FROM collectivity WHERE name = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }


    public CollectivityResponse assignIdentification(String id, AssignIdentificationRequest request) {
        String sql = """
            UPDATE collectivity 
            SET number = ?, name = ? 
            WHERE id = ? 
              AND (number IS NULL OR number = '')
              AND (name IS NULL OR name = '')
            RETURNING id, number, name, location
        """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, request.getNumber());
            ps.setString(2, request.getName());
            ps.setString(3, id);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return findById(id);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }


    private List<MemberResponse> getMembersByIds(List<String> memberIds) {
        List<MemberResponse> members = new ArrayList<>();
        if (memberIds != null) {
            for (String memberId : memberIds) {
                MemberResponse member = memberRepository.findById(memberId);
                if (member != null) {
                    members.add(member);
                }
            }
        }
        return members;
    }


    private List<String> getMemberIdsByCollectivityId(String collectivityId) {
        String sql = "SELECT member_id FROM collectivity_members WHERE collectivity_id = ?";
        List<String> memberIds = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, collectivityId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                memberIds.add(rs.getString("member_id"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return memberIds;
    }
}