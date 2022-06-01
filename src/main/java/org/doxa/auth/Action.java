package org.doxa.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Getter
@NoArgsConstructor
public class Action {
    private boolean read;
    private boolean write;
    private boolean approve;
}
