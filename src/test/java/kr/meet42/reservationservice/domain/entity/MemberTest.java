package kr.meet42.reservationservice.domain.entity;

import kr.meet42.reservationservice.domain.repository.MemberRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
public class MemberTest {
    @Autowired
    MemberRepository memberRepository;

    @AfterEach
    public void cleanup() {
        memberRepository.deleteAll();
    }

    @Test
    public void 멤버_생성() {
        // given
        Member member = new Member();

        member.setMember_id(12345L);
        member.setIntra("taehkim");

        // when
        memberRepository.save(member);

        // then
        Member finded = memberRepository.findById(member.getMember_id()).get();
        assertThat(member.getIntra()).isEqualTo(finded.getIntra());
    }
}
