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
package com.oceanbase.odc.test.dataloader;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import lombok.Data;

/**
 * Simple YAML data loader for testing
 */
public class DataLoaders {

    public static YamlDataLoader yaml() {
        return new YamlDataLoader();
    }

    public static class YamlDataLoader {
        public <T> T fromFile(String fileName, Class<T> clazz) {
            Yaml yaml = new Yaml(new Constructor(clazz));
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(fileName);
            if (inputStream == null) {
                throw new RuntimeException("Cannot find resource file: " + fileName);
            }
            return yaml.load(inputStream);
        }
    }

    /**
     * Test data structure for YAML loading
     */
    @Data
    @lombok.Getter
    @lombok.Setter
    public static class TestData {
        private String origin;
        private List<String> expected;
        private String expected_end_delimiter;
    }
}