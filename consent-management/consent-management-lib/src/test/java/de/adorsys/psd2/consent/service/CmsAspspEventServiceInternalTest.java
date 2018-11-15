/*
 * Copyright 2018-2018 adorsys GmbH & Co KG
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.adorsys.psd2.consent.service;

import de.adorsys.psd2.consent.api.event.CmsEvent;
import de.adorsys.psd2.consent.domain.event.EventEntity;
import de.adorsys.psd2.consent.repository.EventRepository;
import de.adorsys.psd2.consent.service.mapper.EventMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CmsAspspEventServiceInternalTest {
    @InjectMocks
    private CmsAspspEventServiceInternal cmsAspspEventServiceInternal;

    @Mock
    private EventRepository eventRepository;
    @Mock
    private EventMapper eventMapper;

    @Test
    public void getEventsForPeriod() {
        OffsetDateTime start = OffsetDateTime.parse("2018-11-01T00:00:00Z");
        OffsetDateTime between = OffsetDateTime.parse("2018-11-10T00:00:00Z");
        OffsetDateTime end = OffsetDateTime.parse("2018-12-01T00:00:00Z");

        when(eventMapper.mapToCmsEventList(any()))
            .thenReturn(Collections.singletonList(buildCmsEvent(between)));
        when(eventRepository.findByDateTimeBetween(start, end))
            .thenReturn(Collections.singletonList(buildEventEntity(between)));

        // Given
        CmsEvent expected = buildCmsEvent(between);

        // When
        List<CmsEvent> events = cmsAspspEventServiceInternal.getEventsForPeriod(start, end);

        // Then
        assertThat(events.isEmpty()).isFalse();
        assertThat(events.get(0)).isEqualTo(expected);
    }

    private CmsEvent buildCmsEvent(OffsetDateTime dateTime) {
        CmsEvent event = new CmsEvent();
        event.setDateTime(dateTime);
        return event;
    }

    private EventEntity buildEventEntity(OffsetDateTime dateTime) {
        EventEntity eventEntity = new EventEntity();
        eventEntity.setDateTime(dateTime);
        return eventEntity;
    }
}