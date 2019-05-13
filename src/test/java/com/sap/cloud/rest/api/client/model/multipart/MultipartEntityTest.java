package com.sap.cloud.rest.api.client.model.multipart;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.Test;

import com.sap.cloud.rest.api.client.model.multipart.EntityPart;
import com.sap.cloud.rest.api.client.model.multipart.MultipartEntity;

import nl.jqno.equalsverifier.EqualsVerifier;

public class MultipartEntityTest {

    private static final String TEST_NAME = "test_name";
    private static final String TEST_ENTITY = "test_entity";

    private static final EntityPart<String> ENTITY_PART = new EntityPart<String>(TEST_NAME, TEST_ENTITY);

    @Test
    public void testCreateMultipartEntity() {
        ArrayList<EntityPart<String>> parts = new ArrayList<>();
        parts.add(new EntityPart<String>(TEST_NAME, TEST_ENTITY));
        MultipartEntity<String> multipartEntity = new MultipartEntity<>(parts);

        assertEquals(1, multipartEntity.getParts().size());
        assertEquals(ENTITY_PART, multipartEntity.getPartsByName(TEST_NAME).get(0));
    }

    @Test
    public void testCreateMultipartEntityEmptyList() {
        MultipartEntity<String> multipartEntity = new MultipartEntity<>(new ArrayList<>());

        assertTrue(multipartEntity.getParts().isEmpty());
    }

    @Test
    public void testMultipartEntityToString() {
        ArrayList<EntityPart<String>> parts = new ArrayList<>();
        parts.add(new EntityPart<String>(TEST_NAME, TEST_ENTITY));
        MultipartEntity<String> multipartEntity = new MultipartEntity<>(parts);

        assertEquals("{\"multipartEntity\":[{\"name\":\"" + TEST_NAME + "\",\"entity\":\"" + TEST_ENTITY + "\"}]}",
                multipartEntity.toString());
    }

    @Test
    public void testEqualsAndHashcode() {
        EqualsVerifier.forClass(MultipartEntity.class).usingGetClass().verify();
    }
}
