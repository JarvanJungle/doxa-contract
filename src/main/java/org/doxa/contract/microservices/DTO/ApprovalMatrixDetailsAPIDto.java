package org.doxa.contract.microservices.DTO;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApprovalMatrixDetailsAPIDto {


	private String uuid;

	private String approvalGroupSequence;

	private List<String> approvalGroupSequenceUuid;

	private List<ApprovalGroupDetail> groups;

	private String approvalMatrixName;


	public ApprovalGroupDetail getFirstApproval() {
		if (groups != null) {
			return groups.get(0);
		}
		return null;
	}

	public ApprovalGroupDetail getNextApprovalUuid(String currentUuid) {
		if (groups == null) {
			return null;
		}
		int index = 0;
		for (ApprovalGroupDetail detail : groups) {
			if (detail.getUuid().equals(currentUuid)) {
				break;
			}
			index++;
		}
		return ++index >= groups.size() ? null : groups.get(index);
	}

	public ApprovalGroupDetail getCurrentApprovalGroup(String groupUuid) {
		if (groups == null) {
			return null;
		}
		log.info("Getting {}: " + groupUuid);
		for (ApprovalGroupDetail detail : groups) {
			if (detail.getUuid().equals(groupUuid)) {
				log.info("Found {}: " + detail.toString());
				return detail;
			}
		}
		return null;
	}

	@Override
	public String toString() {
		return "ApprovalMatrixDetailsAPIDto{" +
				"uuid='" + uuid + '\'' +
				", approvalGroupSequence='" + approvalGroupSequence + '\'' +
				", approvalGroupSequenceUuid=" + approvalGroupSequenceUuid +
				", groups=" + groups +
				", approvalMatrixName='" + approvalMatrixName + '\'' +
				'}';
	}
}
