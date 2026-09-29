package co.edu.uptc.servicio_web_calculadora.dto;

import java.util.List;

public record PaginaResponseDTO(
                long totalRegistrosGenerales,
                int registrosEnEstaPagina,
                int paginaActual,
                int totalPaginas,
                String contenedorId,
                String contenedorNombre,
                String contenedorIp,
                List<PersonaDTO> datos) {
}
