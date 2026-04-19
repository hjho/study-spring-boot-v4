package io.github.hjho.common.util;

import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ModelUtils {
	// implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.21.1")
	
	
	// 정적 내부 클래스를 이용한 Lazy Holder 방식 (가장 권장됨)
	private static class MapperHolder {
		private static final ObjectMapper INSTANCE = new ObjectMapper()
				.registerModule(new JavaTimeModule()) // 날짜 타입 지원
				.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false); // 모르는 필드 무시
	}
	
	public static ObjectMapper getMapper() {
		return MapperHolder.INSTANCE;
	}
	
    /**
     * 단일 객체 깊은 복사
     */
    public static <T> T deepCopy(Object origin, Class<T> clazz) {
        if (origin == null) return null;
        try {
            return getMapper().convertValue(origin, clazz);
        } catch (Exception e) {
            throw new RuntimeException("객체 복사 중 오류가 발생했습니다.", e);
        }
    }

    /**
     * 리스트 깊은 복사
     */
    public static <T> List<T> deepCopyList(List<?> origin, Class<T> clazz) {
        if (origin == null) return null;
        try {
            return getMapper().convertValue(origin, new TypeReference<List<T>>() {});
        } catch (Exception e) {
            throw new RuntimeException("리스트 복사 중 오류가 발생했습니다.", e);
        }
    }
    
    /**
     * 단일 객체 부분 속성 머지
     */
    public static <T> T merge(T target, Object update) {
    	if (target == null ) return null;
    	if (update == null) return target;
    	try {
    		ObjectMapper mapper = getMapper();
    		
            JsonNode sourceNode = mapper.valueToTree(update);
            
            return mapper.readerForUpdating(target).readValue(sourceNode);
    	} catch (Exception e) {
    		throw new RuntimeException("객체 머지 중 오류가 발생했습니다.", e);
    	}
    }
    
    
    
    public static String toJsonString(Object origin) {
    	if (origin == null) return null;
    	try {
    		return getMapper().writeValueAsString(origin);
    	} catch(Exception e) {
    		throw new RuntimeException("JOSN 변환 중 오류가 발생했습니다.", e);
    	}
    }
    
    public static <T> T toModel(String jsonString, Class<T> target) {
    	if (jsonString == null || "".equals(jsonString.trim())) return null;
    	try {
    		return getMapper().readValue(jsonString, target);
    	} catch(Exception e) {
    		throw new RuntimeException("JOSN 변환 중 오류가 발생했습니다.", e);
    	}
    }

}
