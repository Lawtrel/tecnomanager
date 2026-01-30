package br.lawtrel.tecnomanager;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// 1. @RestController avisa ao Spring: "Isso aqui recebe requisições Web"
@RestController
// 2. @RequestMapping define o endereço base. Igual definir rota no Express.
@RequestMapping("/api")
public class HelloController {

    // 3. @GetMapping diz: "Quando chamarem GET /api/ola, execute isso"
    @GetMapping("/ola")
    public String saudacao() {
        return "Olá, Tecno System! O sistema está no ar.";
    }
}