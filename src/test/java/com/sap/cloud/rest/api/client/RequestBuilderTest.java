package com.sap.cloud.rest.api.client;

import static com.sap.cloud.rest.api.client.RequestBuilder.*;
import static java.text.MessageFormat.format;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sap.cloud.rest.api.client.RequestBuilder;
import com.sap.cloud.rest.api.client.exceptions.RequestBuilderException;
import com.sap.cloud.rest.api.client.model.Request;
import com.sap.cloud.rest.api.client.model.multipart.MultipartEntity;
import com.sap.cloud.rest.api.client.utils.ValidateArgument;

public class RequestBuilderTest {

    private static final String HTTP_GET = "GET";
    private static final String HTTP_POST = "POST";
    private static final String HTTP_PUT = "PUT";
    private static final String HTTP_DELETE = "DELETE";
    private static final String HTTP_PATCH = "PATCH";

    private static final String TEST_VALUE_ONE = "value1";
    private static final String TEST_VALUE_TWO = "value2";

    private static final String VALID_URL = "https://example.com/";
    private static final String INVALID_URL = "https://example^.com/";

    private static final Header TEST_HEADER_ONE = new BasicHeader("header1", "value1");
    private static final Header TEST_HEADER_TWO = new BasicHeader("header2", "value2");

    private static final String PARAM_ONE_NAME = "param1";
    private static final String PARAM_ONE_VALUE = "value1";
    private static final String PARAM_TWO_NAME = "param2";
    private static final String PARAM_TWO_VALUE = "value2";
    
    private static final String FIRST_PART_NAME = "first_part_name";
    private static final String SECOND_PART_NAME = "second_part_name";

    @Rule
    public ExpectedException expected = ExpectedException.none();

    private class TestEntity {

        public String field;

        public TestEntity(String field) {
            this.field = field;
        }
    }

    @Test
    public void buildGetRequestTest() {
        Request<String> request = getRequest().uri(VALID_URL).build();

        assertEquals(HTTP_GET, request.getHttpRequest().getMethod());
    }

    @Test
    public void buildPostRequestTest() {
        Request<String> request = postRequest().uri(VALID_URL).build();

        assertEquals(HTTP_POST, request.getHttpRequest().getMethod());
    }

    @Test
    public void buildPutRequestTest() {
        Request<String> request = putRequest().uri(VALID_URL).build();

        assertEquals(HTTP_PUT, request.getHttpRequest().getMethod());
    }

    @Test
    public void buildPatchRequestTest() {
        Request<String> request = patchRequest().uri(VALID_URL).build();

        assertEquals(HTTP_PATCH, request.getHttpRequest().getMethod());
    }

    @Test
    public void buildDeleteRequestTest() {
        Request<String> request = deleteRequest().uri(VALID_URL).build();

        assertEquals(HTTP_DELETE, request.getHttpRequest().getMethod());
    }

    @Test
    public void buildTestEntityPostRequestTest() {
        Request<TestEntity> request = postRequest(TestEntity.class).uri(VALID_URL).build();

        assertEquals(HTTP_POST, request.getHttpRequest().getMethod());
    }

    @Test
    public void buildTestEntityPutRequestTest() {
        Request<TestEntity> request = putRequest(TestEntity.class).uri(VALID_URL).build();

        assertEquals(HTTP_PUT, request.getHttpRequest().getMethod());
    }

    @Test
    public void buildTestEntityPatchRequestTest() {
        Request<TestEntity> request = patchRequest(TestEntity.class).uri(VALID_URL).build();

        assertEquals(HTTP_PATCH, request.getHttpRequest().getMethod());
    }

    @Test
    public void buildRequestWithUriSetTest() {
        Request<String> request = getRequest().uri(VALID_URL).build();

        assertEquals(VALID_URL, request.getHttpRequest().getURI().toString());
    }

    @Test
    public void buildRequestWithNoUriSetTest() {
        expected.expect(RequestBuilderException.class);
        expected.expectMessage(FAILED_TO_BUILD_REQUEST_OBJECT_MSG + URI_IS_NOT_SET_MSG);

        getRequest().build();
    }

    @Test
    public void buildRequestWithUriObjectTest() throws URISyntaxException {
        Request<String> request = getRequest().uri(new URI(VALID_URL)).build();

        assertEquals(VALID_URL, request.getHttpRequest().getURI().toString());
    }

    @Test
    public void buildRequestWithInvalidUriTest() {
        expected.expect(RequestBuilderException.class);
        expected.expectMessage(format(FAILED_TO_SET_URI_X_MSG, INVALID_URL));

        getRequest().uri(INVALID_URL).build();
    }

    @Test
    public void buildStringRequestEntityIsDeserializedCorrectlyTest() throws ParseException, IOException {
        String stringEntity = "entity";
        Request<String> request = postRequest().uri(VALID_URL).entity(stringEntity).build();

        HttpEntityEnclosingRequest httpRequest = (HttpEntityEnclosingRequest) request.getHttpRequest();
        String deserializedEntity = EntityUtils.toString(httpRequest.getEntity());

        assertEquals(stringEntity, deserializedEntity);
    }

    @Test
    public void buildTestEntityRequestEntityIsDeserializedCorrectlyTest() throws ParseException, IOException {
        TestEntity testEntity = new TestEntity(TEST_VALUE_ONE);
        Request<TestEntity> request = postRequest(TestEntity.class).uri(VALID_URL).entity(testEntity).build();

        HttpEntityEnclosingRequest httpRequest = (HttpEntityEnclosingRequest) request.getHttpRequest();
        String deserializedEntity = EntityUtils.toString(httpRequest.getEntity());

        assertEquals(new ObjectMapper().writeValueAsString(testEntity), deserializedEntity);
    }

    @Test
    public void buildTestEntityRequestWithEntitySetTest() {
        TestEntity entity = new TestEntity(TEST_VALUE_ONE);
        Request<TestEntity> request = postRequest(TestEntity.class).uri(VALID_URL).entity(entity).build();

        assertTrue(request.getHttpRequest() instanceof HttpEntityEnclosingRequest);
        assertEquals(entity, request.getEntity());
        assertEquals(TEST_VALUE_ONE, entity.field);
    }

    @Test
    public void buildTestEntityRequestWithNoEntitySetTest() {
        Request<TestEntity> request = postRequest(TestEntity.class).uri(VALID_URL).build();

        assertFalse(request.getHttpRequest() instanceof HttpEntityEnclosingRequest);
        assertEquals(null, request.getEntity());
    }

    @Test
    public void buildTestEntityRequestEntityIsNullTest() {
        expected.expect(IllegalArgumentException.class);
        expected.expectMessage(RequestBuilder.ENTITY_DISPLAY_NAME + ValidateArgument.CANNOT_BE_NULL_MSG);
        
        Request<TestEntity> request = postRequest(TestEntity.class).uri(VALID_URL).entity(null).build();

        assertFalse(request.getHttpRequest() instanceof HttpEntityEnclosingRequest);
        assertEquals(null, request.getEntity());
    }

    @Test
    public void buildRequestWithHeaderObjectTest() {
        Request<String> request = getRequest().uri(VALID_URL).addHeader(TEST_HEADER_ONE).build();

        assertEquals(TEST_HEADER_ONE, findHeader(request.getHeaders(), TEST_HEADER_ONE.getName()));
    }

    @Test
    public void buildRequestWithHeaderNameAndValueTest() {
        Request<String> request = getRequest()
                .uri(VALID_URL)
                .addHeader(TEST_HEADER_ONE.getName(), TEST_HEADER_ONE.getValue())
                .build();

        Header foundHeader = findHeader(request.getHeaders(), TEST_HEADER_ONE.getName());
        assertEquals(TEST_HEADER_ONE.getName(), foundHeader.getName());
        assertEquals(TEST_HEADER_ONE.getValue(), foundHeader.getValue());
    }

    @Test
    public void buildRequestWithHeaderObjectsListTest() {
        List<Header> headerList = new ArrayList<>();
        headerList.add(TEST_HEADER_ONE);
        headerList.add(TEST_HEADER_TWO);

        Request<String> request = getRequest().uri(VALID_URL).addHeaders(headerList).build();

        assertEquals(TEST_HEADER_ONE, findHeader(request.getHeaders(), TEST_HEADER_ONE.getName()));
        assertEquals(TEST_HEADER_TWO, findHeader(request.getHeaders(), TEST_HEADER_TWO.getName()));
    }

    @Test
    public void buildRequestWithHeaderObjectsArrayTest() {
        Header[] headerArray = new Header[] { TEST_HEADER_ONE, TEST_HEADER_TWO };

        Request<String> request = getRequest().uri(VALID_URL).addHeaders(headerArray).build();
        assertEquals(TEST_HEADER_ONE, findHeader(request.getHeaders(), TEST_HEADER_ONE.getName()));
        assertEquals(TEST_HEADER_TWO, findHeader(request.getHeaders(), TEST_HEADER_TWO.getName()));
    }

    @Test
    public void buildRequestWithParametersSetTest() {
        Request<String> request = getRequest().uri(VALID_URL).addParameter(PARAM_ONE_NAME, PARAM_ONE_VALUE).build();

        assertTrue(request.getHttpRequest().getURI().toString().contains(PARAM_ONE_NAME + "=" + PARAM_ONE_VALUE));
    }

    @Test
    public void buildRequestWithNoParametersSetTest() {
        Request<String> request = getRequest().uri(VALID_URL).build();

        assertFalse(request.getHttpRequest().getURI().toString().contains("?"));
    }

    @Test
    public void buildRequestWithInvalidParameterTest() {
        String invalidParamName = "invalid name";
        String exceptionMessage = VALID_URL + "?" + invalidParamName + "=" + PARAM_ONE_VALUE;

        expected.expect(RequestBuilderException.class);
        expected.expectMessage(format(FAILED_TO_SET_URI_X_MSG, exceptionMessage));

        getRequest().uri(VALID_URL).addParameter(invalidParamName, PARAM_ONE_VALUE).build();
    }

    @Test
    public void buildRequestWithNVPParametersTest() {
        List<NameValuePair> nvps = new ArrayList<>();
        nvps.add(new BasicNameValuePair(PARAM_ONE_NAME, PARAM_ONE_VALUE));
        nvps.add(new BasicNameValuePair(PARAM_TWO_NAME, PARAM_TWO_VALUE));

        Request<String> request = getRequest().uri(VALID_URL).addParameters(nvps).build();

        assertTrue(request.getHttpRequest().getURI().toString().contains(PARAM_ONE_NAME + "=" + PARAM_ONE_VALUE));
        assertTrue(request.getHttpRequest().getURI().toString().contains(PARAM_TWO_NAME + "=" + PARAM_TWO_VALUE));
    }

    @Test
    public void buildTestEntityMultipartRequestUnparsableEntityTest() {
        TestEntity entity = new TestEntity(null);

        expected.expect(RequestBuilderException.class);
        expected.expectMessage(format(FAILED_TO_PARSE_ENTITY_TO_JSON_MSG, entity));

        postRequest(TestEntity.class).uri(VALID_URL).multipartEntity(FIRST_PART_NAME, entity).buildMultipart();
    }

    @Test
    public void buildMultipartRequestWithUriSetTest() {
        Request<MultipartEntity<String>> request = getRequest().uri(VALID_URL).buildMultipart();

        assertEquals(VALID_URL, request.getHttpRequest().getURI().toString());
    }

    @Test
    public void buildMultipartRequestWithNoUriSetTest() {
        expected.expect(RequestBuilderException.class);
        expected.expectMessage(FAILED_TO_BUILD_REQUEST_OBJECT_MSG + URI_IS_NOT_SET_MSG);

        postRequest(TestEntity.class).buildMultipart();
    }

    @Test
    public void buildMultipartRequestWithParametersSetTest() {
        Request<MultipartEntity<TestEntity>> request = postRequest(TestEntity.class)
                .uri(VALID_URL)
                .addParameter(PARAM_ONE_NAME, PARAM_ONE_VALUE)
                .buildMultipart();

        assertTrue(request.getHttpRequest().getURI().toString().contains(PARAM_ONE_NAME + "=" + PARAM_ONE_VALUE));
    }

    @Test
    public void buildMultipartRequestWithNoParametersSetTest() {
        Request<MultipartEntity<TestEntity>> request = postRequest(TestEntity.class).uri(VALID_URL).buildMultipart();

        assertFalse(request.getHttpRequest().getURI().toString().contains("?"));
    }

    @Test
    public void buildTestEntityMultipartRequestWithSinglePartSetTest() {
        TestEntity entity = new TestEntity(TEST_VALUE_ONE);
        
        Request<MultipartEntity<TestEntity>> request = postRequest(TestEntity.class)
                .uri(VALID_URL)
                .multipartEntity(FIRST_PART_NAME, entity)
                .buildMultipart();

        assertTrue(request.getHttpRequest() instanceof HttpEntityEnclosingRequest);
        assertEquals(1, request.getEntity().getParts().size());
        assertEquals(entity, request.getEntity().getPartsByName(FIRST_PART_NAME).get(0).getEntity());
        assertEquals(FIRST_PART_NAME, request.getEntity().getPartsByName(FIRST_PART_NAME).get(0).getName());
    }

    @Test
    public void buildTestEntityMultipartRequestWithMultiplePartsSetTest() {
        TestEntity entity1 = new TestEntity(TEST_VALUE_ONE);
        TestEntity entity2 = new TestEntity(TEST_VALUE_TWO);

        Request<MultipartEntity<TestEntity>> request = postRequest(TestEntity.class)
                .uri(VALID_URL)
                .multipartEntity(FIRST_PART_NAME, entity1)
                .multipartEntity(SECOND_PART_NAME, entity2)
                .buildMultipart();

        assertTrue(request.getHttpRequest() instanceof HttpEntityEnclosingRequest);
        assertEquals(2, request.getEntity().getParts().size());
        assertEquals(entity1, request.getEntity().getPartsByName(FIRST_PART_NAME).get(0).getEntity());
        assertEquals(FIRST_PART_NAME, request.getEntity().getPartsByName(FIRST_PART_NAME).get(0).getName());
        assertEquals(entity2, request.getEntity().getPartsByName(SECOND_PART_NAME).get(0).getEntity());
        assertEquals(SECOND_PART_NAME, request.getEntity().getPartsByName(SECOND_PART_NAME).get(0).getName());
    }
    
    @Test
    public void buildStringMultipartRequestSetTest() {
        Request<MultipartEntity<String>> request = postRequest()
                .uri(VALID_URL)
                .multipartEntity(FIRST_PART_NAME, TEST_VALUE_ONE)
                .buildMultipart();

        assertTrue(request.getHttpRequest() instanceof HttpEntityEnclosingRequest);
        assertEquals(1, request.getEntity().getParts().size());
        assertEquals(TEST_VALUE_ONE, request.getEntity().getPartsByName(FIRST_PART_NAME).get(0).getEntity());
        assertEquals(FIRST_PART_NAME, request.getEntity().getPartsByName(FIRST_PART_NAME).get(0).getName());
    }

    @Test
    public void buildTestEntityMultipartRequestEntityIsNullTest() {
        expected.expect(IllegalArgumentException.class);
        expected.expectMessage(RequestBuilder.ENTITY_DISPLAY_NAME + ValidateArgument.CANNOT_BE_NULL_MSG);
        
        Request<MultipartEntity<TestEntity>> request = postRequest(TestEntity.class)
                .uri(VALID_URL)
                .multipartEntity(FIRST_PART_NAME, null)
                .buildMultipart();

        assertFalse(request.getHttpRequest() instanceof HttpEntityEnclosingRequest);
        assertEquals(null, request.getEntity());
    }

    @Test
    public void buildTestEntityMultipartRequestEntityPartNameIsNullTest() {
        expected.expect(IllegalArgumentException.class);
        expected.expectMessage(RequestBuilder.NAME_DISPLAY_NAME + ValidateArgument.CANNOT_BE_BLANK_MSG);
        
        TestEntity entity = new TestEntity(TEST_VALUE_ONE);

        Request<MultipartEntity<TestEntity>> request = postRequest(TestEntity.class)
                .uri(VALID_URL)
                .multipartEntity(null, entity)
                .buildMultipart();

        assertFalse(request.getHttpRequest() instanceof HttpEntityEnclosingRequest);
        assertEquals(null, request.getEntity());
    }

    @Test
    public void buildTestEntityMultipartRequestEntityPartNameIsEmptyTest() {
        expected.expect(IllegalArgumentException.class);
        expected.expectMessage(RequestBuilder.NAME_DISPLAY_NAME + ValidateArgument.CANNOT_BE_BLANK_MSG);

        TestEntity entity = new TestEntity(TEST_VALUE_ONE);

        Request<MultipartEntity<TestEntity>> request = postRequest(TestEntity.class)
                .uri(VALID_URL)
                .multipartEntity("", entity)
                .buildMultipart();

        assertFalse(request.getHttpRequest() instanceof HttpEntityEnclosingRequest);
        assertEquals(null, request.getEntity());
    }

    @Test
    public void buildTestEntityMultipartRequestWithNoEntitySetTest() {
        Request<MultipartEntity<TestEntity>> request = postRequest(TestEntity.class)
                .uri(VALID_URL)
                .buildMultipart();

        assertFalse(request.getHttpRequest() instanceof HttpEntityEnclosingRequest);
        assertTrue(request.getEntity().getParts().isEmpty());
    }

    private Header findHeader(Header[] headers, String name) {
        for (Header header : headers) {
            if (header.getName().equals(name)) {
                return header;
            }
        }
        return null;
    }

}