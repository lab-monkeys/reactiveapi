package org.labmonkeys.reactiveapi.model;

import java.util.UUID;

import lombok.Data;

@Data
public class Message {
    UUID messageId;
    String message;
}
