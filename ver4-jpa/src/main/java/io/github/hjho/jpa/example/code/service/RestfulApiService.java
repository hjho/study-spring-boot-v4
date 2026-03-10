package io.github.hjho.jpa.example.code.service;

import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import io.github.hjho.common.exception.InvalidRequestException;
import io.github.hjho.common.exception.ResourceNotFoundException;
import io.github.hjho.common.util.ModelUtils;
import io.github.hjho.jpa.example.code.model.AutoKeyResponseDto;
import io.github.hjho.jpa.example.code.model.RestfulApiRequestDto;
import io.github.hjho.jpa.example.code.model.SelfKeyResponseDto;
import io.github.hjho.jpa.example.code.repository.AutoKeyRepository;
import io.github.hjho.jpa.example.code.repository.SelfKeyRepository;
import io.github.hjho.jpa.model.entity.AutoKey;
import io.github.hjho.jpa.model.entity.SelfKey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class RestfulApiService {
	
	private final AutoKeyRepository autoKeyRepository;
	
	private final SelfKeyRepository selfKeyRepository;
	
	
	@Transactional(readOnly = true)
	public List<AutoKeyResponseDto> list() {
		
		List<AutoKey> entities = autoKeyRepository.findAll();
		
		return ModelUtils.deepCopyList(entities, AutoKeyResponseDto.class);
	}
	
	@Transactional(readOnly = true)
	public AutoKeyResponseDto get(Long id) {
		
		AutoKey entity = autoKeyRepository.findById(id)
				.orElseThrow(
						() -> new ResourceNotFoundException("E001")
					);
		
		return ModelUtils.deepCopy(entity, AutoKeyResponseDto.class);
	}
	
	@Transactional(readOnly = true)
	public SelfKeyResponseDto process(RestfulApiRequestDto requestDto) {
		
		return null;
	}

	@Transactional
	public AutoKeyResponseDto insert(RestfulApiRequestDto requestDto) {
		if(ObjectUtils.isEmpty(requestDto)) {
			throw new InvalidRequestException("E002");
		} else {
			if(StringUtils.isEmpty(requestDto.getTitle())) {
				throw new InvalidRequestException("E003", "제목");
			}
		}
		
		AutoKey entity = autoKeyRepository.save(
				new AutoKey(requestDto.getTitle(), requestDto.getContent())
			);
		
		return ModelUtils.deepCopy(entity, AutoKeyResponseDto.class);
	}
	
	@Transactional
	public AutoKeyResponseDto update(Long id, RestfulApiRequestDto requestDto) {
		if(ObjectUtils.isEmpty(requestDto)) {
			throw new InvalidRequestException("E002");
		} else {
			if(StringUtils.isEmpty(requestDto.getTitle())) {
				throw new InvalidRequestException("E003", "제목");
			}
		}
		
		AutoKey entity = autoKeyRepository.findById(id)
				.orElseThrow(
						() -> new ResourceNotFoundException("E001")
					);
		
		entity.changeContent(requestDto.getContent());
		
		return ModelUtils.deepCopy(entity, AutoKeyResponseDto.class);
	}

	@Transactional(readOnly = true)
	public SelfKeyResponseDto get(String id) {
		SelfKey entity = selfKeyRepository.findById(id)
				.orElseThrow(
						() -> new ResourceNotFoundException("E001")
					);
		return ModelUtils.deepCopy(entity, SelfKeyResponseDto.class);
	}
	
	
	@Transactional
	public Boolean upsert(String id, RestfulApiRequestDto requestDto) {
		if(ObjectUtils.isEmpty(requestDto)) {
			throw new InvalidRequestException("E002");
		} else {
			if(StringUtils.isEmpty(id)) {
				throw new InvalidRequestException("E003", "코드");
				
			} else if(StringUtils.isEmpty(requestDto.getTitle())) {
				throw new InvalidRequestException("E003", "제목");
			}
		}
		
		Optional<SelfKey> optional = selfKeyRepository.findById(id);
		Boolean isInsert = optional.isEmpty();
		
		SelfKey entity = null;
		if(isInsert) {
			entity = new SelfKey(id, requestDto.getTitle(), requestDto.getContent());
			
		} else {
			entity = ModelUtils.merge(optional.get(), requestDto);
		}
		
		selfKeyRepository.save(entity);
		return isInsert;
	}
	
	@Transactional
	public Boolean delete(Long id) {
		Optional<AutoKey> optional = autoKeyRepository.findById(id);
		if(optional.isPresent()) {
			autoKeyRepository.delete(optional.get());
			return true;
		}
		return false;
	}

}
