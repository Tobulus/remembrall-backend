package remembrall.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import remembrall.controller.BasicController;
import remembrall.model.Invitation;
import remembrall.model.User;
import remembrall.model.repository.InvitationRepository;
import remembrall.model.repository.UserRepository;
import remembrall.service.InvitationService;

import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class InvitationApi implements BasicController {

    @Autowired
    private InvitationService invitationService;

    @Autowired
    private InvitationRepository invitationRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping(value = "/api/invitations")
    public List<Invitation> invitations() {
        User currentUser = userRepository.getOne(getUserPrincipalOrThrow().getUserId());
        return invitationRepository.findByReceiver(currentUser);
    }

    @PostMapping(value = "/api/invitation/{id}")
    public Map<String, String> editInvitation(@PathVariable Long id, @RequestParam(required = false) Boolean ack,
                                              @RequestParam(required = false) Boolean deny) {
        Map<String, String> result = new HashMap<>();

        User currentUser = userRepository.getOne(getUserPrincipalOrThrow().getUserId());

        if (ack != null && deny != null) {
            throw new InvalidParameterException("Ack and deny cannot be set at the same time.");
        }

        if (ack != null) {
            invitationService.ackknowledge(currentUser, id);
        }

        if (deny != null) {
            invitationService.deny(currentUser, id);
        }

        result.put("result", "success");

        return result;
    }
}
