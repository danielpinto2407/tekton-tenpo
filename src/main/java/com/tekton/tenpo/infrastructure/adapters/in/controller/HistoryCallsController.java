package com.tekton.tenpo.infrastructure.adapters.in.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.tekton.tenpo.application.port.in.GetAllHistoryCallsPort;
import com.tekton.tenpo.application.usecase.GetAllHistoryCallsUseCase;
import com.tekton.tenpo.infrastructure.adapters.in.controller.dto.CallHistoryDto;

import lombok.RequiredArgsConstructor;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/history")
@RequiredArgsConstructor
@Tag(name = "Historial de Llamadas", description = "Operaciones para consultar y registrar el historial de llamadas")
public class HistoryCallsController {

    private final GetAllHistoryCallsPort getCallHistoryUseCase;

    @GetMapping
    @Operation(
        summary = "Obtener historial de llamadas",
        description = "Devuelve una lista paginada del historial de llamadas realizadas a los endpoints"
    )
    public ResponseEntity<Page<CallHistoryDto>> getHistory(
            @Parameter(description = "Número de página, inicia en 0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Tamaño de la página") @RequestParam(defaultValue = "10") int size) {
        var pageable = PageRequest.of(page, size);
        var historyPage = getCallHistoryUseCase.getHistory(pageable)
                .map(CallHistoryDto::fromEntity);
        return ResponseEntity.ok(historyPage);
    }
}
