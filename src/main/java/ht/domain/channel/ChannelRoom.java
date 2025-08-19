package ht.domain.channel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChannelRoom {

	@NotEmpty
	String channelId;

	@NotEmpty
	String channelServiceId;

	@NotEmpty
	String channelCustomerId;

	String openParams;

	@NotEmpty
	String chatRoomUid;
}
