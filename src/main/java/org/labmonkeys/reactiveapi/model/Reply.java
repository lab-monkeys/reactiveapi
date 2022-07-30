package org.labmonkeys.reactiveapi.model;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Reply {
    UUID messageId;
    UUID replyId;
    String reply;
}
