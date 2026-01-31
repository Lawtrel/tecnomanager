package br.lawtrel.tecnomanager.repository;

import br.lawtrel.tecnomanager.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long>{
    @Query("SELECT COUNT(m) FROM Member m WHERE m.id NOT IN (SELECT t.member.id FROM Task t WHERE t.member IS NOT NULL)")
    long countMembrosSemTarefas();
}
