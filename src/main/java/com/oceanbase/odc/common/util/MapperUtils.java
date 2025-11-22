/*
 * Copyright (c) 2023 OceanBase.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.oceanbase.odc.common.util;

import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

/**
 * Simple mapper utilities for YAML processing
 */
@Slf4j
public class MapperUtils {

    /**
     * PathMatcher interface for path matching
     */
    public interface PathMatcher {
        boolean match(@NonNull List<Object> prefix, Object current);
    }

    public static <T> T get(@NonNull Map<?, ?> map, @NonNull TypeReference<T> typeRef,
            @NonNull PathMatcher judger) {
        Object value = get(map, judger);
        if (value == null) {
            return null;
        }
        ObjectMapper jsonMapper = new ObjectMapper();
        try {
            String jsonStr = jsonMapper.writeValueAsString(value);
            return jsonMapper.readValue(jsonStr, typeRef);
        } catch (JsonProcessingException e) {
            log.warn("Failed to parse from the map", e);
            throw new RuntimeException(e);
        }
    }

    private static Object get(@NonNull Map<?, ?> map, @NonNull PathMatcher judger) {
        return get(map, new LinkedList<>(), judger);
    }

    private static Object get(@NonNull Map<?, ?> map, List<Object> prefix, @NonNull PathMatcher judger) {
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            Object key = entry.getKey();
            Object value = entry.getValue();

            List<Object> newPrefix = new LinkedList<>(prefix);
            newPrefix.add(key);

            if (judger.match(newPrefix, value)) {
                return value;
            }

            if (value instanceof Map) {
                Object result = get((Map<?, ?>) value, newPrefix, judger);
                if (result != null) {
                    return result;
                }
            }
        }
        return null;
    }
}