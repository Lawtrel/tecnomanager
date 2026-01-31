package br.lawtrel.tecnomanager.repository;
import br.lawtrel.tecnomanager.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long>{
    long countByStatus(String status);
    long countByStatusIn(List<String> status);
}
