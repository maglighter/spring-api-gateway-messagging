package com.maglighter.api.gateway.messaging.backend.messaging;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.Converter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;

public class SpringDataModule extends SimpleModule {

    private static final long serialVersionUID = 1L;

    public SpringDataModule() {
        super("springData");

        setMixInAnnotation(Pageable.class, PageableMixin.class);
        setMixInAnnotation(Sort.class, SortMixin.class);
        setMixInAnnotation(PageRequest.class, PageableMixin.class);
    }

    @Override
    public Object getTypeId() {
        return getModuleName();
    }

    @JsonDeserialize(as = PageRequest.class)
    @JsonPropertyOrder(alphabetic = true)
    public static class PageableMixin {

        @JsonCreator
        public PageableMixin(
            @JsonProperty(value = "pageNumber") int page,
            @JsonProperty("pageSize") int size, @JsonProperty(value = "sort") Sort sort
        ) {
        }
    }

    public static class SortConverter implements Converter<SortMixin, Sort> {

        @Override
        public Sort convert(SortMixin value) {
            return value.getProperties() != null ? Sort.by(value.getProperties())
                : Sort.by(new String[0]);
        }

        @Override
        public JavaType getInputType(TypeFactory typeFactory) {
            return typeFactory.constructType(SortMixin.class);
        }

        @Override
        public JavaType getOutputType(TypeFactory typeFactory) {
            return typeFactory.constructType(Sort.class);
        }
    }

    public static class SortMixinConverter implements Converter<Sort, SortMixin> {

        @Override
        public SortMixin convert(Sort value) {
            List<String> properties = new ArrayList<>();
            Iterator<Order> iterator = value.iterator();
            while (iterator.hasNext()) {
                properties.add(iterator.next()
                                       .getProperty());
            }
            SortMixin result = new SortMixin();
            result.setProperties(properties.toArray(new String[0]));
            return result;
        }

        @Override
        public JavaType getInputType(TypeFactory typeFactory) {
            return typeFactory.constructType(Sort.class);
        }

        @Override
        public JavaType getOutputType(TypeFactory typeFactory) {
            return typeFactory.constructType(SortMixin.class);
        }
    }

    @JsonPropertyOrder(alphabetic = true)
    @JsonDeserialize(converter = SortConverter.class)
    @JsonSerialize(converter = SortMixinConverter.class)
    public static class SortMixin {

        private String[] properties;

        @JsonCreator
        public SortMixin() {
        }

        public String[] getProperties() {
            return this.properties;
        }

        public void setProperties(String[] properties) {
            this.properties = properties;
        }
    }

}