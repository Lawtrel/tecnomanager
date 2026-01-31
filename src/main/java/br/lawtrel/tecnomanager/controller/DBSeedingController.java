package br.lawtrel.tecnomanager.controller;

import br.lawtrel.tecnomanager.service.DBSeedingService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/seed")
//@Profile("!prod") // Segurança: Não deixa rodar se o profile for 'prod' (opcional)
public class DBSeedingController {

    private final DBSeedingService seedingService;

    public DBSeedingController(DBSeedingService seedingService) {
        this.seedingService = seedingService;
    }

    @Operation(summary = "Popula o banco de dados com dados fictícios (CUIDADO: APAGA TUDO ANTES)")
    @PostMapping
    public ResponseEntity<String> seed() {
        seedingService.popularBanco();
        return ResponseEntity.ok("Banco de dados populado com sucesso!");
    }
}