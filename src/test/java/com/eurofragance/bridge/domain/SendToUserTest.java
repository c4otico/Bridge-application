package com.eurofragance.bridge.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.eurofragance.bridge.web.rest.TestUtil;

public class SendToUserTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SendToUser.class);
        SendToUser sendToUser1 = new SendToUser();
        sendToUser1.setId(1L);
        SendToUser sendToUser2 = new SendToUser();
        sendToUser2.setId(sendToUser1.getId());
        assertThat(sendToUser1).isEqualTo(sendToUser2);
        sendToUser2.setId(2L);
        assertThat(sendToUser1).isNotEqualTo(sendToUser2);
        sendToUser1.setId(null);
        assertThat(sendToUser1).isNotEqualTo(sendToUser2);
    }
}
