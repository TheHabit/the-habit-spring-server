package com.habit.thehabit.friend.app.service;

import com.habit.thehabit.friend.app.dto.AIRequestDTO;
import com.habit.thehabit.friend.app.dto.RecommendedMemberDTO;
import com.habit.thehabit.friend.app.dto.UpdateDataDTO;
import com.habit.thehabit.member.command.domain.aggregate.Member;
import com.habit.thehabit.member.command.infra.repository.MemberInfraRepository;
import com.habit.thehabit.record.command.app.dto.RecordDTO;
import com.habit.thehabit.record.command.app.exception.RecordNotFoundException;
import com.habit.thehabit.record.command.domain.aggregate.Record;
import com.habit.thehabit.record.command.infra.repository.RecordInfraRepository;
import com.habit.thehabit.util.AIRequestAPI;
import org.hibernate.tool.schema.internal.exec.ScriptTargetOutputToFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class FriendService {

    private final AIRequestAPI aiRequestAPI;
    private final MemberInfraRepository memberInfraRepository;
    private final RecordInfraRepository recordInfraRepository;

    @Autowired
    public FriendService(AIRequestAPI aiRequestAPI, MemberInfraRepository memberInfraRepository, RecordInfraRepository recordInfraRepository) {
        this.aiRequestAPI = aiRequestAPI;
        this.memberInfraRepository = memberInfraRepository;
        this.recordInfraRepository = recordInfraRepository;
    }

    /* ai 서버에 memberCode를 전송하여, 친구추전목록을 받아옴(추천된 친구의 memberCode값들) */
    public List<RecommendedMemberDTO> getRecommendedFriends(Member member) {
        int memberCode = member.getMemberCode();
        AIRequestDTO aiRequestDTO = new AIRequestDTO(memberCode,5);//요청할 파라미터때 필요한 입력: (memberCode, 추천 받을 인원 수)

        List<Integer> recommanedFriends = aiRequestAPI.callRecommanedFriends(aiRequestDTO); // AI서버에 요청
        //List<Integer> recommanedFriends =new ArrayList<Integer>(List.of(1,2,3,4)); // AI서버에 요청 test

        /* !!!!!!!!!!~~~~~~~~~~살아 있는 유저인지 검증해야함~~~~~~~~~~!!!!!!!!!! */
        System.out.println(recommanedFriends);

        List<RecommendedMemberDTO> recommendedMemberDTOList = new ArrayList<>();

        for(int RecommendedMemberCode : recommanedFriends){
            RecommendedMemberDTO recommendedMemberDTO = new RecommendedMemberDTO();
            /* ============== 회원이 읽은 책 목록 생성 담기 ================*/
            List<Record> recordList = recordInfraRepository.findByMemberCodeAndIsDone(RecommendedMemberCode, "Y");
            System.out.println("memberCode" + RecommendedMemberCode);
            System.out.println("recordList = " + recordList);

            /** 조회된 것이 없을 때 예외 처리 */
            if(recordList == null){
                throw new RecordNotFoundException("독서 기록이 존재하지 않습니다.");
            }
            /** Entity List -> DTO List */
            List<RecordDTO> recordDTOList = new ArrayList<>();
            for(Record record : recordList){
                recordDTOList.add(record.entityToDTO());
            }
            recommendedMemberDTO.setRecords(recordDTOList);
            /* ============== 회원이 읽은 책 목록 생성 담기 ================*/

            Member recommendedMember = memberInfraRepository.findByMemberCode(RecommendedMemberCode); //회원의 memberCode, memberId, name을 담음
            recommendedMemberDTO.setMemberCode(RecommendedMemberCode);
            recommendedMemberDTO.setMemberId(recommendedMember.getMemberId());
            recommendedMemberDTO.setName(recommendedMember.getName());

            recommendedMemberDTOList.add(recommendedMemberDTO);
        }
        /*------------------------------------------------------------------------------*/
        return recommendedMemberDTOList;
    }

    /* AI 서버에 데이터 전송 */
    public Object sendDataToAiServer() {

        List<Record> updateData = recordInfraRepository.findAllByIsSent("N");
        System.out.println("recordList : " + updateData);
        //List<Record> recordList = recordInfraRepository.findByMemberCodeAndIsDone(memberCode, "Y");
        //System.out.println("recordList = " + recordList);

        /* 조회된 것이 없을 때 예외 처리 */
        if(updateData.size() == 0){
            System.out.println("예외처리 발생 아무런값도 들어있지않습니다.");
            throw new RecordNotFoundException("독서 기록이 존재하지 않습니다.");
        }

        /* entity를 DTO로 바꿈 */
        List<UpdateDataDTO> UpdateDataDTOList = new ArrayList<>();
        for(Record record : updateData){
            UpdateDataDTO updateDataDTO = record.entityToUpdateDataDTO();
            System.out.println("memberCode :" + updateDataDTO.getMemberCode());
            System.out.println("rating : " + updateDataDTO.getRating());
            System.out.println("bookISBN : "+ updateDataDTO.getBookISBN());
            UpdateDataDTOList.add(updateDataDTO);
            record.setIsSent("Y");
            recordInfraRepository.save(record); //조회한 커리의 isSent값을 "Y"로 변경
        }

        System.out.println("============================");

        System.out.println("============================");
        Object result = aiRequestAPI.sendDataToAi(UpdateDataDTOList);

        return result;
    }
}
