package com.sap.cloud.rest.api.client.model.multipart;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.sap.cloud.rest.api.client.model.multipart.EntityPart;

import nl.jqno.equalsverifier.EqualsVerifier;

public class EntityPartTest {

    private static final String TEST_NAME = "test_name";
    private static final String TEST_ENTITY = "test_entity";

    @Test
    public void testCreateEntityPart() {
        EntityPart<String> entityPart = new EntityPart<String>(TEST_NAME, TEST_ENTITY);
        
        assertEquals(TEST_NAME, entityPart.getName());
        assertEquals(TEST_ENTITY, entityPart.getEntity());
    }
    
    @Test
    public void testEntityPartToString() {
        EntityPart<String> entityPart = new EntityPart<String>(TEST_NAME, TEST_ENTITY);

        assertEquals("{\"name\":\"" + TEST_NAME + "\",\"entity\":\"" + TEST_ENTITY + "\"}", entityPart.toString());
    }
    
    @Test
    public void testEqualsAndHashcode() {
        EqualsVerifier.forClass(EntityPart.class).usingGetClass().verify();
    }
}
