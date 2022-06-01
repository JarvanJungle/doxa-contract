package org.doxa.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.doxa.auth.Action;

@AllArgsConstructor
@Getter
@NoArgsConstructor
public class Authority {
    private String featureCode;
    private Action actions;
}
