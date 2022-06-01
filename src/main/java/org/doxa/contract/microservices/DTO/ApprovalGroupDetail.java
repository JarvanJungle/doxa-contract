package org.doxa.contract.microservices.DTO;

import java.util.List;

import lombok.Data;
import lombok.Setter;

@Data
@Setter
public class ApprovalGroupDetail {

    int totalApproves;
    String uuid;
    String name;
    List<String> users;

    public boolean isUserInGroup(String userUuid) {
        if (users == null) {
            return false;
        }
        return users.contains(userUuid);
    }

    public int getTotalApproves() {
        if(totalApproves < 1) {
            if (users == null) {
                return 0;
            }
            return users.size();
        }
        return totalApproves;
    }

    public String getApprovalNameAndCount() {
        return String.format("%s(%s)", name, totalApproves);
    }

    @Override
    public String toString() {
        return "ApprovalGroupDetail{" +
                "totalApproves=" + totalApproves +
                ", uuid='" + uuid + '\'' +
                ", name='" + name + '\'' +
                ", users=" + users +
                '}';
    }
}
