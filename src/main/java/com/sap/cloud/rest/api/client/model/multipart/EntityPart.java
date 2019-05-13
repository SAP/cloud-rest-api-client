package com.sap.cloud.rest.api.client.model.multipart;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Represents an entity part in a multipart entity. Consist of an entity of type
 * {@link T} and name.
 *
 * @param <T>
 *            the type of the entity
 */
public class EntityPart<T> {

    private final String name;

    private final T entity;

    public EntityPart(String name, T entity) {
        this.name = name;
        this.entity = entity;
    }

    public String getName() {
        return name;
    }

    public T getEntity() {
        return entity;
    }

    public String toString() {
        return new ToStringBuilder(EntityPart.class.getName(), ToStringStyle.JSON_STYLE)
                .append("name", name)
                .append("entity", entity)
                .toString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((entity == null) ? 0 : entity.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        EntityPart<?> other = (EntityPart<?>) obj;
        if (entity == null) {
            if (other.entity != null) return false;
        } else if (!entity.equals(other.entity)) return false;
        if (name == null) {
            if (other.name != null) return false;
        } else if (!name.equals(other.name)) return false;
        return true;
    }
}
