package org.doxa.contract.utils;

import org.doxa.contract.enums.ContractActionEnum;
import org.doxa.contract.models.contract.ContractAuditTrail;
import org.doxa.contract.repositories.contract.ContractAuditTrailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

@Component
public class ApprovalSequence {


    @Autowired
    ContractAuditTrailRepository contractAuditTrailRepository;


    private HashMap<Integer, String> sequenceOfApproval(String approvalSequence){

        HashMap<Integer, String> approval = new HashMap<Integer, String>();
        //split a string with GroupA(1)>GroupB(2)>GroupC(3)
        StringTokenizer st = new StringTokenizer(approvalSequence,">");
        Integer positionInSequence=0;
        while (st.hasMoreTokens()){

            String groupName = st.nextToken().trim();
            approval.put(positionInSequence, groupName);
            positionInSequence++;
        }
        return approval;
    }

    private HashMap<String, Integer> positionOfSequence(String approvalSequence){

        HashMap<String, Integer>  position = new HashMap<String, Integer> ();
        //split a string with GroupA(1)>GroupB(2)>GroupC(3)
        StringTokenizer st = new StringTokenizer(approvalSequence,">");
        Integer positionInSequence=0;
        while (st.hasMoreTokens()){

            String groupName = st.nextToken().trim();
            position.put(groupName, positionInSequence);
            positionInSequence++;
        }
        return position;
    }

    private HashMap<String, Integer> groupInfo(String approvalSequence){
        HashMap<String, Integer> groupInformation = new HashMap<>();
        //split a string with GroupA(1)>GroupB(2)>GroupC(3)
        StringTokenizer st = new StringTokenizer(approvalSequence,">");
        while(st.hasMoreTokens()){
            String group = st.nextToken().trim();
            StringTokenizer token = new StringTokenizer(group,"()");
            String groupName = token.nextToken().trim();
            Integer noOfApprovers = Integer.parseInt(token.nextToken());
            groupInformation.put(group, noOfApprovers);
        }
        return groupInformation;
    }

    public String groupNameFromSequence(String approvalSequence, Integer position){
        StringTokenizer st = new StringTokenizer(approvalSequence,">");
        int i =0;
        String groupName = new String();
        while(st.hasMoreTokens() && i<=position){
            groupName = st.nextToken().trim();
            i++;
        }
        return(groupName);
    }


    public String nextSequence(String approvalSequence, String currentGroup, Long invoiceId){
        HashMap<Integer, String> sequence = sequenceOfApproval(approvalSequence);
        HashMap<String, Integer> groupInformation = groupInfo(approvalSequence);
        HashMap<String, Integer> positionOfSequence = positionOfSequence(approvalSequence);
        //how many has approved the PPR
        int currentCount = noOfApprovedForAGroup(invoiceId, currentGroup);
        int position = positionOfSequence.get(currentGroup);
        if (currentCount >= groupInformation.get(currentGroup)-1){
            //if there is more to the sequence
            if (position < positionOfSequence.size()-1) {
                //then move to the next
                return (sequence.get(position+1));
            }
            if (position == positionOfSequence.size()-1){
                //return a null so we know we can change the status to submit
                return null;
            }

        }
        return currentGroup;

    }


    //find the number of approvers that has been approved for the group by using the
    private Integer noOfApprovedForAGroup(Long contractId, String nextGroup){

        List<ContractAuditTrail> auditTrailList = contractAuditTrailRepository.auditTrailByContractId(contractId);
        int i = 0;
        int count =0;

        while(i<auditTrailList.size()-1){
            boolean sameGroup = auditTrailList.get(i).getCurrentGroup().equals(nextGroup);
            boolean sameAction = auditTrailList.get(i).getAction().equals(ContractActionEnum.APPROVE.toString());
            if(sameGroup==true && sameAction ==true) {
                count++;

            }else
                return count;
            i++;
        }
        return (count);
    }

    //check if the user has approved before
    public boolean hasApproverApproveBefore(String userUuid, Long contractId, String currentGroup){
        List<ContractAuditTrail> auditTrailList = contractAuditTrailRepository.auditTrailByContractId(contractId);
        boolean sameGroup;
        boolean sameAction;
        for(ContractAuditTrail c : auditTrailList){
            sameGroup = c.getCurrentGroup().equals(currentGroup);
            sameAction = c.getAction().equals(ContractActionEnum.APPROVE.toString());
            if (sameGroup==true && sameAction ==true){
                if (c.getUserUuid().equals(userUuid)) {
                    return true;
                }
            }
            if (sameGroup==false || sameAction==false){
                return false;
            }

        }
        return false;
    }








}
