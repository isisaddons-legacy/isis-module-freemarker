/*
 *  Copyright 2013~2014 Dan Haywood
 *
 *  Licensed under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */
package org.isisaddons.module.freemarker.dom.service;

import java.util.Map;

import com.google.common.collect.ImmutableMap;

import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import org.apache.isis.applib.services.config.ConfigurationService;
import org.apache.isis.core.unittestsupport.jmocking.JUnitRuleMockery2;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class FreeMarkerService_Joda_Test {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Rule
    public JUnitRuleMockery2 context = JUnitRuleMockery2.createFor(JUnitRuleMockery2.Mode.INTERFACES_AND_CLASSES);

    @JUnitRuleMockery2.Ignoring
    @Mock
    ConfigurationService mockConfigurationService;

    Map<String, Object> properties;

    FreeMarkerService service;

    @Before
    public void setUp() throws Exception {
        service = new FreeMarkerService();
        service.configurationService = mockConfigurationService;

        properties = ImmutableMap.<String,Object>of(
                "jodaDateTime", new org.joda.time.DateTime(2017,2,1,14,30),
                "jodaLocalDateTime", new org.joda.time.LocalDateTime(2017,2,1,14,30),
                "jodaLocalDate", new org.joda.time.LocalDate(2017,2,1),
                "jodaLocalTime", new org.joda.time.LocalTime(14,30,45)
        );
    }

    @Test
    public void joda_date_time() throws Exception {

        // given
        service.init(ImmutableMap.of(FreeMarkerService.JODA_SUPPORT_KEY, "true"));

        // when
        String merged = service.render(
                "WelcomeUserTemplate:/GBR:",
                "result: ${jodaDateTime}", properties);

        // then
        assertThat(merged, is("result: 01-Feb-2017 14:30:00"));
    }

    @Test
    public void joda_support_disabled() throws Exception {

        // given
        service.init(ImmutableMap.of(FreeMarkerService.JODA_SUPPORT_KEY, "false"));

        // when
        String merged = service.render(
                "WelcomeUserTemplate:/GBR:",
                "result: ${jodaDateTime}", properties);

        // then
        assertThat(merged, is("result: 2017-02-01T14:30:00.000Z"));
    }

    private void expectingConfigurationServiceGetPropertyJodaSupportReturns(final String result) {
        context.checking(new Expectations() {{
            allowing(mockConfigurationService).getProperty(with(FreeMarkerService.JODA_SUPPORT_KEY), with("true"));
            will(returnValue(result));
        }});
    }

    @Test
    public void joda_date_time_with_formats() throws Exception {

        // given
        service.init(ImmutableMap.of(FreeMarkerService.JODA_SUPPORT_KEY, "true"));


        // when
        String merged = service.render(
                "WelcomeUserTemplate:/GBR:",
                "result: ${jodaDateTime?date}", properties);

        // then
        assertThat(merged, is("result: 01-Feb-2017"));

        // when
        merged = service.render(
                "WelcomeUserTemplate:/GBR2:",
                "result: ${jodaDateTime?datetime}", properties);

        // then
        assertThat(merged, is("result: 01-Feb-2017 14:30:00"));

        // when
        merged = service.render(
                "WelcomeUserTemplate:/GBR3:",
                "result: ${jodaDateTime?time}", properties);

        // then
        assertThat(merged, is("result: 14:30:00"));
    }

    @Test
    public void joda_local_date_time() throws Exception {

        // given
        service.init(ImmutableMap.of(FreeMarkerService.JODA_SUPPORT_KEY, "true"));


        // when
        String merged = service.render(
                "WelcomeUserTemplate:/GBR:",
                "result: ${jodaLocalDateTime}", properties);

        // then
        assertThat(merged, is("result: 01-Feb-2017 14:30:00"));
    }

    @Test
    public void joda_local_date() throws Exception {

        // given
        service.init(ImmutableMap.of(FreeMarkerService.JODA_SUPPORT_KEY, "true"));

        // when
        String merged = service.render(
                "WelcomeUserTemplate:/GBR:",
                "result: ${jodaLocalDate}", properties);

        // then
        assertThat(merged, is("result: 01-Feb-2017"));
    }

    @Test
    public void joda_local_time() throws Exception {

        // given
        service.init(ImmutableMap.of(FreeMarkerService.JODA_SUPPORT_KEY, "true"));

        // when
        String merged = service.render(
                "WelcomeUserTemplate:/GBR:",
                "result: ${jodaLocalTime}", properties);

        // then
        assertThat(merged, is("result: 14:30:45"));
    }


}
