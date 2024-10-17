package com.payguard.domain;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Entity
public class Member {
	@Id
	@GeneratedValue
	@Column(name = "MEMBER_ID")
	private long id;
	
	@Column(unique = true, nullable = false)
	@Email(message = "유효한 이메일을 입력해주세요.")
	@NotBlank(message = "이메일 입력은 필수입니다.")
	private String email;
	
	@Column(nullable = false)
	@NotBlank(message = "비밀번호 입력은 필수입니다.")
	@Size(min = 6, message = "비밀번호는 최소 6자 이상이어야 합니다.")
	private String password;
	
	@Column(nullable = false)
	@NotBlank(message = "사용자명 입력은 필수입니다.")
	private String name;
	
	@Column(nullable = false, updatable = false)
	private LocalDateTime created_at;
	
	private LocalDateTime updated_at;
	
	@OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Budget> budgets;
	
	@PrePersist
	protected void onCreate() {
		created_at = LocalDateTime.now();
		updated_at = LocalDateTime.now();
	}
	
	@PreUpdate
	protected void onUpdate() {
		updated_at = LocalDateTime.now();
	}
}
