package org.lubrimax.msvc_acopio_temporal.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class ManejadorGlobalDeErrores {
    @ExceptionHandler({IllegalArgumentException.class, IllegalStateException.class})
    public ResponseEntity<Map<String, Object>> manejar(RuntimeException exception, HttpServletRequest request) {
        return respuesta(HttpStatus.BAD_REQUEST, exception.getMessage(), request.getRequestURI());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> manejarValidacion(MethodArgumentNotValidException exception,
                                                                  HttpServletRequest request) {
        String mensaje = exception.getBindingResult().getFieldErrors().stream().findFirst()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .orElse("La solicitud contiene datos invalidos");
        return respuesta(HttpStatus.BAD_REQUEST, mensaje, request.getRequestURI());
    }

    private ResponseEntity<Map<String, Object>> respuesta(HttpStatus estado, String mensaje, String ruta) {
        Map<String, Object> cuerpo = new LinkedHashMap<>();
        cuerpo.put("timestamp", LocalDateTime.now());
        cuerpo.put("status", estado.value());
        cuerpo.put("error", estado.getReasonPhrase());
        cuerpo.put("message", mensaje);
        cuerpo.put("path", ruta);
        return ResponseEntity.status(estado).body(cuerpo);
    }
}
