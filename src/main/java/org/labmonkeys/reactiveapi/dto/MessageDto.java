package org.labmonkeys.reactiveapi.dto;

import java.util.UUID;

public record MessageDto(UUID messageId, String message) {}
