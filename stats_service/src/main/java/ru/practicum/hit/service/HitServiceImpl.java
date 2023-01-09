package ru.practicum.hit.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.hit.mapper.HitMapper;
import ru.practicum.hit.model.EndpointHit;
import ru.practicum.hit.model.ViewStats;
import ru.practicum.hit.repository.HitRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static ru.practicum.utility.ConstantStats.DATE_TIME_FORMAT;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class HitServiceImpl implements HitService {
    private final HitRepository hitRepository;

    @Transactional
    @Override
    public void saveHit(EndpointHit endpointHit) {
        hitRepository.save(HitMapper.toHit(endpointHit));
    }

    @Override
    public List<ViewStats> getStats(String start, String end, List<String> uris, Boolean uniq) {
        LocalDateTime startDate = LocalDateTime.parse(start, DATE_TIME_FORMAT);
        LocalDateTime endDate = LocalDateTime.parse(end, DATE_TIME_FORMAT);
        List<ViewStats> hits;
        if (Boolean.TRUE.equals(uniq)) {
            hits = hitRepository.findDistinct(startDate, endDate)
                    .stream()
                    .peek(viewStats -> viewStats.setHits(Long.valueOf(countViewsByUri(viewStats.getUri()))))
                    .collect(Collectors.toList());
        } else {
            hits = hitRepository.findAllByTimestampBetween(startDate, endDate)
                    .stream()
                    .map(HitMapper::toViewStats)
                    .peek(viewStats -> viewStats.setHits(Long.valueOf(countViewsByUri(viewStats.getUri()))))
                    .collect(Collectors.toList());
        }
        return uris == null ? hits : hits.stream()
                .map(viewStats -> filterByUris(viewStats, uris))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private Integer countViewsByUri(String uri) {
        return hitRepository.countByUri(uri);
    }

    private ViewStats filterByUris(ViewStats v, List<String> uris) {
        if (uris.contains(v.getUri())) {
            return v;
        } else {
            return null;
        }
    }
}
