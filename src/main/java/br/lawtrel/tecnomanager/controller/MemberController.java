package br.lawtrel.tecnomanager.controller;

import br.lawtrel.tecnomanager.model.Member;
import br.lawtrel.tecnomanager.dto.MemberDTO;
import br.lawtrel.tecnomanager.repository.MemberRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/membros")
public class MemberController {
    @Autowired
    private MemberRepository repository;

    //GET /membros
    @GetMapping
    public List<Member> listarTodos() {
        return  repository.findAll();
    }

    //POST /membros
    @PostMapping
    public Member cadastrar(@RequestBody @Valid MemberDTO dados) {
        Member member = new Member();
        member.setNome(dados.nome());
        member.setCargo(dados.cargo());
        member.setEmail(dados.email());

        return repository.save(member);
    }
}
