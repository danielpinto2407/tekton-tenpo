package com.tekton.tenpo.infrastructure.adapters.out.external.dto;

public record RandomResponse (  
    String status, 
    Integer min, 
    Integer max,
    Double random) {}
