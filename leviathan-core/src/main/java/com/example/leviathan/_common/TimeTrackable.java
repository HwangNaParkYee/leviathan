package com.example.leviathan._common;

import java.time.Instant;

public interface TimeTrackable {
	Instant getCreatedAt();

	Instant getUpdatedAt();
}
