package io.github.hjho.jpa.example.code.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.hjho.jpa.example.code.model.AutoKeyResponseDto;
import io.github.hjho.jpa.example.code.model.RestfulApiRequestDto;
import io.github.hjho.jpa.example.code.model.SelfKeyResponseDto;
import io.github.hjho.jpa.example.code.service.RestfulApiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * <pre>
 * @GetMapping <b>목록조회, 단건조회.</b>
 *   | 200 | OK        | 단건/목록 조회가 정상적으로 이루어짐.
 *   | 404 | NOT_FOUND | 단건 조회 시 해당 ID의 데이터가 존재하지 않음.
 *   |     |           |  ㄴ ResourceNotFoundException
 *    
 * @PostMapping <b>등록, 프로세스성 조회.</b>
 *   | 201 | CREATED     | 새로운 리소스(회원, 게시글 등)가 성공적으로 등록됨.
 *   | 200 | OK          | 개인정보를 Body 에 담아 수행한 프로세스성 조회가 성공함.
 *   |     |             |  ㄴ consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE
 *   | 400 | BAD_REQUEST | 유효성 검사(Validation) 실패, 필수 파라미터 누락.
 *   |     |             |  ㄴ InvalidRequestException
 *   |     |             |  ㄴ MethodArgumentNotValidException
 *   
 * @PatchMapping <b>부분수정.</b>
 *   | 200 | OK          | Null 이 아닌 필드만 선택적으로 수정 완료함.
 *   | 404 | NOT_FOUND   | 부분 수정하려는 대상이 DB에 존재하지 않음.
 *   |     |             |  ㄴ ResourceNotFoundException
 *   | 400 | BAD_REQUEST | 유효성 검사(Validation) 실패.
 *   |     |             |  ㄴ InvalidRequestException
 *   |     |             |  ㄴ MethodArgumentNotValidException
 *   
 * @PutMapping <b>전체수정 또는 등록. [UPSERT]</b>
 *   | 200 | OK          | 기존 리소스의 전체 데이터를 교체 완료함.
 *   | 201 | CREATED     | 요청한 ID가 없어 UPSERT 로직에 의해 새로 생성함.
 *   | 400 | BAD_REQUEST | 유효성 검사(Validation) 실패, 필수 파라미터 누락.
 *   |     |             |  ㄴ InvalidRequestException
 *   |     |             |  ㄴ MethodArgumentNotValidException
 *   
 * @DeleteMapping <b>삭제, 약한삭제.</b>
 *   | 204 | NO_CONTENT | Soft Delete 완료 또는 이미 삭제된 건에 대한 멱등적 성공 처리.
 *   |     |            |  ㄴ 404 를 응답시키지 말자. (멱등성)
 * 
 * @Exception <b>스프링이 발생시키는 에러.</b>
 *   | 400 | BAD_REQUEST | ConstraintViolationException
 *   |     |             |  ㄴ 제약 조건 위반: DB 레벨의 NOT NULL 제약 조건을 위반, @Validated(개별 필드) 검증 실패 시 발생.
 *   | 400 | BAD_REQUEST | MethodArgumentNotValidException
 *   |     |             |  ㄴ 유효성 검증 실패: 클라이언트가 잘못된 형식의 JSON 데이터를 전송하거나 필수 값을 누락, @Valid(객체 전체) 검증 실패 시 발생.
 *   | 404 | NOT_FOUND   | EntityNotFoundException
 *   |     |             |  ㄴ 데이터 부재: getReferenceById() 등을 호출했는데 실제 DB에 해당 식별자가 없을 때 발생.
 *   | 409 | CONFLICT    | DataIntegrityViolationException
 *   |     |             |  ㄴ 중복 데이터: Unique Index가 걸린 컬럼(예: 이메일, 아이디)에 중복된 값을 넣으려고 할 때 발생.
 *   | 409 | CONFLICT    | ObjectOptimisticLockingFailureException
 *   |     |             |  ㄴ 동시 수정(낙관적 락): @Version을 사용 중인데, 내가 수정하기 직전에 다른 사용자가 먼저 데이터를 수정해서 버전이 올라갔을 때 발생.
 *   | 415 | UNSUPPORTED | HttpMediaTypeNotSupportedException
 *   |     | _MEDIA_TYPE |  ㄴ 미디어 타입 오류: 클라이언트가 서버로 보내는 데이터의 형식(Content-Type 헤더)과 서버가 처리할 수 있는 데이터 형식이 일치하지 않을 때 발생
 * 
 * </pre> 
 */
@Slf4j
@RestController
@RequestMapping("/example/code/restful-apis")
@RequiredArgsConstructor
public class RestfulApiController {
	
	// https://learn.microsoft.com/ko-kr/azure/architecture/best-practices/api-design
	// https://www.bezkoder.com/spring-boot-controlleradvice-exceptionhandler/
	
	private final RestfulApiService restfulApiService;
	
	/**
	 * @GetMapping 목록조회.
	 */
	@GetMapping
	public ResponseEntity<List<AutoKeyResponseDto>> list() {
		return ResponseEntity.ok(restfulApiService.list());
	}
	
	/**
	 * @GetMapping 단건조회.
	 */
	@GetMapping("/{id}")
	public ResponseEntity<AutoKeyResponseDto> get(@PathVariable("id") Long id) {
		return ResponseEntity.ok(restfulApiService.get(id));
	}
	
	/**
	 * @PostMapping 등록.
	 */
	@PostMapping
	public ResponseEntity<AutoKeyResponseDto> insert(@RequestBody RestfulApiRequestDto requestDto) {
		
		AutoKeyResponseDto responseDto = restfulApiService.insert(requestDto);
		
		URI location = URI.create("/example/code/restful-apis/" + responseDto.getId());
		return ResponseEntity.created(location).body(responseDto);
	}
	
	/**
	 * @PostMapping 프로세스성 조회(process).
	 */
	@PostMapping(path = "/process", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseEntity<SelfKeyResponseDto> process(@ModelAttribute RestfulApiRequestDto requestDto) {
		
		SelfKeyResponseDto responseDto = restfulApiService.process(requestDto);
		
		return ResponseEntity.ok(responseDto);
	}
	
	
	/**
	 * @PatchMapping 부분수정.
	 */
	@PatchMapping("/{id}")
	public ResponseEntity<AutoKeyResponseDto> update(@PathVariable("id") Long id, @RequestBody RestfulApiRequestDto requestDto) {
		return ResponseEntity.ok(restfulApiService.update(id, requestDto));
	}
	
	
	/**
	 * @PutMapping 전체수정 또는 등록.
	 */
	@PutMapping("/{id}")
	public ResponseEntity<SelfKeyResponseDto> upsert(@PathVariable("id") String id, @RequestBody RestfulApiRequestDto requestDto) {
		
		Boolean isInsert = restfulApiService.upsert(id, requestDto);
		
		SelfKeyResponseDto responseDto = restfulApiService.get(id);
		
		if(isInsert) {
			URI location = URI.create("/example/code/restful-apis/" + responseDto.getId());
			return ResponseEntity.created(location).body(responseDto);
		}
		return ResponseEntity.ok(responseDto);
	}
	
	
	/**
	 * @DeleteMapping 삭제, 약한삭제.
	 */
	@DeleteMapping("/{id}")
	public ResponseEntity<AutoKeyResponseDto> delete(@PathVariable("id") Long id) {
		
		Boolean isDelete = restfulApiService.delete(id);
		
		if(!isDelete) {
			// throw new ResourceNotFoundException("E001");
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.noContent().build();
	}
	
}