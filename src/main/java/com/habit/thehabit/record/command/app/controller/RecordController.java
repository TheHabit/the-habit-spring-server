package com.habit.thehabit.record.command.app.controller;

import com.habit.thehabit.common.command.app.dto.ResponseDTO;
import com.habit.thehabit.member.command.domain.aggregate.Member;
import com.habit.thehabit.record.command.app.dto.RecordDTO;
import com.habit.thehabit.record.command.app.exception.RecordDeleteException;
import com.habit.thehabit.record.command.app.exception.RecordNotFoundException;
import com.habit.thehabit.record.command.app.service.RecordService;
import com.habit.thehabit.record.command.domain.aggregate.Record;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Getter;
import org.hibernate.annotations.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

//@Tag(name = "독서 기록", description = "독서 기록 API")
@RestController
@RequestMapping("/v1/records")
public class RecordController {
    
    private final RecordService recordService;
    
    @Autowired
    public RecordController(RecordService recordService){
        this.recordService = recordService;
    }


//    @Operation(summary = "독서기록 삽입하기", description = "새로운 독서 기록을 삽입합니다.")
//    @ApiResponses({
//            @ApiResponse(responseCode = "201", description = "OK", content = @Content(schema = @Schema(implementation = RecordDTO.class))),
//            @ApiResponse(responseCode = "500", description = "Internal Sever Error")
//    })
//    @Parameters({
//            @Parameter(name = "bookName", description = "책 제목", example = "지적 대화를 위한 넓고 얉은 지식"),
//            @Parameter(name = "bookISBN", description = "책 ISBN", example = "11211312314"),
//            @Parameter(name = "bookReview", description = "독서록"),
//            @Parameter(name = "startDate", description = "독서 시작 일자", example = "2022-10-11"),
//            @Parameter(name = "endDate", description = "독서 종료 일자", example = "2022-11-11")
//    })
    @PostMapping(value = "/add", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ResponseDTO> addRecord(@RequestPart(value = "bookImg") @Nullable MultipartFile bookImg,
                                                 @RequestPart(value = "pageCapture") @Nullable MultipartFile pageCap,
                                                 @RequestPart RecordDTO record , @AuthenticationPrincipal Member member){
        try{
            return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "독서기록 입력 성공", recordService.addRecord(bookImg, pageCap, record, member)));
        } catch(Exception e){
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR, "독서기록 입력 실패", "내부 에러 발생"));
        }
    }


    /** 책 담기(읽을 책 삽입 -> isDone : False) 또는 책 삽입도 가능. 즉, 모든 독서록 '쓰기' 행위를 이것으로 진행 */
    @PostMapping(value = "/write", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ResponseDTO> writeRecord(@RequestPart(value = "bookImg") @Nullable MultipartFile bookImg,
                                                   @RequestPart(value = "pageCapture") @Nullable MultipartFile pageCap,
                                                   @RequestPart RecordDTO record , @AuthenticationPrincipal Member member){
        try{
            return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "독서기록 입력 성공", recordService.writeRecord(bookImg, pageCap, record, member)));
        } catch(Exception e){
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR, "독서기록 입력 실패", e.getMessage()));
        }
    }


    /** bookISBN으로 해당하는 독서록 리스트 찾아오기 */
    @GetMapping("")
    public ResponseEntity<ResponseDTO> selectRecordListByISBN(@RequestParam String bookISBN){
        System.out.println("bookISBN = " + bookISBN);

        try{
            return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "해당 ISBN의 독서기록 조회", recordService.selectRecordListByISBN(bookISBN)));
        } catch (RecordNotFoundException re){
            return ResponseEntity.status(204).body(new ResponseDTO(HttpStatus.NO_CONTENT, "독서기록 조회 실패", re.getMessage()));
        } catch (Exception e){
            System.out.println("e = " + e);
            return ResponseEntity.internalServerError().body(new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR, "독서기록 조회 실패", "내부 에러 발생"));
        }
    }

    /** recordCode로 해당하는 독서기록 찾아오기 */
//    @GetMapping("")
//    public ResponseEntity<ResponseDTO> selectRecordListByISBN(@RequestParam String bookISBN){
//        System.out.println("bookISBN = " + bookISBN);
//
//        try{
//            return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "해당 ISBN의 독서기록 조회", recordService.selectRecordListByISBN(bookISBN)));
//        } catch (RecordNotFoundException re){
//            return ResponseEntity.status(204).body(new ResponseDTO(HttpStatus.NO_CONTENT, "독서기록 조회 실패", re.getMessage()));
//        } catch (Exception e){
//            System.out.println("e = " + e);
//            return ResponseEntity.internalServerError().body(new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR, "독서기록 조회 실패", "내부 에러 발생"));
//        }
//    }

    /** 회원 정보로 '읽고 있는' 도서 리스트 찾아오기 */
    @GetMapping("/reading")
    public ResponseEntity<ResponseDTO> selectRecordListByUserInfo(@AuthenticationPrincipal Member member){
        int memberCode = member.getMemberCode();
        System.out.println("memberCode = " + memberCode);

        try{
            return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "해당 유저의 읽고 있는 도서 조회", recordService.selectRecordListByUserInfo(memberCode)));
        } catch (RecordNotFoundException re){
            return ResponseEntity.status(204).body(new ResponseDTO(HttpStatus.NO_CONTENT, "유저가 읽고 있는 도서 조회 실패", re.getMessage()));
        } catch (Exception e){
            System.out.println("e = " + e);
            return ResponseEntity.internalServerError().body(new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR, "유저가 읽고 있는 도서 조회 실패", "내부 에러 발생"));
        }
    }

    /** 회원 정보로 '읽은' 도서 권수 찾아오기
     * 2022-11-13 모든 정보 다 반환하기*/
    @GetMapping("/count")
    public ResponseEntity<ResponseDTO> countingRecordByUserInfo(@AuthenticationPrincipal Member member){
        int memberCode = member.getMemberCode();
        System.out.println("memberCode = " + memberCode);

        try{
            return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "유저가 다 읽은 도서 권수 조회", recordService.countingRecordByUserInfo(memberCode)));
        } catch (RecordNotFoundException re){
            return ResponseEntity.status(204).body(new ResponseDTO(HttpStatus.NO_CONTENT, "유저가 다 읽은 도서 권수 조회 실패", re.getMessage()));
        } catch (Exception e){
            System.out.println("e = " + e);
            return ResponseEntity.internalServerError().body(new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR, "유저가 다 읽은 도서 권수 조회 실패", "내부 에러 발생"));
        }
    }

    /** 전체 독서록 리스트(평점, 한줄평) 가져오기 */
    @GetMapping("/all")
    public ResponseEntity<ResponseDTO> selectAllRecordOneLineReview(){

        try{
            return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "전체 독서기록 한줄평 가져오기", recordService.selectAllRecordGradeAndOneLineReview()));
        } catch (RecordNotFoundException re){
            return ResponseEntity.status(204).body(new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR, "독서기록 조회 실패", re.getMessage()));
        } catch (Exception e){
            System.out.println("e = " + e);
            return ResponseEntity.internalServerError().body(new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR, "독서기록 조회 실패", "내부 에러 발생"));
        }
    }

    /** 독서기록 수정 - 부분 수정 가능하다는 의미로 Patch Method로 작성 */
    @PatchMapping("")
    public ResponseEntity<ResponseDTO> updateRecord(@RequestBody RecordDTO recordDTO, @AuthenticationPrincipal Member member){

        System.out.println("recordDTO = " + recordDTO);

        try{
            return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "독서기록 수정 성공", recordService.updateRecord(recordDTO, member)));
        } catch (Exception e){
            System.out.println("Exception = " + e);
            return ResponseEntity.internalServerError().body(new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR, "독서기록 수정 실패", "내부 에러"));
        }
    }

    /** 독서기록 삭제 */
    @DeleteMapping("")
    public ResponseEntity<ResponseDTO> deleteRecord(@RequestParam Long recordCode){
        System.out.println("recordCode = " + recordCode);

        try{
            return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "독서기록 삭제 성공", "삭제 완료되었습니다."));
        } catch (RecordDeleteException rde){
            return ResponseEntity.status(204).body(new ResponseDTO(HttpStatus.NO_CONTENT, "독서기록 삭제 실패", rde.getMessage()));
        } catch(Exception e){
            System.out.println("exception = " + e);
            return ResponseEntity.internalServerError().body(new ResponseDTO(HttpStatus.INTERNAL_SERVER_ERROR, "독서기록 삭제 실패", "내부 에러"));
        }

    }

}
