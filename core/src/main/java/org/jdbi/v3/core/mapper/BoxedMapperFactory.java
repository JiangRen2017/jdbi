/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jdbi.v3.core.mapper;

import java.lang.reflect.Type;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.jdbi.v3.core.config.ConfigRegistry;

import static org.jdbi.v3.core.generic.GenericTypes.getErasedType;

/**
 * Column mapper factory which knows how to map boxed java primitives:
 * <ul>
 *     <li>{@link Boolean}</li>
 *     <li>{@link Byte}</li>
 *     <li>{@link Character}</li>
 *     <li>{@link Short}</li>
 *     <li>{@link Integer}</li>
 *     <li>{@link Long}</li>
 *     <li>{@link Float}</li>
 *     <li>{@link Double}</li>
 * </ul>
 */
class BoxedMapperFactory implements ColumnMapperFactory {
    private final Map<Class<?>, ColumnMapper<?>> mappers = new HashMap<>();

    BoxedMapperFactory() {
        mappers.put(Boolean.class, new ReferenceMapper<>(ResultSet::getBoolean));
        mappers.put(Byte.class, new ReferenceMapper<>(ResultSet::getByte));
        mappers.put(Character.class, new ReferenceMapper<>(BoxedMapperFactory::getCharacter));
        mappers.put(Short.class, new ReferenceMapper<>(ResultSet::getShort));
        mappers.put(Integer.class, new ReferenceMapper<>(ResultSet::getInt));
        mappers.put(Long.class, new ReferenceMapper<>(ResultSet::getLong));
        mappers.put(Float.class, new ReferenceMapper<>(ResultSet::getFloat));
        mappers.put(Double.class, new ReferenceMapper<>(ResultSet::getDouble));
    }

    @Override
    public Optional<ColumnMapper<?>> build(Type type, ConfigRegistry config) {
        Class<?> rawType = getErasedType(type);

        return Optional.ofNullable(mappers.get(rawType));
    }

    private static Character getCharacter(ResultSet r, int i) throws SQLException {
        String s = r.getString(i);
        return s == null || s.isEmpty() ? null : s.charAt(0);
    }
}
