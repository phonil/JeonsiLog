package depth.jeonsilog.domain.reply.presentation;

import depth.jeonsilog.domain.reply.application.ReplyService;
import depth.jeonsilog.domain.reply.dto.ReplyRequestDto;
import depth.jeonsilog.global.config.security.token.CurrentUser;
import depth.jeonsilog.global.config.security.token.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/replies")
public class ReplyController implements ReplyApi {

    private final ReplyService replyService;

    @GetMapping("/reviews/{reviewId}")
    public ResponseEntity<?> findReplyList(
            @RequestParam(value = "page") Integer page,
            @PathVariable(value = "reviewId") Long reviewId
    ) {
        return replyService.findReplyList(page, reviewId);
    }

    @PostMapping
    public ResponseEntity<Void> createReply(
            @CurrentUser UserPrincipal userPrincipal,
            @RequestBody ReplyRequestDto.CreateReplyReq createReplyReq
            ) throws IOException {
        replyService.createReply(userPrincipal, createReplyReq);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{replyId}")
    public ResponseEntity<Void> deleteReply(
            @CurrentUser UserPrincipal userPrincipal,
            @PathVariable(value = "replyId") Long replyId
    ) {
        replyService.deleteReply(userPrincipal, replyId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{replyId}")
    public ResponseEntity<?> existReply(
            @PathVariable(value = "replyId") Long replyId
    ) {
        return replyService.existReply(replyId);
    }
}
