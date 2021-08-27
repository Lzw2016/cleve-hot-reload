package org.clever.hot.reload.spring.component;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;

/**
 * 作者：lizw <br/>
 * 创建时间：2021/08/27 22:16 <br/>
 */
public class JacksonMapper {
    /**
     * 对象转换器
     */
    private final ObjectMapper mapper;

    public JacksonMapper(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    /**
     * Object可以是POJO，也可以是Collection或数组。 如果对象为Null, 返回"null". 如果集合为空集合, 返回"[]".<br/>
     *
     * @param object 需要序列化的对象
     * @return 序列化后的Json字符串
     */
    @SneakyThrows
    public String toJson(Object object) {
        return mapper.writeValueAsString(object);
    }

    /**
     * 反序列化POJO或简单Collection如List&lt;String&gt;<br/>
     * 如果JSON字符串为null或空字符串, 返回null. 如果JSON字符串为"[]", 返回空集合<br/>
     * 如需反序列化复杂Collection如List&lt;MyBean&gt;，请使用fromJson(String, JavaType)<br/>
     *
     * @param jsonString Json字符串
     * @param clazz      反序列化的对象类型
     * @return 反序列化的对象
     */
    @SneakyThrows
    public <T> T fromJson(String jsonString, Class<T> clazz) {
        if (StringUtils.isEmpty(jsonString)) {
            return null;
        }
        return mapper.readValue(jsonString, clazz);
    }

    /**
     * 返回当前 Jackson 对应的 ObjectMapper
     */
    public ObjectMapper getMapper() {
        return mapper;
    }
}
