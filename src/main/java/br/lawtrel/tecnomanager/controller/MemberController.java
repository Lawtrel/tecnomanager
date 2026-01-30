package br.lawtrel.tecnomanager.controller;

import br.lawtrel.tecnomanager.model.Member;
import br.lawtrel.tecnomanager.repository.MemberRepository;
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
    public Member Cadastrar(@RequestBody Member member) {
        //@RequestBody pega o JSON que você enviar e transforma num objeto Java automaticamente
        return repository.save(member);
    }
}
