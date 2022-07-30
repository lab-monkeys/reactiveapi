package org.labmonkeys.reactiveapi.dto;

import java.util.UUID;

public record ReplyDto(UUID messageId, UUID replyId, String reply) {}
