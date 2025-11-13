package com.tekton.tenpo.infrastructure.adapters.out.persistence.mapper;

import com.tekton.tenpo.domain.model.CallHistory;
import com.tekton.tenpo.infrastructure.adapters.out.persistence.CallHistoryEntity;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CallHistoryMapper {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "timestamp", source = "timestamp")
    @Mapping(target = "endpoint", source = "endpoint")
    @Mapping(target = "parameters", source = "parameters")
    @Mapping(target = "response", source = "response")
    @Mapping(target = "statusCode", source = "statusCode")
    CallHistory toDomain(CallHistoryEntity entity);

    @InheritInverseConfiguration
    CallHistoryEntity toEntity(CallHistory domain);
}
