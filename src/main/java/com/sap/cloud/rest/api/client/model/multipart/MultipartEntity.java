package com.sap.cloud.rest.api.client.model.multipart;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Represents a multipart entity. It consists of a list of {@link EntityPart}s.
 *
 * @param <T>
 *            the type of the entity parts
 */
public class MultipartEntity<T> {

    private final List<EntityPart<T>> multipartEntity;

    public MultipartEntity(List<EntityPart<T>> parts) {
        this.multipartEntity = parts;
    }

    public List<EntityPart<T>> getParts() {
        return multipartEntity;
    }

    public List<EntityPart<T>> getPartsByName(String name) {
        return multipartEntity.stream()
                .filter(x -> x.getName().equals(name))
                .collect(Collectors.toList());
    }

    public String toString() {
        return new ToStringBuilder(MultipartEntity.class.getName(), ToStringStyle.JSON_STYLE)
                .append("multipartEntity", multipartEntity)
                .toString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((multipartEntity == null) ? 0 : multipartEntity.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        MultipartEntity<?> other = (MultipartEntity<?>) obj;
        if (multipartEntity == null) {
            if (other.multipartEntity != null) return false;
        } else if (!multipartEntity.equals(other.multipartEntity)) return false;
        return true;
    }
}
