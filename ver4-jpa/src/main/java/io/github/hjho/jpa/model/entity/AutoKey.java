package io.github.hjho.jpa.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "auto_key")
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AutoKey {
	
	
	@Id
	@GeneratedValue(generator = "auto_key_id", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(
			name = "auto_key_id", 			// @GeneratedValue(generator) 매핑.
			sequenceName = "auto_key_seq", 	// DB에 실제 생성되는 시퀀스테이블명.
			initialValue = 10, // 시작 값
			allocationSize = 10 // 시퀀스 한 번 호출에 증가하는 수. (성능 최적화 시 50 이상 설정)
	)
	private Long id;
	
	private String title;
	
	private String content;
	
	public AutoKey(String title, String content) {
		this.title = title;
		this.content = content;
	}
	
	public void changeContent(String content) {
		this.content = content;
	}
}
