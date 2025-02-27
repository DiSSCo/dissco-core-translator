package eu.dissco.core.translator.service;

import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class CacheEvictionService {

  private final CacheManager cacheManager;

  @Scheduled(fixedRateString = "43200000")
  public void evictAllCaches() {
    log.info("Clearing caches");
    cacheManager.getCacheNames()
        .forEach(cacheName -> Objects.requireNonNull(cacheManager.getCache(cacheName)).clear());
  }

}
