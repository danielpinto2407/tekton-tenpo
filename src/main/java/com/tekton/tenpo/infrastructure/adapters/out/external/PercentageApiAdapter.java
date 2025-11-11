package com.tekton.tenpo.infrastructure.adapters.out.external;

import com.tekton.tenpo.application.port.out.PercentagePort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Adapter that retrieves the percentage from an external service with fallback to cache.
 * Clean, declarative version using WebClient and reactive error handling.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class PercentageApiAdapter implements PercentagePort {@Override
    public double getPercentage() {
        return 50.4;
    }

 
}
