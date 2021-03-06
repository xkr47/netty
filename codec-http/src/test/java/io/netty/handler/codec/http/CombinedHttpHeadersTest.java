/*
 * Copyright 2015 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package io.netty.handler.codec.http;

import io.netty.handler.codec.DefaultHeaders;
import io.netty.handler.codec.http.HttpHeadersTestUtils.HeaderValue;
import org.junit.Test;

import java.util.Collections;

import static io.netty.util.AsciiString.contentEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CombinedHttpHeadersTest {
    private static final CharSequence HEADER_NAME = "testHeader";

    @Test
    public void addCharSequencesCsv() {
        final CombinedHttpHeaders headers = newCombinedHttpHeaders();
        headers.add(HEADER_NAME, HeaderValue.THREE.asList());
        assertCsvValues(headers, HeaderValue.THREE);
    }

    @Test
    public void addCharSequencesCsvWithExistingHeader() {
        final CombinedHttpHeaders headers = newCombinedHttpHeaders();
        headers.add(HEADER_NAME, HeaderValue.THREE.asList());
        headers.add(HEADER_NAME, HeaderValue.FIVE.subset(4));
        assertCsvValues(headers, HeaderValue.FIVE);
    }

    @Test
    public void addCombinedHeadersWhenEmpty() {
        final CombinedHttpHeaders headers = newCombinedHttpHeaders();
        final CombinedHttpHeaders otherHeaders = newCombinedHttpHeaders();
        otherHeaders.add(HEADER_NAME, "a");
        otherHeaders.add(HEADER_NAME, "b");
        headers.add(otherHeaders);
        assertEquals("a,b", headers.get(HEADER_NAME).toString());
    }

    @Test
    public void addCombinedHeadersWhenNotEmpty() {
        final CombinedHttpHeaders headers = newCombinedHttpHeaders();
        headers.add(HEADER_NAME, "a");
        final CombinedHttpHeaders otherHeaders = newCombinedHttpHeaders();
        otherHeaders.add(HEADER_NAME, "b");
        otherHeaders.add(HEADER_NAME, "c");
        headers.add(otherHeaders);
        assertEquals("a,b,c", headers.get(HEADER_NAME).toString());
    }

    @Test
    public void setCombinedHeadersWhenNotEmpty() {
        final CombinedHttpHeaders headers = newCombinedHttpHeaders();
        headers.add(HEADER_NAME, "a");
        final CombinedHttpHeaders otherHeaders = newCombinedHttpHeaders();
        otherHeaders.add(HEADER_NAME, "b");
        otherHeaders.add(HEADER_NAME, "c");
        headers.set(otherHeaders);
        assertEquals("b,c", headers.get(HEADER_NAME).toString());
    }

    @Test
    public void addUncombinedHeaders() {
        final CombinedHttpHeaders headers = newCombinedHttpHeaders();
        headers.add(HEADER_NAME, "a");
        final DefaultHttpHeaders otherHeaders = new DefaultHttpHeaders();
        otherHeaders.add(HEADER_NAME, "b");
        otherHeaders.add(HEADER_NAME, "c");
        headers.add(otherHeaders);
        assertEquals("a,b,c", headers.get(HEADER_NAME).toString());
    }

    @Test
    public void setUncombinedHeaders() {
        final CombinedHttpHeaders headers = newCombinedHttpHeaders();
        headers.add(HEADER_NAME, "a");
        final DefaultHttpHeaders otherHeaders = new DefaultHttpHeaders();
        otherHeaders.add(HEADER_NAME, "b");
        otherHeaders.add(HEADER_NAME, "c");
        headers.set(otherHeaders);
        assertEquals("b,c", headers.get(HEADER_NAME).toString());
    }

    @Test
    public void addCharSequencesCsvWithValueContainingComma() {
        final CombinedHttpHeaders headers = newCombinedHttpHeaders();
        headers.add(HEADER_NAME, HeaderValue.SIX_QUOTED.subset(4));
        assertTrue(contentEquals(HeaderValue.SIX_QUOTED.subsetAsCsvString(4), headers.get(HEADER_NAME)));
        assertTrue(contentEquals(HeaderValue.SIX_QUOTED.subsetAsCsvString(4), headers.getAll(HEADER_NAME).get(0)));
    }

    @Test
    public void addCharSequencesCsvWithValueContainingCommas() {
        final CombinedHttpHeaders headers = newCombinedHttpHeaders();
        headers.add(HEADER_NAME, HeaderValue.EIGHT.subset(6));
        assertTrue(contentEquals(HeaderValue.EIGHT.subsetAsCsvString(6), headers.get(HEADER_NAME)));
        assertTrue(contentEquals(HeaderValue.EIGHT.subsetAsCsvString(6), headers.getAll(HEADER_NAME).get(0)));
    }

    @Test (expected = NullPointerException.class)
    public void addCharSequencesCsvNullValue() {
        final CombinedHttpHeaders headers = newCombinedHttpHeaders();
        final String value = null;
        headers.add(HEADER_NAME, value);
    }

    @Test
    public void addCharSequencesCsvMultipleTimes() {
        final CombinedHttpHeaders headers = newCombinedHttpHeaders();
        for (int i = 0; i < 5; ++i) {
            headers.add(HEADER_NAME, "value");
        }
        assertTrue(contentEquals("value,value,value,value,value", headers.get(HEADER_NAME)));
    }

    @Test
    public void addCharSequenceCsv() {
        final CombinedHttpHeaders headers = newCombinedHttpHeaders();
        addValues(headers, HeaderValue.ONE, HeaderValue.TWO, HeaderValue.THREE);
        assertCsvValues(headers, HeaderValue.THREE);
    }

    @Test
    public void addCharSequenceCsvSingleValue() {
        final CombinedHttpHeaders headers = newCombinedHttpHeaders();
        addValues(headers, HeaderValue.ONE);
        assertCsvValue(headers, HeaderValue.ONE);
    }

    @Test
    public void addIterableCsv() {
        final CombinedHttpHeaders headers = newCombinedHttpHeaders();
        headers.add(HEADER_NAME, HeaderValue.THREE.asList());
        assertCsvValues(headers, HeaderValue.THREE);
    }

    @Test
    public void addIterableCsvWithExistingHeader() {
        final CombinedHttpHeaders headers = newCombinedHttpHeaders();
        headers.add(HEADER_NAME, HeaderValue.THREE.asList());
        headers.add(HEADER_NAME, HeaderValue.FIVE.subset(4));
        assertCsvValues(headers, HeaderValue.FIVE);
    }

    @Test
    public void addIterableCsvSingleValue() {
        final CombinedHttpHeaders headers = newCombinedHttpHeaders();
        headers.add(HEADER_NAME, HeaderValue.ONE.asList());
        assertCsvValue(headers, HeaderValue.ONE);
    }

    @Test
    public void addIterableCsvEmtpy() {
        final CombinedHttpHeaders headers = newCombinedHttpHeaders();
        headers.add(HEADER_NAME, Collections.<CharSequence>emptyList());
        assertTrue(contentEquals("", headers.getAll(HEADER_NAME).get(0)));
    }

    @Test
    public void addObjectCsv() {
        final CombinedHttpHeaders headers = newCombinedHttpHeaders();
        addObjectValues(headers, HeaderValue.ONE, HeaderValue.TWO, HeaderValue.THREE);
        assertCsvValues(headers, HeaderValue.THREE);
    }

    @Test
    public void addObjectsCsv() {
        final CombinedHttpHeaders headers = newCombinedHttpHeaders();
        headers.add(HEADER_NAME, HeaderValue.THREE.asList());
        assertCsvValues(headers, HeaderValue.THREE);
    }

    @Test
    public void addObjectsIterableCsv() {
        final CombinedHttpHeaders headers = newCombinedHttpHeaders();
        headers.add(HEADER_NAME, HeaderValue.THREE.asList());
        assertCsvValues(headers, HeaderValue.THREE);
    }

    @Test
    public void addObjectsCsvWithExistingHeader() {
        final CombinedHttpHeaders headers = newCombinedHttpHeaders();
        headers.add(HEADER_NAME, HeaderValue.THREE.asList());
        headers.add(HEADER_NAME, HeaderValue.FIVE.subset(4));
        assertCsvValues(headers, HeaderValue.FIVE);
    }

    @Test
    public void setCharSequenceCsv() {
        final CombinedHttpHeaders headers = newCombinedHttpHeaders();
        headers.set(HEADER_NAME, HeaderValue.THREE.asList());
        assertCsvValues(headers, HeaderValue.THREE);
    }

    @Test
    public void setIterableCsv() {
        final CombinedHttpHeaders headers = newCombinedHttpHeaders();
        headers.set(HEADER_NAME, HeaderValue.THREE.asList());
        assertCsvValues(headers, HeaderValue.THREE);
    }

    @Test
    public void setObjectObjectsCsv() {
        final CombinedHttpHeaders headers = newCombinedHttpHeaders();
        headers.set(HEADER_NAME, HeaderValue.THREE.asList());
        assertCsvValues(headers, HeaderValue.THREE);
    }

    @Test
    public void setObjectIterableCsv() {
        final CombinedHttpHeaders headers = newCombinedHttpHeaders();
        headers.set(HEADER_NAME, HeaderValue.THREE.asList());
        assertCsvValues(headers, HeaderValue.THREE);
    }

    private static CombinedHttpHeaders newCombinedHttpHeaders() {
        return new CombinedHttpHeaders(true);
    }

    private static void assertCsvValues(final CombinedHttpHeaders headers, final HeaderValue headerValue) {
        assertTrue(contentEquals(headerValue.asCsv(), headers.get(HEADER_NAME)));
        assertTrue(contentEquals(headerValue.asCsv(), headers.getAll(HEADER_NAME).get(0)));
    }

    private static void assertCsvValue(final CombinedHttpHeaders headers, final HeaderValue headerValue) {
        assertTrue(contentEquals(headerValue.toString(), headers.get(HEADER_NAME)));
        assertTrue(contentEquals(headerValue.toString(), headers.getAll(HEADER_NAME).get(0)));
    }

    private static void addValues(final CombinedHttpHeaders headers, HeaderValue... headerValues) {
        for (HeaderValue v: headerValues) {
            headers.add(HEADER_NAME, v.toString());
        }
    }

    private static void addObjectValues(final CombinedHttpHeaders headers, HeaderValue... headerValues) {
        for (HeaderValue v: headerValues) {
            headers.add(HEADER_NAME, v.toString());
        }
    }
}
