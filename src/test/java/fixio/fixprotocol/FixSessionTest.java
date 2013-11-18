/*
 * Copyright 2013 The FIX.io Project
 *
 * The FIX.io Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package fixio.fixprotocol;

import org.junit.Before;
import org.junit.Test;

import static org.apache.commons.lang.RandomStringUtils.randomAlphanumeric;
import static org.apache.commons.lang.math.RandomUtils.nextInt;
import static org.junit.Assert.assertEquals;

public class FixSessionTest {

    private FixSession session;
    private String beginString;
    private String senderCompId;
    private String senderSubId;
    private String targetCompId;
    private String targetSubId;

    @Before
    public void setUp() throws Exception {

        beginString = randomAlphanumeric(5);
        senderCompId = randomAlphanumeric(5);
        senderSubId = randomAlphanumeric(5);
        targetCompId = randomAlphanumeric(5);
        targetSubId = randomAlphanumeric(5);

        session = FixSession.newBuilder()
                .beginString(beginString)
                .senderCompId(senderCompId)
                .senderSubId(senderSubId)
                .targetCompId(targetCompId)
                .targetSubId(targetSubId)
                .build();
    }

    @Test
    public void testPrepareOutgoing() throws Exception {
        int nextOutgoingMsgSeqNum = nextInt(100);
        session.setNextOutgoingMessageSeqNum(nextOutgoingMsgSeqNum);

        FixMessage messageBuilder = new SimpleFixMessage();

        session.prepareOutgoing(messageBuilder);

        assertEquals(nextOutgoingMsgSeqNum + 1, session.getNextOutgoingMessageSeqNum());

        final FixMessageHeader header = messageBuilder.getHeader();

        assertEquals("nextOutgoingMsgSeqNum", nextOutgoingMsgSeqNum, header.getMsgSeqNum());
        assertEquals("beginString", beginString, header.getBeginString());
        assertEquals("senderCompId", senderCompId, header.getSenderCompID());
        assertEquals("senderSubId", senderSubId, header.getSenderSubID());
        assertEquals("targetCompId", targetCompId, header.getTargetCompID());
        assertEquals("targetSubId", targetSubId, header.getTargetSubID());
    }
}
