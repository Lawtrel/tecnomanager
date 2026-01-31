package br.lawtrel.tecnomanager.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("TecnoManager API")
                        .version("1.0")
                        .description("API para gestão de projetos e membros"))
                .servers(List.of(
                        // 1. URL de Produção (HTTPS)
                        new Server().url("https://tecnomanager-production.up.railway.app").description("Servidor de Produção"),
                        // 2. URL Local
                        new Server().url("http://localhost:8080").description("Ambiente Local")
                ));
    }
}