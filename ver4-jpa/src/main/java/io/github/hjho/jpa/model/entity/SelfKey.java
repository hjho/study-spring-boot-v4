package io.github.hjho.jpa.model.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "self_key")
@EntityListeners(AuditingEntityListener.class)
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SelfKey {
	
	
	@Id
	private String id;
	
	private String title;
	
	private String content;
	
	@CreatedDate
	private LocalDateTime createdDate;
	
	@LastModifiedDate
	private LocalDateTime lastModifiedDate;
	
	
	public SelfKey(String id, String title, String content) {
		this.id = id;
		this.title = title;
		this.content = content;
	}
	
}
