package com.exam.federation.repository;

import com.exam.federation.config.DataSource;
import com.exam.federation.entity.Member;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@AllArgsConstructor
public class MemberRepository {
    private final DataSource dataSource;

    public List<Member> createMembers(List<Member> members) {

    }
}
