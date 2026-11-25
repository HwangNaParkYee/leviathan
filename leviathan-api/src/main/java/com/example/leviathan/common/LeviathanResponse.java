package com.example.leviathan.common;

import com.example.leviathan.common.exception.ErrorCode;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class LeviathanResponse<T> {

	private final String code;

	private final String message;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private final T data;

	private LeviathanResponse(final String code, final String message) {
		this.code = code;
		this.message = message;
		this.data = null;
	}

	public static<T> LeviathanResponse<T> success() {
		return new LeviathanResponse<>("SUCCESS", "요청이 성공했습니다.");
	}

	public static<T> LeviathanResponse<T> success(final T data) {
		return new LeviathanResponse<>("SUCCESS", "요청이 성공했습니다.", data);
	}

	public static<T> LeviathanResponse<T> fail(final ErrorCode errorCode) {
		return new LeviathanResponse<>(errorCode.getCode(), errorCode.getMessage());
	}

	public static<T> LeviathanResponse<T> fail(final String code, final String message) {
		return new LeviathanResponse<>(code, message);
	}

	public static<T> LeviathanResponse<T> fail(final String code, final String message, final T data) {
		return new LeviathanResponse<>(code, message, data);
	}
}
